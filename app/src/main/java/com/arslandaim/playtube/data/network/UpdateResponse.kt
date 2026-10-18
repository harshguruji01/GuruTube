/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
 */
package com.arslandaim.playtube.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionConfig(
    @SerialName("versionCode")
    val versionCode: Int = 0,
    @SerialName("versionName")
    val versionName: String = "",
    @SerialName("releaseNotes")
    val releaseNotes: String = "",
    @SerialName("downloadUrl")
    val downloadUrl: String = "",
    @SerialName("apkSize")
    val apkSize: Long = 0L,
    @SerialName("commitSha")
    val commitSha: String = "",
    @SerialName("sha256")
    val sha256: String = ""
)

@Serializable
data class GitHubRelease(
    @SerialName("tag_name")
    val tagName: String = "",
    @SerialName("body")
    val body: String? = "",
    @SerialName("html_url")
    val htmlUrl: String = "",
    @SerialName("assets")
    val assets: List<GitHubAsset> = emptyList()
)

@Serializable
data class GitHubAsset(
    @SerialName("name")
    val name: String = "",
    @SerialName("size")
    val size: Long = 0L,
    @SerialName("browser_download_url")
    val browserDownloadUrl: String = ""
)

@Serializable
data class GitHubCommit(
    @SerialName("sha")
    val sha: String = "",
    @SerialName("commit")
    val commitDetail: GitHubCommitDetail? = null,
    @SerialName("html_url")
    val htmlUrl: String = ""
)

@Serializable
data class GitHubCommitDetail(
    @SerialName("message")
    val message: String = ""
)
