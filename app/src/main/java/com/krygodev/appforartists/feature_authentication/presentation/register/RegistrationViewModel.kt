package com.krygodev.appforartists.feature_authentication.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.domain.util.Screen
import com.krygodev.appforartists.core.domain.util.UIEvent
import com.krygodev.appforartists.feature_authentication.domain.use_case.AuthenticationUseCases
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val _authenticationUseCases: AuthenticationUseCases
) : ViewModel() {

    private val _state = mutableStateOf(AuthenticationState())
    val state: State<AuthenticationState> = _state

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _repeatPassword = mutableStateOf("")
    val repeatPassword: State<String> = _repeatPassword

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.EnteredEmail -> {
                _email.value = event.value
            }
            is RegistrationEvent.EnteredPassword -> {
                _password.value = event.value
            }
            is RegistrationEvent.EnteredRepeatPassword -> {
                _repeatPassword.value = event.value
            }
            is RegistrationEvent.SignUp -> {
                viewModelScope.launch {
                    _authenticationUseCases.signUpWithEmailAndPassword(
                        email = email.value,
                        password = password.value,
                        repeatPassword = repeatPassword.value
                    ).onEach { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    isLoading = true,
                                    error = "",
                                    result = result.data
                                )
                            }
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    isLoading = false,
                                    error = "",
                                    result = result.data
                                )
                                _eventFlow.emit(UIEvent.ShowSnackbar("Konto utworzone! Potwierdź adres email aby się zalogować."))
                                delay(1000)
                                _eventFlow.emit(UIEvent.NavigateTo(Screen.LoginScreen.route))
                            }
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    isLoading = false,
                                    error = result.message!!,
                                    result = result.data
                                )
                                _eventFlow.emit(UIEvent.ShowSnackbar(result.message))
                            }
                        }
                    }.launchIn(this)
                }
            }
        }
    }
}