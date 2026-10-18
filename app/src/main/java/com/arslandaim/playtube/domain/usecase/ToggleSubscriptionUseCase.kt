/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.domain.usecase

import com.arslandaim.playtube.data.local.SubscriptionEntity
import com.arslandaim.playtube.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleSubscriptionUseCase @Inject constructor(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(subscription: SubscriptionEntity) {
        val isSubscribed = repository.isSubscribed(subscription.channelId).first()
        if (isSubscribed) {
            // Use fuzzy delete to ensure both ID and legacy URL records are removed
            repository.unsubscribeByIdFuzzy(subscription.channelId)
        } else {
            repository.subscribe(subscription)
        }
    }
}
