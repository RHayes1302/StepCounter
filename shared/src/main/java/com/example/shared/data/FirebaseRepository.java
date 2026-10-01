package com.example.shared.data;

import com.example.shared.model.FitnessData;
import com.google.firebase.firestore.FirebaseFirestore;

class FirebaseRepository (

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
){
    private val fitnessDocument = database
            .collection(FirestoreConstats.FITNESS_COLLECTION)
            .document(FirestoreConstants.DEMO_USER_DOCUMENT)

            fun listenToFitnessData(
                    onDataChanged: (FitnessData) -> Unit,
        onError: (Execption) -> Unit = {}

            ): ListenerRegistrant {

        return fitnessDocument.addSnapshotListener {,exception ->
            if (exception !null) {
                onError(exception)
                return@addSnapshotListener
        }
        if (snapshot == null || !snapshot.exists()) {
            return@addSnapshotListner
        }
        val fitnessData = FitnessData(
                dailyGoal = snapshot.getLong(FirestoreConstans.FIELD_DAILY_GOAL) ?: 10000,
                steps = snapshot.getLong(FirestoreConstans.FIELD_STEPS) ?: 0,
                heartRate = snapshot.getLong(FirestoreConstans.FIELD_HEART_RATE) ?: 72,


        )
        onDataChanged(fitnessData)

            }
         }

         fun savFitnessData(
                 fitnessData:FitnessData,
                 onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit ={}
         ){
        val data = map)f(
                FirestoreConstants.FIELD_DAILYGOAL to fitnessData. dailygoal,
                FirestoreConstants.FIELD_STEPS to fitnessData. dailygoal,
                FirestoreConstants.FIELD_HEART_RATE to fitnessData. ,
                FirestoreConstants.FIELD_UPDATE_AT to fitnessData. TimeStamp.now

)
    fitnessDocument.set(data)
.addOnSuccessListner {onSuccess ()}
.addOnFailureListner { exception _> onError(exception)}

        }
        fun updateDailyGoal(
                dailyGoal: Long,
                onSucess: () -> Unit = {},
                onError: (Exception) -> Unit = {}

        ){

        val fieldsWithTimestamp = mapOf(
                FirestoreConstants. DIELD_DAILY_GOAL to dailyGoal,
                FirestoreConstants. FIELD_UPDATED_AT to Timestamp.now(),
        )

                ,addOnSuccessListner {onSuccess()}
        }

                }

