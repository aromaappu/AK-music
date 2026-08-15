/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

package com.aromaappu.akmusic.innertube.pages

import com.aromaappu.akmusic.innertube.models.Album
import com.aromaappu.akmusic.innertube.models.AlbumItem
import com.aromaappu.akmusic.innertube.models.Artist
import com.aromaappu.akmusic.innertube.models.ArtistItem
import com.aromaappu.akmusic.innertube.models.MusicResponsiveListItemRenderer
import com.aromaappu.akmusic.innertube.models.MusicTwoRowItemRenderer
import com.aromaappu.akmusic.innertube.models.PlaylistItem
import com.aromaappu.akmusic.innertube.models.SongItem
import com.aromaappu.akmusic.innertube.models.YTItem
import com.aromaappu.akmusic.innertube.models.oddElements
import com.aromaappu.akmusic.innertube.utils.parseTime

data class LibraryAlbumsPage(
    val albums: List<AlbumItem>,
    val continuation: String?,
) {
    companion object {
        fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): AlbumItem? {
            val browseId = renderer.navigationEndpoint.browseEndpoint?.browseId ?: return null
            val playlistId = renderer.thumbnailOverlay?.musicItemThumbnailOverlayRenderer?.content
                ?.musicPlayButtonRenderer?.playNavigationEndpoint
                ?.watchPlaylistEndpoint?.playlistId
                ?: renderer.menu?.menuRenderer?.items?.firstOrNull()
                    ?.menuNavigationItemRenderer?.navigationEndpoint
                    ?.watchPlaylistEndpoint?.playlistId
                ?: browseId.removePrefix("MPREb_").let { "OLAK5uy_$it" }

            return AlbumItem(
                browseId = browseId,
                playlistId = playlistId,
                title = renderer.title.runs?.firstOrNull()?.text ?: return null,
                artists = null,
                year = renderer.subtitle?.runs?.lastOrNull()?.text?.toIntOrNull(),
                thumbnail = renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl() ?: return null,
                explicit = renderer.subtitleBadges?.find {
                    it.musicInlineBadgeRenderer?.icon?.iconType == "MUSIC_EXPLICIT_BADGE"
                } != null
            )
        }
    }
}
