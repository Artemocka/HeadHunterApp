package com.dracul.feature_main.model

sealed interface SignInEvents {
    data object NextScreen : SignInEvents
    data object ClearEmailET : SignInEvents
}