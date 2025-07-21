package com.ppp.pegasussociety.Login

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ppp.pegasussociety.utils.LoadingAlertDialog
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.ppp.pegasussociety.CountryData.CountryCode
import com.ppp.pegasussociety.R
import com.ppp.pegasussociety.SharedPrefManager
import com.ppp.pegasussociety.ui.theme.Greeny
import com.ppp.pegasussociety.ui.theme.LightGreeny
import com.ppp.pegasussociety.utils.Custombtn2


@Composable
fun LoginScreen(navController: NavController? = null) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val loginCode by loginViewModel.loginResponseCode.collectAsState()
    val showProgressDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val otpResult by loginViewModel.otpResult
    val isLoading by loginViewModel.isVerifying.collectAsState()
    val isOnline by loginViewModel.isOnline.collectAsState()

    val countryList = loginViewModel.countryList.value
    val selectedCountry = loginViewModel.selectedCountryCode.value

    val sharedPrefManager = SharedPrefManager(context)

    LaunchedEffect(Unit) {
        loginViewModel.fetchCountries()
    }

    if (showProgressDialog.value) {
        LoadingAlertDialog()
    }

    if (loginCode == 200) {
        loginViewModel.isLogin = true

        //Log.d("Login", "Login Successful with code $loginCode")
    }

    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
        topBar = {}
    ) { padding ->

        if (!isOnline) {
            OfflineBanner()
        }


        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(padding)
        ) {
            val screenHeight = maxHeight
            val screenWidth = maxWidth
            val titleFontSize = when {
                screenWidth < 360.dp -> 24.sp
                screenWidth < 480.dp -> 28.sp
                else -> 32.sp
            }

            Image(
                painter = painterResource(id = R.drawable.loginspacing),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState) // ✅ makes it scrollable
                    .padding(horizontal = screenWidth * 0.05f)
                    .systemBarsPadding(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.40f))

                Text(
                    text = "Login",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = titleFontSize,
                    color = Greeny
                )

                otpResult?.let {
                    Text(
                        text = it,
                        color = if (it.startsWith("Error")) Color.Red else Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }

                CountryCodeDropdown(
                    countryList = countryList,
                    selectedCountry = selectedCountry,
                    onCountrySelected = { loginViewModel.onCountrySelected(it) }
                )

                Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                CustomTextField2(
                    value = loginViewModel.email,
                    label = "Email address or phone number"
                ) {
                    loginViewModel.onEmailTextChanged(it)
                    sharedPrefManager.saveEmail(it)
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                CustombtnLoading(
                    text = "Continue",
                    background = Greeny,
                    showProgress = isLoading
                ) {
                    coroutineScope.launch {
                        keyboardController?.hide()

                        val input = loginViewModel.email.trim()
                        val selectedCountry = loginViewModel.selectedCountryCode.value

                        if (input.isBlank() || selectedCountry == null) {
                            snackbarHostState.showSnackbar("Please fill in all required fields.")
                            return@launch
                        }

                        val finalInput = if (input.matches(Regex("^[0-9]{6,15}$"))) {
                            val dialCode = selectedCountry.dial_code.removePrefix("+")
                            dialCode + input
                        } else {
                            input
                        }

                        snackbarHostState.showSnackbar("Please Wait...")

                        loginViewModel.verifyPhoneOrEmailNow(input).collect { result ->
                            result.onSuccess { response ->
                                val message = response.message
                                if (message.contains("Email exists.", ignoreCase = true) ||
                                    message.contains("Mobile number exists.", ignoreCase = true)) {

                                    // Already saved to SharedPreferences in ViewModel
                                    navController?.navigate("otpscreen/${Uri.encode(finalInput)}")
                                    Toast.makeText(context, "Sending OTP to $finalInput", Toast.LENGTH_LONG).show()
                                } else {
                                    snackbarHostState.showSnackbar("User not registered")
                                    navController?.navigate("signup")

                                }
                            }.onFailure {

                                snackbarHostState.showSnackbar("Please SignUp first !!")
                                navController?.navigate("signup")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                LineWithText("Don't have an Account?")

                Spacer(modifier = Modifier.height(screenHeight * 0.01f))

                Custombtn2(text = "Register Now", background = LightGreeny) {
                    navController?.navigate("signup")
                }

                Spacer(modifier = Modifier.height(screenHeight * 0.01f))
            }
        }
    }
}

@Composable
fun OfflineBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3F3) // Soft light red
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = "Offline",
                tint = Color(0xFFD32F2F), // Red
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Oops! You're offline 😢",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Please check your internet connection.",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun LineWithText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(modifier = Modifier.weight(1f))
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(modifier = Modifier.weight(1f))
    }
}

@Composable
fun CustombtnLoading(
    text: String,
    background: Color,
    modifier: Modifier = Modifier,
    showProgress: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = background,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = !showProgress) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (showProgress) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    maxLines = 1,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .background(background)
                        .padding(5.dp, 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField2(
    value: String,
    label: String,
    prefix: String? = null,
    onValueChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)

            // ✅ Auto-hide keyboard when `.com` is typed
            if (it.contains(".com") && it.length >= 5) {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        },
        label = { Text(label, color = Color.Gray) },
        singleLine = true,
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = LightGreeny,
            focusedBorderColor = Greeny,
            cursorColor = Greeny
        ),
        leadingIcon = prefix?.let {
            { Text(text = it, color = Color.White, modifier = Modifier.padding(start = 18.dp)) }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodeDropdown(
    countryList: List<CountryCode>,
    selectedCountry: CountryCode?,
    onCountrySelected: (CountryCode) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSheetOpen by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredList = remember(searchQuery, countryList) {
        if (searchQuery.isBlank()) countryList
        else countryList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.dial_code.contains(searchQuery) ||
                    it.code.contains(searchQuery, ignoreCase = true)
        }
    }

    // Bottom sheet when open
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                isSheetOpen = false
                searchQuery = ""
            },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search Country Code") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(filteredList) { country ->
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCountrySelected(country)
                                    isSheetOpen = false
                                    searchQuery = ""
                                },
                            headlineContent = {
                                Text("${country.flag} ${country.name} (${country.dial_code})")
                            }
                        )
                    }
                }
            }
        }
    }

    // Full clickable text field
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isSheetOpen = true }
    ) {
        OutlinedTextField(
            value = selectedCountry?.let { "${it.flag}   ${it.name}  (${it.dial_code})" } ?: "",
            onValueChange = {},
            label = { Text("Select Country") },
            readOnly = true,
            enabled = false, // disable to prevent focus issues
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Black,
                disabledBorderColor = LightGreeny,
                disabledTrailingIconColor = Greeny,
                disabledLabelColor = Color.Gray
            )
        )
    }
}



