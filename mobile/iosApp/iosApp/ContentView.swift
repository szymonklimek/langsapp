import SwiftUI
import app

struct ContentView: View {
    @StateObject var viewModel: AppViewModel
    
    var body: some View {
        switch viewModel.state {
        case is HomeState:
            HomeScreen(state: viewModel.state as! HomeState, actionSender: viewModel)
        default:
            fatalError("Unknown state: \(viewModel.state)")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(viewModel: .init(keyValueStorage: AppKeyValueStorage()))
    }
}
