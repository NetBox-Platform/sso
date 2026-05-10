package ir.net_box.sso.core

import android.content.Context
import android.content.Intent

interface Client {
    /**
     * Ensures Netstore is installed and up to date before starting the SSO sign-in flow.
     *
     * - If Netstore is not installed, throws IllegalStateException.
     * - If Netstore must be updated, starts the update flow.
     * - If everything is OK, invokes [onSignInReady].
     */
    fun ensureNetstoreReadyForSignIn(context: Context, onSignInReady: () -> Unit)

    fun getSignInIntent(context: Context): Intent
}