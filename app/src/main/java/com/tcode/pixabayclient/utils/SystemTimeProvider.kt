package com.tcode.pixabayclient.utils

import javax.inject.Inject

class SystemTimeProvider
    @Inject
    constructor() : TimerProvider {
        override fun currentTimeMillis(): Long {
            return System.currentTimeMillis()
        }
    }
