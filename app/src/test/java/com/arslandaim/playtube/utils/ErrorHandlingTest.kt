/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.utils

import org.junit.Assert.*
import org.junit.Test
import java.io.IOException
import java.net.UnknownHostException

class ErrorHandlingTest {

    @Test
    fun `fromThrowable maps Network errors correctly`() {
        assertTrue(GuruTubeError.fromThrowable(UnknownHostException()) is GuruTubeError.Network)
        assertTrue(GuruTubeError.fromThrowable(IOException()) is GuruTubeError.Network)
    }

    @Test
    fun `fromThrowable maps AuthError correctly`() {
        assertTrue(GuruTubeError.fromThrowable(SecurityException()) is GuruTubeError.AuthError)
    }

    @Test
    fun `fromThrowable maps ApiThrottled correctly`() {
        val throttledException = Exception("HTTP 429 Too Many Requests")
        assertTrue(GuruTubeError.fromThrowable(throttledException) is GuruTubeError.ApiThrottled)
    }

    @Test
    fun `getMessage returns human readable strings`() {
        assertEquals("No internet connection", GuruTubeError.Network.getMessage())
        assertEquals("Authentication required", GuruTubeError.AuthError.getMessage())
        assertEquals("Custom Error", GuruTubeError.Unknown("Custom Error").getMessage())
    }
}
