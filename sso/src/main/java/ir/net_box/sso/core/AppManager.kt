package ir.net_box.sso.core

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.NET_STORE_PACKAGE_NAME
import ir.net_box.sso.security.Security
import ir.net_box.sso.util.getLauncherVersion
import ir.net_box.sso.util.getNetstoreVersion
import ir.net_box.sso.util.getPackageInfo

object AppManager {

    fun isNetboxLauncherInstalled(context: Context): Boolean =
        getPackageInfo(context, LAUNCHER_PACKAGE_NAME) != null &&
                Security.verifyAppIsInstalled(context, LAUNCHER_PACKAGE_NAME)

    fun isNetstoreInstalled(context: Context): Boolean =
        getPackageInfo(context, NET_STORE_PACKAGE_NAME) != null &&
                Security.verifyAppIsInstalled(context, NET_STORE_PACKAGE_NAME)

    fun shouldUpdateNetboxLauncher(context: Context, minLauncherVersionCode: Int) =
        getLauncherVersion(context) < minLauncherVersionCode

    fun shouldUpdateNetstore(context: Context, minStoreVersionCode: Int) =
        getNetstoreVersion(context) < minStoreVersionCode

    fun updateNetboxLauncher(context: Context) {
        if (getPackageInfo(context, NET_STORE_PACKAGE_NAME) != null) {
            updateNetboxLauncherToLatestVersion(context)
        } else {
            throw IllegalStateException("This is not a netbox device or netstore is not installed!")
        }
    }

    fun updateNetstore(context: Context) {
        if (getPackageInfo(context, NET_STORE_PACKAGE_NAME) != null) {
            updateNetstoreToLatestVersion(context)
        } else {
            throw IllegalStateException("This is not a netbox device or netstore is not installed!")
        }
    }

    /**
     * This function takes the launcher package and fetches its latest version
     * from the store using a deep link
     */
    private fun updateNetboxLauncherToLatestVersion(context: Context) {
        val browserIntent: Intent?
        try {
            browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.store.net_box.ir/store=$LAUNCHER_PACKAGE_NAME")
            )
            browserIntent.setPackage(NET_STORE_PACKAGE_NAME)
            context.startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * This function takes the Netstore package and fetches its latest version
     * from the store using a deep link
     */
    private fun updateNetstoreToLatestVersion(context: Context) {
        val browserIntent: Intent?
        try {
            browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.store.net_box.ir/store=$NET_STORE_PACKAGE_NAME")
            )
            browserIntent.setPackage(NET_STORE_PACKAGE_NAME)
            context.startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}