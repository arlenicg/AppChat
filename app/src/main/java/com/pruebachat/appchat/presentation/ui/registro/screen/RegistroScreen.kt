package com.pruebachat.appchat.presentation.ui.registro.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pruebachat.appchat.R
import com.pruebachat.appchat.presentation.ui.login.screen.Email
import com.pruebachat.appchat.presentation.ui.login.screen.Password
import com.pruebachat.appchat.presentation.ui.registro.viewmodel.RegistroViewModel
import com.pruebachat.appchat.ui.theme.orange

/* *[RegistroScreen] Pantalla de registro de usuario.
 *
 * Esta pantalla permite a los usuarios registrar sus crendeciales y almacenar la informacion en Firebase.
  * @registroViewModel viewModel específico para manejar la lógica de registro de usuario  y la gestión de estados.
 * @navController navController NavController para la navegación entre pantallas.
 */
@Composable
fun RegistroScreen(
    registroViewModel: RegistroViewModel,
    navController: NavController
) {
    val isLoading by registroViewModel.isLoading.observeAsState(false)
    val successMessage by registroViewModel.successMessage.observeAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column {
            ImageBack(navController)

            Spacer(modifier = Modifier.height(52.dp))

            BodyRecover(registroViewModel)

            Spacer(modifier = Modifier.height(20.dp))
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            registroViewModel.onShowSuccessMessageComplete()
        }
    }
}


@Composable
fun ImageBack(navController: NavController) {

    Box(
        modifier = Modifier

            .offset(x = 22.dp, y = 56.dp)
            .requiredSize(size = 41.dp)
    ) {
        Box(
            modifier = Modifier
                .requiredSize(size = 41.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .border(
                    border = BorderStroke(1.dp, Color(0xffe8ecf4)),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {

            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(R.string.content_description_back_arrow),
                colorFilter = ColorFilter.tint(Color(0xff1e232c)),
                modifier = Modifier

                    .requiredSize(24.dp)
                    .clickable { navController.navigateUp() }
            )
        }
    }


}

@Composable
fun BodyRecover(registroViewModel: RegistroViewModel) {
    val email: String by registroViewModel.email.observeAsState(initial = "")
    val password: String by registroViewModel.password.observeAsState(initial = "")
    val isEnabled: Boolean by registroViewModel.isEnable.observeAsState(
        initial = false
    )
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = stringResource(R.string.title_registro),
            color = Color(0xff566a7f),
            lineHeight = 1.25.em,
            style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.title_subtitleregistro),
            color = Color(0xff8391a1),
            lineHeight = 1.38.em,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier

                .fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(20.dp))

        Email(email) {
            registroViewModel.onChanged(email = it, password = password)

        }
        Spacer(modifier = Modifier.height(20.dp))


        Password(password) {
            registroViewModel.onChanged(email = email, password = it)
        }


        Spacer(modifier = Modifier.height(20.dp))


        Mensaje(registroViewModel)


        Spacer(modifier = Modifier.height(20.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = Color(0xff5c4ec9))
            )
            ButtonSendCode(registroViewModel, isEnabled)
        }
    }
}

@Composable
fun Mensaje(registroViewModel: RegistroViewModel) {

    val recoveryMessage: String by registroViewModel.message.observeAsState(
        initial = ""
    )
    Text(
        text = recoveryMessage,
        color =orange,
        textAlign = TextAlign.Center,
        lineHeight = 1.em,
        style = TextStyle(
            fontSize = 18.sp, fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .fillMaxWidth()

    )
}


@SuppressLint("ResourceAsColor")
@Composable
fun ButtonSendCode(
    registroViewModel: RegistroViewModel,
    isRecoveryEnabled: Boolean,

) {
    Button(
        shape = RoundedCornerShape(8.dp),
        onClick = { registroViewModel.onSelected() },
        enabled = isRecoveryEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),

        colors = ButtonDefaults.buttonColors(

            contentColor = orange,
            disabledContentColor =orange,

            )
    ) {

        Text(
            text = stringResource(R.string.title_registrase),
            style = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        )
    }
}




