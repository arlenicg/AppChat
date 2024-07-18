package com.pruebachat.appchat.domain.usecase.auth

import android.location.Location
import com.pruebachat.appchat.domain.repository.LocationTrackerRepository
import javax.inject.Inject

// UseCase para obtener la ubicación actual
class GetCurrentLocationUseCase @Inject constructor(private val locationTrackerRepository: LocationTrackerRepository) {

    // Método principal que es llamado desde el viewmodel
    suspend operator fun invoke(): Location? {
        return locationTrackerRepository.getCurrentLocation()
    }
}