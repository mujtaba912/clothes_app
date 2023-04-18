package com.example.myapplication.data

import com.example.myapplication.data.model.*
import org.json.JSONObject

fun JSONObject.toTemperatureData(): TemperatureData {
    val dataObjectJson = this.getJSONObject("data")
    val timelinesListJson = dataObjectJson.getJSONArray("timelines")
    val timelineObject = timelinesListJson.getJSONObject(0)
    val intervalsList = timelineObject.getJSONArray("intervals")
    val intervalList = mutableListOf<Interval>()
    for (i in 0 until intervalsList.length()) {
        val intervalObject = intervalsList.getJSONObject(i)
        val startTime = intervalObject.getString("startTime")
        val valuesObjectJson = intervalObject.getJSONObject("values")
        val temperature = valuesObjectJson.getDouble("temperature")
        val value = Value(temperature)
        val interval = Interval(startTime, value)
        intervalList.add(interval)
    }
    val dataObject = Data(Timeline(intervalList))
    return TemperatureData(dataObject)
}