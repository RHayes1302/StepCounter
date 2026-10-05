package com.example.shared.data;

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.example.shared.model.FitnessData
import com.google.firebase.firestore.SetOptions

class FirebaseRepository (

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val fitnessDocument = database
        .collection(FirestoreConstants.FITNESS_COLLECTION)
        .document(FirestoreConstants.DEMO_USER_DOCUMENT)

    fun listenToFitnessData(
        onDataChanged: (FitnessData) -> Unit,
        onError: (Exception) -> Unit = {}

    ): ListenerRegistration {

        return fitnessDocument.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                onError(exception)
                return@addSnapshotListener
            }
            if (snapshot == null || !snapshot.exists()) {
                return@addSnapshotListener
            }
            val fitnessData = FitnessData(
                dailyGoal = snapshot.getLong(FirestoreConstants.FIELD_DAILY_GOAL) ?: 10000,
                steps = snapshot.getLong(FirestoreConstants.FIELD_STEPS) ?: 0,
                heartRate = snapshot.getLong(FirestoreConstants.FIELD_HEART_RATE) ?: 72,


                )
            onDataChanged(fitnessData)

        }
    }

    fun savFitnessData(
        fitnessData: FitnessData,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        val data = mapOf(
            FirestoreConstants.FIELD_DAILY_GOAL to fitnessData.dailyGoal,
            FirestoreConstants.FIELD_STEPS to fitnessData.steps,
            FirestoreConstants.FIELD_HEART_RATE to fitnessData.heartRate,
            FirestoreConstants.FIELD_UPDATE_AT to Timestamp.now()

        )

        fitnessDocument.set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }

    }

    fun updateDailyGoal(
        dailyGoal: Long,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        val fieldsWithTimestamp = mapOf(
            FirestoreConstants.FIELD_DAILY_GOAL to dailyGoal,
            FirestoreConstants.FIELD_UPDATE_AT to Timestamp.now(),
        )

        fitnessDocument.set(fieldsWithTimestamp, SetOptions.merge())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onError(exception) }
    }
}