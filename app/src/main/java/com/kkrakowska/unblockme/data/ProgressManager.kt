package com.kkrakowska.unblockme.data

import android.content.Context
import android.content.SharedPreferences

class ProgressManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("UnblockMeProgress", Context.MODE_PRIVATE)

    fun getUnlockedLevel(): Int {
        return prefs.getInt("UNLOCKED_LEVEL", 1)
    }

    fun unlockLevel(level: Int) {
        val currentUnlocked = getUnlockedLevel()
        if (level > currentUnlocked) {
            prefs.edit().putInt("UNLOCKED_LEVEL", level).apply()
        }
    }

    fun saveStars(level: Int, stars: Int) {
        val key = "STARS_$level"
        val current = prefs.getInt(key, 0)
        if (stars > current) {
            prefs.edit().putInt(key, stars).apply()
        }
    }

    fun getStars(level: Int): Int {
        return prefs.getInt("STARS_$level", 0)
    }

    fun getAllStars(totalLevels: Int): Map<Int, Int> {
        return (1..totalLevels).associateWith { getStars(it) }
    }
}