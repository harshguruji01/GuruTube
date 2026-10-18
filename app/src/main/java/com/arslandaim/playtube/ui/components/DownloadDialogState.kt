/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.ui.components

import com.arslandaim.playtube.domain.model.VideoItem
import com.arslandaim.playtube.domain.model.StreamBundle

sealed class DownloadDialogState {
    object Idle : DownloadDialogState()
    data class Loading(val video: VideoItem) : DownloadDialogState()
    data class ShowDialog(val video: VideoItem, val bundle: StreamBundle) : DownloadDialogState()
}
