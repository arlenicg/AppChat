package com.pruebachat.appchat.core

import com.pruebachat.appchat.ProyectoAppChat


/**
 * [Response] clase sellada que representa el estado de la operación del estado de autenticación.
 */
sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val message: String) : Response<Nothing>()
}