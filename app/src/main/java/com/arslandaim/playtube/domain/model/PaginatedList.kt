/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.domain.model

import org.schabi.newpipe.extractor.Page
import androidx.annotation.Keep

@Keep
data class PaginatedList<T>(
    val items: List<T>,
    val nextPage: Page?
)
