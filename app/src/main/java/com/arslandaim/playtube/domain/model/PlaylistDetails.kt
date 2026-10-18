/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.domain.model

import androidx.annotation.Keep

@Keep
data class PlaylistDetails(
    val id: String,
    val title: String,
    val uploaderName: String,
    val uploaderUrl: String?,
    val thumbnailUrl: String,
    val videos: List<VideoItem>
)
