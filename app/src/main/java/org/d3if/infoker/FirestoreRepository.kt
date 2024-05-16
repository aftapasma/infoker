package org.d3if.infoker

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.d3if.infoker.model.Job

class FirestoreRepository(private val db: FirebaseFirestore) {
    suspend fun addJob(job: Job): Boolean {
        return try {
            db.collection("jobs").add(job).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}