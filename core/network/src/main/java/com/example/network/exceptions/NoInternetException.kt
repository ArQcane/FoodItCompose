package com.example.network.exceptions

import okio.IOException

class NoInternetException : IOException(
    "No internet available, please check your connected WIFi or Data"
)