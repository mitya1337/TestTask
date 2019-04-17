package com.mitya.photosapp

import android.graphics.Bitmap
import androidx.collection.LruCache

object Cache {
    private val memoryCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 2

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    fun getBitmapFromCache(id: Int): Bitmap? {
        return memoryCache.get(id.toString())
    }

    fun saveBitmapToCache(id: Int, bitmap: Bitmap) {
        memoryCache.put(id.toString(), bitmap)
    }
}