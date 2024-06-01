package org.d3if.infoker.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
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
                "description" to description,
                "createdAt" to FieldValue.serverTimestamp()
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
                "email" to email.trim().lowercase()
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

    suspend fun getUserByEmail(email: String): DocumentSnapshot? {
        return try {
            val trimmedEmail = email.trim().lowercase()
            Log.d("FirestoreRepository", "Searching user with email: $trimmedEmail")
            val userQuery = db.collection("users").whereEqualTo("email", trimmedEmail).get().await()
            if (userQuery.documents.isNotEmpty()) {
                Log.d("FirestoreRepository", "User found: ${userQuery.documents[0].id}")
                userQuery.documents[0]
            } else {
                Log.d("FirestoreRepository", "No user found with email: $trimmedEmail")
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting user by email", e)
            null
        }
    }

    suspend fun applyForJob(user: DocumentSnapshot, job: DocumentSnapshot): Boolean {
        val jobData = job.data?.toMutableMap() ?: mutableMapOf()
        jobData["id"] = job.id
        return try {
            val applicationMap = hashMapOf(
                "user" to user.data,
                "job" to jobData
            )
            db.collection("applications").add(applicationMap).await()
            Log.d("FirestoreRepository", "Application added to Firestore.")
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding application", e)
            false
        }
    }

    suspend fun addBookmark(user: DocumentSnapshot, job: DocumentSnapshot): Boolean {
        val jobData = job.data?.toMutableMap() ?: mutableMapOf()
        jobData["id"] = job.id
        return try {
            val bookmarkMap = hashMapOf(
                "user" to user.data,
                "job" to jobData
            )
            db.collection("bookmarks").add(bookmarkMap).await()
            Log.d("FirestoreRepository", "Bookmark added to Firestore.")
            true
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error adding bookmark", e)
            false
        }
    }

    suspend fun isJobBookmarkedByUser(userEmail: String, jobId: String): Boolean {
        return try {
            val bookmarkQuery = db.collection("bookmarks")
                .whereEqualTo("user.email", userEmail)
                .whereEqualTo("job.id", jobId)
                .get()
                .await()

            bookmarkQuery.documents.isNotEmpty()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error checking bookmark", e)
            false
        }
    }

    suspend fun isJobAppliedByUser(userEmail: String, jobId: String): Boolean {
        return try {
            val applicationQuery = db.collection("applications")
                .whereEqualTo("user.email", userEmail)
                .whereEqualTo("job.id", jobId)
                .get()
                .await()

            applicationQuery.documents.isNotEmpty()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error checking applications", e)
            false
        }
    }

    suspend fun deleteBookmark(userEmail: String, jobId: String): Boolean {
        return try {
            // Query to find the bookmark
            val bookmarkQuery = db.collection("bookmarks")
                .whereEqualTo("user.email", userEmail)
                .whereEqualTo("job.id", jobId)
                .get()
                .await()

            if (bookmarkQuery.documents.isNotEmpty()) {
                for (document in bookmarkQuery.documents) {
                    db.collection("bookmarks").document(document.id).delete().await()
                }
                Log.d("FirestoreRepository", "Bookmark deleted successfully.")
                true
            } else {
                Log.d("FirestoreRepository", "No bookmark found to delete.")
                false
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error deleting bookmark", e)
            false
        }
    }

    suspend fun deleteApplication(userEmail: String, jobId: String): Boolean {
        return try {
            // Query to find the bookmark
            val applicationQuery = db.collection("applications")
                .whereEqualTo("user.email", userEmail)
                .whereEqualTo("job.id", jobId)
                .get()
                .await()

            if (applicationQuery.documents.isNotEmpty()) {
                for (document in applicationQuery.documents) {
                    db.collection("applications").document(document.id).delete().await()
                }
                Log.d("FirestoreRepository", "Application deleted successfully.")
                true
            } else {
                Log.d("FirestoreRepository", "No application found to delete.")
                false
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error deleting application", e)
            false
        }
    }

    suspend fun getApplicationsForCurrentUser(email: String): List<DocumentSnapshot> {
        return try {
            val applicationsQuery = db.collection("applications")
                .whereEqualTo("user.email", email)
                .get()
                .await()

            if (applicationsQuery.isEmpty) {
                Log.d("FirestoreRepository", "No applications found for user with email: $email")
                emptyList()
            } else {
                Log.d("FirestoreRepository", "Applications found: ${applicationsQuery.documents.size}")
                applicationsQuery.documents
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting applications for user", e)
            emptyList()
        }
    }

    suspend fun getBookmarksForCurrentUser(email: String): List<DocumentSnapshot> {
        return try {
            val bookmarksQuery = db.collection("bookmarks")
                .whereEqualTo("user.email", email)
                .get()
                .await()

            if (bookmarksQuery.isEmpty) {
                Log.d("FirestoreRepository", "No bookmarks found for user with email: $email")
                emptyList()
            } else {
                Log.d("FirestoreRepository", "Bookmarks found: ${bookmarksQuery.documents.size}")
                bookmarksQuery.documents
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting bookmarks for user", e)
            emptyList()
        }
    }
}