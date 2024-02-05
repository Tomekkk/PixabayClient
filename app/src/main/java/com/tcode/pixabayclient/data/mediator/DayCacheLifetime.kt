package com.tcode.pixabayclient.data.mediator

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DayCacheLifetime
    @Inject
    constructor() : CacheLifetime {
        override val lifetimeMs = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
    }
