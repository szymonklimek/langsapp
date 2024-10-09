import SwiftUI
import app

struct ContentView: View {
    @StateObject var viewModel: AppViewModel
    @SwiftUI.State var toastMessage: ToastMessage? = nil
    
    var body: some View {
        switch viewModel.state {
        case is HomeState:
            HomeScreen(state: viewModel.state as! HomeState, actionSender: viewModel)
                .onReceive(viewModel.sideEffectsSubject) { handleSideEffect(sideEffect: $0)}
                .toastView(toastMessage: $toastMessage)
        default:
            fatalError("Unknown state: \(viewModel.state)")
        }
    }
    
    func handleSideEffect(sideEffect: SideEffect) {
        AppLogger().d(message: "Handle SideEffect: \(sideEffect)")
        switch sideEffect {
        case is CommonSideEffectShowPopUpMessage:
            toastMessage = ToastMessage(message: (sideEffect as!CommonSideEffectShowPopUpMessage).message)
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
