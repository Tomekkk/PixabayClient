package com.tcode.pixabayclient.data.api

import com.tcode.pixabayclient.BuildConfig
import javax.inject.Inject

class ConfigApiKeyProvider
    @Inject
    constructor() : ApiKeyProvider {
        override val apiKey: String = BuildConfig.pixabayapikey
    }
