package com.example.restaurant.search

sealed class SearchEvent{
    class OnSearchedQueryChanged(val searchedQuery: String): SearchEvent()
    object OnSearch: SearchEvent()
}
