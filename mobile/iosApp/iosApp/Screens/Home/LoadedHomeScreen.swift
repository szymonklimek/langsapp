//
//  LoadedHomeScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 07.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct LoadedHomeScreen: View {
    let state: HomeState.Loaded
    let actionSender: ActionSender
    
    init(state: HomeState.Loaded, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        VStack {
            Text("Home loaded")
            Button("Sign up") {
                actionSender.sendAction(action: HomeAction.SignUpTapped())
            }
        }
    }
}
