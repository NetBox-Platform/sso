package ir.net_box.sso.core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import ir.net_box.sso.NET_STORE_PACKAGE_NAME

object NetboxClient : Client {

    override fun ensureNetstoreReadyForSignIn(context: Context, onSignInReady: () -> Unit) {
        if (AppManager.isNetstoreInstalled(context)) {
            if (!AppManager.shouldUpdateNetstore(context)) {
                onSignInReady()
            } else {
                AppManager.updateNetstore(context)
            }
        } else {
            throw IllegalStateException("Netstore is not installed!")
        }
    }

    override fun getSignInIntent(context: Context): Intent {
        return Intent().apply {
            putExtra(PACKAGE_NAME_ARG_KEY, context.packageName)
            component =
                ComponentName.unflattenFromString(
                    "$NET_STORE_PACKAGE_NAME/" +
                            "$NET_STORE_PACKAGE_NAME.sso.presentation.view.SsoActivity"
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

    // Key to retrieve the signature
    const val SIGNATURE_ARG_KEY = "signature_key"

    const val NETBOX_SSO_REQ_CODE = 123
}