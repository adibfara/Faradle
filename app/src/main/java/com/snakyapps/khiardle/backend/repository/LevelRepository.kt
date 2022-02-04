package com.snakyapps.khiardle.backend.repository

import com.snakyapps.khiardle.backend.models.Level

interface LevelRepository {
    fun getCurrentLevelNumber(): Long
    fun levelPassed(level: Level)
    fun reset()
}