package com.example.authentication.splashAnims

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        checkIfLoggedIn()
    }

    private fun checkIfLoggedIn() {
        viewModelScope.launch {
            when (val tokenResource = userRepository.getToken()) {
                is Resource.Success -> {
                    println("sharedPrefVal: ${tokenResource.result}")
                    when (userRepository.validateToken(tokenResource.result)) {
                        is Resource.Success -> {
                            _isLoggedIn.value = true
                        }
                        is Resource.Failure -> {
                            _isLoggedIn.value = false
                        }
                        else -> Unit
                    }
                }
                else -> Unit
            }
        }
    }
}