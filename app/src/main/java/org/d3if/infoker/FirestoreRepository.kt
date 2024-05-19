package org.d3if.infoker

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.d3if.infoker.model.Job

class FirestoreRepository(private val db: FirebaseFirestore) {
    suspend fun addJob(job: Job): Boolean {
        return try {
            val documentRef = db.collection("jobs").add(job).await()
            val generatedId = documentRef.id
            db.collection("jobs").document(generatedId).update("id", generatedId).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding job", e)
            false
        }
    }

    suspend fun getJobs(): List<Job> {
        return try {
            val result = db.collection("jobs").get().await()
            if (result.isEmpty) {
                Log.d("FirestoreRepository", "No jobs found in Firestore.")
            } else {
                Log.d("FirestoreRepository", "Jobs found: ${result.documents.size}")
            }
            val jobs = result.documents.mapNotNull { document ->
                document.toObject(Job::class.java)
            }
            Log.d("FirestoreRepository", "Fetched jobs: $jobs")
            jobs
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting jobs", e)
            emptyList()
        }
    }
}
