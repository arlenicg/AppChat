package com.pruebachat.appchat.presentation.ui.home.viewmodel

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.pruebachat.appchat.domain.usecase.auth.GetCurrentLocationUseCase
import com.pruebachat.appchat.domain.usecase.auth.GetUserUidUseCase
import com.pruebachat.appchat.domain.usecase.auth.LogoutUseCase
import com.pruebachat.appchat.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val locationUseCase: GetCurrentLocationUseCase
) : ViewModel() {
    init {
        getMessages()
    }
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    private val _navigateToLoginScreen = MutableLiveData<Boolean>()
    val navigateToLoginScreen: LiveData<Boolean> get() = _navigateToLoginScreen

    /**
     * Método Cierra la sesión del usuario actual en Firebase Auth.
     *
     * Esta función llama al caso de uso [logoutUseCase] para cerrar la sesión del usuario actual.
     * Posteriormente, establece el valor de [_navigateToLoginScreen] a `true` para navegar a la pantalla de inicio de sesión.
     */
    fun logout() = viewModelScope.launch {
        logoutUseCase.invoke()
        _navigateToLoginScreen.value = true

    }


    /**
     * Método que crea y devuelve un intent para seleccionar una imagen desde la galería.

     */

    fun pickImage(): Intent {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        return intent
    }


    /**
     * Método que envia una imagen al almacenamiento de Firebase (FireStore).
     *
     * Esta función toma un URI de imagen seleccionado, lo carga en el almacenamiento de Firebase bajo
     * el directorio "chat_images" con un nombre único generado aleatoriamente, y llama a [onComplete]
     * con la URL de descarga del archivo una vez que la carga ha sido exitosa.
     *
     * @param uri URI de la imagen que se va a subir.
     * @param context Contexto utilizado para mostrar mensajes Toast en caso de fallo en la carga.
     * @param onComplete Callback que se llama con la URL de descarga de la imagen una vez que se completa la carga.
     */


    fun uploadImageToStorage(uri: Uri, context: Context, onComplete: (String) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference.child("chat_images/${UUID.randomUUID()}")
        storageReference.putFile(uri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { url ->
                    onComplete(url.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Método que Agrega un mensaje de imagen a la colección de mensajes en Firestore de Firebase.
     *
     * Esta función crea un documento nuevo en la colección de mensajes utilizando [Firebase.firestore].
     * El documento contiene la URL de la imagen [imageUrl], un indicador [Constants.IS_IMAGE] que especifica
     * que es un mensaje de imagen, el ID del usuario que envía el mensaje [Constants.SENT_BY], y el
     * tiempo en milisegundos [Constants.SENT_ON] cuando se envió el mensaje.
     *
     * Después de agregar exitosamente el mensaje de imagen, establece el valor de [_message] a una cadena vacía.
     *
     * @param imageUrl URL de la imagen que se va a agregar al mensaje.
     */

     fun addImageMessage(imageUrl: String) {
        viewModelScope.launch {
            Firebase.firestore.collection(Constants.MESSAGES).document().set(
                hashMapOf(
                    Constants.MESSAGE to imageUrl,
                    Constants.IS_IMAGE to true,
                    Constants.SENT_BY to Firebase.auth.currentUser?.uid,
                    Constants.SENT_ON to System.currentTimeMillis()
                )
            ).addOnSuccessListener {
                _message.value = ""
            }
        }
    }

    //Variable que contendrá el estado de ubicación actual y se actualizará con la función getCurrentLocation.
    var currentLocation by mutableStateOf<Location?>(null)



    /**
     * Obtención de  ubicación actual del dispositivo.
     *
     * Esta función utiliza un caso de uso [locationUseCase] para obtener la ubicación actual del dispositivo
     * en un hilo de ejecución de entrada/salida (IO). La ubicación resultante se asigna a la propiedad
     * [currentLocation].
     */

    fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            currentLocation = locationUseCase.invoke() // Location

        }
    }


    // Obtenemos el valor de la georeferencia para hacer el envio automático del mensaje en el chat

     fun addMessageLocation(location: Location?) {
        val locationMessage = location?.let {
            "Mi ubicación: Latitud:${it.latitude}, Longitud:${it.longitude}"
        } ?: ""

        updateMessage(locationMessage)
        addMessage()
    }

    /**
     * Actualización del valor de message
     */
    fun updateMessage(message: String) {
        _message.value = message
    }

    /**
     * Envio de mensajes
     */
     fun addMessage() {

        viewModelScope.launch {
            val message: String = _message.value ?: throw IllegalArgumentException("Mensaje vacío")
            if (message.isNotEmpty()) {
                Firebase.firestore.collection(Constants.MESSAGES).document().set(
                    hashMapOf(
                        Constants.MESSAGE to message,
                        Constants.SENT_BY to Firebase.auth.currentUser?.uid,
                        Constants.SENT_ON to System.currentTimeMillis()
                    )
                ).addOnSuccessListener {
                    _message.value = ""
                }
            }

        }
    }




    /**
     * Se obtiene mensajes para presentar en pantalla
     */
    private fun getMessages() {
        Firebase.firestore.collection(Constants.MESSAGES)
            .orderBy(Constants.SENT_ON)
            .addSnapshotListener { value, e ->
                if (e != null) {

                    return@addSnapshotListener
                }

                val list = emptyList<Map<String, Any>>().toMutableList()

                if (value != null) {
                    for (doc in value) {
                        val data = doc.data
                        data[Constants.IS_CURRENT_USER] =
                            Firebase.auth.currentUser?.uid.toString() == data[Constants.SENT_BY].toString()

                        list.add(data)
                    }
                }

                updateMessages(list)
            }
    }

    /**
     * Actualizamos la lista después de obtener los detalles del chat a través de Firestore
     */
    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }
}