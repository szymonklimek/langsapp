//
//  AppAuthScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 09.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import AppAuth
import SwiftUI

struct AppAuthScreen: View {
    @StateObject var viewModel: AppAuthViewModel
    @Binding var authConfig: AuthConfig?
    @Binding var showAuthentication: Bool
    @Binding var authResult: AuthResult?
    
    var body: some View {
        ProgressView()
            .onReceive(viewModel.authResultSubject) {
                showAuthentication = false
                authResult = $0
            }
            .onAppear(perform: {
                guard authConfig != nil else {
                    AppLogger().e(message: "Auth config is missing when screen appeared")
                    showAuthentication = false
                    authResult = AuthResult.Error(details: "Auth config is missing when screen appeared")
                    return
                }
                viewModel.authenticate(authConfig: authConfig!, hostingViewController: getHostingViewController())
            })
    }
    
    private func getHostingViewController() -> UIViewController {
        return UIApplication.shared.windows.first!.rootViewController!
    }
}

