package com.ppp.pegasussociety.Login

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ppp.pegasussociety.SharedPrefManager
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(navController: NavController, userIdentifier: String) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = hiltViewModel()
    val saveEmail = remember { SharedPrefManager(context).getEmail() ?: "" }
    val saveMobile = remember{SharedPrefManager(context).getPhone() ?: ""}

    val otpLength = 4
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    val otpStatus by loginViewModel.otpStatus.collectAsState()
    val isLoading by loginViewModel._isLoading
    var triggerErrorAnim by remember { mutableStateOf(false) }

    var secondsLeft by remember { mutableStateOf(30) }
    val isTimerRunning = remember { mutableStateOf(true) }

    LaunchedEffect(userIdentifier) {
        //Log.d("OtpScreen", "Received identifier: $userIdentifier")
        loginViewModel.handleSendOTP(userIdentifier)
    }

    LaunchedEffect(otpStatus) {
        when {
            otpStatus == "sent" -> {
                Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show()
            }
            otpStatus == "success" -> {
                navController.navigate("home") {
                    popUpTo("otpscreen") { inclusive = true }
                }
            }
            otpStatus.startsWith("error") -> {
                Toast.makeText(context, otpStatus.removePrefix("error: "), Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(isTimerRunning.value) {
        if (isTimerRunning.value) {
            while (secondsLeft > 0) {
                delay(1000)
                secondsLeft--
            }
            isTimerRunning.value = false
        }
    }

    LaunchedEffect(Unit) {
        delay(500)
        focusRequesters.first().requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter the OTP sent to", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))

        Text(userIdentifier, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            otpValues.forEachIndexed { i, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            otpValues[i] = newValue
                            if (newValue.isNotEmpty() && i < otpLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }

                            // ✅ Reset OTP error status when user starts typing again
                            //loginViewModel.resetOtpStatus()
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .focusRequester(focusRequesters[i]),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isTimerRunning.value) {
            TextButton(
                onClick = {
                    secondsLeft = 30
                    isTimerRunning.value = true
                    loginViewModel.handleSendOTP(saveEmail)
                }
            ) {
                Text("Resend OTP", color = Color(0xFFbb2030))
            }
        } else {
            Text("Resend in ${secondsLeft}s", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val enteredOtp = otpValues.joinToString("")
                if (enteredOtp.length == otpLength) {
                    if (loginViewModel.reqId.isNotEmpty()) {
                        loginViewModel.handleVerifyOtp(enteredOtp)
                    } else {
                        triggerErrorAnim = true
                        Toast.makeText(context, "OTP not yet sent. Please wait.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    triggerErrorAnim = true
                }
            },
            // ✅ Always enabled when fields are filled
            enabled = otpValues.all { it.isNotEmpty() },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFbb2030),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Verify", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}



/*@Composable
fun OtpScreen(navController: NavController, userIdentifier: String) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = hiltViewModel()
    val saveEmail = remember { SharedPrefManager(context).getEmail() ?: "" }

    val otpLength = 6
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    val otpStatus by loginViewModel.otpStatus.collectAsState()
    val isLoading by loginViewModel._isLoading
    var triggerErrorAnim by remember { mutableStateOf(false) }

    var secondsLeft by remember { mutableStateOf(30) }
    val isTimerRunning = remember { mutableStateOf(true) }

 //   val userIdentifier = loginViewModel.userIdentifier.value

 *//*   LaunchedEffect(userIdentifier) {
        userIdentifier?.let {
            //Log.d("OtpScreen", "Calling handleSendOTP with: $it")
            loginViewModel.handleSendOTP(it)
        } ?: run {
            //Log.e("OtpScreen", "userIdentifier is null")
        }
    }*//*
    LaunchedEffect(userIdentifier) {
        //Log.d("OtpScreen", "Received identifier: $userIdentifier")
        loginViewModel.handleSendOTP(userIdentifier)
    }

    LaunchedEffect(otpStatus) {
        when {
            otpStatus == "sent" -> {
                Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show()
            }
            otpStatus == "success" -> {
                navController.navigate("home") {
                    popUpTo("otpscreen") { inclusive = true }
                }
            }
            otpStatus.startsWith("error") -> {
                Toast.makeText(context, otpStatus.removePrefix("error: "), Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(isTimerRunning.value) {
        if (isTimerRunning.value) {
            while (secondsLeft > 0) {
                delay(1000)
                secondsLeft--
            }
            isTimerRunning.value = false
        }
    }

    LaunchedEffect(Unit) {
        delay(500)
        focusRequesters.first().requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter the OTP sent to", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))
        Text(saveEmail, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            otpValues.forEachIndexed { i, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            otpValues[i] = newValue
                            if (newValue.isNotEmpty() && i < otpLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .focusRequester(focusRequesters[i]),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isTimerRunning.value) {
            TextButton(
                onClick = {
                    secondsLeft = 30
                    isTimerRunning.value = true
                    loginViewModel.handleSendOTP(saveEmail)
                }
            ) {
                Text("Resend OTP", color = Color(0xFFbb2030))
            }
        } else {
            Text("Resend in ${secondsLeft}s", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val enteredOtp = otpValues.joinToString("")
                if (enteredOtp.length == otpLength) {
                    if (loginViewModel.reqId.isNotEmpty()) {
                        loginViewModel.handleVerifyOtp(enteredOtp)
                    } else {
                        triggerErrorAnim = true
                        Toast.makeText(context, "OTP not yet sent. Please wait.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    triggerErrorAnim = true
                }
            },
            enabled = otpValues.all { it.isNotEmpty() } && otpStatus == "sent",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFbb2030),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Verify", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}*/


/*@Composable
fun OtpScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = hiltViewModel()
    val saveEmail = remember { SharedPrefManager(context).getEmail() ?: "" }

    val otpLength = 6
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    var triggerErrorAnim by remember { mutableStateOf(false) }
    val loginCode by viewModel.loginResponseCode.collectAsState()
    val isLoading by viewModel._isLoading

    // Resend OTP Timer
    var secondsLeft by remember { mutableStateOf(30) }
    val isTimerRunning = remember { mutableStateOf(true) }

    val otpStatus by viewModel.otpStatus.collectAsState()
  //  val context = LocalContext.current
    val reqId = viewModel.reqId


    LaunchedEffect(otpStatus) {
        when {
            otpStatus == "success" -> {
                navController.navigate("home") {
                    popUpTo("otpscreen") { inclusive = true }
                }
            }
            otpStatus.startsWith("error") -> {
                Toast.makeText(context, otpStatus.removePrefix("error: "), Toast.LENGTH_LONG).show()
            }
        }
    }
    LaunchedEffect(isTimerRunning.value) {
        if (isTimerRunning.value) {
            while (secondsLeft > 0) {
                delay(1000)
                secondsLeft--
            }
            isTimerRunning.value = false
        }
    }

    // Shake animation on error
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(triggerErrorAnim) {
        if (triggerErrorAnim) {
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 1f,
                animationSpec = repeatable(
                    iterations = 4,
                    animation = tween(100, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            triggerErrorAnim = false
        }
    }


    LaunchedEffect(loginCode) {
        if (loginCode == 200) {
            navController.navigate("scanner") {
                popUpTo("OtpScreen") { inclusive = true }
            }
        } else if (loginCode != 0) {
            triggerErrorAnim = true
        }
    }

    // Navigate on success
*//*    LaunchedEffect(otpSuccess) {
        if (isVerifying) {
            delay(1000)
            if (otpSuccess) {
                navController.navigate("next_screen")
            } else {
                isVerifying = false
                triggerErrorAnim = true
            }
        }
    }*//*

    // Safe initial focus after layout is complete
    LaunchedEffect(Unit) {
        delay(500)
        focusRequesters.first().requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter The Verification Code Sent To", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))
        Text("$saveEmail", fontSize = 14.sp, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.offset(x = shakeOffset.value.dp * 8)
        ) {
            for (i in 0 until otpLength) {
                val value = otpValues[i]
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue ->
                        when {
                            newValue.length == 1 && newValue[0].isDigit() -> {
                                otpValues[i] = newValue
                                if (i < otpLength - 1) {
                                    focusRequesters[i + 1].requestFocus()
                                } else {
                                    keyboardController?.hide()
                                }
                            }

                            newValue.length > 1 -> {
                                newValue.filter { it.isDigit() }
                                    .take(otpLength - i)
                                    .forEachIndexed { index, c ->
                                        otpValues[i + index] = c.toString()
                                    }
                                val next = (i + newValue.length).coerceAtMost(otpLength - 1)
                                focusRequesters[next].requestFocus()
                            }

                            newValue.isEmpty() -> {
                                otpValues[i] = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .height(56.dp)
                        .focusRequester(focusRequesters[i])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                                if (otpValues[i].isEmpty() && i > 0) {
                                    otpValues[i - 1] = ""
                                    focusRequesters[i - 1].requestFocus()
                                } else if (otpValues[i].isNotEmpty()) {
                                    otpValues[i] = ""
                                }
                                true
                            } else false
                        },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    singleLine = true,
                    isError = triggerErrorAnim,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (i == otpLength - 1) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (i < otpLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        },
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

       *//* if (!isTimerRunning.value) {
            TextButton(
                onClick = {
                    secondsLeft = 10
                    isTimerRunning.value = true
                    viewModel.sendOtp(saveEmail, context) // ← Your API call
                }
            ) {
                Text("Resend OTP", color = Color(0xFFbb2030))
            }
        } else {
            Text("Resend in ${secondsLeft}s", color = Color.Gray)
        }
*//*
        Spacer(modifier = Modifier.height(24.dp))

     *//*   Button(
            onClick = {
                val enteredOtp = otpValues.joinToString("")
                if (enteredOtp.length == otpLength && saveEmail.isNotEmpty()) {
                    viewModel.login(saveEmail, enteredOtp, context)
                    if(loginCode == 200){
                    navController.navigate("scanner")}
                    else{
                        triggerErrorAnim = true
                        Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    triggerErrorAnim = true
                }
            },*//*
        Button(
            onClick = {
                val enteredOtp = otpValues.joinToString("")

                if (enteredOtp.length == otpLength && saveEmail.isNotEmpty()) {
                    if (reqId.isNotEmpty()) {
                        viewModel.handleVerifyOtp(enteredOtp)
                    } else {
                        Toast.makeText(context, "OTP not sent or reqId missing", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    triggerErrorAnim = true
                }
            },
            enabled = otpValues.all { it.isNotEmpty() } && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFbb2030),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFbb2030).copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Verify", fontSize = 24.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}*/


/*@Composable
fun OtpScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = hiltViewModel()
    val saveEmail = remember { SharedPrefManager(context).getEmail() ?: "" }

    val otpLength = 6
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    var triggerErrorAnim by remember { mutableStateOf(false) }
    val loginCode by viewModel.loginResponseCode.collectAsState()
    val isLoading by viewModel._isLoading

    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(triggerErrorAnim) {
        if (triggerErrorAnim) {
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 1f,
                animationSpec = repeatable(
                    iterations = 4,
                    animation = tween(100, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            triggerErrorAnim = false
        }
    }

    LaunchedEffect(loginCode) {
        if (loginCode == 200) {
            navController.navigate("scanner") {
                popUpTo("OtpScreen") { inclusive = true }
            }
        } else if (loginCode != 0) {
            triggerErrorAnim = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter OTP", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.offset(x = shakeOffset.value.dp * 8)
        ) {
            for (i in 0 until otpLength) {
                val value = otpValues[i]
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue ->
                        when {
                            newValue.length == 1 && newValue[0].isDigit() -> {
                                otpValues[i] = newValue
                                if (i < otpLength - 1) {
                                    focusRequesters[i + 1].requestFocus()
                                } else {
                                    keyboardController?.hide()
                                }
                            }

                            newValue.length > 1 -> {
                                newValue.filter { it.isDigit() }
                                    .take(otpLength - i)
                                    .forEachIndexed { index, c ->
                                        otpValues[i + index] = c.toString()
                                    }
                                val next = (i + newValue.length).coerceAtMost(otpLength - 1)
                                focusRequesters[next].requestFocus()
                            }

                            newValue.isEmpty() -> {
                                otpValues[i] = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .height(56.dp)
                        .focusRequester(focusRequesters[i])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                                if (otpValues[i].isEmpty() && i > 0) {
                                    otpValues[i - 1] = ""
                                    focusRequesters[i - 1].requestFocus()
                                } else if (otpValues[i].isNotEmpty()) {
                                    otpValues[i] = ""
                                }
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    singleLine = true,
                    isError = triggerErrorAnim,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (i == otpLength - 1) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (i < otpLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        },
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val enteredOtp = otpValues.joinToString("")
                if (enteredOtp.length == otpLength && saveEmail.isNotEmpty()) {
                    viewModel.login(saveEmail, enteredOtp, context)
                } else {
                    triggerErrorAnim = true
                }
            },
            enabled = otpValues.all { it.isNotEmpty() } && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFbb2030),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFbb2030).copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Verify", fontSize = 24.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}*/


/*@Composable
fun OtpScreen(navController: NavController) {
    val context = LocalContext.current
    val saveEmail = remember { SharedPrefManager(context).getEmail() ?: "" }

    val viewModel: LoginViewModel = hiltViewModel()
//    val isLoading by viewModel._isLoading.collectAsState()
    val loginResponseCode by viewModel.loginResponseCode.collectAsState()
    val sharedPrefManager = SharedPrefManager(context)

    val otpLength = 6
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    var triggerErrorAnim by remember { mutableStateOf(false) }

    val loginCode by viewModel.loginResponseCode.collectAsState()
    val isLoading by viewModel._isLoading

*//*    if (isLoading) {
        LoadingDialog()
    }*//*



    LaunchedEffect(loginCode) {
        if (loginCode == 200) {
          //  navController.navigate("scanner")
        }
    }

    // Shake animation on error
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(triggerErrorAnim) {
        if (triggerErrorAnim) {
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 1f,
                animationSpec = repeatable(
                    iterations = 4,
                    animation = tween(100, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            triggerErrorAnim = false
        }
    }

    // Observe login success
    LaunchedEffect(loginResponseCode) {
        if (loginResponseCode == 200) {
            navController.navigate("scanner") {
                popUpTo("OtpScreen") { inclusive = true }
            }
        } else if (loginResponseCode != 0) {
            triggerErrorAnim = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter OTP", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.offset(x = shakeOffset.value.dp * 8)
        ) {
            for (i in 0 until otpLength) {
                OutlinedTextField(
                    value = otpValues[i],
                    onValueChange = { value ->
                        if (value.length <= 1 && value.all { it.isDigit() }) {
                            otpValues[i] = value
                            if (value.isNotEmpty() && i < otpLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .height(56.dp)
                        .focusRequester(focusRequesters[i])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                                if (otpValues[i].isEmpty() && i > 0) {
                                    otpValues[i - 1] = ""
                                    focusRequesters[i - 1].requestFocus()
                                } else if (otpValues[i].isNotEmpty()) {
                                    otpValues[i] = ""
                                }
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    singleLine = true,
                    isError = triggerErrorAnim,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (i == otpLength - 1) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (i < otpLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        },
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val enteredOtp = otpValues.joinToString("")
             //   val email = viewModel.email//sharedPrefManager.getEmail() ?: ""

                if (enteredOtp.length == otpLength && saveEmail.isNotEmpty()) {
                    //Log.d("OtpScreen", "Entered OTP: $enteredOtp, Email: $saveEmail")

                    viewModel.login(saveEmail, enteredOtp, context)

                    navController.navigate("scanner")

                } else {
                    triggerErrorAnim = true
                }
            },
            enabled = otpValues.all { it.isNotEmpty() } && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFbb2030),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFbb2030).copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Verify",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun LoadingDialog(message: String = "Sending OTP...") {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              CircularProgressIndicator(color = Greeny)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = message, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}*/


/*
@Composable
fun OtpScreen(navController: NavController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current
    val otpLength = 6
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    var isVerifying by remember { mutableStateOf(false) }
    var triggerErrorAnim by remember { mutableStateOf(false) }
    var otpSuccess by remember { mutableStateOf(false) }

    var canResend by remember { mutableStateOf(false) }
    var resendTime by remember { mutableStateOf(30) }

    val shakeOffset = remember { Animatable(0f) }
    val showLoadingDialog = remember { mutableStateOf(true) }
    val otpStatus = loginViewModel.otpStatus.collectAsState()

  */
/*  if(showLoadingDialog.value){
        LoadingBar(true, "Sending OTP")
    }*//*




    if(otpStatus.value == "otp sent"){
        showLoadingDialog.value = false
        Toast.makeText(context, "OTP sent", Toast.LENGTH_SHORT).show()
        //Log.d("Otpload", "${otpStatus.value}")
    }


    // Countdown for resend OTP
    LaunchedEffect(resendTime) {
        if (resendTime > 0) {
            delay(1000)
            resendTime--
        } else {
            canResend = true
        }
    }


    // Shake animation on error
    LaunchedEffect(triggerErrorAnim) {
        if (triggerErrorAnim) {
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 1f,
                animationSpec = repeatable(
                    iterations = 4,
                    animation = tween(100, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            triggerErrorAnim = false
        }
    }

    // Navigate on success
    LaunchedEffect(otpSuccess) {
        if (isVerifying) {
            delay(1000)
            if (otpSuccess) {
                navController.navigate("next_screen")
            } else {
                isVerifying = false
                triggerErrorAnim = true
            }
        }
    }

    // Safe initial focus after layout is complete
    LaunchedEffect(Unit) {
        delay(500)
        focusRequesters.first().requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter OTP", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.offset(x = shakeOffset.value.dp * 8)
        ) {
            for (i in 0 until otpLength) {
                OutlinedTextField(
                    value = otpValues[i],
                    onValueChange = { value ->
                        if (value.length <= 1 && value.all { it.isDigit() }) {
                            otpValues[i] = value
                            if (value.isNotEmpty() && i < otpLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .height(56.dp)
                        .focusRequester(focusRequesters[i])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                                if (otpValues[i].isEmpty() && i > 0) {
                                    otpValues[i - 1] = ""
                                    focusRequesters[i - 1].requestFocus()
                                } else if (otpValues[i].isNotEmpty()) {
                                    otpValues[i] = ""
                                }
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    singleLine = true,
                    isError = triggerErrorAnim,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (i == otpLength - 1) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (i < otpLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        },
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
            }



        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                if (canResend) {
                    otpValues.replaceAll { "" }
                    focusRequesters.first().requestFocus()
                    resendTime = 30
                    canResend = false
                }
            },
            enabled = canResend
        ) {
            Text(
                if (canResend) "Resend OTP" else "Resend OTP in $resendTime sec",
                color = if (canResend) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
              //  val enteredOtp = otpValues.joinToString("")
                // loginViewModel.otp = enteredOtp

            },
            enabled = otpValues.all { it.isNotEmpty() } && !isVerifying,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFbb2030), // Same green as your image
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFbb2030).copy(alpha = 0.5f), // Optional disabled look
                disabledContentColor = Color.White.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(10.dp) // Rounded like in the image
        ) {
            val enteredOtp = otpValues.joinToString("")

            if (enteredOtp.length == otpLength) {
//
                isVerifying = true
                //  otpSuccess = enteredOtp == "123456"
                loginViewModel.login(loginViewModel.email,loginViewModel.otp,context)
            //Log.d("otpemail", "${loginViewModel.email}, ${loginViewModel.otp}")// <-- Replace with actual logic/API
            }



           */
/* if (isVerifying) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Verify",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }*//*

        }

    }
}
*/



/*
@Composable
fun OtpScreen(navController: NavController) {
   //private  val loginViewModel: LoginViewModel = hiltViewModel()
    val otpLength = 6
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = List(otpLength) { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var isVerifying by remember { mutableStateOf(false) }
    var triggerErrorAnim by remember { mutableStateOf(false) }
    var otpSuccess by remember { mutableStateOf(false) }

    var canResend by remember { mutableStateOf(false) }
    var resendTime by remember { mutableStateOf(30) }

    val shakeOffset = remember { Animatable(0f) }

    // Timer for resend
    LaunchedEffect(resendTime) {
        if (resendTime > 0) {
            delay(1000)
            resendTime--
        } else {
            canResend = true
        }
    }

    // Error animation effect
    LaunchedEffect(triggerErrorAnim) {
        if (triggerErrorAnim) {
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 1f,
                animationSpec = repeatable(
                    iterations = 4,
                    animation = tween(100, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            triggerErrorAnim = false
        }
    }

    // OTP verification effect
    LaunchedEffect(otpSuccess) {
        if (isVerifying) {
            delay(1000)
            if (otpSuccess) {
                navController.navigate("next_screen")
            } else {
                isVerifying = false
                triggerErrorAnim = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter OTP", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.offset(x = shakeOffset.value.dp * 8)
        ) {
            for (i in 0 until otpLength) {
                OutlinedTextField(
                    value = otpValues[i],
                    onValueChange = { value ->
                        if (value.length <= 1 && value.all { it.isDigit() }) {
                            otpValues[i] = value
                            if (value.isNotEmpty()) {
                                if (i < otpLength - 1) {
                                    focusRequesters[i + 1].requestFocus()
                                } else {
                                    keyboardController?.hide()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .height(56.dp)
                        .focusRequester(focusRequesters[i])
                        .focusProperties {
                            next = if (i < otpLength - 1) focusRequesters[i + 1] else FocusRequester.Default
                            previous = if (i > 0) focusRequesters[i - 1] else FocusRequester.Default
                        },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    isError = triggerErrorAnim,
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (i == otpLength - 1) ImeAction.Done else ImeAction.Next
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                if (canResend) {
                    otpValues.replaceAll { "" }
                    focusRequesters.first().requestFocus()
                    resendTime = 30
                    canResend = false
                }
            },
            enabled = canResend
        ) {
            Text(
                if (canResend) "Resend OTP" else "Resend OTP in $resendTime sec",
                color = if (canResend) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val enteredOtp = otpValues.joinToString("")
                if (enteredOtp.length == otpLength) {
                    isVerifying = true
                    otpSuccess = enteredOtp == "123456"
                }
            },
            enabled = otpValues.all { it.isNotEmpty() } && !isVerifying,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isVerifying) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Verify")
            }
        }
    }

    // Auto-focus on first field
    LaunchedEffect(Unit) {
        delay(300)
        focusRequesters.first().requestFocus()
    }
}
*/

/*@Preview
@Composable
fun PreviewOtpScreen() {
    OtpScreen(navController = rememberNavController())
}*/



/*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun OtpScreen(
   navController: NavController? = null
) {
    val modifier: Modifier = Modifier
    val onOtpEntered: (String) -> Unit = {}
    val otpLength = 6
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        otpValues.forEachIndexed { index, value ->
            OutlinedTextField(
                value = value,
                onValueChange = { input ->
                    if (input.length <= 1 && input.all { it.isDigit() }) {
                        otpValues[index] = input

                        // Move to next field if input added
                        if (input.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }

                        // Check if OTP complete
                        if (otpValues.all { it.isNotEmpty() }) {
                            onOtpEntered(otpValues.joinToString(""))
                        }
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .width(48.dp)
                    .focusRequester(focusRequesters[index])
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown &&
                            keyEvent.key == Key.Backspace &&
                            otpValues[index].isEmpty()
                        ) {
                            if (index > 0) {
                                otpValues[index - 1] = ""
                                focusRequesters[index - 1].requestFocus()
                            }
                            true
                        } else {
                            false
                        }
                    },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }

    // Automatically focus the first box on launch
    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }
}

@Preview
@Composable
fun PreviewOtpScreen(){
    OtpScreen(navController = rememberNavController())
}
*/
