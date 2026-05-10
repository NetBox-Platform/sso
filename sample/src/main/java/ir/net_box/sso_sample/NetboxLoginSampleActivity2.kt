package ir.net_box.sso_sample

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ir.net_box.sso.SSOConfirmationStatus
import ir.net_box.sso.core.AppManager
import ir.net_box.sso.core.NetboxClient
import ir.net_box.sso.findSSOConfirmationStatusByCode
import ir.net_box.sso.widget.LoginButton

/**
 * Sample activity demonstrating how to integrate Netbox SSO login.
 */
class NetboxLoginSampleActivity2 : AppCompatActivity() {

    // Using Netbox LoginButton is optional, you can use any other button instead
    private lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_layout)

        loginButton = findViewById(R.id.sso_login_button)

        val netboxSigningIntent = NetboxClient.getSignInIntent(applicationContext)

        // Check if Netstore is installed before showing the login button (Optional)
        if (AppManager.isNetstoreInstalled(this)) {
            loginButton.apply {
                isVisible = true
                setOnClickListener {
                    // Prepare Netbox SSO and start sign-in via Activity Result API
                    NetboxClient.ensureNetstoreReadyForSignIn(this@NetboxLoginSampleActivity2) {
                        try {
                            startActivityForResult(netboxSigningIntent, NetboxClient.NETBOX_SSO_REQ_CODE)
                        } catch (e: ActivityNotFoundException) {
                            Log.e(TAG, "Netstore not found", e)
                        }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NetboxClient.NETBOX_SSO_REQ_CODE) {
            data?.let { resultIntent ->
                // Get result status
                val statusCode = resultIntent.getIntExtra(NetboxClient.STATUS_CODE_ARG_KEY, -1)
                Log.d(TAG, "Status code: $statusCode")

                when (findSSOConfirmationStatusByCode(statusCode)) {
                    SSOConfirmationStatus.OK -> {   // Successful login
                        // Get user phone number
                        resultIntent.getStringExtra(NetboxClient.PHONE_NUMBER_ARG_KEY)?.let {
                            Log.d(TAG, "Phone number: $it")
                        }

                        // Get status message
                        resultIntent.getStringExtra(NetboxClient.STATUS_CODE_MESSAGE_ARG_KEY)?.let {
                            Log.d(TAG, "Status message: $it")
                        }

                        // Get signature
                        resultIntent.getStringExtra(NetboxClient.SIGNATURE_ARG_KEY)?.let {
                            Log.d(TAG, "signature: $it")
                        }
                    }

                    SSOConfirmationStatus.PACKAGE_NAME_NOT_FOUND -> {
                        Log.w(TAG, "Package name is invalid or not found.")
                    }

                    SSOConfirmationStatus.PUBLIC_KEY_INVALID -> {
                        Log.w(TAG, "Public key invalid.")
                    }

                    SSOConfirmationStatus.KID_PROFILE_NOT_ACCESS -> {
                        Log.w(TAG, "Kid profile not accessible.")
                    }

                    SSOConfirmationStatus.REGULAR_PROFILE_WITH_OUT_PHONE_NUMBER -> {
                        Log.w(TAG, "User profile without phone number.")
                    }

                    SSOConfirmationStatus.REJECT -> {
                        Log.w(TAG, "SSO request rejected.")
                    }

                    SSOConfirmationStatus.NOT_ACCESS -> {
                        Log.w(TAG, "User does not have access.")
                    }

                    SSOConfirmationStatus.BACK_PRESSED -> {
                        Log.d(TAG, "User canceled SSO flow (back pressed).")
                    }

                    else -> {
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "NetboxClient"
    }
}