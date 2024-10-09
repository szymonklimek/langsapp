//
//  AuthResponseHandler.swift
//  iosApp
//
//  Created by Szymon Klimek on 10.10.24.
//  Copyright © 2024 orgName. All rights reserved.
//
// Inspired by: https://github.com/curityio/openid-client-ios-appauth/blob/a16ec95785339345ab917c0fb5c820608fbad154/Source/LoginResponseHandler.swift

import Foundation
import AppAuth

class AuthResponseHandler<T> {
    var storedContinuation: CheckedContinuation<T?, Error>?

    func waitForCallback() async throws -> T? {
        try await withCheckedThrowingContinuation { continuation in
            storedContinuation = continuation
        }
    }

    func callback(response: T?, ex: Error?) {
        if ex != nil {
            storedContinuation?.resume(throwing: ex!)
        } else {
            storedContinuation?.resume(returning: response)
        }
    }
}
