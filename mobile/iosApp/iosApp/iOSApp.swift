import SwiftUI
import app

@main
struct iOSApp: App {
    let logger = AppLogger()
    let keyValueStorage = AppKeyValueStorage()
    
    init() {
        app.AppConfig.shared.doInit(
            log: logger,
            keyValueStorage: keyValueStorage,
            // TODO Implement condition "if release build" here instead hardcoding to true
            devOptionsEnabled: true
        )
        let buildConfig = BuildConfig()
        AppLogger().d(message: "Starting application. Version: \(buildConfig.APP_VERSION), commit: \(buildConfig.BUILD_COMMIT_HASH), built at: \(buildConfig.BUILD_TIME)")
    }
    
	var body: some Scene {
		WindowGroup {
            ContentView(viewModel: .init(keyValueStorage: keyValueStorage))
		}
	}
}
