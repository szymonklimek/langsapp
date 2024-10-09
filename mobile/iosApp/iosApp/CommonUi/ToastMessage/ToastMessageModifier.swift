//
//  ToastModifier.swift
//  iosApp
//
//  Created by Szymon Klimek on 08.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ToastMessageModifier: ViewModifier {
    
    @Binding var toastMessage: ToastMessage?
    @State private var workItem: DispatchWorkItem?
    
    func body(content: Content) -> some View {
        content
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .overlay(
                ZStack {
                    mainToastView()
                        .offset(y: 32)
                }.animation(.spring(), value: toastMessage)
            )
            .onChange(of: toastMessage) { value in
                showToast()
            }
    }
    
    @ViewBuilder func mainToastView() -> some View {
        if let toastMessage = toastMessage {
            VStack {
                ToastMessageView(
                    message: toastMessage.message
                ) {
                    dismissToast()
                }
                Spacer()
            }
        }
    }
    
    private func showToast() {
        AppLogger().d(message: "Show toast")
        guard let toastMessage = toastMessage else {
            return
        }
        
        UIImpactFeedbackGenerator(style: .light)
            .impactOccurred()
        
        if toastMessage.duration > 0 {
            workItem?.cancel()
            
            let task = DispatchWorkItem {
                dismissToast()
            }
            
            workItem = task
            DispatchQueue.main.asyncAfter(deadline: .now() + toastMessage.duration, execute: task)
        }
    }
    
    private func dismissToast() {
        AppLogger().d(message: "Dismiss toast")
        withAnimation {
            toastMessage = nil
        }
        
        workItem?.cancel()
        workItem = nil
    }
}
