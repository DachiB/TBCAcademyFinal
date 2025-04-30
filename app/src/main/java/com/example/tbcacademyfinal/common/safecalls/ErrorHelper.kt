package com.example.tbcacademyfinal.common.safecalls

import com.example.tbcacademyfinal.common.Resource

fun <T> Resource<T>.errorOrNull(): String? =
    (this as? Resource.Error)?.message

