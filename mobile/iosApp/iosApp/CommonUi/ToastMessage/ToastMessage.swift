//
//  ToastMessage.swift
//  iosApp
//
//  Created by Szymon Klimek on 08.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//

struct ToastMessage: Equatable {
  var message: String
  var duration: Double = 3
  var width: Double = .infinity
}
