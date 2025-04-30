package com.example.tbcacademyfinal.common

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.SurfaceView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun SurfaceView.snapshotBitmap(): Bitmap =
    suspendCancellableCoroutine { cont ->
        val w = width.coerceAtLeast(1)
        val h = height.coerceAtLeast(1)
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        PixelCopy.request(
            this, bmp,
            { code ->
                if (code == PixelCopy.SUCCESS) cont.resume(bmp)
                else cont.resumeWithException(RuntimeException("PixelCopy failed: $code"))
            },
            Handler(Looper.getMainLooper())
        )
    }