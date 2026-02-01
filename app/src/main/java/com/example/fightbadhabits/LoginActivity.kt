package com.example.fightbadhabits

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.fightbadhabits.ui.screens.LoginScreen
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            goToMainActivity()
            finish()
            return
        }

        setContent {
            LoginScreen(
                onLoginClick = { email, password ->
                    logInRegisteredUser(email, password)
                },
                onRegisterClick = {
                    navigateToRegister()
                }
            )
        }
    }

    private fun logInRegisteredUser(email: String, password: String) {
        val trimmedEmail = email.trim { it <= ' ' }
        val trimmedPassword = password.trim { it <= ' ' }

        if (trimmedEmail.isEmpty() || trimmedPassword.isEmpty()) {
            showErrorSnackBar("Please fill all fields", true)
            return
        }

        auth.signInWithEmailAndPassword(trimmedEmail, trimmedPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    goToMainActivity()
                    finish()
                } else {
                    showErrorSnackBar(task.exception?.message.orEmpty(), true)
                }
            }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}