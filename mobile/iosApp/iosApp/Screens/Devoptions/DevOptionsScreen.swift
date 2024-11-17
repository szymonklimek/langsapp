//
//  DevOptionsScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 18.11.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import app

struct DevOptionsScreen: View {
    let state: DevOptionsState
    let actionSender: ActionSender
    
    init(state: DevOptionsState, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        switch state {
        case is DevOptionsState.Loading:
            ProgressView()
        case is DevOptionsState.Loaded:
            VStack {
                let loadedState = state as! DevOptionsState.Loaded
                let availableEnvironments = loadedState.apiEnvironments
                
                Button("Back") {
                    actionSender.sendAction(action: DevOptionsAction.BackTapped())
                }
                EnvironmentSelector(
                    selectedEnvironment:loadedState.selectedEnvironment,
                    environments: availableEnvironments
                ) { environment in
                    actionSender.sendAction(action: DevOptionsAction.EnvironmentChanged(selectedEnvironment: environment))
                }
            }
        default:
            fatalError("Unknown dev options state: \(state.description)")
        }
    }
}

struct EnvironmentSelector: View {
    @SwiftUI.State private var selectedEnvironmentName: String
    private let environments: [ApiEnvironment]
    private let onEnvironmentChanged : (ApiEnvironment) -> ()
    
    init(
        selectedEnvironment: ApiEnvironment,
        environments: [ApiEnvironment],
        onEnvironmentChanged: @escaping (ApiEnvironment) -> ()) {
            selectedEnvironmentName = selectedEnvironment.name
            self.environments = environments
            self.onEnvironmentChanged = onEnvironmentChanged
        }
    var body: some View {
        Picker("environments", selection: $selectedEnvironmentName) {
            ForEach(environments, id: \.self) {
                Text($0.name).tag($0.name)
            }
        }
        .pickerStyle(.wheel)
        .onAppear(perform: {
            let selectedEnvironment = environments.first(where: { $0.name == selectedEnvironmentName })
            onEnvironmentChanged(selectedEnvironment!)
        })
        .onChange(of: selectedEnvironmentName, perform: { envName in
            let selectedEnvironment = environments.first(where: { $0.name == envName })
            onEnvironmentChanged(selectedEnvironment!)
        })
    }
}
