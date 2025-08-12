package com.ppp.pegasussociety.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.ppp.pegasussociety.R
import java.util.*

@Composable
fun ProfileScreen(navController: NavController? = null) {
    var children by remember { mutableStateOf(sampleChildren.toMutableList()) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFFB4DB6F)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Child")
            }
        },
        containerColor = Color(0xFFF7FDE2)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text(
                "Child Profile",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF2F3E46)
            )

            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(children) { child ->
                    ChildCard(child, onDelete = {
                        children = children.toMutableList().apply { remove(child) }
                    })
                }
            }
        }
    }

    if (showDialog) {
        AddChildDialog(
            onAdd = { child ->
                children = (children + child).toMutableList()
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun ChildCard(child: ChildProfile, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = child.avatarResId ?: R.drawable.babyimg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Name: ${child.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Age: ${child.age} years", fontSize = 14.sp)
                Text("Gender: ${child.gender}", fontSize = 14.sp)
                child.hobbies?.let {
                    Text("Hobbies: $it", fontSize = 14.sp)
                }
                child.notes?.let {
                    Text("Notes: $it", fontSize = 14.sp)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}

@Composable
fun AddChildDialog(onAdd: (ChildProfile) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var hobbies by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Add Child", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
                OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") })
                OutlinedTextField(value = hobbies, onValueChange = { hobbies = it }, label = { Text("Hobbies") })
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") })

                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        if (name.isNotBlank() && age.toIntOrNull() != null) {
                            onAdd(
                                ChildProfile(
                                    name = name,
                                    age = age.toInt(),
                                    gender = gender,
                                    hobbies = hobbies.takeIf { it.isNotBlank() },
                                    notes = notes.takeIf { it.isNotBlank() },
                                    avatarResId = R.drawable.babyimg
                                )
                            )
                        }
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

data class ChildProfile(
    val name: String,
    val age: Int,
    val gender: String,
    val hobbies: String? = null,
    val notes: String? = null,
    val avatarResId: Int? = null
)

val sampleChildren = listOf(
    ChildProfile("Aanya", 6, "Girl", "Drawing, Reading", "Very curious", avatarResId = R.drawable.babyimg),
    ChildProfile("Vivaan", 8, "Boy", "Science, Cycling", "Loves math", avatarResId = R.drawable.babyimg)
)

/*import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ppp.pegasussociety.ViewModel.ProfileViewModel
// Core Compose
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.ppp.pegasussociety.Model.ChildProfile


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController?) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val profiles = viewModel.profiles
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Child")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Child Profiles") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(profiles) { profile ->
                ChildProfileCard(profile)
            }
        }

        if (showDialog) {
            AddChildDialog(
                onDismiss = { showDialog = false },
                onAdd = {
                    viewModel.addProfile(it)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun ChildProfileCard(profile: ChildProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Name: ${profile.name}", style = MaterialTheme.typography.titleMedium)
            Text("Age: ${profile.age}", style = MaterialTheme.typography.bodyLarge)
            Text("Gender: ${profile.gender}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AddChildDialog(
    onDismiss: () -> Unit,
    onAdd: (ChildProfile) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && age.isNotBlank() && gender.isNotBlank()) {
                        onAdd(ChildProfile(name, age.toIntOrNull() ?: 0, gender))
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add Child Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age (years)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}*/
/*

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(ProfileViewModel())
}

*/
