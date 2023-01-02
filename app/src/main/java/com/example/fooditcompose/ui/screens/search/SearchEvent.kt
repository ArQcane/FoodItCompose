package com.example.fooditcompose.ui.screens.search

sealed class SearchEvent{
    class OnSearchedQueryChanged(val searchedQuery: String): SearchEvent()
    object OnSearch: SearchEvent()
}
