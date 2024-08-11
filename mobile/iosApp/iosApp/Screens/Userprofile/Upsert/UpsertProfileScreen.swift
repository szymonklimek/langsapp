//
//  UpsertProfileScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 30.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct UpsertProfileScreen: View {
    let state: UpsertProfileState
    let actionSender: ActionSender
    
    init(state: UpsertProfileState, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        switch state {
        case is UpsertProfileState.Input:
            InputScreen(state: state as! UpsertProfileState.Input, actionSender: actionSender)
        case is UpsertProfileState.Loading:
            VStack {
                Text("Loading...")
                ProgressView()
            }
        default:
            fatalError("Unknown upsert profile state: \(state.description)")
        }
    }
}
