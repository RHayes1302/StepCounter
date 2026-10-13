package com.example.mobile.ui.dashboard

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.shared.model.FitnessData

@Preview(name = "Compact Phone", widthDp = 360, heightDp = 800)
@Composable
private fun CompactDashboardPreview() {
    MaterialTheme {
        CompactDashboard(
            stepsGoal = 10000,
            sendStatus = "Not sent",
            layoutLabel = "Compact",
            onDecrease = {},
            onIncrease = {},
            onSendToWatch = {},
            onSaveToFirebase = {}
        )
    }
}

@Preview(name = "Tablet Landscape", widthDp = 1280, heightDp = 800)
@Composable
private fun WideDashboardPreview() {
    MaterialTheme {
        WideDashboard(
            stepsGoal = 10000,
            sendStatus = "Goal sent to watch",
            layoutLabel = "Expanded",
            cloudData = FitnessData(dailyGoal = 10000, steps = 0, heartRate = 72),
            onDecrease = {},
            onIncrease = {},
            onSendToWatch = {},
            onSaveToFirebase = {},
            extraEditorContent = { GoalPresetRow() },
            onGoalDropped = {}
        )
    }
}