package com.example.fightbadhabits.data.firebase

import com.example.fightbadhabits.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    suspend fun registerOrUpdateUser(user: User) {
        try {
            mFireStore.collection("users")
                .document(user.id)
                .set(user, com.google.firebase.firestore.SetOptions.merge())
                .await()
        } catch (e: Exception) {
            throw Exception("Error: ${e.message}")
        }
    }

    /**
     * Loads user data from firestore
     *
     * @param userId The ID of the user to load data for.
     * @return A map of user data.
     * @throws Exception If there is an error during the loading process.
     */
    suspend fun loadUserData(userId: String): Map<String, Any>? {
        try {
            val documentSnapshot = mFireStore.collection("users")
                .document(userId)
                .get()
                .await()
            return documentSnapshot.data
        } catch (e: Exception) {
            throw Exception("Error while loading user data: ${e.message}")
        }
    }

    /**
     * Updates specific fields in a user's firestore document
     *
     * @param userId The ID of the user to update.
     * @param updatedData A map of field-value pairs to update.
     * @throws Exception If there is an error during the update process.
     */
    suspend fun updateUserData(userId: String, updatedData: Map<String, Any?>) {
        try {
            val filteredData = updatedData.filterValues { value ->
                value != null && !(value is String && value.isBlank())
            }

            if (filteredData.isEmpty()) return

            mFireStore.collection("users")
                .document(userId)
                .update(filteredData)
                .await()
        } catch (e: Exception) {
            throw Exception("Error while updating user data: ${e.message}")
        }
    }
}