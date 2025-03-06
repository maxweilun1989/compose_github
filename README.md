# compose_github

# Feature
https://www.bilibili.com/video/BV1g3RNYVErm/?vd_source=c1787d85bea9b3113f619d0d7a011cef

## Suported
1. Browse the most 10 popular repositories of specific programming langugae.
2. Change programming language and sort repositories by its stars.
3. View Repository's Detail.
4. Login(Only supoprt OAuth2, password and username seed not supported by github)
5. View Current login user's profile.
6. Remember user Login State.
7. Screen rotated supported.

## Not Supported yet.
1. Raise issues for authentical user(need list all the repos in the profile page, and another screen to fill the issue info)
2. Repo List page should supprt load more.
3. Fetch all the supported lanuages from github server, not hardcoded.
4. Repo list page should support pull down to refresh.

# Arch

Basically, we adapte the architecture that google recommended. [Layer Architecture](https://developer.android.com/topic/architecture/recommendations). There is a simple illustration diagram below.
![image](https://github.com/user-attachments/assets/a918c81f-c820-4ab6-944f-c5e7fd79d9ad)

# Some Teck debts.
1. Should not store APPID and APP_SECRET in source code. Maybe should try [keystore](https://developer.android.com/privacy-and-security/keystore).
2. ViewModel should not contains too much business logic. Move it into Domain or Data Layer.
3. Should not pass ViewModel or Navigator to inner Compose Component. If the cmoponent want to interact with ViewModel or Navigation, using callback. or try other data store pattern like redux in react.
4. Should import the coverate of unit test and intrumented test, Not really driven by test, need improve.

# Tech Stacks 

## Android and Kotlin official library
* StateFlow/Coroutine
* LifecycleScope
* Compose
* Hilt

## third-part library
* [Coil](https://github.com/coil-kt/coil) for dispaly async image.
* [Okttp](https://github.com/square/okhttp) the network engine.
* [retrofit](https://github.com/square/retrofit) easy network request.
* [moshi](https://github.com/square/moshi) the kotlin friendly json library.

## Test
* JUnit, the unit test framework
* Espresso, the intrumented test framework
* [turbine](https://github.com/cashapp/turbine) easy to collect every state for StateFlow, recommanded by google.

## CI
* [lefthook](https://github.com/evilmartians/lefthook?locale=zh_CN) for hook git commit.
* [Ktlint](https://github.com/pinterest/ktlint) for kotlin lint check when commit
* [commitlint](https://github.com/conventional-changelog/commitlint) for check commit msg
* Github Action: to check on the server side.
