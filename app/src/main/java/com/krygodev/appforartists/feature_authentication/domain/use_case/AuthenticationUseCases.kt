package com.krygodev.appforartists.feature_authentication.domain.use_case

data class AuthenticationUseCases(
    val signInWithEmailAndPassword: SignInWithEmailAndPassword,
    val signInWithGoogle: SignInWithGoogle,
    val signUpWithEmailAndPassword: SignUpWithEmailAndPassword,
    val resetAccountPassword: ResetAccountPassword
)
