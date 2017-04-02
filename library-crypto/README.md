Preferences-Crypto
===============

This module contains implementation of `SharedPreferences` that supports both **keys** and **values**
**encryption** and **decryption**. All cryptography related operations are delegated by the crypto
preferences to a supplied **[Crypto](https://github.com/universum-studios/android_crypto)** 
implementations (for keys and values respectively).

## Download ##
[![Bintray Badge](https://api.bintray.com/packages/universum-studios/android/universum.studios.android%3Apreferences/images/download.svg)](https://bintray.com/universum-studios/android/universum.studios.android%3Apreferences/_latestVersion)

### Gradle ###

    compile "universum.studios.android:preferences-crypto:${DESIRED_VERSION}@aar"

_depends on:_
[preferences-core](https://github.com/universum-studios/android_preferences/tree/master/library-core)

## Components ##

Below are listed some of **primary components** that are available in this module:

- [CryptoSharedPreferences](https://github.com/universum-studios/android_preferences/blob/master/library-crypto/src/main/java/universum/studios/android/preference/crypto/CryptoSharedPreferences.java)
