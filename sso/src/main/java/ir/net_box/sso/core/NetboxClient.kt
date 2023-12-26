package ir.net_box.sso.core

import android.content.Context
import ir.net_box.sso.MINIMUM_LAUNCHER_VERSION_SUPPORT
import ir.net_box.sso.core.authintication.Authentication
import java.lang.IllegalStateException

object NetboxClient : Client {

    override fun startLauncherSignIn(context: Context, onSsoButtonClicked: () -> Unit) {

        if (Authentication.isLauncherInstalled(context)) {
            if (!Authentication.shouldUpdateLauncher(context, MINIMUM_LAUNCHER_VERSION_SUPPORT)) {
                onSsoButtonClicked()
            } else {
                Authentication.updateLauncher(context)
            }
        } else {
            throw IllegalStateException("This is not a netbox device or netstore is not installed!")
        }
    }
}