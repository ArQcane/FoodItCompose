package com.example.fooditcompose.ui.screens.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.user.User
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _currentPage = MutableStateFlow(CurrentPage.LOGIN)
    val currentPage = _currentPage.asStateFlow()

    private val _isLoggedIn = Channel<Boolean>()
    val isLoggedIn = _isLoggedIn.receiveAsFlow()

    private val _invalidLoginMessage = MutableLiveData<String>()
    val invalidLoginMessage: LiveData<String>
        get() = _invalidLoginMessage

    init {
        checkIfLoggedIn()
    }



    private fun checkIfLoggedIn() {
        viewModelScope.launch {
            val tokenResource = withContext(Dispatchers.IO) {
                userRepository.getToken()
            }
            when (tokenResource) {
                is Resource.Failure -> {
                    val resource = withContext(Dispatchers.IO){
                        tokenResource.error
                    }
                    Log.d("Failure Resource Checked if logged in", resource.toString())
                }
                is Resource.Success -> {
                    val resource = withContext(Dispatchers.IO) {
                        userRepository.validateToken(
                            token = tokenResource.result
                        )
                    }
                    when (resource) {
                        is Resource.Success -> _isLoggedIn.send(true)
                        else -> Unit
                    }
                }
                else -> Unit
            }
        }
    }

    fun loginUser(username: String, password: String) {
        if (!validateLoginFields(username, password)) return
        viewModelScope.launch {
            val loginResourceToken = withContext(Dispatchers.IO) {
                userRepository.login(username, password)
            }
            when (loginResourceToken) {
                is Resource.Failure -> {
                    val resource = withContext(Dispatchers.IO){
                        loginResourceToken.error
                    }
                    Log.d("Failure Resource", resource.toString())
                }
                is Resource.Success -> {
                    val resource = withContext(Dispatchers.IO){
                        userRepository.validateToken(token = loginResourceToken.result)
                    }
                    when(resource){
                        is Resource.Success -> {
                            _isLoggedIn.send(true)
                        }
                        is Resource.Failure -> {
                            _isLoggedIn.send(false)
                        }
                        else -> Unit
                    }
                }
                else -> Unit
            }
        }
    }

//    fun getProfile(token: String) {
//        viewModelScope.launch {
//            val profile = userRepository.getProfile(token)
//            if (profile.body() == null || profile.body()!!.size == 0)
//                return@launch _profile.postValue(
//                    null
//                )
//            _profile.postValue(profile.body()!![0])
//        }
//    }

    fun togglePage() {
        _currentPage.update { page ->
            when (page) {
                CurrentPage.LOGIN -> CurrentPage.SIGNUP
                CurrentPage.SIGNUP -> CurrentPage.LOGIN
            }
        }
    }

    private fun validateLoginFields(username: String, password: String): Boolean {
        if (username.isEmpty()) return run {
            _invalidLoginMessage.postValue("Username required")
            false
        }

        if (password.isEmpty()) return run {
            _invalidLoginMessage.postValue("Password required")
            false
        }

        return true
    }
}



data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false
)

data class SignUpState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val signUpStage: SignUpStage = SignUpStage.NAME,
    val isLoading: Boolean = false
)

enum class SignUpStage {
    NAME, PASSWORD
}


enum class CurrentPage {
    LOGIN, SIGNUP
}