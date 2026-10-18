/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
 */
package com.arslandaim.playtube.data.repository

import com.arslandaim.playtube.BuildConfig
import com.arslandaim.playtube.data.network.GitHubCommit
import com.arslandaim.playtube.data.network.GitHubRelease
import com.arslandaim.playtube.data.network.VersionConfig
import com.arslandaim.playtube.domain.repository.CheckStatus
import com.arslandaim.playtube.domain.repository.UpdateInfo
import com.arslandaim.playtube.domain.repository.UpdateRepository
import com.arslandaim.playtube.utils.PTLog
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

    private val _updateInfo = MutableStateFlow(UpdateInfo(latestVersion = BuildConfig.VERSION_NAME))
    override val updateInfo: StateFlow<UpdateInfo> = _updateInfo.asStateFlow()

    override suspend fun checkForUpdates() {
        _updateInfo.value = _updateInfo.value.copy(
            isChecking = true,
            checkStatus = CheckStatus.CHECKING,
            errorMessage = ""
        )

        // 1. Check raw version.json from repository main branch (Instant, fast, no GitHub API rate limits)
        try {
            val versionConfig: VersionConfig = client.get("https://raw.githubusercontent.com/harshguruji01/GuruTube/main/version.json").body()
            val latestVersion = versionConfig.versionName.ifBlank { "v${versionConfig.versionCode}" }.removePrefix("v").trim()
            val currentVersion = BuildConfig.VERSION_NAME

            val hasUpdate = (versionConfig.versionCode > BuildConfig.VERSION_CODE) || isVersionNewer(currentVersion, latestVersion)
            val downloadUrl = versionConfig.downloadUrl.ifBlank {
                "https://raw.githubusercontent.com/harshguruji01/GuruTube/main/GuruTube.apk"
            }

            _updateInfo.value = UpdateInfo(
                hasUpdate = hasUpdate,
                latestVersion = latestVersion,
                releaseNotes = versionConfig.releaseNotes,
                updateUrl = "https://github.com/harshguruji01/GuruTube",
                downloadUrl = downloadUrl,
                isChecking = false,
                checkStatus = if (hasUpdate) CheckStatus.UPDATE_AVAILABLE else CheckStatus.UP_TO_DATE,
                apkSize = versionConfig.apkSize
            )
            return
        } catch (e: Exception) {
            PTLog.w("UpdateRepository", "Failed to check version.json, falling back to releases: ${e.message}")
        }

        // 2. Check GitHub Releases if available
        try {
            val response: GitHubRelease = client.get("https://api.github.com/repos/harshguruji01/GuruTube/releases/latest").body()
            val latestVersion = response.tagName.removePrefix("v").trim()
            val currentVersion = BuildConfig.VERSION_NAME

            val hasUpdate = isVersionNewer(currentVersion, latestVersion)
            val apkAsset = response.assets.firstOrNull { it.name.endsWith(".apk", ignoreCase = true) }
            val downloadUrl = apkAsset?.browserDownloadUrl?.ifBlank {
                "https://raw.githubusercontent.com/harshguruji01/GuruTube/main/GuruTube.apk"
            } ?: "https://raw.githubusercontent.com/harshguruji01/GuruTube/main/GuruTube.apk"

            _updateInfo.value = UpdateInfo(
                hasUpdate = hasUpdate,
                latestVersion = latestVersion,
                releaseNotes = response.body ?: "",
                updateUrl = response.htmlUrl,
                downloadUrl = downloadUrl,
                isChecking = false,
                checkStatus = if (hasUpdate) CheckStatus.UPDATE_AVAILABLE else CheckStatus.UP_TO_DATE,
                apkSize = apkAsset?.size ?: 0L
            )
            return
        } catch (e: Exception) {
            PTLog.w("UpdateRepository", "Failed to check GitHub releases, falling back to commit info: ${e.message}")
        }

        // 3. Fallback to latest commit check
        try {
            val commit: GitHubCommit = client.get("https://api.github.com/repos/harshguruji01/GuruTube/commits/main").body()
            val commitMsg = commit.commitDetail?.message ?: "GuruTube repository update"

            _updateInfo.value = UpdateInfo(
                hasUpdate = false,
                latestVersion = BuildConfig.VERSION_NAME,
                releaseNotes = commitMsg,
                updateUrl = commit.htmlUrl,
                downloadUrl = "https://raw.githubusercontent.com/harshguruji01/GuruTube/main/GuruTube.apk",
                isChecking = false,
                checkStatus = CheckStatus.UP_TO_DATE
            )
        } catch (e: Exception) {
            PTLog.e("UpdateRepository", "All update checks failed", e)
            _updateInfo.value = _updateInfo.value.copy(
                isChecking = false,
                checkStatus = CheckStatus.ERROR,
                errorMessage = e.localizedMessage ?: "Failed to check for updates"
            )
        }
    }

    private fun isVersionNewer(current: String, latest: String): Boolean {
        if (latest.isBlank()) return false
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
