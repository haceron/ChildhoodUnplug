package com.ppp.pegasussociety.Authentication.Signup

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.ppp.pegasussociety.R
import com.ppp.pegasussociety.SharedPrefManager
import com.ppp.pegasussociety.ui.theme.Greeny
import com.ppp.pegasussociety.ui.theme.LightGreeny


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun SignupScreen(navController: NavController? = null) {
    val context = LocalContext.current
    val activity = context as Activity
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val sharedPrefManager = SharedPrefManager(context)
    val signUpViewModel: SignUpViewmodel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val signUpResponseCode by signUpViewModel.signUpResponseCode.collectAsState()
    val isSigningUp by signUpViewModel.isSigningUp.collectAsState()
    /*
        val account by googleViewModel.googleSignInResult.collectAsState()

        var showExtraFields by remember { mutableStateOf(false) }*/
    var showProgressDialog by remember { mutableStateOf(false) }

    /*   var country by remember { mutableStateOf("+91") }
       var isPickerOpen by remember { mutableStateOf(false) }
       val loginViewModel: LoginViewModel = hiltViewModel()*/
    val selectedCountry = signUpViewModel.selectedCountryCode.value
    val countryList = signUpViewModel.countryList.value

    LaunchedEffect(Unit) {
        signUpViewModel.fetchCountries()
    }

    LaunchedEffect(signUpResponseCode) {
        if (signUpResponseCode == 200) {
            sharedPrefManager.saveLoginStatus(true)
            Toast.makeText(context, "Signup Successful.", Toast.LENGTH_SHORT).show()
            navController?.navigate("home") {
                popUpTo("signup") { inclusive = true }
            }
        } else if (signUpResponseCode == 400) {
            Toast.makeText(context, "User already exists !!", Toast.LENGTH_SHORT).show()
        }

    }


    if (showProgressDialog) {
        LoadingAlertDialog()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
        topBar = {}

    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .verticalScroll(rememberScrollState())

        ) {

            Image(
                painter = painterResource(id = R.drawable.registerpadding),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(padding)
                    .systemBarsPadding(),
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Spacer(modifier = Modifier.height(150.dp))
                Text(
                    text = "Register",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    color = Greeny
                )

                Spacer(modifier = Modifier.height(5.dp))
                // Text Fields
                CustomTextField(value = signUpViewModel.NameSP, label = "Name") {
                    signUpViewModel.NameSP = it
                    sharedPrefManager.saveFullName(it)
                }
        /*        CustomTextField(value = signUpViewModel.citySP, label = "City") {
                    signUpViewModel.citySP = it
                    sharedPrefManager.saveCity(it)
                }*/
                CustomTextField2(value = signUpViewModel.emailSP, label = "Email Address") {
                    signUpViewModel.emailSP = it
                    sharedPrefManager.saveEmail(it)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CountryCodeBottomSheetSelector(
                        countryList = countryList,
                        selectedCountry = selectedCountry,
                        onCountrySelected = { signUpViewModel.onCountrySelected(it) },
                        modifier = Modifier.weight(0.4f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    CustomTextFields(
                        value = signUpViewModel.mobileNoSP,
                        label = "Mobile No.",
                        keyboardType = KeyboardType.Phone,
                        modifier = Modifier.weight(0.6f)
                    ) {
                        signUpViewModel.mobileNoSP = it
                        sharedPrefManager.savePhone(it)
                    }
                }

          /*      CustomTextField(
                    value = signUpViewModel.kidsSP,
                    label = "No. of Kids",
                    keyboardType = KeyboardType.Number
                ) {
                    signUpViewModel.kidsSP = it
                    sharedPrefManager.saveKidsNum(it)
                }*/
                Spacer(modifier = Modifier.height(10.dp))
                CustombtnLoad(
                    text = if (isSigningUp) "Verifying..." else "Sign Up",
                    background = Greeny,
                    showProgress = isSigningUp
                ) {
                    if (!isSigningUp) {
                        when {
                            signUpViewModel.NameSP.length < 3 -> {
                                Toast.makeText(context, "Please enter a valid name", Toast.LENGTH_SHORT).show()
                            }
                            signUpViewModel.emailSP.length < 7 -> {
                                Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                            }
                            signUpViewModel.mobileNoSP.length < 6 -> {
                                Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                            }
                       /*     signUpViewModel.citySP.length < 3 -> {
                                Toast.makeText(context, "Please enter your city's name", Toast.LENGTH_SHORT).show()
                            }*/
                            else -> {
                                signUpViewModel.signupUser(context)
                            }
                        }
                    }
                }


                //  Spacer(modifier = Modifier.height(8.dp))

                Spacer(modifier = Modifier.height(10.dp))

                LineWithText("Already have an Account?")

                Spacer(modifier = Modifier.height(10.dp))

                Custombtn2(text = "Login Now", background = LightGreeny) {
                    navController?.navigate("login")
                }

                Spacer(modifier = Modifier.height(1.dp))

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
                ) {
                    Text(text = "By signing up, you agree to our ", fontSize = 12.sp)
                    Text(
                        text = "Privacy Policy",
                        fontSize = 16.sp,
                        color = Greeny,
                        modifier = Modifier.clickable { navController?.navigate("privacypolicy") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun CustombtnLoad(
    text: String,
    background: Color,
    modifier: Modifier = Modifier,
    showProgress: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(enabled = !showProgress) {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .background(background)
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
fun CustomTextField(
    value: String,  // Changed from TextFieldValue to String
    label: String,
    prefix: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,// New parameter with default value
    onValueChange: (String) -> Unit  // Updated to use String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label, color = Color.Gray) },
        singleLine = true,
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType), // Applied here

        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = LightGreeny,
            focusedBorderColor = Greeny,  //MaterialTheme.colorScheme.primary,
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
fun CountryCodeBottomSheetSelector(
    countryList: List<CountryCode>,
    selectedCountry: CountryCode?,
    onCountrySelected: (CountryCode) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(searchQuery, countryList) {
        if (searchQuery.isBlank()) countryList
        else countryList.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.dial_code.contains(searchQuery) ||
                    it.code.contains(searchQuery, ignoreCase = true)
        }
    }

    // Bottom Sheet
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
                    placeholder = { Text("Search Country") },
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

    // Wrapping Box makes entire field clickable
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isSheetOpen = true }
    ) {
        OutlinedTextField(
            value = selectedCountry?.let { "${it.flag} ${it.dial_code}" } ?: "",
            onValueChange = {},
            label = { Text("Country Code") },
            readOnly = true,
            enabled = false, // Prevent unwanted focus
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFields(
    value: String,
    label: String,
    prefix: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier, // Accept modifier
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        singleLine = true,
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = LightGreeny,
            focusedBorderColor = Greeny,
            cursorColor = Greeny
        ),
        leadingIcon = prefix?.let {
            { Text(text = it, color = Color.White, modifier = Modifier.padding(start = 18.dp)) }
        },
        modifier = modifier.padding(vertical = 4.dp)
    )
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
fun Custombtn2(
    text: String,
    background: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = background,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                maxLines = 1,
                style = TextStyle(
                    color = Greeny, // Match loading button text color
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


@Composable
fun LoadingAlertDialog(){
    Dialog(onDismissRequest = { }, properties = DialogProperties(
        dismissOnBackPress = false, dismissOnClickOutside = false
    )
    ) {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(8.dp),

            shape = RoundedCornerShape(10.dp),
            shadowElevation = 8.dp

        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Please Wait!!",
                    modifier = Modifier.padding(8.dp), fontSize = 20.sp
                )
                Text(
                    text = "Loading...",
                    modifier = Modifier.padding(8.dp)
                )
            }


        }


    }
}



