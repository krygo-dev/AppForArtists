package com.krygodev.appforartists.feature_authentication.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.use_case.AuthenticationUseCases
import com.krygodev.appforartists.feature_authentication.presentation.util.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val _authenticationUseCases: AuthenticationUseCases
): ViewModel() {

    private val _state = mutableStateOf(AuthenticationState())
    val state: State<AuthenticationState> = _state

    fun signInWithEmailAndPass(email: String, password: String) {
        _authenticationUseCases.signInWithEmailAndPassword(email, password).onEach { result ->
            when(result) {
                is Resource.Success<*> -> {
                    _state.value = AuthenticationState(result = result.data as AuthResult?)
                }
                is Resource.Error<*> -> {
                    _state.value = AuthenticationState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading<*> -> {
                    _state.value = AuthenticationState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


    /** fun signInWithEmailAndPass(email: String, password: String) {
        when(val result = _authenticationUseCases.signInWithEmailAndPassword(email, password)) {
            is Resource.Success<*> -> {
                _state.value = AuthenticationState(result = result.data as AuthResult?)
            }
            is Resource.Error<*> -> {
                _state.value = AuthenticationState(error = result.message ?: "An unexpected error occurred.")
            }
            is Resource.Loading<*> -> {
                _state.value = AuthenticationState(isLoading = true)
            }
        }
    } **/
}