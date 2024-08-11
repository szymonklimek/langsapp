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
                let availableLanguages = (state as! LanguageSettingsState.Input).availableLanguages
                Text("Language settings input")
                Button("Back") {
                    actionSender.sendAction(action: LanguageSettingsAction.BackTapped())
                }
                Spacer()
                Text("Learn language")
                LanguageSettingsSelector(languages: availableLanguages) { language in
                    actionSender.sendAction(action: LanguageSettingsAction.LearnLanguageChanged(language: language))
                }
                Text("Base language")
                LanguageSettingsSelector(languages: availableLanguages) { language in
                    actionSender.sendAction(action: LanguageSettingsAction.BaseLanguageChanged(language: language))
                }
                Text("Support language")
                LanguageSettingsSelector(languages: availableLanguages) { language in
                    actionSender.sendAction(action: LanguageSettingsAction.SupportLanguageChanged(language: language))
                }
                Spacer()
                Button("Confirm") {
                    actionSender.sendAction(action: LanguageSettingsAction.ConfirmTapped())
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

struct LanguageSettingsSelector: View {
    @SwiftUI.State private var selectedLanguage: String
    private let languages: [Language]
    private let onLanguageChanged : (Language) -> ()
    
    init(languages: [Language], onLanguageChanged: @escaping (Language) -> ()) {
        selectedLanguage = languages.first!.code
        self.languages = languages
        self.onLanguageChanged = onLanguageChanged
    }
    var body: some View {
        Picker("languages", selection: $selectedLanguage) {
            ForEach(languages, id: \.self) {
                Text($0.code).tag($0.code)
            }
        }
        .pickerStyle(.wheel)
        .onAppear(perform: {
            onLanguageChanged(Language(code:selectedLanguage))
        })
        .onChange(of: selectedLanguage, perform: { languageCode in
            onLanguageChanged(Language(code:selectedLanguage))
        })
    }
}
