//
//  HomeScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 07.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct HomeScreen: View {
    let state: HomeState
    let actionSender: ActionSender
    
    init(state: HomeState, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        switch state {
        case is HomeState.Welcome:
            WelcomeScreen(state: state as! HomeState.Welcome, actionSender: actionSender)
        case is HomeState.Loading:
            LoadingScreen()
        case is HomeState.Loaded:
            LoadedHomeScreen(state: state as! HomeState.Loaded, actionSender: actionSender)
        default:
            fatalError("Unknown home state: \(state.description)")
        }
    }
}
