/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.domain.usecase

import com.arslandaim.playtube.data.local.HistoryEntity
import com.arslandaim.playtube.data.local.PreferencesManager
import com.arslandaim.playtube.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddToHistoryUseCase @Inject constructor(
    private val repository: LibraryRepository,
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(history: HistoryEntity) {
        if (preferencesManager.isIncognitoMode.first()) return
        repository.addToHistory(history)
    }
}
