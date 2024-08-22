package com.dracul.feature_main.model

sealed interface SignInActions {
    data object ClickContinueButton : SignInActions
    data object ClickClearButton : SignInActions
    data class EditEmailEditText(val email:String) : SignInActions
}