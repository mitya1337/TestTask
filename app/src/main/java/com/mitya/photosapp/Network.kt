package com.mitya.photosapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.mitya.photosapp.albums.Album
import com.mitya.photosapp.photos.Photo
import com.mitya.photosapp.users.User
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun makeHttpRequest(url: String, params: Map<String, String> = HashMap()): String {
    var result = ""
    var urlConnection: HttpURLConnection? = null
    try {
        val builder = Uri.Builder()
        for (entry in params.entries) {
            builder.appendQueryParameter(entry.key, entry.value)
        }
        val encodedParams = builder.build().encodedQuery
        val tmpUrl = "$url?$encodedParams"
        val urlObj = URL(tmpUrl)
        urlConnection = urlObj.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.connect()
        val inputStream = urlConnection.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        reader.forEachLine { sb.append(it) }
        inputStream.close()
        result = sb.toString()

    } catch (e: JSONException) {
        Log.e("JSON Parser", "Error parsing data $e")
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        urlConnection?.disconnect()
    }
    return result
}

fun downloadImage(src: String): Bitmap {
    var result: Bitmap? = null
    try {
        val url = URL(src)
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.connect()
        val input = urlConnection.inputStream
        result = BitmapFactory.decodeStream(input)

    } catch (e: IOException) {
        e.printStackTrace()
    }
    return result as Bitmap
}

fun parseUsersData(data: String): List<User> {
    val result = ArrayList<User>()
    val jsonArray = JSONArray(data)
    var i = 0
    while (!jsonArray.isNull(i)) {
        val jsonObject = jsonArray.getJSONObject(i)
        result.add(User(jsonObject.getInt("id"), jsonObject.getString("name")))
        i++
    }
    return result
}

fun parseAlbumData(data: String): List<Album> {
    val result = ArrayList<Album>()
    val jsonArray = JSONArray(data)
    var i = 0
    while (!jsonArray.isNull(i)) {
        val jsonObject = jsonArray.getJSONObject(i)
        result.add(Album(jsonObject.getInt("id"), jsonObject.getString("title")))
        i++
    }
    return result
}

fun parsePhotoData(data: String): List<Photo> {
    val result = ArrayList<Photo>()
    val jsonArray = JSONArray(data)
    var i = 0
    while (!jsonArray.isNull(i)) {
        val jsonObject = jsonArray.getJSONObject(i)
        result.add(
            Photo(
                jsonObject.getInt("id"),
                jsonObject.getString("title"),
                jsonObject.getString("url")
            )
        )
        i++
    }
    return result
}