package com.krygodev.appforartists.feature_authentication.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.use_case.AuthenticationUseCases
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val _authenticationUseCases: AuthenticationUseCases
): ViewModel() {

    private val _state = mutableStateOf(AuthenticationState())
    val state: State<AuthenticationState> = _state

    fun signUpWithEmailAndPass(email: String, password: String, repeatPassword: String) {
        _authenticationUseCases.signUpWithEmailAndPassword(email, password, repeatPassword).onEach { result ->
            when(result) {
                is Resource.Success<*> -> {
                    _state.value = AuthenticationState(result = result.data)
                }
                is Resource.Error<*> -> {
                    _state.value = AuthenticationState(error = result.message ?: "Wystąpił niespodziewany błąd.")
                }
                is Resource.Loading<*> -> {
                    _state.value = AuthenticationState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}