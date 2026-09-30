package com.example.mobile


import android.content.Context
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

fun sendStepsGoalToWatch(
    context: Context,
    stepsGoal: Int,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
){
    val putDataMapRequest = PutDataMapRequest.create(edu.sdgku.stepcounter.FITNESS_GOALS_PATH).apply {
        dataMap.putInt(edu.sdgku.stepcounter.STEPS_GOAL_KEY, stepsGoal)
        dataMap.putLong(edu.sdgku.stepcounter.TIMESTAMP_KEY, System.currentTimeMillis())
    }

    val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

    Wearable.getDataClient(context)
        .putDataItem(putDataRequest)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception ->
            onError(exception.message ?: "Unknown DataLayer Error")
        }
}
