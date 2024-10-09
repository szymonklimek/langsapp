//
//  ToastMessageView.swift
//  iosApp
//
//  Created by Szymon Klimek on 08.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ToastMessageView: View {
    var message: String
    var width = CGFloat.infinity
    var onCancelTapped: (() -> Void)
    
    var body: some View {
        HStack(alignment: .center, spacing: 12) {
              Text(message)
                .font(Font.caption)
              
              Spacer(minLength: 10)
              
              Button {
                onCancelTapped()
              } label: {
                Image(systemName: "xmark")
              }
            }
            .padding()
            .frame(minWidth: 0, maxWidth: width)
            .cornerRadius(8)
            .overlay(
              RoundedRectangle(cornerRadius: 8)
                .opacity(0.6)
            )
            .padding(.horizontal, 16)
    }
}

#Preview {
    ToastMessageView(message: "Example message!", onCancelTapped: {})
}
