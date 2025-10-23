package com.example.matchpet.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.matchpet.data.repository.AuthRepository
import com.example.matchpet.data.repository.UserRepository
import com.example.matchpet.domain.model.UserProfile
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val observeSessionUseCase: () -> Flow<String?>,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeSessionUseCase().collectLatest { token ->
                val loggedIn = !token.isNullOrBlank()
                _isLoggedIn.value = loggedIn
                if (loggedIn) {
                    loadProfile()
                } else {
                    _authState.update {
                        it.copy(
                            profile = ProfileUiState(),
                            editProfile = EditProfileUiState()
                        )
                    }
                }
            }
        }
    }

    fun onRegisterEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NameChanged -> updateRegisterState { copy(name = event.value, error = null) }
            is RegisterEvent.EmailChanged -> updateRegisterState { copy(email = event.value, error = null) }
            is RegisterEvent.PasswordChanged -> updateRegisterState { copy(password = event.value, error = null) }
            is RegisterEvent.ConfirmPasswordChanged -> updateRegisterState { copy(confirmPassword = event.value, error = null) }
            RegisterEvent.Submit -> submitRegistration()
            RegisterEvent.SuccessConsumed -> updateRegisterState { copy(success = false) }
        }
    }

    fun onLoginEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> updateLoginState { copy(email = event.value, error = null) }
            is LoginEvent.PasswordChanged -> updateLoginState { copy(password = event.value, error = null) }
            LoginEvent.Submit -> submitLogin()
            LoginEvent.SuccessConsumed -> updateLoginState { copy(success = false) }
        }
    }

    fun onEditProfileEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.NameChanged -> updateEditProfileState { copy(name = event.value, error = null, message = null) }
            is EditProfileEvent.PhoneChanged -> updateEditProfileState { copy(phone = event.value, error = null, message = null) }
            is EditProfileEvent.BioChanged -> updateEditProfileState { copy(bio = event.value, error = null, message = null) }
            is EditProfileEvent.PreferencesChanged -> updateEditProfileState { copy(preferences = event.value, error = null, message = null) }
            EditProfileEvent.Submit -> submitEditProfile()
            EditProfileEvent.MessageConsumed -> updateEditProfileState { copy(message = null) }
        }
    }

    fun onGoogleTokenReceived(token: String) {
        viewModelScope.launch {
            updateLoginState { copy(isLoading = true, error = null, success = false) }
            runCatching {
                authRepository.loginWithGoogle(token)
            }.onSuccess { response ->
                handleAuthSuccess(response.user.toDomain())
                updateLoginState { copy(isLoading = false, success = true, error = null) }
                showMessage("Inicio de sesión con Google exitoso")
            }.onFailure { throwable ->
                updateLoginState { copy(isLoading = false, error = throwable.message ?: "Error inesperado") }
                showMessage("No se pudo iniciar sesión con Google")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _isLoggedIn.value = false
        _authState.update { AuthState() }
    }

    private fun submitRegistration() {
        val current = _authState.value.register
        val validationError = when {
            current.name.isBlank() -> "Ingresa tu nombre"
            current.email.isBlank() -> "Ingresa un correo válido"
            current.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            current.password != current.confirmPassword -> "Las contraseñas no coinciden"
            else -> null
        }

        if (validationError != null) {
            updateRegisterState { copy(error = validationError) }
            return
        }

        viewModelScope.launch {
            updateRegisterState { copy(isLoading = true, error = null, success = false) }
            runCatching {
                authRepository.register(current.name, current.email, current.password)
            }.onSuccess { response ->
                handleAuthSuccess(response.user.toDomain())
                updateRegisterState { RegisterUiState(success = true) }
                showMessage("Registro exitoso")
            }.onFailure { throwable ->
                updateRegisterState {
                    copy(isLoading = false, error = throwable.message ?: "No se pudo completar el registro", success = false)
                }
            }
        }
    }

    private fun submitLogin() {
        val current = _authState.value.login
        if (current.email.isBlank() || current.password.isBlank()) {
            updateLoginState { copy(error = "Ingresa tus credenciales") }
            return
        }

        viewModelScope.launch {
            updateLoginState { copy(isLoading = true, error = null, success = false) }
            runCatching {
                authRepository.login(current.email, current.password)
            }.onSuccess { response ->
                handleAuthSuccess(response.user.toDomain())
                updateLoginState { LoginUiState(success = true) }
                showMessage("Inicio de sesión exitoso")
            }.onFailure { throwable ->
                updateLoginState {
                    copy(isLoading = false, error = throwable.message ?: "Credenciales incorrectas", success = false)
                }
            }
        }
    }

    private fun submitEditProfile() {
        val current = _authState.value.editProfile
        val profile = _authState.value.profile.profile ?: return
        val updatedProfile = profile.copy(
            name = current.name,
            phone = current.phone,
            bio = current.bio,
            preferences = current.preferences
        )

        viewModelScope.launch {
            updateEditProfileState { copy(isLoading = true, error = null, message = null) }
            runCatching {
                userRepository.updateProfile(updatedProfile)
            }.onSuccess { profileResponse ->
                _authState.update {
                    it.copy(
                        profile = it.profile.copy(profile = profileResponse, isLoading = false, error = null),
                        editProfile = it.editProfile.copy(
                            isLoading = false,
                            message = "Perfil actualizado"
                        )
                    )
                }
                showMessage("Perfil actualizado")
            }.onFailure { throwable ->
                updateEditProfileState {
                    copy(isLoading = false, error = throwable.message ?: "No se pudo actualizar el perfil")
                }
            }
        }
    }

    private fun handleAuthSuccess(userProfile: UserProfile) {
        _isLoggedIn.value = true
        _authState.update {
            it.copy(
                profile = ProfileUiState(profile = userProfile, isLoading = false, error = null),
                editProfile = EditProfileUiState(
                    name = userProfile.name,
                    email = userProfile.email,
                    phone = userProfile.phone,
                    bio = userProfile.bio,
                    preferences = userProfile.preferences
                )
            )
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _authState.update { it.copy(profile = it.profile.copy(isLoading = true)) }
            runCatching {
                userRepository.fetchProfile()
            }.onSuccess { profile ->
                _authState.update {
                    it.copy(
                        profile = ProfileUiState(profile = profile, isLoading = false),
                        editProfile = EditProfileUiState(
                            name = profile.name,
                            email = profile.email,
                            phone = profile.phone,
                            bio = profile.bio,
                            preferences = profile.preferences
                        )
                    )
                }
            }.onFailure { throwable ->
                _authState.update {
                    it.copy(
                        profile = ProfileUiState(isLoading = false, error = throwable.message ?: "Error al cargar el perfil")
                    )
                }
            }
        }
    }

    private fun updateRegisterState(update: RegisterUiState.() -> RegisterUiState) {
        _authState.update { it.copy(register = it.register.update()) }
    }

    private fun updateLoginState(update: LoginUiState.() -> LoginUiState) {
        _authState.update { it.copy(login = it.login.update()) }
    }

    private fun updateEditProfileState(update: EditProfileUiState.() -> EditProfileUiState) {
        _authState.update { it.copy(editProfile = it.editProfile.update()) }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.ShowMessage(message))
        }
    }

    sealed interface UiEvent {
        data class ShowMessage(val message: String) : UiEvent
    }
}

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val observeSessionUseCase: () -> Flow<String?>,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository, observeSessionUseCase, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
