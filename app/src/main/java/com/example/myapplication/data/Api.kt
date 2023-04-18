package com.example.myapplication.data

import android.util.Log
import com.example.myapplication.data.model.TemperatureData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Api {
    private val client = OkHttpClient()
    fun makeRequest(callback: (TemperatureData?, String?) -> Unit): Unit {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.tomorrow.io")
            .addPathSegment("v4")
            .addPathSegment("timelines")
            .addQueryParameter("location", "41.023568, 28.632935")
            .addQueryParameter("fields", "temperature")
            .addQueryParameter("timesteps", "1d")
            .addQueryParameter("apikey", "scS5qxlykmQKuh9Ch9HfllG1sGqsrCWe")
            .build()


        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("mujtaba1", "fail: ${e.message}")
                callback(null, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val result = JSONObject(jsonString).toTemperatureData()
                    callback(result, null)
                }
            }
        })
    }
}
