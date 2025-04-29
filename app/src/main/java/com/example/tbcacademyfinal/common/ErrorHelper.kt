package com.example.tbcacademyfinal.common

fun <T> Resource<T>.errorOrNull(): String? =
    (this as? Resource.Error)?.message