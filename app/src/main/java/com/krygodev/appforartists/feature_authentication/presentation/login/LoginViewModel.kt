package com.krygodev.appforartists.feature_authentication.presentation.login

import androidx.lifecycle.ViewModel
import com.krygodev.appforartists.feature_authentication.domain.use_case.AuthenticationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases
): ViewModel() {
    
}