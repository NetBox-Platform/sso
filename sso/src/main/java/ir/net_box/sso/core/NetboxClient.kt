package ir.net_box.sso.core

import android.content.Context
import android.widget.Toast
import ir.net_box.sso.core.authintication.Authentication

object NetboxClient : Client {

    override fun startLauncherSignIn(context: Context, onSsoButtonClicked: () -> Unit) {

        if (Authentication.isLauncherInstalledOnDevice(context)) {
            if (!Authentication.isNeededToUpdateLauncher(context)) {
                onSsoButtonClicked()
            } else {
                Authentication.updateLauncher(context)
            }
        } else {
            Toast.makeText(context, "This is not a netbox device", Toast.LENGTH_SHORT).show()
        }
    }
}