import SwiftUI
import app

struct ContentView: View {
    @StateObject var viewModel: AppViewModel
    @SwiftUI.State var toastMessage: ToastMessage? = nil
    @SwiftUI.State var showAuthentication = false
    @SwiftUI.State var authResult: AuthResult? = nil
    @SwiftUI.State var authConfig: AuthConfig? = nil
    
    
    var body: some View {
        NavigationView {
            switch viewModel.state {
            case is HomeState:
                HomeScreen(state: viewModel.state as! HomeState, actionSender: viewModel)
            case is LanguageSettingsState:
                LanguageSettingsScreen(state: viewModel.state as! LanguageSettingsState, actionSender: viewModel)
            case is UpsertProfileState:
                UpsertProfileScreen(state: viewModel.state as! UpsertProfileState, actionSender: viewModel)
            case is ManageContentState:
                ManageContentScreen(state: viewModel.state as! ManageContentState, actionSender: viewModel)
            default:
                fatalError("Unknown state: \(viewModel.state)")
            }
        }
        .onReceive(viewModel.sideEffectsSubject) { handleSideEffect(sideEffect: $0)}
        .toastView(toastMessage: $toastMessage)
        .navigate(
            to: AppAuthScreen(
                viewModel: AppAuthViewModel(),
                authConfig: $authConfig,
                showAuthentication: $showAuthentication,
                authResult: $authResult
            ),
            when: $showAuthentication
        )
        .onChange(of: authResult) { value in
            AppLogger().d(message: "Auth result changed to: \(String(describing: value))")
            if case .SignedIn(
                let accessToken,
                let refreshToken,
                let userId,
                let accessTokenExpiresAtTimestampMs
            ) = value {
                viewModel.sendAction(
                    action: IdentityAction.UserSignedIn(
                        accessToken: accessToken,
                        refreshToken: refreshToken,
                        userId: userId,
                        accessTokenExpiresAtTimestampMs: accessTokenExpiresAtTimestampMs
                    )
                )
            }
        }
    }
    
    func handleSideEffect(sideEffect: SideEffect) {
        AppLogger().d(message: "Handle SideEffect: \(sideEffect)")
        switch sideEffect {
        case is CommonSideEffectShowPopUpMessage:
            toastMessage = ToastMessage(message: (sideEffect as!CommonSideEffectShowPopUpMessage).message)
        case is HomeNavigationSideEffect.SignUp:
            authConfig = (sideEffect as!HomeNavigationSideEffect.SignUp).authConfig
            showAuthentication = true
        default:
            AppLogger().e(message: "Unknown SideEffect: \(sideEffect)")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(viewModel: .init(keyValueStorage: AppKeyValueStorage()))
    }
}
