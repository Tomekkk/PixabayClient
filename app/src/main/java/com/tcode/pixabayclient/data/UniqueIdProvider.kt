package com.tcode.pixabayclient.data

interface UniqueIdProvider {
    fun provideUniqueId(): UniqueId
}

@JvmInline
value class UniqueId(val id: String)
