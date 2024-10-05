//
//  AuthResult.swift
//  iosApp
//
//  Created by Szymon Klimek on 09.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

enum AuthResult: Equatable {
    case SignedIn(
        accessToken: String,
        refreshToken: String,
        userId: String,
        accessTokenExpiresAtTimestampMs: Int64
    )
    case Cancelled(details: String?)
    case Error(details: String?)
}

extension AuthResult {
    static func == (lhs: AuthResult, rhs: AuthResult) -> Bool {
        switch (lhs, rhs) {
        case (
            .SignedIn(let lhsAccessToken, let lhsRefreshToken, let lhsUserId, let lhsAccessTokenExpiresAtTimestampMs),
            .SignedIn(let rhsAccessToken, let rhsRefreshToken, let rhsUserId, let rhsAccessTokenExpiresAtTimestampMs)
        ): return lhsAccessToken == rhsAccessToken
            && lhsRefreshToken == rhsRefreshToken
            && lhsUserId == rhsUserId
            && lhsAccessTokenExpiresAtTimestampMs == rhsAccessTokenExpiresAtTimestampMs
        case (.Cancelled(let lhsDetails), .Cancelled(let rhsDetails)):
            return lhsDetails == rhsDetails
        case (.Error(let lhsDetails), .Error(let rhsDetails)):
            return lhsDetails == rhsDetails
        default:
            return false
        }
    }
}
