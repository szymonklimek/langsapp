//
//  AppKeyValueStorage.swift
//  iosApp
//
//  Created by Szymon Klimek on 07.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import app
import Foundation


class AppKeyValueStorage: KeyValueStorage {
    var localDictionary = [String: String]()
    
    func get(key: String) -> String? {
        return localDictionary[key]
    }
    
    func getAll() -> [String : String] {
        return localDictionary
    }
    
    func remove(key: String) {
        localDictionary.removeValue(forKey: key)
    }
    
    func set(key: String, value: String) {
        localDictionary.updateValue(value, forKey: key)
    }
}
