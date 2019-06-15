
<p float="left">
<img src="https://user-images.githubusercontent.com/24315306/59548158-6677aa80-8f68-11e9-95f7-931e39e11278.jpg" width="24%"/>
<img src="https://user-images.githubusercontent.com/24315306/59548397-67aad680-8f6c-11e9-889b-f283aaa1f576.jpg" width="24%"/>
<img src="https://user-images.githubusercontent.com/24315306/59548398-67aad680-8f6c-11e9-9b90-70ac5279c45d.jpg" width="24%"/>
<img src="https://user-images.githubusercontent.com/24315306/59548425-d425d580-8f6c-11e9-9ad2-86d0e1f96561.jpg" width="24%"/>
</p>

# MoonShot
###### A SpaceX Companion App

<a href='https://play.google.com/store/apps/details?id=com.haroldadmin.moonshot&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width = "150px"/></a>



MoonShot is an app that uses the [SpaceX API](https://github.com/r-spacex/SpaceX-API) to display information about upcoming launches. It is a SpaceX companion app to help you keep up the company's rocket launches.

MoonShot tries to follow architectural best practices for Android, and is built using [Vector](https://github.com/haroldadmin/Vector), a Kotlin Coroutines based MVI architecture library for Android. It is completely offline ready, and caches all the data once it has been retrieved. Most of the UI has been built using [Epoxy](https://github.com/airbnb/epoxy), a fantastic RecyclerView library by AirBnb that let's you build Recycler Views with multiple view types easily.



### Features
* View Next Launch information right on the home page
* Get reminders before every launch
* Clean, minimal design. Has Dark theme support as well.
* Offline support, can display saved information when there's no network available.
* View all the rockets used by SpaceX along with the launches they have attempted
* View Launch Pad information about every launch


### Project Architecture
* [Vector](https://github.com/haroldadmin/Vector), to help easily implement the MVI architecture pattern.
* [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for nearly all asynchronous operations
* [Coroutines Network Response Adapter](https://github.com/haroldadmin/CoroutinesNetworkResponseAdapter) to easily handle error states in network calls.
* [Epoxy](https://github.com/airbnb/Epoxy) along with databinding to build most of the UI. I highly recommend this approach for building apps.
* [Room](https://developer.android.com/topic/libraries/architecture/room) for the local persistence layer, although I want to experiment with [SQLDelight](https://github.com/square/sqldelight) in the future.
* [Moshi](https://github.com/square/moshi) for JSON parsing
* [Koin](https://github.com/insertKoinIO/koin) for Dependency Injection (or as a Service Locator, for those that disagree)
* Multi Module app, following Modularization by layer. I plan to move every UI feature into a separate Dynamic Feature module once support for them stabilizes in the Navigation AAC.
* [Navigation Architecture Component](https://developer.android.com/guide/navigation/navigation-getting-started) for in-app navigation

### Development
The project is still in early stages of development.
Please feel free to open issues and/or make Pull Requests for any issues you might find.

I hope you like the project, and if you do please consider starring the repository to show your support!