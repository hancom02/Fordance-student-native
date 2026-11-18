package com.fordance.student.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fordance.student.core.common.ResultWrapper
import com.fordance.student.features.auth.domain.model.StudentUser
import com.fordance.student.features.auth.domain.usecase.GetSessionUseCase
import com.fordance.student.features.auth.domain.usecase.SignInUseCase
import com.fordance.student.features.auth.domain.usecase.SignOutUseCase
import com.fordance.student.features.auth.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val emailInput: String = "",
    val passwordInput: String = "",
    val usernameInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: StudentUser? = null,
    val isSessionChecked: Boolean = false
)

sealed interface AuthUiEvent {
    data class EmailChanged(val value: String) : AuthUiEvent
    data class PasswordChanged(val value: String) : AuthUiEvent
    data class UsernameChanged(val value: String) : AuthUiEvent
    data object SignInSubmitted : AuthUiEvent
    data object SignUpSubmitted : AuthUiEvent
    data object SignOutRequested : AuthUiEvent
    data object ClearError : AuthUiEvent
}

sealed interface AuthUiEffect {
    data object NavigateToHome : AuthUiEffect
    data object NavigateToLogin : AuthUiEffect
    data class ShowToast(val message: String) : AuthUiEffect
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getSessionUseCase: GetSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _effectChannel = Channel<AuthUiEffect>(Channel.BUFFERED)
    val effectFlow = _effectChannel.receiveAsFlow()

    init {
        checkPersistedSession()
    }

    private fun checkPersistedSession() {
        viewModelScope.launch {
            getSessionUseCase().collect { cachedUser ->
                _uiState.update {
                    it.copy(
                        currentUser = cachedUser,
                        isSessionChecked = true
                    )
                }
                if (cachedUser != null) {
                    _effectChannel.send(AuthUiEffect.NavigateToHome)
                }
            }
        }
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailChanged -> _uiState.update { it.copy(emailInput = event.value, errorMessage = null) }
            is AuthUiEvent.PasswordChanged -> _uiState.update { it.copy(passwordInput = event.value, errorMessage = null) }
            is AuthUiEvent.UsernameChanged -> _uiState.update { it.copy(usernameInput = event.value, errorMessage = null) }
            is AuthUiEvent.SignInSubmitted -> signIn()
            is AuthUiEvent.SignUpSubmitted -> signUp()
            is AuthUiEvent.SignOutRequested -> signOut()
            is AuthUiEvent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun signIn() {
        val currentState = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = signInUseCase(currentState.emailInput, currentState.passwordInput)) {
                is ResultWrapper.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentUser = result.data,
                            errorMessage = null
                        )
                    }
                    _effectChannel.send(AuthUiEffect.NavigateToHome)
                }
                is ResultWrapper.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is ResultWrapper.Loading -> Unit
            }
        }
    }

    private fun signUp() {
        val currentState = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val username = currentState.usernameInput.ifEmpty { currentState.emailInput.substringBefore("@") }
            when (val result = signUpUseCase(currentState.emailInput, currentState.passwordInput, username)) {
                is ResultWrapper.Success -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                    _effectChannel.send(AuthUiEffect.ShowToast("Đăng ký thành công! Vui lòng đăng nhập."))
                    _effectChannel.send(AuthUiEffect.NavigateToLogin)
                }
                is ResultWrapper.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is ResultWrapper.Loading -> Unit
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            signOutUseCase()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentUser = null,
                    emailInput = "",
                    passwordInput = ""
                )
            }
            _effectChannel.send(AuthUiEffect.NavigateToLogin)
        }
    }
}
