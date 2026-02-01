package com.example.fightbadhabits.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fightbadhabits.R

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }

    val leafGreen = Color(0xFF4CAF50)
    val darkLeafGreen = Color(0xFF2E7D32)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = null,
                modifier = Modifier.size(120.dp).padding(bottom = 16.dp)
            )

            Text(
                text = "Fight Bad Habits!",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkLeafGreen
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = emailState,
                onValueChange = { emailState = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = darkLeafGreen,
                    focusedLabelColor = darkLeafGreen,
                    cursorColor = darkLeafGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordState,
                onValueChange = { passwordState = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = darkLeafGreen,
                    focusedLabelColor = darkLeafGreen,
                    cursorColor = darkLeafGreen
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onLoginClick(emailState, passwordState) },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = leafGreen)
            ) {
                Text("LOGIN", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text("Don't have an account? ", color = Color.Black)
                Text(
                    text = "Register Now",
                    color = darkLeafGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }
        }
    }
}

@Composable
fun RegisterScreen(
    onSignUpClick: (String, String, String, String) -> Unit,
    onLoginClick: () -> Unit
) {
    var nameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var confirmPasswordState by remember { mutableStateOf("") }

    val leafGreen = Color(0xFF4CAF50)
    val darkLeafGreen = Color(0xFF2E7D32)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = null,
                modifier = Modifier.size(100.dp).padding(bottom = 16.dp)
            )

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkLeafGreen
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = nameState,
                onValueChange = { nameState = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkLeafGreen, focusedLabelColor = darkLeafGreen)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = emailState,
                onValueChange = { emailState = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkLeafGreen, focusedLabelColor = darkLeafGreen)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = passwordState,
                onValueChange = { passwordState = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkLeafGreen, focusedLabelColor = darkLeafGreen)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPasswordState,
                onValueChange = { confirmPasswordState = it },
                label = { Text("Repeat Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkLeafGreen, focusedLabelColor = darkLeafGreen)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onSignUpClick(nameState, emailState, passwordState, confirmPasswordState) },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = leafGreen)
            ) {
                Text("REGISTER", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text("Already have an account? ", color = Color.Black)
                Text(
                    text = "Login here",
                    color = darkLeafGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}