# compose_github

# Feature

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

## Backlog

# Tech Stacks 

## Android and Kotlin official library
* StateFlow/Coroutine
* LifecycleScope
* Compose

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
