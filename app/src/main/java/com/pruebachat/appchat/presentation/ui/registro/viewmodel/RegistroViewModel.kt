package com.pruebachat.appchat.presentation.ui.registro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.pruebachat.appchat.core.Response

import com.pruebachat.appchat.domain.usecase.auth.RegistroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject



/**
 * ViewModel para la pantalla de registro de usuario.
 *
 * Esta clase gestiona la lógica y el estado relacionados con el registro de usuarios.
 * Utiliza un [registroUseCase] para ejecutar la lógica de negocio del registro.
  * @property isLoading LiveData booleano que indica si la aplicación está en un estado de carga.
 * @property email LiveData que contiene el email ingresado por el usuario.
 * @property password LiveData que contiene la password ingresada por el usuario.
 * @property isEnable LiveData booleano que indica si el botón de registro está habilitado.
 * @property message LiveData que contiene mensajes relacionados con el registro, como errores.
 * @property successMessage LiveData que contiene mensajes de éxito relacionados con el registro.
 * @property navigateToLoginScreen LiveData booleano que indica si se debe navegar a la pantalla de inicio de sesión después de un registro exitoso.
 */
@HiltViewModel
class RegistroViewModel @Inject constructor(private val registroUseCase: RegistroUseCase) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isEnable = MutableLiveData<Boolean>()
    val isEnable: LiveData<Boolean> = _isEnable

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

    private val _navigateToLoginScreen = MutableLiveData<Boolean>()
    val navigateToLoginScreen: LiveData<Boolean> get() = _navigateToLoginScreen

    /**
     * Actualiza el valor de [_email], [_password] y [_isEnable] cuando cambian los datos de registro.
     *
     * @param email ingresado por el usuario.
     * @param password ingresado por el usuario.
     */
    fun onChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isEnable.value = enable(email, password)
    }

    /**
     * Verifica si el botón de registro debe estar habilitado basado en la validez del correo electrónico y la contraseña.
     *
     * @param email  ingresado por el usuario.
     * @param password  ingresado or el usuario.
     * @return `true` si ambos campos no están vacíos, `false` de lo contrario.
     */
    private fun enable(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    /**
     * Método llamado cuando el usuario selecciona la opción de registro.
     *
     * Este método maneja el flujo de registro, actualizando [_isLoading], [_successMessage], [_navigateToLoginScreen] y [_message],
     * y emitiendo eventos a través de [registroUseCase].
     */
    fun onSelected() {
        _isEnable.value = false
        _message.value = ""
        viewModelScope.launch {
            _isLoading.value = true
            try {
                registroUseCase.invoke(email.value!!, password.value!!).collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            _isLoading.value = true
                        }
                        is Response.Success -> {
                            _successMessage.value = "Registro correcto"
                            _navigateToLoginScreen.value = true
                            clearFields()
                        }
                        is Response.Error -> {
                            _message.value = response.message
                        }
                    }
                }
            } catch (e: Exception) {
                _message.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia los campos de email,password y estado de habilitación después de un registro exitoso.
     */
    private fun clearFields() {
        _email.value = ""
        _password.value = ""
        _isEnable.value = false
    }

    /**
     * Método llamado para restablecer [_navigateToLoginScreen] después de la navegación a la pantalla de inicio de sesión.
     */
    fun onNavigateToLoginScreenComplete() {
        _navigateToLoginScreen.value = false
    }

    /**
     * Método llamado para restablecer [_successMessage] después de mostrar un mensaje de éxito.
     */
    fun onShowSuccessMessageComplete() {
        _successMessage.value = null
    }
}
