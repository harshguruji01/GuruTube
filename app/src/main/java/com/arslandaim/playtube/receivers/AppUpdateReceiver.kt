/*
 * GuruTube Project Original (2026)
 * HarshGuruJi (https://github.com/harshguruji01/GuruTube)
 * Licenced Under GPL-3.0+
 */
package com.arslandaim.playtube.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arslandaim.playtube.utils.PTLog

class AppUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            PTLog.d("AppUpdateReceiver", "Package updated successfully. Automatically reopening GuruTube...")
            try {
                val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                launchIntent?.let { context.startActivity(it) }
            } catch (e: Exception) {
                PTLog.e("AppUpdateReceiver", "Failed to relaunch app after package replacement", e)
            }
        }
    }
}
