package com.pruebachat.appchat.domain.repository

import com.google.firebase.auth.AuthResult
import com.pruebachat.appchat.core.Response
import kotlinx.coroutines.flow.Flow


/**
 * Interfaz que define las operaciones para un repositorio de autenticación.
 */
interface AuthenticationRepository {

    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param email Dirección de correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Flow que emite un objeto Response con el resultado de la operación de inicio de sesión.
     */
    suspend fun login(email: String, password: String): Flow<Response<AuthResult>>


    /**
     * Registra un nuevo usuario con las credenciales proporcionadas.
     *
     * @param email  del nuevo usuario.
     * @param password del nuevo usuario.
     * @return Flow que emite un objeto Response con el resultado de la operación de registro.
     */
    suspend fun registro(email: String, password: String): Flow<Response<AuthResult>>


    /**
     * Cierra la sesión del usuario actualmente autenticado.
     */
    suspend fun logout()


    /**
     * Obtiene el UID del usuario actualmente autenticado.
     *
     * @return String que representa el UID del usuario, o una cadena vacía si no hay usuario autenticado.
     */
    suspend fun userUid(): String


}