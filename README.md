# AppChat
El objetivo de este proyecto es proporcionar una aplicación móvil moderna y eficiente que permita a los usuarios autenticarse de manera segura, intercambiar mensajes de chat multimedia y compartir su ubicación geográfica de manera intuitiva y eficaz.

Descripción del Proyecto:

Este proyecto es una aplicación móvil desarrollada en Kotlin utilizando Jetpack Compose y sigue los principios de arquitectura limpia (Clean Architecture), implementando Dagger Hilt para la inyección de dependencias.

Características Principales:

Autenticación: Utiliza Firebase Authentication para permitir el registro y login mediante email y password.

Almacenamiento de Datos: Utiliza Firebase Firestore para almacenar los mensajes del chat, incluyendo texto, imágenes y georeferencias.

Almacenamiento de Imágenes: Las imágenes enviadas a través del chat se guardan en Firebase Storage.

Tecnologías Utilizadas:

Kotlin: Lenguaje de programación principal.

Jetpack Compose: Biblioteca moderna de UI para construir interfaces de usuario nativas de manera declarativa.

Clean Architecture: Diseño que separa las responsabilidades en capas para facilitar la mantenibilidad y testabilidad del código.

Dagger Hilt: Biblioteca de inyección de dependencias para Android, que facilita la organización y gestión de las dependencias en la aplicación.

Firebase Authentication: Para la autenticación de usuarios mediante email y password.

Firebase Firestore: Base de datos en tiempo real de Firebase para almacenar y sincronizar datos entre usuarios y dispositivos.

Firebase Storage: Para el almacenamiento de archivos binarios como imágenes.
