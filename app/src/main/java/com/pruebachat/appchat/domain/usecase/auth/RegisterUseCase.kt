package com.pruebachat.appchat.domain.usecase.auth

import com.pruebachat.appchat.domain.repository.AuthenticationRepository
import javax.inject.Inject

/**
 * Caso de uso que maneja la lógica de registro de usuarios.
 *
 * @param authenticationRepository Repositorio de autenticación que proporciona métodos para realizar operaciones de registro.
 */
class RegistroUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authenticationRepository.registro(email, password)
}