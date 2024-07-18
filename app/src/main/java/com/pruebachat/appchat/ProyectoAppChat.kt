package com.pruebachat.appchat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * [ProyectoAppChat]  clase de aplicación principal para la configuración de Hilt.
 *
 * Anotación [@HiltAndroidApp]  para inicializar Hilt y
 * configurar el contenedor de dependencias a nivel de  aplicación.
 */
@HiltAndroidApp
class ProyectoAppChat : Application() {

}