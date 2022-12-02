package com.example.fooditcompose.domain.common.exceptions

import okio.IOException

class NoInternetException : IOException(
    "No internet available, please check your connected WIFi or Data"
)