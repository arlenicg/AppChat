package com.pruebachat.appchat.domain.usecase.auth

import com.pruebachat.appchat.domain.repository.AuthenticationRepository
import javax.inject.Inject


// Caso de uso que maneja la lógica para cerrar sesión
class LogoutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke() = authenticationRepository.logout()
}