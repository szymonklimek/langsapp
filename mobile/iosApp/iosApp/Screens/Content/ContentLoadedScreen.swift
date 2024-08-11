//
//  ContentLoadedScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 30.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct ContentLoadedScreen: View {
    let state: ManageContentState.Loaded
    let actionSender: ActionSender
    
    init(state: ManageContentState.Loaded, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        VStack {
            Text("Manage content screen loaded")
            
            Text("Has content: \(String(describing: state.hasContent))")
            
            Button("Download content") {
                actionSender.sendAction(action: ManageContentAction.DownloadUnitsTapped())
            }
        }
    }
}
