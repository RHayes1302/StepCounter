package com.example.mobile.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shared.data.FirebaseRepository
import com.example.shared.model.FitnessData
import com.example.stepcounter.R
import com.example.stepcounter.sendStepsGoalToWatch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneCompanionApp(
    repository: FirebaseRepository,
    widthSizeClass: WindowWidthSizeClass
) {
    val context = LocalContext.current
    var stepsGoal by remember { mutableIntStateOf(10000) }
    var sendStatus by remember { mutableStateOf("Not Sent") }
    var cloudData by remember { mutableStateOf<FitnessData?>(null) }

    DisposableEffect(repository) {
        val listenerRegistration = repository.listenToFitnessData(
            onDataChanged = { fitnessData ->
                cloudData = fitnessData
                stepsGoal = fitnessData.dailyGoal.toInt()
                sendStatus = "Goal received from Firebase: $stepsGoal"
            },
            onError = { exception ->
                sendStatus = "Firebase listener error: " +
                        (exception.message ?: "Unknown error")
            }
        )
        onDispose { listenerRegistration.remove() }
    }

    val onDecrease = {
        if (stepsGoal > 500) stepsGoal -= 500
    }

    val onIncrease = {
        stepsGoal += 500
    }

    val onGoalDropped: (Int) -> Unit = { dropped ->
        stepsGoal = dropped.coerceAtLeast(500)
        sendStatus = "Dropped preset $dropped (not saved yet)"
    }

    val onSendToWatch = {
        sendStatus = "Sending..."
        sendStepsGoalToWatch(
            context = context,
            stepsGoal = stepsGoal,
            onSuccess = { sendStatus = "Sent $stepsGoal to the watch." },
            onError = { errorMessage -> sendStatus = "Error: $errorMessage" }
        )
    }

    val onSaveToFirebase = {
        sendStatus = "Saving to Firebase..."
        repository.updateDailyGoal(
            dailyGoal = stepsGoal.toLong(),
            onSuccess = { sendStatus = "Saved $stepsGoal in Firebase" },
            onError = { exception ->
                sendStatus = "Firebase error: " +
                        (exception.message ?: "Unknown error")
            }
        )
    }

    val layoutLabel = widthSizeClass.label()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    Text(
                        text = layoutLabel,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (widthSizeClass.isWide()) {
                WideDashboard(
                    stepsGoal = stepsGoal,
                    sendStatus = sendStatus,
                    layoutLabel = layoutLabel,
                    cloudData = cloudData,
                    onDecrease = onDecrease,
                    onIncrease = onIncrease,
                    onSendToWatch = onSendToWatch,
                    onSaveToFirebase = onSaveToFirebase,
                    extraEditorContent = { GoalPresetRow() },
                    onGoalDropped = onGoalDropped
                )
            } else {
                CompactDashboard(
                    stepsGoal = stepsGoal,
                    sendStatus = sendStatus,
                    layoutLabel = layoutLabel,
                    onDecrease = onDecrease,
                    onIncrease = onIncrease,
                    onSendToWatch = onSendToWatch,
                    onSaveToFirebase = onSaveToFirebase
                )
            }
        }
    }
}