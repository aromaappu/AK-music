/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.aromaappu.akmusic.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aromaappu.akmusic.innertube.YouTube
import com.aromaappu.akmusic.innertube.pages.MoodAndGenres
import com.aromaappu.akmusic.utils.reportException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodAndGenresViewModel
@Inject
constructor() : ViewModel() {
    val moodAndGenres = MutableStateFlow<List<MoodAndGenres.Item>?>(null)

    init {
        viewModelScope.launch {
            YouTube
                .explore()
                .onSuccess {
                    moodAndGenres.value = it.moodAndGenres
                }.onFailure {
                    reportException(it)
                }
        }
    }
}
