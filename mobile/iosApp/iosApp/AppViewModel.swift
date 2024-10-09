//
//  AppViewModel.swift
//  iosApp
//
//  Created by Szymon Klimek on 07.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import Combine
import Foundation

class AppViewModel: ObservableObject, StateObserver, SideEffectConsumer, ActionSender {
    
    @Published var state: app.State
    let sideEffectsSubject = PassthroughSubject<SideEffect, Never>()
    
    private let keyValueStorage: KeyValueStorage
    private let appStateManager: AppStateManager
    
    init(keyValueStorage: KeyValueStorage) {
        self.keyValueStorage = keyValueStorage
        self.appStateManager = AppStateManager(keyValueStorage: keyValueStorage)
        state = appStateManager.currentState
        self.appStateManager.stateObserver = self
        self.appStateManager.sideEffectsConsumer = self
    }
    
    func onSideEffect(sideEffect: SideEffect) {
        AppLogger().d(message: "\(String(describing: sideEffect))")
        sideEffectsSubject.send(sideEffect)
    }
    
    func onNewState(state newState: app.State, previousState: app.State?, transition: StateTransition?) {
        AppLogger().d(message: "newState \(newState),  previousState \(String(describing: previousState)), transition: \(String(describing: transition))")
        self.state = newState
    }
    
    func sendAction(action: Action) {
        appStateManager.sendAction(action: action)
    }
}
