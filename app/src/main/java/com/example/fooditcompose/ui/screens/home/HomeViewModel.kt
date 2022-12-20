package com.example.fooditcompose.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.domain.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun logout() {
        userRepository.deleteToken()
    }
}