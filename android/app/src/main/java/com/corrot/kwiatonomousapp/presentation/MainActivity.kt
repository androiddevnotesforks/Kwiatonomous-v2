package com.corrot.kwiatonomousapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.corrot.kwiatonomousapp.domain.repository.PreferencesRepository
import com.corrot.kwiatonomousapp.presentation.app_settings.AppTheme
import com.corrot.kwiatonomousapp.presentation.theme.KwiatonomousAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPreferencesRepository: PreferencesRepository

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            appPreferencesRepository.getAppTheme().collect { currentAppTheme ->
                setContent {
                    val isDarkTheme = when (currentAppTheme) {
                        AppTheme.AUTO -> isSystemInDarkTheme()
                        AppTheme.LIGHT -> false
                        AppTheme.DARK -> true
                    }

                    KwiatonomousAppTheme(darkTheme = isDarkTheme) {
                        Surface(color = MaterialTheme.colors.background) {
                            Navigation()
                        }
                    }
                }
            }
        }
    }
}