package com.snakyapps.khiardle.backend.repository

import android.content.SharedPreferences
import com.snakyapps.khiardle.backend.models.Level

class LocalStorageLevelRepository(
    private val sharedPreferences: SharedPreferences,
) : LevelRepository {
    private var lastLevel: Long
        get() {
            return sharedPreferences.getLong("LastLevel", 1)
        }
        set(value) {
            sharedPreferences.edit().putLong("LastLevel", value).apply()
        }

    override fun getCurrentLevelNumber(): Long {
        return lastLevel
    }

    override fun levelPassed(level: Level) {
        val settingLevel = kotlin.math.max(level.number + 1, lastLevel)
        lastLevel = settingLevel
    }

    override fun reset() {
        lastLevel = 1
    }
}