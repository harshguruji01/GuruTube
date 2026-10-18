/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
 */
package com.arslandaim.playtube.domain.repository

import kotlinx.coroutines.flow.StateFlow

enum class CheckStatus {
    IDLE,
    CHECKING,
    UP_TO_DATE,
    UPDATE_AVAILABLE,
    ERROR
}

data class UpdateInfo(
    val hasUpdate: Boolean = false,
    val latestVersion: String = "",
    val releaseNotes: String = "",
    val updateUrl: String = "",
    val downloadUrl: String = "",
    val isChecking: Boolean = false,
    val checkStatus: CheckStatus = CheckStatus.IDLE,
    val apkSize: Long = 0L,
    val errorMessage: String = ""
)

interface UpdateRepository {
    val updateInfo: StateFlow<UpdateInfo>
    suspend fun checkForUpdates()
}
