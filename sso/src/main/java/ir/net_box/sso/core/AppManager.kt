package ir.net_box.sso.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import ir.net_box.sso.MIN_NETSTORE_VERSION
import ir.net_box.sso.NET_STORE_PACKAGE_NAME
import ir.net_box.sso.security.Security
import ir.net_box.sso.util.getNetstoreVersion
import ir.net_box.sso.util.getPackageInfo
import androidx.core.net.toUri

object AppManager {
    fun isNetstoreInstalled(context: Context): Boolean =
        getPackageInfo(context, NET_STORE_PACKAGE_NAME) != null &&
                Security.verifyAppIsInstalled(context, NET_STORE_PACKAGE_NAME)

    fun shouldUpdateNetstore(context: Context) = getNetstoreVersion(context) < MIN_NETSTORE_VERSION

    fun updateNetstore(context: Context) {
        if (getPackageInfo(context, NET_STORE_PACKAGE_NAME) != null) {
            updateNetstoreToLatestVersion(context)
        } else {
            throw IllegalStateException("Netstore is not installed!")
        }
    }

    private fun updateNetstoreToLatestVersion(context: Context) {
        val browserIntent: Intent?
        try {
            browserIntent = Intent(
                Intent.ACTION_VIEW,
                "https://www.store.net_box.ir/store=$NET_STORE_PACKAGE_NAME".toUri()
            )
            browserIntent.setPackage(NET_STORE_PACKAGE_NAME)
            context.startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}