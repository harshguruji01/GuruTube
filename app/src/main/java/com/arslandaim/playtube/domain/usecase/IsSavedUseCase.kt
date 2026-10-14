/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.domain.usecase

import com.arslandaim.playtube.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsSavedUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    operator fun invoke(videoId: String): Flow<Boolean> = repository.isVideoInAnyLocalPlaylist(videoId)
}
