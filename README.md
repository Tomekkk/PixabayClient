## Requirements

The PixabayClient app uses [Pixabay API](https://pixabay.com/api/docs/). The API requires a key to
search images. The key can be obtained
from [Pixabay API's documentation](https://pixabay.com/api/docs/) by registering in Pixabay. Add the
key to `./local.properties` file as `pixabay.api.key=KEY` property.
The required minimum API level is 23.

## Features

- Home screen with search interface of [Pixabay API](https://pixabay.com/api/docs/)
- User-friendly search results presented
  on [StaggeredGrid](https://developer.android.com/jetpack/compose/lists#lazy-staggered-grid)
  that dynamically adjust cells to the available space during screen orientation changes
- Dynamic pages loading while scrolling
- Caching search results in a local database
- Load and cache images on-device storage
- Image details screen with larger image and stats accessible from the list
- Dynamic theming based on the system theme (light or dark)

https://github.com/Tomekkk/PixabayClient/assets/3057880/41b03ffe-9962-4916-8df3-df8ddcdac21d

## Architecture

The PixabayClient app is built on top of layered architecture that follows uni-directional data
flow, drives the separation of concerns and focuses on making testing easier. MVVM pattern is used
to separate the UI and business logic.
The architecture has clearly defined UI, data and domain layer reflected
in [data](/app/src/main/java/com/tcode/pixabayclient/data), [domain](/app/src/main/java/com/tcode/pixabayclient/domain)
ad [ui](/app/src/main/java/com/tcode/pixabayclient/ui) packages. The data layer expose interfaces that
can be easily faked in tests. A dependency injection pattern is used to provide the dependencies to
the abstract components making them easier to maintain and test. Coroutines and flows are used to
handle asynchronous operations and provide data streams. Taking into account the size of the project it
was implemented as a single module.

## Data Layer

The data layer is built with a Repository pattern that exposes data stored in the Room database
fetched from API. Defined interfaces provide a clear abstraction for the data
sources. Each search results are paged by 40 items per page and loaded from the network
with the help
of [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview).

## Cache

Pixabay API requires [caching requests for 24h](https://pixabay.com/api/docs/#api_rate_limit) to
avoid unnecessary network calls. The app
uses [RemoteMediator](https://developer.android.com/topic/libraries/architecture/paging/v3-overview#repository)
implementation to fetch, cache and invalidate the search results for a given query. The cache is
cleared for a given query when the user resends the search request and data stored in the cache is
older than 24 hours.

## [DBCachedImagesResultsMediator](/app/src/main/java/com/tcode/pixabayclient/data/mediator/DBCachedImagesResultsMediator.kt)

Validate the cached data and initially refresh it when needed, load next pages from network and
store them in the database cache. The mediator is used by the PagingSource to provide the data to
the UI layer. Based on the state of the anchor position(scroll position) and currently loaded pages, 
the mediator decides whether to load the next page and how to append new items to the stream presented 
in the UI.

## Domain Layer

Following the Clean Architecture principles the domain layer is built with use cases that provide
better separation of concerns. The use cases use repository interfaces defined in the data layer.
Data models are converted to domain models in the domain layer. The use cases are used by the view
models to provide the data to the UI layer.

## UI Layer

[AAC ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel) are used to
manage UI state in a lifecycle conscious way with the support of persisting it through configuration
changes. Jetpack Compose is used to build the UI layer. The UI is built with a single activity.
Navigation was implemented with [Jetpack Navigation](https://developer.android.com/guide/navigation)
Hilt extension is used to provide the ViewModel instances in composable functions.

## Testing

In favor of fakes over the mocks, dependencies of repositories, data sources, mediators or
time/cache providers are defined as abstractions. Data/business logic layers are unit tested
with JUnit, Mockk and
Robolectric. [DBCachedImagesResultsMediatorTest](app/src/androidTest/java/com/tcode/pixabayclient/data/DBCachedImagesResultsMediatorTest.kt)
is an example of instrumented test that checks the behavior of the mediator interacting with Room
database.

## Continuous Integration

CI is implemented with GitHub Actions.
The [on-pull-request.yml](/.github/workflows/on-pull-request.yml) workflow has defined two jobs
responsible for running the tests and linting the code on every pull request. Both jobs run in
parallel.

## Dependencies

All the libraries and plugins used in the project are listed in
the [libs.versions.toml](/gradle/libs.versions.toml) file.
Some of the libraries used in the Application are:

- [Dagger Hilt](https://dagger.dev/hilt/) - Dependency injection framework recommended for Android
  projects
- [Glide](https://bumptech.github.io/glide/) - Image loading with cache mechanism focused on smooth
  scrolling
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI toolkit
- [Jetpack navigation](https://developer.android.com/guide/navigation) - Navigation component with
  support for deep linking and jetpack compose
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - Asynchronous
  programming
- [Kotlin Flows](https://kotlinlang.org/docs/flow.html) - Asynchronous data streams
- [Material 3](https://m3.material.io/) - Material Design 3 components
- [Mockk](https://mockk.io/) - Mocking library for Kotlin
- [Moshi](https://github.com/square/moshi) - API responses JSON parsing
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) -
  Pagination with Jetpack Compose and Room integrations
- [Retrofit](https://square.github.io/retrofit/) - Type-safe HTTP client
- [Robolectric](http://robolectric.org/) - Unit testing framework
- [Room](https://developer.android.com/topic/libraries/architecture/room) - Cache Database with
  Paging 3 integration
