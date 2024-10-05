//
//  AppAuthScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 09.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct AppAuthScreen: View {
    @Binding var authConfig: AuthConfig?
    @Binding var showAuthentication: Bool
    @Binding var authResult: AuthResult?
    
    var body: some View {
        VStack {
            Text("Temporary auth screen")
            Button("Finish with error") {
                AppLogger().d(message: "Finish with error tapped")
                showAuthentication = false
                authResult = AuthResult.Error(details: "")
            }
            Button("Finish with cancel") {
                AppLogger().d(message: "Finish with error tapped")
                showAuthentication = false
                authResult = AuthResult.Error(details: "")
            }
            Button("Finish auth") {
                AppLogger().d(message: "Finish auth tapped")
                showAuthentication = false
                authResult = AuthResult.SignedIn(
                    accessToken: "accessToken",
                    refreshToken: "refreshToken",
                    userId: "user1",
                    accessTokenExpiresAtTimestampMs: 0
                )
            }
        }
        .onAppear(perform: {
            guard authConfig != nil else {
                AppLogger().e(message: "Auth config is missing when screen appeared")
                showAuthentication = false
                authResult = AuthResult.Error(details: "Auth config is missing when screen appeared")
                return
            }
        })
    }
}

