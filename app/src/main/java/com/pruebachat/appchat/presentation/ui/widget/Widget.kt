package com.pruebachat.appchat.presentation.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter

import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.em
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Image
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter


/// Widget que del diseño de la card que contiene el texto mensaje enviado o recibido por el usuario en el chat
@Composable
fun TextMessage(message: String, isCurrentUser: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp),

        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) MaterialTheme.colorScheme.primary else Color.White
        )
    ) {
        Text(
            text = message,
            textAlign = if (isCurrentUser) TextAlign.End else TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = if (isCurrentUser) Color.White else MaterialTheme.colorScheme.primary
        )
    }
}




/// Widget que dibuja la imagen  enviado o recibida por el usuario en el chat
@Composable
fun ImageMessage(imageUrl: String, isCurrentUser: Boolean) {
    val horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
    }
}
