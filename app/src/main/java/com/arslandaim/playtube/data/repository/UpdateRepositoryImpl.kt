/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.data.repository

import com.arslandaim.playtube.BuildConfig
import com.arslandaim.playtube.data.network.GitHubRelease
import com.arslandaim.playtube.domain.repository.UpdateInfo
import com.arslandaim.playtube.domain.repository.UpdateRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : UpdateRepository {

    private val _updateInfo = MutableStateFlow(UpdateInfo())
    override val updateInfo: StateFlow<UpdateInfo> = _updateInfo.asStateFlow()

    override suspend fun checkForUpdates() {
        _updateInfo.value = _updateInfo.value.copy(isChecking = true)
        try {
            val response: GitHubRelease = client.get("https://api.github.com/repos/harshguruji01/GuruTube/releases/latest").body()
            val latestVersion = response.tagName.removePrefix("v").trim()
            val currentVersion = BuildConfig.VERSION_NAME

            val hasUpdate = isVersionNewer(currentVersion, latestVersion)
            val apkAsset = response.assets.firstOrNull { it.name.endsWith(".apk", ignoreCase = true) }
            val downloadUrl = apkAsset?.browserDownloadUrl?.ifBlank { response.htmlUrl } ?: response.htmlUrl

            _updateInfo.value = UpdateInfo(
                hasUpdate = hasUpdate,
                latestVersion = latestVersion,
                releaseNotes = response.body ?: "",
                updateUrl = response.htmlUrl,
                downloadUrl = downloadUrl,
                isChecking = false
            )
        } catch (e: Exception) {
            e.printStackTrace()
            _updateInfo.value = _updateInfo.value.copy(isChecking = false)
        }
    }

    private fun isVersionNewer(current: String, latest: String): Boolean {
        val currentParts = current.split(Regex("[^0-9]+")).filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }
        val latestParts = latest.split(Regex("[^0-9]+")).filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }

        if (latestParts.isEmpty()) return false
        if (currentParts.isEmpty()) return true

        val size = maxOf(currentParts.size, latestParts.size)
        for (i in 0 until size) {
            val currentPart = currentParts.getOrElse(i) { 0 }
            val latestPart = latestParts.getOrElse(i) { 0 }
            if (latestPart > currentPart) return true
            if (latestPart < currentPart) return false
        }
        return false
    }
}
