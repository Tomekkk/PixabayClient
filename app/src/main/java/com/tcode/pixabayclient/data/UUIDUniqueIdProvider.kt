package com.tcode.pixabayclient.data

import java.util.UUID
import javax.inject.Inject

class UUIDUniqueIdProvider
    @Inject
    constructor() : UniqueIdProvider {
        override fun provideUniqueId(): UniqueId = UniqueId(UUID.randomUUID().toString())
    }
