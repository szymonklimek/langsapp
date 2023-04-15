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

Project is planned to consist of following main components:
1. Mobile applications for Android and iOS systems
2. Service applications exposing API managing social features of the project
3. API layer defining communication contract between apps
4. Infrastructure scripts, definitions and descriptions for managing additional resources such as object storages,
   databases, observability tools etc.
5. Automation tasks helpful in managing the project

On the technical side project consists of several separate [Gradle](https://gradle.org/) projects:
1. Root project specified by top [build.gradle.kts](build.gradle.kts) and [settings.gradle.kts](settings.gradle.kts)
2. Mobile project specified in [mobile](mobile) directory

### Client application (iOS/Android)

See: [mobile](mobile) directory to learn more.
