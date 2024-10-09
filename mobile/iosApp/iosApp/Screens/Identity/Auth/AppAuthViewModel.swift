//
//  AppAuthViewModel.swift
//  iosApp
//
//  Created by Szymon Klimek on 10.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import AppAuth
import Combine
import JWTDecode

class AppAuthViewModel: ObservableObject {
    let authResultSubject = PassthroughSubject<AuthResult, Never>()
    private var authResponseHandler: AuthResponseHandler<OIDAuthorizationResponse?>?
    private var tokenResponseHandler: AuthResponseHandler<OIDTokenResponse?>?
    private var userAgentSession: OIDExternalUserAgentSession?
    
    func authenticate(authConfig: AuthConfig, hostingViewController: UIViewController) {
        AppLogger().d(message: "Authentication started")
        self.authResponseHandler = AuthResponseHandler()
        self.tokenResponseHandler = AuthResponseHandler()
        
        Task {
            do {
                await MainActor.run {
                    AppLogger().d(message: "Redirecting to browser")
                    self.userAgentSession = OIDAuthorizationService.present(
                        OIDAuthorizationRequest(
                            configuration: OIDServiceConfiguration(
                                authorizationEndpoint: URL(string: authConfig.authorizationEndpoint)!,
                                tokenEndpoint: URL(string: authConfig.tokenEndpoint)!
                            ),
                            clientId: authConfig.clientId,
                            clientSecret: nil,
                            scopes: nil,
                            redirectURL: URL(string: authConfig.redirectUri)!,
                            responseType: OIDResponseTypeCode,
                            additionalParameters: nil
                        ),
                        presenting: hostingViewController,
                        callback: authResponseHandler!.callback
                    )
                }
                let authResponse = try await authResponseHandler!.waitForCallback()
                if authResponse == nil {
                    AppLogger().e(message: "Auth response is unexpectedly nil")
                    throw AuthError()
                }
                let tokenRequest = authResponse!?.tokenExchangeRequest()
                AppLogger().d(message: "Performing token exchange request")
                OIDAuthorizationService.perform(
                    tokenRequest!,
                    originalAuthorizationResponse: authResponse!,
                    callback: self.tokenResponseHandler!.callback
                )
                
                let tokenResponse = try await tokenResponseHandler!.waitForCallback()
                
                if tokenResponse == nil {
                    AppLogger().e(message: "Token response is unexpectedly nil")
                    throw AuthError()
                }
                AppLogger().d(message: "Decoding token response")
                let accessToken = tokenResponse!?.accessToken
                let refreshToken = tokenResponse!?.refreshToken
                let userId = try decode(jwt: accessToken!).subject
                let expirationTimestamp = tokenResponse!?.accessTokenExpirationDate?.timeIntervalSince1970
                
                await MainActor.run {
                    AppLogger().d(message: "User signed in successfully. User Id: \(String(describing: userId))")
                    authResultSubject.send(AuthResult.SignedIn(
                        accessToken: accessToken!,
                        refreshToken: refreshToken!,
                        userId: userId!,
                        accessTokenExpiresAtTimestampMs: Int64(expirationTimestamp!)
                    ))
                }
            } catch {
                await MainActor.run {
                    if (isUserCancellationErrorCode(ex: error)) {
                        AppLogger().d(message: "User cancelled authentication")
                        authResultSubject.send(AuthResult.Cancelled(details: ""))
                    } else {
                        AppLogger().e(message: "Session error: \(String(describing: error))")
                        authResultSubject.send(AuthResult.Error(details: ""))
                    }
                }
            }
        }
    }
    
    /*
     * This check for specific error codes to handle the user cancelling the ASWebAuthenticationSession window
     */
    private func isUserCancellationErrorCode(ex: Error) -> Bool {
        
        let error = ex as NSError
        return error.domain == OIDGeneralErrorDomain && error.code == OIDErrorCode.userCanceledAuthorizationFlow.rawValue
    }
    
    struct AuthError: Error {
        
    }
}
