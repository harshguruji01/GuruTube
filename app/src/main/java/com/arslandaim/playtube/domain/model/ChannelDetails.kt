/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.domain.model

import org.schabi.newpipe.extractor.Page
import androidx.annotation.Keep

@Keep
data class ChannelDetails(
    val id: String,
    val name: String,
    val description: String?,
    val bannerUrl: String?,
    val avatarUrl: String?,
    val subscriberCount: Long?,
    val videos: List<VideoItem>,
    val nextVideosPage: Page? = null,
    val playlists: List<PlaylistItem> = emptyList()
)

@Keep
data class ChannelInfoBasic(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val subscriberCount: Long?
)
