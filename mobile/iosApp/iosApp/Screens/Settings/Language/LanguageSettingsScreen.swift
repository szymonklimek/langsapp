//
//  LanguageSettingsScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 11.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct LanguageSettingsScreen: View {
    let state: LanguageSettingsState
    let actionSender: ActionSender
    
    init(state: LanguageSettingsState, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        switch state {
        case is LanguageSettingsState.Loading:
            ProgressView()
        case is LanguageSettingsState.Input:
            VStack {
                Text("Language settings input")
                Button("Back") {
                    actionSender.sendAction(action: LanguageSettingsAction.BackTapped())
                }
            }
            
        case is LanguageSettingsState.DataLoadingFailure:
            VStack {
                Text("Language settings data loading failure")
                Button("Back") {
                    actionSender.sendAction(action: LanguageSettingsAction.BackTapped())
                }
            }
        default:
            fatalError("Unknown language settings state: \(state.description)")
        }
    }
}
