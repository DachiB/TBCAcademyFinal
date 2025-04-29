package com.example.tbcacademyfinal.common

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.SurfaceView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun <T> Resource<T>.errorOrNull(): String? =
    (this as? Resource.Error)?.message

suspend fun SurfaceView.snapshotBitmap(): Bitmap = suspendCancellableCoroutine { cont ->
    // make sure view has been laid out
    if (width == 0 || height == 0) {
        cont.resumeWithException(IllegalStateException("view has no size"))
        return@suspendCancellableCoroutine
    }

    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    PixelCopy.request(this, bmp, { result ->
        if (result == PixelCopy.SUCCESS) cont.resume(bmp)
        else cont.resumeWithException(RuntimeException("PixelCopy failed with code $result"))
    }, Handler(Looper.getMainLooper()))
}