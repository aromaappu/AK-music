/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

package com.aromaappu.akmusic.innertube.models.response

import com.aromaappu.akmusic.innertube.models.Continuation
import com.aromaappu.akmusic.innertube.models.ContinuationItemRenderer
import com.aromaappu.akmusic.innertube.models.MusicResponsiveListItemRenderer
import com.aromaappu.akmusic.innertube.models.Tabs
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val contents: Contents?,
    val continuationContents: ContinuationContents?,
) {
    @Serializable
    data class Contents(
        val tabbedSearchResultsRenderer: Tabs?,
    )

    @Serializable
    data class ContinuationContents(
        val musicShelfContinuation: MusicShelfContinuation,
    ) {
        @Serializable
        data class MusicShelfContinuation(
            val contents: List<Content>,
            val continuations: List<Continuation>?,
        ) {
            @Serializable
            data class Content(
                val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer? = null,
                val continuationItemRenderer: ContinuationItemRenderer? = null,
            )
        }
    }
}
