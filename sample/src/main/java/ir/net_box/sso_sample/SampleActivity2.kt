package ir.net_box.sso_sample

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.core.NetboxClient
import ir.net_box.sso.core.authintication.Authentication
import ir.net_box.sso.widget.LoginButton

class SampleActivity2 : AppCompatActivity() {

    private lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_layout)

        loginButton = findViewById(R.id.sso_login_button)

        val intent = Intent()
        val launcherPackageName = LAUNCHER_PACKAGE_NAME
        val activityName = "$launcherPackageName.ui.activities.SsoActivity"
        val currentAppPackageName = this.packageName

        intent.apply {
            // The app package should be sent along with an intent for identity verification
            putExtra(PACKAGE_NAME_ARG_KEY, currentAppPackageName)
            component = ComponentName.unflattenFromString("$launcherPackageName/$activityName")
        }

        /**
         * You can use this code to check whether the launcher is installed
         * and then display the login button if it's not installed
         **/
        if (Authentication.isLauncherInstalledOnDevice(this)) {
            loginButton.apply {
                isVisible = true
                setOnClickListener {
                    NetboxClient.startLauncherSignIn(this@SampleActivity2) {
                        // If you are using 'startActivityForResult' make use of the following code
                        try {
                            startActivityForResult(intent, REQ_CODE)
                        } catch (e: ActivityNotFoundException) {
                            Log.e("activityNotFound", "onCreate: ${e.message}")
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
        if (requestCode == REQ_CODE) {
            data?.let { intent ->
                intent.apply {
                    getStringExtra(PHONE_NUMBER_ARG_KEY)?.let {
                        Toast.makeText(
                            this@SampleActivity2, it, Toast.LENGTH_SHORT
                        ).show()
                    }
                    getStringExtra(STATUS_CODE_MESSAGE_ARG_KEY)?.let {
                        Toast.makeText(
                            this@SampleActivity2, it, Toast.LENGTH_SHORT
                        ).show()
                    }
                    /**
                     * for each status code:
                     * 1 -> "ok" : Represents a successful response code.
                     * 2 -> "invalid package name" : Indicates an invalid package name.
                     * 3 -> "invalid key", // Indicates an invalid public key.
                     * 4 -> "not access to the kid profile", // Indicates no access to the kid profile.
                     * 5 -> "not access due to not having a mobile number", // Indicates no access due to missing mobile number.
                     * 6 -> "rejected", // Indicates a rejection(cancellation).
                     * 7 -> "not access", // Indicates general access denial.
                     **/
                    getIntExtra(STATUS_CODE_ARG_KEY, -1).let {
                        Toast.makeText(
                            this@SampleActivity2, "$it", Toast.LENGTH_SHORT
                        ).show()
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