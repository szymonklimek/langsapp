//
//  WelcomeScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 07.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct WelcomeScreen: View {
    let state: HomeState.Welcome
    let actionSender: ActionSender
    
    init(state: HomeState.Welcome, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        VStack {
            Text("Welcome slides: \(state.slides.description)")
            Button("Skip") {
                AppLogger().d(message: "Skip tapped")
                actionSender.sendAction(action: HomeAction.SkipTapped())
            }
        }
    }
}
