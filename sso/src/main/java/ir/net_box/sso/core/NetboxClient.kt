package ir.net_box.sso.core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.MINIMUM_LAUNCHER_VERSION_SUPPORT

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

    override fun getSignInIntent(context: Context): Intent {
        return Intent().apply {
            // The app package should be sent along with an intent for identity verification
            putExtra(PACKAGE_NAME_ARG_KEY, context.packageName)
            component =
                ComponentName.unflattenFromString(
                    "$LAUNCHER_PACKAGE_NAME/$LAUNCHER_PACKAGE_NAME.ui.activities.SsoActivity"
                )
        }
    }

    private const val PACKAGE_NAME_ARG_KEY = "package_name_key"

    // Key to retrieve the user's mobile number
    const val PHONE_NUMBER_ARG_KEY = "phone_number_key"

    // Key to retrieve the status code
    const val STATUS_CODE_ARG_KEY = "status_arg_key"

    // Key to retrieve the status code message
    const val STATUS_CODE_MESSAGE_ARG_KEY = "status_message_arg_key"

    const val NETBOX_SSO_REQ_CODE = 123
}