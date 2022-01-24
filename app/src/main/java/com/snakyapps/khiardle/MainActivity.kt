package com.snakyapps.khiardle

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.snakyapps.khiardle.backend.repository.AssetFileWordRepository
import com.snakyapps.khiardle.backend.repository.LocalStorageLevelRepository
import com.snakyapps.khiardle.backend.usecase.GetNextLevel
import com.snakyapps.khiardle.backend.usecase.GetWordStatus
import com.snakyapps.khiardle.backend.viewmodel.LevelsViewModel
import com.snakyapps.khiardle.ui.WordScreen
import com.snakyapps.khiardle.ui.theme.KhiardleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhiardleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // simple dependency injection
                    val assetWordRepository = remember {
                        AssetFileWordRepository(assets)
                    }
                    val getWordStatus = remember {
                        GetWordStatus(assetWordRepository)
                    }

                    val sharedPreferences: SharedPreferences = remember {
                        getSharedPreferences("default", MODE_PRIVATE)
                    }
                    val levelRepository = remember {
                        LocalStorageLevelRepository(sharedPreferences)
                    }

                    val getNextLevel = remember {
                        GetNextLevel(assetWordRepository, levelRepository)
                    }
                    val levelViewModel = remember {
                        LevelsViewModel(levelRepository, getNextLevel)
                    }

                    val level = levelViewModel.state().collectAsState().value.currentLevel
                    if (level != null) {
                        WordScreen(level, getWordStatus) {
                            levelViewModel.levelPassed()
                        }
                    }

                }
            }
        }
    }


}
