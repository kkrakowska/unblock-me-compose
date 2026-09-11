package com.kkrakowska.unblockme.data

import android.content.Context
import com.google.gson.Gson
import com.kkrakowska.unblockme.models.Level
import java.io.IOException

class LevelLoader(private val context: Context) {

    fun loadLevel(levelNumber: Int): Level? {
        val fileName = "level_$levelNumber.json"

        return try{
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val gson = Gson()
            gson.fromJson(jsonString, Level::class.java)
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }


}