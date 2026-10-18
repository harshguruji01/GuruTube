/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.domain.model

import androidx.annotation.Keep

@Keep
data class CommentItem(
    val authorName: String,
    val authorThumbnailUrl: String?,
    val authorUrl: String?,
    val commentText: String,
    val publishedTime: String?,
    val likeCount: Int = 0,
    val isHeartedByUploader: Boolean = false,
    val replyCount: Int = 0,
    val commentId: String
)
