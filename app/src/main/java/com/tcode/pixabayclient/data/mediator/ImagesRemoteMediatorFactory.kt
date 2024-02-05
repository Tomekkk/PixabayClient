package com.tcode.pixabayclient.data.mediator

interface ImagesRemoteMediatorFactory {
    fun create(query: String): ImagesRemoteMediator
}
