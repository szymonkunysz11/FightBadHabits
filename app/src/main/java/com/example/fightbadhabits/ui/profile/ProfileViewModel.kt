package com.example.fightbadhabits.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fightbadhabits.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    //Here we will store the data from firestore
    var userData = mutableStateOf<User?>(null)

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    userData.value = snapshot.toObject(User::class.java)
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
}