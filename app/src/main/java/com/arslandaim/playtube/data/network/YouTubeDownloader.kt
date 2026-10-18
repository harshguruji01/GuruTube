/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.data.network

import com.arslandaim.playtube.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request as OkHttpRequest
import okhttp3.RequestBody.Companion.toRequestBody
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import java.io.IOException

class YouTubeDownloader(private val client: OkHttpClient) : Downloader() {

    @Throws(IOException::class)
    override fun execute(request: Request): Response {
        val url = request.url()
        val method = request.httpMethod()
        val headers = request.headers()
        val data = request.dataToSend()

        val okHttpRequestBuilder = OkHttpRequest.Builder()
            .url(url)
            .method(method, data?.toRequestBody())

        val mergedHeaders = mutableMapOf<String, MutableList<String>>()
        headers.forEach { (key, values) ->
            mergedHeaders[key] = values.toMutableList()
        }

        // Set default Accept-Language if missing
        if (!mergedHeaders.containsKey("Accept-Language")) {
            mergedHeaders["Accept-Language"] = mutableListOf("en-US,en;q=0.9")
        }

        // Only set default User-Agent if NewPipeExtractor did not specify one
        if (!mergedHeaders.containsKey("User-Agent")) {
            mergedHeaders["User-Agent"] = mutableListOf(Constants.DEFAULT_USER_AGENT)
        }

        // Bypass YouTube Consent/GDPR redirection only on youtube.com web pages, NEVER on googlevideo CDN
        if (url.contains("youtube.com") && !url.contains("googlevideo.com")) {
            val existingCookies = mergedHeaders["Cookie"] ?: mutableListOf()
            if (existingCookies.none { it.contains("CONSENT=YES") || it.contains("SOCS=") }) {
                existingCookies.add(Constants.YouTube.CONSENT_COOKIE)
                mergedHeaders["Cookie"] = existingCookies
            }
        }

        mergedHeaders.forEach { (key, values) ->
            values.forEach { value ->
                okHttpRequestBuilder.addHeader(key, value)
            }
        }

        val maxRetries = 2
        var attempt = 0
        var lastResponse: okhttp3.Response? = null
        var lastException: IOException? = null

        while (attempt <= maxRetries) {
            try {
                val okHttpResponse = client.newCall(okHttpRequestBuilder.build()).execute()
                
                // If it's a 403 or 429, it might be an IP block or temporary throttle.
                if (okHttpResponse.code == 403 || okHttpResponse.code == 429) {
                    okHttpResponse.close()
                    attempt++
                    if (attempt <= maxRetries) {
                        Thread.sleep(1000L * attempt) // Linear backoff
                        continue
                    }
                }
                
                lastResponse = okHttpResponse
                break
            } catch (e: IOException) {
                lastException = e
                attempt++
                if (attempt <= maxRetries) {
                    Thread.sleep(500L * attempt)
                }
            }
        }

        val response = lastResponse ?: throw lastException ?: IOException("Request failed after $maxRetries retries")

        val responseBody = response.body?.string()
        val responseCode = response.code
        val responseMessage = response.message
        val responseHeaders = response.headers.toMultimap()

        return Response(responseCode, responseMessage, responseHeaders, responseBody, url)
    }
}
