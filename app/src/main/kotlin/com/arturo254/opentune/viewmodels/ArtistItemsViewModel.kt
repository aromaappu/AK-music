/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.aromaappu.akmusic.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aromaappu.akmusic.innertube.YouTube
import com.aromaappu.akmusic.innertube.models.BrowseEndpoint
import com.aromaappu.akmusic.innertube.models.filterExplicit
import com.aromaappu.akmusic.innertube.models.filterVideo
import com.aromaappu.akmusic.constants.HideExplicitKey
import com.aromaappu.akmusic.constants.HideVideoKey
import com.aromaappu.akmusic.models.ItemsPage
import com.aromaappu.akmusic.utils.dataStore
import com.aromaappu.akmusic.utils.get
import com.aromaappu.akmusic.utils.reportException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistItemsViewModel
@Inject
constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val browseId = savedStateHandle.get<String>("browseId")!!
    private val params = savedStateHandle.get<String>("params")

    val title = MutableStateFlow("")
    val itemsPage = MutableStateFlow<ItemsPage?>(null)

    init {
        viewModelScope.launch {
            YouTube
                .artistItems(
                    BrowseEndpoint(
                        browseId = browseId,
                        params = params,
                    ),
                ).onSuccess { artistItemsPage ->
                    title.value = artistItemsPage.title
                    itemsPage.value =
                        ItemsPage(
                            items = artistItemsPage.items
                                .distinctBy { it.id }
                                .filterExplicit(context.dataStore.get(HideExplicitKey, false))
                                .filterVideo(context.dataStore.get(HideVideoKey, false)),
                            continuation = artistItemsPage.continuation,
                        )
                }.onFailure {
                    reportException(it)
                }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            val oldItemsPage = itemsPage.value ?: return@launch
            val continuation = oldItemsPage.continuation ?: return@launch
            YouTube
                .artistItemsContinuation(continuation)
                .onSuccess { artistItemsContinuationPage ->
                    itemsPage.update {
                        ItemsPage(
                            items =
                            (oldItemsPage.items + artistItemsContinuationPage.items)
                                .distinctBy { it.id }
                                .filterExplicit(context.dataStore.get(HideExplicitKey, false)),
                            continuation = artistItemsContinuationPage.continuation,
                        )
                    }
                }.onFailure {
                    reportException(it)
                }
        }
    }
}
