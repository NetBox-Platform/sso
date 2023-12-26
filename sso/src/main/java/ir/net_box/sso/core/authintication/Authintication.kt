package ir.net_box.sso.core.authintication

import android.content.Context
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.NET_STORE_PACKAGE_NAME
import ir.net_box.sso.security.Security
import ir.net_box.sso.util.getLauncherVersion
import ir.net_box.sso.util.getNetstoreVersion
import ir.net_box.sso.util.getPackageInfo
import ir.net_box.sso.util.updateLauncherToLatestVersion
import ir.net_box.sso.util.updateNetstoreToLatestVersion
import java.lang.IllegalStateException

object Authentication {

    fun isLauncherInstalled(context: Context): Boolean =
        getPackageInfo(context, LAUNCHER_PACKAGE_NAME) != null &&
                Security.verifyLauncherIsInstalled(context)

    fun isNetstoreInstalled(context: Context): Boolean =
        getPackageInfo(context, NET_STORE_PACKAGE_NAME) != null &&
                Security.verifyNetstoreIsInstalled(context)

    fun shouldUpdateLauncher(context: Context, minLauncherVersionCode: Int) =
        getLauncherVersion(context) < minLauncherVersionCode

    private fun shouldUpdateNetstore(context: Context, minStoreVersionCode: Int) =
        getNetstoreVersion(context) < minStoreVersionCode

    fun updateLauncher(context: Context) {
        if (getPackageInfo(context, NET_STORE_PACKAGE_NAME) != null) {
            updateLauncherToLatestVersion(context)
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
}