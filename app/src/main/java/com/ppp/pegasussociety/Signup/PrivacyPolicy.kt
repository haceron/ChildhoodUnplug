/*
package com.ppp.pegasussociety.Signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ppp.pegasussociety.ui.theme.Greeny
import com.ppp.pegasussociety.ui.theme.Greeny

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policies", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
               */
/* actions = {
                    IconButton(onClick = {
                        navController.navigate("scanner")
                    }) {
                        Icon(
                            Icons.Default.QrCodeScanner,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Scan",
                            tint = Color.White
                        )
                    }
                },*//*

                colors = TopAppBarDefaults.topAppBarColors(
                    Greeny
                )

                */
/*,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )*//*

            )
        }
    ) { padding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
                .verticalScroll(scrollState)
        ) {

            */
/*    Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Privacy Policy",
                style = MaterialTheme.typography.headlineMedium
            )
        }*//*

            */
/*   Text(
            text = "Privacy Policy",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp)
        )*//*

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = """
              
Effective Date: 01 July, 2025

PegasusPlus ("we", "our", or "us") is committed to protecting your privacy and providing a safe, secure experience. This Privacy Policy explains how we collect, use, and protect your personal information when you use our mobile application ("PegasusApp").

1. User Data Collection and Usage
We collect the following data, strictly for the purpose of providing and improving your user experience:

Full Name

Email Address

Country Code

Mobile Number

OTP (used for authentication only)

City

Number of Child's (used to personalize content for parents)

How We Use This Data:

To register and authenticate users via OTP

To personalize educational and age-appropriate content

To improve the functionality and features of the app

To provide support and enhance user experience

We do not sell, rent, or share your personal data with third-party companies for advertising or marketing purposes.

2. Permissions and APIs That Access Sensitive Information
PegasusPlusApp only requests permissions that are essential to the app’s functionality:

Camera: Required for barcode or book cover scanning. We do not store or share your photos without explicit user action.

Internet Access: Used for syncing data and accessing educational content.

Network State: Helps detect internet connectivity to notify the user accordingly.

All permissions are declared clearly and used only for the features they support.

3. Data Security
We use industry-standard encryption and security practices to protect your personal information. Your data is stored securely and only accessible by authorized personnel. We do not retain sensitive information longer than necessary.

4. Children’s Privacy
Although the app may display content related to children’s learning, we do not allow children to use the app independently. All information is submitted by and under the supervision of a parent or legal guardian. We do not knowingly collect data directly from children under the age of 18.

5. Third-Party Services
We may use trusted third-party services such as:

OTP verification (e.g., MSG91 or similar providers)

Analytics tools (e.g., Google Analytics for Firebase)

These services may collect information as described in their own privacy policies. We ensure that third-party providers comply with applicable data privacy regulations.

6. Device and Network Abuse Prevention
PegasusPlus does not engage in any behavior that harms devices or networks, including:

No installation of hidden features or background services without user knowledge

No collection of personal data without clear user consent

No manipulation of device settings or network behavior

7. Deceptive Behavior and Misrepresentation
We are committed to full transparency. PegasusPlus:

Clearly states its purpose and functionality

Does not impersonate other apps or brands

Does not make misleading claims or hide functionality from users

We do not engage in deceptive behavior, and all app features are clearly disclosed.

8. Google Play’s Target API Level Compliance
We comply with Google Play’s Target API Level Policy, ensuring:

Timely updates to support the latest Android security and performance standards

Full compatibility with the latest platform behaviors and privacy protections

9. User Consent
By using PegasusPlus, you confirm that you have read and understood this privacy policy and agree to the collection, use, and processing of your data as described.

You may withdraw your consent at any time by uninstalling the app or contacting us via email.

10. Contact Us
If you have questions, concerns, or feedback regarding this Privacy Policy, please contact:

📧 Email: priyanshu@pegasusforkids.com



            """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}*/
