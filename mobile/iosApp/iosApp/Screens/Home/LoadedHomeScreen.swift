//
//  LoadedHomeScreen.swift
//  iosApp
//
//  Created by Szymon Klimek on 07.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import SwiftUI

struct LoadedHomeScreen: View {
    let state: HomeState.Loaded
    let actionSender: ActionSender
    
    init(state: HomeState.Loaded, actionSender: ActionSender) {
        self.state = state
        self.actionSender = actionSender
    }
    
    var body: some View {
        VStack {
            let userProfileInfoResult = state.userProfileInfo
            let onBoardingInfoResult = state.onBoardingInfo
            
            Text("Home loaded")
            Text("User profile info: \(String(describing: state.userProfileInfo))")
            
            if userProfileInfoResult is CommonResultSuccess {
                if (userProfileInfoResult as! CommonResultSuccess).value is UserProfileInfo.Anonymous {
                    Button("Sign up") {
                        actionSender.sendAction(action: HomeAction.SignUpTapped())
                    }
                }
            } else {
                Text("Failed to load user profile info")
            }
            
            if onBoardingInfoResult is CommonResultSuccess {
                let onBoardingInfo = (onBoardingInfoResult as! CommonResultSuccess).value!
                Text("On-boarding finished: \(String(describing:onBoardingInfo.isFinished))")
                Text("On-boarding sections:")
                ForEach(onBoardingInfo.sections, id: \.self) { section in
                    Text("Section root step: \(String(describing: section.rootStep))")
                    SectionInfoView(actionSender: actionSender, stepInfo: section.rootStep)
                    
                    ForEach(section.childSteps, id: \.self) { stepInfo in
                        SectionInfoView(actionSender: actionSender, stepInfo: stepInfo)
                    }
                }
            }
        }
    }
    
    struct SectionInfoView: View {
        let stepInfo: OnBoardingInfo.StepInfo
        let actionSender: ActionSender
        
        
        init(actionSender: ActionSender, stepInfo: OnBoardingInfo.StepInfo) {
            self.stepInfo = stepInfo
            self.actionSender = actionSender
        }
        
        var body: some View {
            Text("\(String(describing: stepInfo.step)), required: \(String(describing: stepInfo.required)), done: \(String(describing: stepInfo.done)), enabled: \(String(describing: stepInfo.enabled))")
            
            if !stepInfo.done {
                let label = switch stepInfo.step {
                case OnBoardingInfo.OnBoardingStep.selectLanguages:
                    "Select languages"
                case OnBoardingInfo.OnBoardingStep.signUp:
                    "Sign up"
                case OnBoardingInfo.OnBoardingStep.fillProfile:
                    "Fill profile"
                case OnBoardingInfo.OnBoardingStep.downloadContent:
                    "Download content"
                default:
                    fatalError("Unknown step: \(String(describing: stepInfo.step))")
                }
                let action = switch stepInfo.step {
                case OnBoardingInfo.OnBoardingStep.selectLanguages:
                    HomeAction.SelectLanguagesTapped()
                case OnBoardingInfo.OnBoardingStep.signUp:
                    HomeAction.SignUpTapped()
                case OnBoardingInfo.OnBoardingStep.fillProfile:
                    HomeAction.UpsertProfileTapped()
                case OnBoardingInfo.OnBoardingStep.downloadContent:
                    HomeAction.DownloadContentTapped()
                default:
                    fatalError("Unknown step: \(String(describing: stepInfo.step))")
                }
                Button(label) {
                    actionSender.sendAction(action: action)
                }
                .disabled(!stepInfo.enabled)
            }
        }
    }
}
