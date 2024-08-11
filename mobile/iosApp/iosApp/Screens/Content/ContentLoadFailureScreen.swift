//
//  ContentLoadFailureScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 30.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct ContentLoadFailureScreen: View {
    let state: ManageContentState.Failure
    let actionSender: ActionSender
    
    init(state: ManageContentState.Failure, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        VStack {
            Text("Manage content failure screen")
            Button("Retry download content") {
                actionSender.sendAction(action: ManageContentAction.DownloadUnitsTapped())
            }
        }
    }
}
