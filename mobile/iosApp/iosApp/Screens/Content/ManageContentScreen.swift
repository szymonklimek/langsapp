//
//  ManageContentScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 30.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct ManageContentScreen: View {
    let state: ManageContentState
    let actionSender: ActionSender
    
    init(state: ManageContentState, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        switch state {
        case is ManageContentState.Loading:
            VStack {
                Text("Loading...")
                ProgressView()
            }
        case is ManageContentState.Loaded:
            ContentLoadedScreen(state: state as! ManageContentState.Loaded, actionSender: actionSender)
        case is ManageContentState.Failure:
            ContentLoadFailureScreen(state: state as! ManageContentState.Failure, actionSender: actionSender)
        default:
            fatalError("Unknown ManageContentState: \(state.description)")
        }
    }
}
