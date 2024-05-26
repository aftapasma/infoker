package org.d3if.infoker.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository(private val db: FirebaseFirestore) {
    suspend fun addJob(
        title: String,
        location: String,
        salary: Float,
        description: String
    ): Boolean {
        return try {
            val jobMap = hashMapOf(
                "title" to title,
                "location" to location,
                "salary" to salary,
                "description" to description
            )
            db.collection("jobs").add(jobMap).await()
            Log.d("FirestoreRepository", "Job added to Firestore.")
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding job", e)
            false
        }
    }


    suspend fun getJobs(): List<DocumentSnapshot> {
        return try {
            val docRef = db.collection("jobs")
            val result = docRef.get().await()

            if (result.isEmpty) {
                Log.d("FirestoreRepository", "No jobs found in Firestore.")
                emptyList()
            } else {
                Log.d("FirestoreRepository", "Jobs found: ${result.documents.size}")
                result.documents.also { jobs ->
                    Log.d("FirestoreRepository", "Fetched jobs: $jobs")
                }
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting jobs", e)
            emptyList()
        }
    }

    suspend fun addUser(name: String, email: String): Boolean {
        return try {
            val userMap = hashMapOf(
                "name" to name,
                "email" to email
            )
            db.collection("users").add(userMap).await()
            Log.d("FirestoreRepository", "User added to Firestore.")
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding user", e)
            false
        }
    }

    suspend fun getJobById(id: String): DocumentSnapshot? {
        return try {
            val docRef = db.collection("jobs").document(id)
            val result = docRef.get().await()
            if (result.exists()) {
                Log.d("FirestoreRepository", "Job found: ${result.id}")
                result
            } else {
                Log.d("FirestoreRepository", "No job found with ID: $id")
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting job by ID", e)
            null
        }
    }
}
