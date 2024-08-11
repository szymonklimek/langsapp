//
//  InputScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 30.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct InputScreen: View {
    let state: UpsertProfileState.Input
    let actionSender: ActionSender
    
    @SwiftUI.State private var username: String = ""
    
    init(state: UpsertProfileState.Input, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        VStack {
            Text("Upsert profile")
            Text("Current profile: \(String(describing: state.currentProfile))")
            Form {
                TextField("Username", text: $username)
            }
            Button("Confirm") {
                actionSender.sendAction(action: UpsertProfileAction.ConfirmTapped(newUsername: username))
            }
        }
    }
}
