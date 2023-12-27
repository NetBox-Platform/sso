package ir.net_box.sso_sample

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.SSOConfirmationStatus
import ir.net_box.sso.core.NetboxClient
import ir.net_box.sso.core.AppManager
import ir.net_box.sso.findSSOConfirmationStatusByCode
import ir.net_box.sso.widget.LoginButton

class SampleActivity1 : AppCompatActivity() {

    private lateinit var loginButton: LoginButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_layout)

        loginButton = findViewById(R.id.sso_login_button)

        val intent = Intent()
        val launcherPackageName = LAUNCHER_PACKAGE_NAME
        val activityName = "$launcherPackageName.ui.activities.SsoActivity"
        val currentAppPackageName = this.packageName
        val resultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { activityResult ->
                if (activityResult.resultCode == REQ_CODE) {
                    /**
                     * After the sent package is verified and confirmed by the user,
                     * the user's data is received and accessible through the Activity Result
                     **/
                    activityResult.data?.let { intent ->
                        intent.apply {
                            getStringExtra(PHONE_NUMBER_ARG_KEY)?.let {
                                Toast.makeText(
                                    this@SampleActivity1, it, Toast.LENGTH_SHORT
                                ).show()
                            }
                            getStringExtra(STATUS_CODE_MESSAGE_ARG_KEY)?.let {
                                Toast.makeText(
                                    this@SampleActivity1, it, Toast.LENGTH_SHORT
                                ).show()
                            }
                            getIntExtra(STATUS_CODE_ARG_KEY, -1).let {
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
        intent.apply {
            // The app package should be sent along with an intent for identity verification
            putExtra(PACKAGE_NAME_ARG_KEY, currentAppPackageName)
            component = ComponentName.unflattenFromString("$launcherPackageName/$activityName")
        }
        /**
         * You can use this code to check whether the launcher is installed
         * and then display the login button if it's not installed
         **/
        if (AppManager.isNetboxLauncherInstalled(this)) {
            loginButton.apply {
                isVisible = true
                setOnClickListener {
                    NetboxClient.startLauncherSignIn(this@SampleActivity1) {
                        // If you are using 'registerForActivityResult,' make use of the following code
                        try {
                            resultLauncher.launch(intent)
                        } catch (e: ActivityNotFoundException) {
                            Log.e("activityNotFound", "onCreate: ${e.message}")
                        }
                    }
                }
            }
        }
    }

    companion object {
        // Key to retrieve the user's mobile number
        private const val PHONE_NUMBER_ARG_KEY = "phone_number_key"

        // Key to retrieve the status code
        private const val STATUS_CODE_ARG_KEY = "status_arg_key"

        // Key to retrieve the status code message
        private const val STATUS_CODE_MESSAGE_ARG_KEY = "status_message_arg_key"
        private const val PACKAGE_NAME_ARG_KEY = "package_name_key"
        private const val REQ_CODE = 123
    }
}
