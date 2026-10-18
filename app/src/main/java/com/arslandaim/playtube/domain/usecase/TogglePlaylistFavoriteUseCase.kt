/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.domain.usecase

import com.arslandaim.playtube.data.local.PlaylistFavoriteEntity
import com.arslandaim.playtube.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TogglePlaylistFavoriteUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(favorite: PlaylistFavoriteEntity) {
        val isFavorite = repository.isPlaylistFavorite(favorite.playlistId).first()
        if (isFavorite) {
            repository.removeFromPlaylistFavorites(favorite)
        } else {
            repository.addToPlaylistFavorites(favorite)
        }
    }
}
