package com.krygodev.appforartists.feature_authentication.presentation.reset_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_authentication.domain.use_case.AuthenticationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val _authenticationUseCases: AuthenticationUseCases
): ViewModel() {

    private val _state = mutableStateOf(ResetPasswordState())
    val state: State<ResetPasswordState> = _state

    fun resetAccountPassword(email: String) {
        _authenticationUseCases.resetAccountPassword(email).onEach { result ->
            when(result) {
                is Resource.Success<*> -> {
                    _state.value = ResetPasswordState(result = result.data)
                }
                is Resource.Error<*> -> {
                    _state.value = ResetPasswordState(error = result.message ?: "Wystąpił niespodziewany błąd.")
                }
                is Resource.Loading<*> -> {
                    _state.value = ResetPasswordState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}