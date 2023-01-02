package com.example.fooditcompose.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.login.LoginEvent
import com.example.authentication.login.LoginState
import com.example.domain.restaurant.usecases.GetAllRestaurantsUseCase
import com.example.domain.restaurant.usecases.SearchRestaurantUseCase
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import com.example.restaurant.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRestaurantUseCase: SearchRestaurantUseCase,
    private val getAllRestaurantsUseCase: GetAllRestaurantsUseCase,
): ViewModel() {
    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()


    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearchedQueryChanged -> {
                _searchState.update { state ->
                    state.copy(
                        searchedQuery = event.searchedQuery,
                        searchedQueryError = null
                    )
                }
            }
            is SearchEvent.OnSearch -> searchQuery()
        }
    }
    private fun searchQuery() {
        val searchedQuery = _searchState.value.searchedQuery
        if(searchedQuery != "" || searchedQuery != null) {
            searchRestaurantUseCase(
                searchQuery = searchedQuery
            ).onEach {
                when (it) {
                    is Resource.Success -> _searchState.update { state ->
                        delay(1000L)
                        state.copy(
                            searchedRestaurantList = it.result,
                            isLoading = false
                        )
                    }
                    is Resource.Failure -> {
                        _searchState.update { state ->
                            state.copy(isLoading = false)
                        }
                        if (it.error !is ResourceError.Default) return@onEach
                        val defaultError = it.error as ResourceError.Default
                        _errorChannel.send(defaultError.error)
                    }
                    is Resource.Loading -> _searchState.update { state ->
                        state.copy(isLoading = it.isLoading)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
        }
        else{
            getAllRestaurantsUseCase().onEach {
                when (it) {
                    is Resource.Success -> _searchState.update { state ->
                        delay(1000L)
                        state.copy(
                            searchedRestaurantList = it.result,
                            isLoading = false
                        )
                    }
                    is Resource.Failure -> {
                        _searchState.update { state ->
                            state.copy(isLoading = false)
                        }
                        if (it.error !is ResourceError.Default) return@onEach
                        val defaultError = it.error as ResourceError.Default
                        _errorChannel.send(defaultError.error)
                    }
                    is Resource.Loading -> _searchState.update { state ->
                        state.copy(isLoading = it.isLoading)
                    }
                }
            }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
        }
    }


}