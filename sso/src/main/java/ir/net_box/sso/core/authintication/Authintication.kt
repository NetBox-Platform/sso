package ir.net_box.sso.core.authintication

import android.content.Context
import android.widget.Toast
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.MINIMUM_LAUNCHER_VERSION_SUPPORT
import ir.net_box.sso.NET_STORE_PACKAGE_NAME
import ir.net_box.sso.security.Security
import ir.net_box.sso.util.getLauncherVersion
import ir.net_box.sso.util.getPackageInfo
import ir.net_box.sso.util.updateLauncherToLatestVersion

object Authentication {

    fun isLauncherInstalled(context: Context): Boolean =
        getPackageInfo(context, LAUNCHER_PACKAGE_NAME) != null &&
                Security.verifyLauncherIsInstalled(context)

    fun isNeededToUpdateLauncher(context: Context) =
        getLauncherVersion(context) < MINIMUM_LAUNCHER_VERSION_SUPPORT

    /**
     * This function, after verifying that the launcher doesn't require an update and that
     * the store is installed on the device, calls the function `updateLauncherToLatestVersion`.
     */
    fun updateLauncher(context: Context) {
        if (isNeededToUpdateLauncher(context)) {
            if (getPackageInfo(context, NET_STORE_PACKAGE_NAME) != null) {
                updateLauncherToLatestVersion(context)
            } else {
                Toast.makeText(
                    context, "This is not a netbox device", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}