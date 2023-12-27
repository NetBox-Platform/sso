package ir.net_box.sso.core

import android.content.Context

interface Client {
    /**
     * This function handles the final SSO codes after checking for launcher installation
     * and ensuring the required version is installed
     */
    fun startLauncherSignIn(context: Context, onSsoButtonClicked: () -> Unit)

    fun getSignInIntent(context: Context): Intent
}