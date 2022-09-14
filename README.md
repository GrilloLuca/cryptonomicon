# Cryptonomicon
(an easy mobile crypo ₿ scanner)

### How to build and run project
```sh
git clone https://github.com/GrilloLuca/cryptonomicon.git
```
Just open android studio, import new project and press play to run the app in a connected device
| Plugin | README |
| ------ | ------ |
| minSdk | [21] |32
| targetSdk | [32] |


### Brief description of the nice simple app architecture
- The app is built following the clean code paradigm and SOLID principles
- the main app architecture is MVVM with UseCases.
- UseCases that are injected in the viewmodel, are responsable for the business logic. UseCases input are injected as well for better testability.
- The main Datasource is CoinGecko api based on Retrofit implementing the NetwotkRepository contract. No data persistance is being implemented.
- The ViewModel is being injected by Hilt and it's observed as state by composable items
- The api response is wrapped in a dataclass "Resource" who propagate data and errors trough the UI
- few tests are available

### Dependencies used

- 'com.squareup.retrofit2:retrofit:2.9.0'
- 'com.google.dagger:hilt-android:2.42'
- 'com.squareup.retrofit2:retrofit:2.9.0'
- 'com.squareup.okhttp3:logging-interceptor:4.10.0'
- 'io.coil-kt:coil-compose:2.0.0-rc01'
