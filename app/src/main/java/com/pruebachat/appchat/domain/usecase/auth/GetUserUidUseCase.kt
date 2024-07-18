package com.pruebachat.appchat.domain.usecase.auth

import com.pruebachat.appchat.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Caso de uso que maneja la obtención del UID del usuario actual.
 *
 * @param authenticationRepository Repositorio de autenticación que proporciona métodos para obtener el UID del usuario.
 */
class GetUserUidUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke() = flow { emit(authenticationRepository.userUid()) }
}