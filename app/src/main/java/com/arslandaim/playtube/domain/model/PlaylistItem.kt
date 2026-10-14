/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.domain.model

import androidx.compose.runtime.Stable
import androidx.annotation.Keep

@Keep
@Stable
data class PlaylistItem(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val uploaderName: String,
    val uploaderUrl: String?,
    val streamCount: Long
)
