package com.example.shared.presentations;

import com.example.shared.model.FitnessData;

data class FitnessUiState (

    val fitnessData: FitnessData = FitnessData(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
