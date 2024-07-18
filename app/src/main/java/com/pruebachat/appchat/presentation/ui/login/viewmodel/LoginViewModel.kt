package com.pruebachat.appchat.presentation.ui.login.viewmodel

import android.annotation.SuppressLint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.firebase.auth.AuthResult
import com.pruebachat.appchat.core.Response
import com.pruebachat.appchat.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel para la pantalla de inicio de sesión.
 *
 * Esta clase gestiona la lógica y el estado relacionados con el inicio de sesión de usuario.
 * [loginUseCase]  Caso de uso  para ejecutar la lógica de negocio del inicio de sesión.
 *
  * @property navigateToHomeScreen LiveData booleano que indica si se debe navegar a la pantalla principal después de un inicio de sesión exitoso.
 * @property email LiveData que contiene el correo electrónico ingresado por el usuario.
 * @property password LiveData que contiene la contraseña ingresada por el usuario.
 * @property isLoginEnable LiveData booleano que indica si el botón de inicio de sesión está habilitado.
 * @property isLoading LiveData booleano que indica si la aplicación está en un estado de carga.
 * @property loginMessage LiveData que contiene mensajes relacionados con el inicio de sesión, como errores o mensajes de éxito.
 * @property loginFlow Flujo compartido de respuestas de inicio de sesión.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase): ViewModel() {

    private val _navigateToHomeScreen = MutableLiveData<Boolean>()
    val navigateToHomeScreen: LiveData<Boolean> = _navigateToHomeScreen

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginMessage = MutableLiveData<String>()
    val loginMessage: LiveData<String> = _loginMessage

    private val _login = MutableSharedFlow<Response<AuthResult>>()
    val loginFlow = _login


    /**
     * Actualiza el valor de [_email], [_password] y [_isLoginEnable] cuando cambian los datos de inicio de sesión.
     *
     * @param email Email ingresado por el usuario.
     * @param password Password ingresado por el usuario.
     */

    fun onLoginChanged(email: String, password: String) {

        _email.value = email
        _password.value = password
        _isLoginEnable.value = enableLogin(email, password)

    }

    /**
     * Verifica si el botón de inicio de sesión debe estar habilitado basado de acuerdo a la validación del email y password.
     *
     * @param email Email ingresado por el usuario.
     * @param password Password ingresado por el usuario.
     * @return `true` si ambos campos no están vacíos, `false` de lo contrario.
     */

    fun enableLogin(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()

    }


    /**
     * Método llamado cuando el usuario selecciona la opción de inicio de sesión.
     *
     * Este método maneja el flujo de inicio de sesión, actualizando [_isLoading] y [_loginMessage],
         */

    @SuppressLint("HardwareIds")
    fun onLoginSelected() {
        _isLoginEnable.value = false
        _loginMessage.value = ""
        viewModelScope.launch {


            _isLoading.value = true
            try {

                loginUseCase.invoke(email.value!!, password.value!!).collect { response ->
                    _login.emit(response)
                    when (response) {
                        is Response.Loading -> {
                            _isLoading.value = true
                        }
                        is Response.Success -> {
                            _navigateToHomeScreen.value = true
                        }
                        is Response.Error -> {

                            _loginMessage.value = response.message.trim()
                        }
                    }
                }





            } catch (e: Exception) {

                _loginMessage.value = e.message
                _isLoading.value = false
            } finally {
                _isLoading.value = false


            }
            _isLoading.value = false
        }
    }

    /**
     * Método llamado para restablecer [_navigateToHomeScreen] después de la navegación a la pantalla principal.
     */
    fun onNavigateToHomeScreenComplete() {
        _navigateToHomeScreen.value = false
    }



}