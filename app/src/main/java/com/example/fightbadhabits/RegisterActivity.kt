package com.example.fightbadhabits

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.fightbadhabits.data.firebase.FirestoreClass
import com.example.fightbadhabits.data.model.User
import com.example.fightbadhabits.ui.screens.RegisterScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegisterScreen(
                onSignUpClick = { name, email, password, passwordRepeat ->
                    registerUser(name, email, password, passwordRepeat)
                },
                onLoginClick = {
                    navigateToLogin()
                }
            )
        }
    }

    private fun validateRegisterDetails(
        name: String, email: String, password: String, passwordRepeat: String
    ): Boolean {
        return when {
            name.trim { it <= ' ' }.isEmpty() -> {
                showErrorSnackBar("Please enter your name", true)
                false
            }
            email.trim { it <= ' ' }.isEmpty() -> {
                showErrorSnackBar("Please enter an email address", true)
                false
            }
            password.trim { it <= ' ' }.isEmpty() -> {
                showErrorSnackBar("Please enter a password", true)
                false
            }
            passwordRepeat.trim { it <= ' ' }.isEmpty() -> {
                showErrorSnackBar("Please repeat the password", true)
                false
            }
            password.trim { it <= ' ' } != passwordRepeat.trim { it <= ' ' } -> {
                showErrorSnackBar("Passwords do not match", true)
                false
            }
            else -> true
        }
    }

    private fun registerUser(
        name: String, email: String, password: String, passwordRepeat: String
    ) {
        if (!validateRegisterDetails(name, email, password, passwordRepeat)) return

        val trimmedEmail = email.trim { it <= ' ' }
        val trimmedPassword = password.trim { it <= ' ' }
        val trimmedName = name.trim { it <= ' ' }

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(trimmedEmail, trimmedPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    val user = User(
                        id = firebaseUser.uid,
                        name = trimmedName,
                        email = trimmedEmail
                    )
                    lifecycleScope.launch {
                        try {
                            val firestoreClass = FirestoreClass()
                            firestoreClass.registerOrUpdateUser(user)
                            showErrorSnackBar("You are registered successfully!", false)
                            FirebaseAuth.getInstance().signOut()
                            finish()
                        } catch (e: Exception) {
                            showErrorSnackBar("Failed to save data: ${e.message}", true)
                        }
                    }
                } else {
                    showErrorSnackBar(task.exception?.message.toString(), true)
                }
            }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}