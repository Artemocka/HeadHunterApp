package com.dracul.feature_main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dracul.feature_main.model.SignInActions
import com.dracul.feature_main.model.SignInEvents
import com.dracul.feature_main.model.SignInState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignInViewModel:ViewModel() {
    private val _state = MutableStateFlow(SignInState("",false))
    private val _events = MutableSharedFlow<SignInEvents>()

    val state = _state.asStateFlow()
    val events = _events.asSharedFlow()

    fun onAction(action:SignInActions){
        when(action){
            SignInActions.ClickContinueButton -> {
                when (isEmailValid(_state.value.email)){
                    true -> {
                        viewModelScope.launch {
                            _state.value = _state.value.copy(isError = false)
                            _events.emit(SignInEvents.NextScreen)
                        }
                    }
                    false -> {
                        viewModelScope.launch {
                            _state.value = _state.value.copy(isError = true)
                        }
                    }
                }
            }
            is SignInActions.EditEmailEditText -> {
                val state = _state.value
                _state.value = state.copy(email =  action.email)
            }

            SignInActions.ClickClearButton -> {
                viewModelScope.launch {
                    _events.emit(SignInEvents.ClearEmailET)
                    val state = _state.value
                    _state.value = state.copy(isError = false)
                }
            }
        }
    }
}

fun isEmailValid(email: String): Boolean {
    return Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(email).matches()
}