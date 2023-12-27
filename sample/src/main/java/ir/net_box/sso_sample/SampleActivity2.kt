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

class SampleActivity2 : AppCompatActivity() {

    // Using Netbox LoginButton is optional, you can use any other button instead
    private lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_layout)

        loginButton = findViewById(R.id.sso_login_button)

        val netboxSigningIntent = NetboxClient.getSignInIntent(applicationContext)

        if (AppManager.isNetboxLauncherInstalled(this)) {
            loginButton.apply {
                isVisible = true
                setOnClickListener {
                    NetboxClient.startLauncherSignIn(this@SampleActivity2) {
                        // If you are using 'startActivityForResult' make use of the following code
                        try {
                            startActivityForResult(netboxSigningIntent, NetboxClient.NETBOX_SSO_REQ_CODE)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /**
         * After the sent package is verified and confirmed by the user,
         * the user's data is received and accessible through the Activity Result
         **/
        if (requestCode == NetboxClient.NETBOX_SSO_REQ_CODE) {
            data?.let { resultIntent ->

                // Get user phone number
                resultIntent.getStringExtra(NetboxClient.PHONE_NUMBER_ARG_KEY)?.let {
                    Log.d("NetboxClient", "Phone number: $it")
                }

                // Get status message
                resultIntent.getStringExtra(NetboxClient.STATUS_CODE_MESSAGE_ARG_KEY)?.let {
                    Log.d("NetboxClient", "Status message: $it")
                }

                // Get result status
                resultIntent.getIntExtra(NetboxClient.STATUS_CODE_ARG_KEY, -1).let {
                    Log.d("NetboxClient", "Status code: $it")

                    when (findSSOConfirmationStatusByCode(it)) {
                        SSOConfirmationStatus.OK -> TODO()
                        SSOConfirmationStatus.PACKAGE_NAME_NOT_FOUND -> TODO()
                        SSOConfirmationStatus.PUBLIC_KEY_INVALID -> TODO()
                        SSOConfirmationStatus.KID_PROFILE_NOT_ACCESS -> TODO()
                        SSOConfirmationStatus.REGULAR_PROFILE_WITH_OUT_PHONE_NUMBER -> TODO()
                        SSOConfirmationStatus.REJECT -> TODO()
                        SSOConfirmationStatus.NOT_ACCESS -> TODO()
                        SSOConfirmationStatus.BACK_PRESSED -> TODO()
                        else -> TODO()
                    }
                }
            }
        }
    }
}