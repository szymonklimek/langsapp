import SwiftUI
import app

@main
struct iOSApp: App {
    let logger = AppLogger()
    let keyValueStorage = AppKeyValueStorage()
    
    init() {
        app.AppConfig.shared.doInit(log: logger, keyValueStorage: keyValueStorage)
    }
    
	var body: some Scene {
		WindowGroup {
            ContentView(viewModel: .init(keyValueStorage: keyValueStorage))
		}
	}
}
