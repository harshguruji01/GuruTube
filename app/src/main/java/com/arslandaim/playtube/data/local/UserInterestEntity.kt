/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_interests")
data class UserInterestEntity(
    @PrimaryKey val keyword: String,
    val weight: Float,
    val lastUpdated: Long = System.currentTimeMillis()
)
