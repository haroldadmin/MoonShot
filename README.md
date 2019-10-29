
<p float="left">
<img src="https://user-images.githubusercontent.com/24315306/59548158-6677aa80-8f68-11e9-95f7-931e39e11278.jpg" width="24%"/>
<img src="https://user-images.githubusercontent.com/24315306/59548397-67aad680-8f6c-11e9-889b-f283aaa1f576.jpg" width="24%"/>
<img src="https://user-images.githubusercontent.com/24315306/59548398-67aad680-8f6c-11e9-9b90-70ac5279c45d.jpg" width="24%"/>
<img src="https://user-images.githubusercontent.com/24315306/59548425-d425d580-8f6c-11e9-9ad2-86d0e1f96561.jpg" width="24%"/>
</p>

# MoonShot
###### A SpaceX Companion App

<a href='https://play.google.com/store/apps/details?id=com.haroldadmin.moonshot&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width = "150px"/></a>

MoonShot is a SpaceX companion app to help you keep up with their launches. It uses the [SpaceX API](https://github.com/r-spacex/SpaceX-API) to fetch data.

MoonShot is built using [Vector](https://github.com/haroldadmin/Vector), a Kotlin Coroutines based MVI architecture library for Android.

### Features

* View Next Launch information right on the home page
* Get reminders before every launch
* View all the rockets used by SpaceX along with the launches they have attempted
* View Launch Pad information about every launch
* Ability to search through all launches, launch pads and rockets.
* Complete offline support.
* Clean, minimal design with Dark theme support.
* And a lot, lot more.

### Project Architecture

* [Vector](https://github.com/haroldadmin/Vector), for an MVI architecture pattern and state management.
* [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for nearly all asynchronous operations
* [Coroutines Network Response Adapter](https://github.com/haroldadmin/CoroutinesNetworkResponseAdapter) to easily handle error states in network calls.
* [Epoxy](https://github.com/airbnb/Epoxy) to build most of the UI.
* [Room](https://developer.android.com/topic/libraries/architecture/room) for the local persistence layer, although I want to experiment with [SQLDelight](https://github.com/square/sqldelight) in the future.
* [Moshi](https://github.com/square/moshi) for JSON parsing
* [Koin](https://github.com/insertKoinIO/koin) for Dependency Injection (or as a Service Locator, for those that disagree)
* Multi Module app following Modularization by feature, with every feature sharing the same repository layer
* [Navigation Architecture Component](https://developer.android.com/guide/navigation/navigation-getting-started) for in-app navigation

### Contribution

The project is in rapid development. Expect bugs.
Please feel free to open issues and/or make Pull Requests for any issues you might find.
