package com.example.stepcounter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.wearable.Wearable
import com.example.stepcounter.presentation.theme.StepCounterTheme

class MainActivity : ComponentActivity(){
    private var heartRate by mutableIntStateOf(72)
    private var stepsGoal by mutableIntStateOf(10000)
    private lateinit var heartRateSensorManager: HeartRateSensorManager
    private lateinit var wearDataListener: WearDataListener

    private val heartRatePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted){
            heartRateSensorManager.startListening()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)

        heartRateSensorManager = HeartRateSensorManager(
            context = this,
            onHeartRateChanged = { newHeartRate -> heartRate = newHeartRate }
        )
        if (heartRateSensorManager.hasHeartRateSensor && !heartRateSensorManager.hasPermission()){
            heartRatePermissionLauncher.launch(heartRateSensorManager.requiredPermission)
        }

        wearDataListener = WearDataListener(onStepsGoalChanged = { newGoal ->
            runOnUiThread { stepsGoal = newGoal }
        })

        val repository = FirebaseRepository()
        repository.listnerToFitnessData(
            onDataChanged = { fitnessData ->
                log.d("SharedFirebaseWear", "Goal recieved: ${fitnessData.dailyGoal}")
            }
        )

        setContent {
            StepCounterTheme {
                WearFitnessApp(
                    heartRateSensorValue = heartRate,
                    hasHeartRateSensor = heartRateSensorManager.hasHeartRateSensor,
                    stepsGoalFromPhone = stepsGoal
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        heartRateSensorManager.stopListening()
        if (::wearDataListener.isInitialized) {
            Wearable.getDataClient(this).removeListener(wearDataListener)
        }
    }

    override fun onResume() {
        super.onResume()
        if (heartRateSensorManager.hasHeartRateSensor && heartRateSensorManager.hasPermission()) {
            heartRateSensorManager.startListening()
        }
        if (::wearDataListener.isInitialized) {
            Wearable.getDataClient(this).addListener(wearDataListener)
        }
    }
}