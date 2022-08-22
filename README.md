# Langsapp
Simple language learning application.
Choose language, learn new words and repeat them according to the schedule calculated by the app.

## Project background

I found applications for learning and repeating words/sentences from foreign language very useful and decided to write one to experiment with technologies.
I built 1st version on Android that available in [Play Store](https://play.google.com/store/apps/details?id=com.langsapp.android.app) and now would like to rewrite the project trying to apply tools and practices I learned or want to try, specifically:
- structure the project to be easily ported to iOS (checking [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html))
- make use of declarative UI frameworks (Compose/Swift UI) and get first iOS experience
- document choices and architecture on the way
- take care of project health from the beginning (tests, CI, static code analysis, proper depedencies updates)
- make it easy to follow for others and myself in the future

## Project structure

### Client application (iOS/Android)

Client application consists of 3 parts:
- [app](app): module containing domain and business logic that is shared between iOS and Android
- [androidApp](androidApp): module containing Android app implementation
- [iosApp](iosApp): project with iOS app implementation
