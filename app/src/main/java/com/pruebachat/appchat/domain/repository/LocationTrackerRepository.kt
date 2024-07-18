package com.pruebachat.appchat.domain.repository

import android.location.Location

/**
 * Interfaz que define la operación para obtener la ubicación actual del dispositivo.
 */
interface LocationTrackerRepository {
    suspend fun getCurrentLocation(): Location?
}