//
//  AppLogger.swift
//  iosApp
//
//  Created by Szymon Klimek on 07.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import Foundation
import os

class AppLogger: AppConfigLog {
    
    func d(message: String) {
        os_log(.debug, "\(self.getCallerStackEntry() ?? "") \(message)")
    }
    
    func e(message: String) {
        os_log(.error, "\(self.getCallerStackEntry() ?? "") \(message)")
    }
    
    func i(message: String) {
        os_log(.info, "\(self.getCallerStackEntry() ?? "") \(message)")
    }
    
    func v(message: String) {
        os_log(.default, "\(self.getCallerStackEntry() ?? "") \(message)")
    }
    
    func w(message: String) {
        os_log(.info, "\(self.getCallerStackEntry() ?? "") \(message)")
    }
    
    func wtf(message: String) {
        os_log(.fault, "\(self.getCallerStackEntry() ?? "") \(message)")
    }
    
    func getCallerStackEntry() -> String? {
        return Thread.callStackSymbols
            .first(where: { stackEntry in
                stackEntry.range(of: "(Log|Logger)", options: .regularExpression, range: nil, locale: nil) == nil
            })
    }
}
