package ir.net_box.sso.core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.MINIMUM_LAUNCHER_VERSION_SUPPORT
import java.lang.IllegalStateException

object NetboxClient : Client {

    override fun startLauncherSignIn(context: Context, onSsoButtonClicked: () -> Unit) {

        if (AppManager.isNetboxLauncherInstalled(context)) {
            if (!AppManager.shouldUpdateNetboxLauncher(context, MINIMUM_LAUNCHER_VERSION_SUPPORT)) {
                onSsoButtonClicked()
            } else {
                AppManager.updateNetboxLauncher(context)
            }
        } else {
            throw IllegalStateException("This is not a netbox device or netstore is not installed!")
        }
    }
}