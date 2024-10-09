//
//  View_ToastMessageView.swift
//  iosApp
//
//  Created by Szymon Klimek on 09.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension View {
    func toastView(toastMessage: Binding<ToastMessage?>) -> some View {
        self.modifier(ToastMessageModifier(toastMessage: toastMessage))
    }
}
