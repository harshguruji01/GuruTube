/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
 */
package com.arslandaim.playtube.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arslandaim.playtube.data.local.PreferencesManager
import com.arslandaim.playtube.domain.repository.UpdateInfo
import com.arslandaim.playtube.domain.repository.UpdateRepository
import com.arslandaim.playtube.utils.PTLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import javax.inject.Inject

sealed interface UpdateDownloadState {
    data object Idle : UpdateDownloadState
    data class Downloading(
        val progress: Float,
        val downloadedBytes: Long,
        val totalBytes: Long,
        val speedText: String
    ) : UpdateDownloadState
    data class DownloadCompleted(val apkFile: File) : UpdateDownloadState
    data class Error(val message: String) : UpdateDownloadState
}

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val updateRepository: UpdateRepository,
    private val preferencesManager: PreferencesManager,
    private val okHttpClient: OkHttpClient
) : ViewModel() {

    val updateInfo: StateFlow<UpdateInfo> = updateRepository.updateInfo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UpdateInfo())

    val isAutoUpdateEnabled: StateFlow<Boolean> = preferencesManager.isAutoUpdateEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _downloadState = MutableStateFlow<UpdateDownloadState>(UpdateDownloadState.Idle)
    val downloadState: StateFlow<UpdateDownloadState> = _downloadState.asStateFlow()

    private val _showInstallDialog = MutableStateFlow(false)
    val showInstallDialog: StateFlow<Boolean> = _showInstallDialog.asStateFlow()

    private val _apkToInstall = MutableStateFlow<File?>(null)
    val apkToInstall: StateFlow<File?> = _apkToInstall.asStateFlow()

    private var downloadJob: Job? = null

    init {
        viewModelScope.launch {
            isAutoUpdateEnabled.collectLatest { enabled ->
                if (enabled) {
                    updateRepository.checkForUpdates()
                }
            }
        }
    }

    fun setAutoUpdateEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setAutoUpdateEnabled(enabled)
            if (enabled) {
                updateRepository.checkForUpdates()
            }
        }
    }

    fun checkForUpdates() {
        viewModelScope.launch {
            updateRepository.checkForUpdates()
        }
    }

    fun startDownload(context: Context) {
        if (_downloadState.value is UpdateDownloadState.Downloading) return

        val url = updateInfo.value.downloadUrl.ifBlank {
            "https://raw.githubusercontent.com/harshguruji01/GuruTube/main/GuruTube.apk"
        }

        downloadJob?.cancel()
        downloadJob = viewModelScope.launch(Dispatchers.IO) {
            val destinationDir = File(context.cacheDir, "updates").apply { mkdirs() }
            val targetFile = File(destinationDir, "GuruTube_update.apk")
            if (targetFile.exists()) {
                targetFile.delete()
            }

            _downloadState.value = UpdateDownloadState.Downloading(
                progress = 0f,
                downloadedBytes = 0L,
                totalBytes = updateInfo.value.apkSize,
                speedText = "Connecting..."
            )

            try {
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "GuruTube-Updater")
                    .build()

                val response = okHttpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    throw RuntimeException("HTTP download failed: ${response.code}")
                }

                val body = response.body
                val totalBytes = if (body.contentLength() > 0) body.contentLength() else updateInfo.value.apkSize

                val buffer = ByteArray(8192)
                var downloadedBytes = 0L
                var lastTime = System.currentTimeMillis()
                var bytesSinceLastTime = 0L
                var speedText = "0 KB/s"

                body.byteStream().use { input ->
                    FileOutputStream(targetFile).use { output ->
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            downloadedBytes += bytesRead
                            bytesSinceLastTime += bytesRead

                            val now = System.currentTimeMillis()
                            val elapsed = now - lastTime
                            if (elapsed >= 400) {
                                val bytesPerSecond = (bytesSinceLastTime * 1000.0) / elapsed
                                speedText = formatSpeed(bytesPerSecond)
                                lastTime = now
                                bytesSinceLastTime = 0L

                                val progress = if (totalBytes > 0) {
                                    (downloadedBytes.toFloat() / totalBytes).coerceIn(0f, 1f)
                                } else 0f

                                _downloadState.value = UpdateDownloadState.Downloading(
                                    progress = progress,
                                    downloadedBytes = downloadedBytes,
                                    totalBytes = totalBytes,
                                    speedText = speedText
                                )
                            }
                        }
                        output.flush()
                    }
                }

                _apkToInstall.value = targetFile
                _downloadState.value = UpdateDownloadState.DownloadCompleted(targetFile)
                _showInstallDialog.value = true
                PTLog.d("UpdateViewModel", "Download completed: ${targetFile.absolutePath} (${targetFile.length()} bytes)")
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) {
                    PTLog.d("UpdateViewModel", "Download cancelled by user")
                    _downloadState.value = UpdateDownloadState.Idle
                    return@launch
                }
                PTLog.e("UpdateViewModel", "Download failed", e)
                _downloadState.value = UpdateDownloadState.Error(e.localizedMessage ?: "Download failed")
            }
        }
    }

    fun cancelDownload() {
        downloadJob?.cancel()
        downloadJob = null
        _downloadState.value = UpdateDownloadState.Idle
    }

    fun dismissInstallDialog() {
        _showInstallDialog.value = false
    }

    fun installDownloadedUpdate(context: Context) {
        val apkFile = _apkToInstall.value ?: return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!context.packageManager.canRequestPackageInstalls()) {
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                        data = Uri.parse("package:${context.packageName}")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                    return
                }
            }

            val apkUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(installIntent)
            _showInstallDialog.value = false

            // App full close so that system updates cleanly, and AppUpdateReceiver re-opens it after install
            if (context is Activity) {
                context.finishAffinity()
            }
        } catch (e: Exception) {
            PTLog.e("UpdateViewModel", "Failed to launch installer", e)
            _downloadState.value = UpdateDownloadState.Error("Installation failed: ${e.localizedMessage}")
        }
    }

    private fun formatSpeed(bytesPerSecond: Double): String {
        return when {
            bytesPerSecond >= 1024 * 1024 -> String.format(Locale.US, "%.1f MB/s", bytesPerSecond / (1024 * 1024))
            bytesPerSecond >= 1024 -> String.format(Locale.US, "%.0f KB/s", bytesPerSecond / 1024)
            else -> String.format(Locale.US, "%.0f B/s", bytesPerSecond)
        }
    }
}
