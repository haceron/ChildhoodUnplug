package com.ppp.pegasussociety.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Preview
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

@Composable
fun LoadingBar(
    isLoading: Boolean ,
    message: String = "Loading..."
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .wrapContentSize(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
    }
}


@Composable
fun LogoutDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){
    if(showDialog){
        AlertDialog(
            onDismissRequest = {onDismiss()},
            title = { Text(text = "Logout") },
            text = { Text(text = "Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = { onConfirm()}) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}
