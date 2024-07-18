package com.pruebachat.appchat.presentation.ui.home.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.pruebachat.appchat.R

import com.pruebachat.appchat.presentation.ui.home.viewmodel.HomeViewModel
import com.pruebachat.appchat.presentation.ui.widget.ImageMessage
import com.pruebachat.appchat.presentation.ui.widget.TextMessage
import com.pruebachat.appchat.utils.Constants

/* *[HomeScreen] Pantalla que presenta el Chat despúes de iniciar sesión exitosamente.
  * @HomeScreen viewModel específico para manejar la lógica de la carga de mensajes del chat y comunicación para envio de mensajes.

 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
) {
    val message: String by homeViewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by homeViewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val currentLocation by homeViewModel::currentLocation

    val context = LocalContext.current

        // ActivityResultLauncher para seleccionar imágenes
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->

                // Llamar al ViewModel para realizar el envio de la imagen
                homeViewModel.uploadImageToStorage(uri, context) { imageUrl ->
                    homeViewModel.addImageMessage(imageUrl)
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(
                onClick =  { homeViewModel.logout() },
                modifier = Modifier.padding(6.dp)
            ) {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = "Info Icon")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.85f, fill = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean

                val isImage = message[Constants.IS_IMAGE] as Boolean? ?: false

                if (isImage) {
                    // si es una imagen, dibuja en el chat
                    ImageMessage(
                        imageUrl = message[Constants.MESSAGE].toString(),
                        isCurrentUser = isCurrentUser
                    )
                } else {
                    //si es texto, dibuja en el chat
                    TextMessage(
                        message = message[Constants.MESSAGE].toString(),
                        isCurrentUser = isCurrentUser
                    )
                }
            }
        }
        OutlinedTextField(
            value = message,
            onValueChange = {
                homeViewModel.updateMessage(it)
            },
            label = {
                Text(
                    "Escribe tu mensaje"
                )
            },
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 1.dp)
                .fillMaxWidth()
                .weight(weight = 0.09f, fill = true),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,

            trailingIcon = {
                Row {
                    IconButton(  onClick =  {
                        // evento para realizar la selección de imagenes de la galeria del usuario
                        activityResultLauncher.launch(homeViewModel.pickImage())
                    }) {
                        Icon(imageVector = Icons.Filled.CameraAlt, contentDescription = stringResource(
                            R.string.content_description_camera))
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    IconButton(  onClick = {

                      //verificación y solicitud de permisos de localización
                        locationPermissions.launchMultiplePermissionRequest()
                        if (locationPermissions.allPermissionsGranted) {
                            homeViewModel.getCurrentLocation()
                        }
                        //envio de ubicación
                        homeViewModel.addMessageLocation(currentLocation)




                    }) {
                        Icon(imageVector = Icons.Filled.LocationOn, contentDescription =  stringResource(
                            R.string.content_description_send))
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    IconButton(  onClick = {
                        homeViewModel.addMessage()
                    }) {
                        Icon(imageVector = Icons.Filled.Send, contentDescription =  stringResource(
                            R.string.content_description_send))
                    }
                }

            }
        )
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let { location ->
            val locationMessage = "Mi ubicación: Latitud:${location.latitude}, Longitud:${location.longitude}"
            homeViewModel.updateMessage(locationMessage)
            homeViewModel.addMessage()
        }
    }

}

