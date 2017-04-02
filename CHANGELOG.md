Change-Log
===============

### Release 2.0.0-beta1 ###
> 02.04.2017

- Added `SharedPreferencesFacade`, `SimpleSharedPreferencesFacade` and `SharedPreferencesWrapper`
  into **[preferences-core](https://github.com/universum-studios/android_preferences/MODULES.md)**
  library module.
- `PreferencesManager` has been refactored in order to extend `SimpleSharedPreferencesFacade` and has
  been moved into separate library module named **[preferences-manager](https://github.com/universum-studios/android_preferences/MODULES.md)**.
- `SharedPreference` has been refactored along with all its direct implementations provided by the
  library via **[preferences-common](https://github.com/universum-studios/android_preferences/MODULES.md)**
  and **[preferences-collection](https://github.com/universum-studios/android_preferences/MODULES.md)**
  modules.
- Added new library module named **[preferences-crypto](https://github.com/universum-studios/android_preferences/MODULES.md)**
  which contains `SharedPreferences` implementation that supports persisting of values in a **secure**
  way using **[Crypto](https://github.com/universum-studios/android_crypto)** library.
- Added new library module named **[preferences-cache](https://github.com/universum-studios/android_preferences/MODULES.md)**
  which contains common implementations of `SharedPreferencesCache` which may be used in association
  with `CryptoSharedPreferences` in order to supply desired cache for secure preferences.
- `ListPreference` has been deprecated in favor of `CollectionPreference` and will be removed in
  the next release.

### Release 1.0.1 ###
> 22.01.2017

- Fixed issue when preferences file creation mode specified via `PreferencesManager(..., int)`
  constructor has been ignored.

### Release 1.0.0 ###
> 21.12.2016