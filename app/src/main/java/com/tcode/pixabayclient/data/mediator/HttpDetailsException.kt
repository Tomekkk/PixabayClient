package com.tcode.pixabayclient.data.mediator

import retrofit2.HttpException

class HttpWithBodyException(override val message: String?) : RuntimeException(message)

fun HttpException.withErrorBody(): HttpWithBodyException {
    val body = response()?.errorBody()?.string() ?: ""
    val message = body.ifBlank { message() }
    return HttpWithBodyException(message)
}
