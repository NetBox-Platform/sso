package ir.net_box.sso.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.NET_STORE_PACKAGE_NAME
import java.util.*

val PackageInfo.versionCodeSDKAware: String
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            versionName
        } else {
            @Suppress("DEPRECATION")
            versionCode.toString()
        }
    }

internal fun getPackageInfo(context: Context, packageName: String, flags: Int = 0) = try {
    val packageManager = context.packageManager
//    if (Build.VERSION.SDK_INT >= 33) {
//        packageManager.getPackageInfo(
//            packageName, PackageManager.PackageInfoFlags.of(flags.toLong())
//        )
//    } else {
        @Suppress("DEPRECATION") packageManager.getPackageInfo(packageName, flags)
//    }
} catch (e: Exception) {
    Log.d("getPackageInfoError", "getPackageInfo: ${e.message}")
    null
}

internal fun getAppName(context: Context) =
    getPackageInfo(context, context.packageName)?.appName(
        context,
        Locale("fa")
    )

internal fun PackageInfo.appName(context: Context, locale: Locale): String? = try {
    val applicationInfo: ApplicationInfo

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        applicationInfo = context.packageManager.getApplicationInfo(
//            packageName,
//            PackageManager.ApplicationInfoFlags.of(0)
//        )
//    } else {
        @Suppress("DEPRECATION")
        applicationInfo = context.packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        )
//    }
    val configuration = Configuration()
    configuration.setLocale(locale)

    val callingAppContext = context.createPackageContext(
        packageName,
        Context.CONTEXT_IGNORE_SECURITY
    )

    val updatedContext = callingAppContext.createConfigurationContext(configuration)

    if (applicationInfo.labelRes != 0) {
        updatedContext.resources.getString(applicationInfo.labelRes)
    } else {
        applicationInfo.loadLabel(context.packageManager).toString()
    }
} catch (e: Exception) {
    applicationInfo.loadLabel(context.packageManager).toString()
}

fun getLauncherVersion(context: Context): String {
    return getPackageInfo(context, LAUNCHER_PACKAGE_NAME)?.versionCodeSDKAware ?: "-1"
}

/**
 * This function takes the launcher package and fetches its latest version
 * from the store using a deep link
 */
fun updateLauncherToLatestVersion(context: Context) {
    val browserIntent: Intent?
    try {
        browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.store.net_box.ir/store=$LAUNCHER_PACKAGE_NAME")
        )
        browserIntent.setPackage(NET_STORE_PACKAGE_NAME)
        context.startActivity(browserIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "${e.message}",
            Toast.LENGTH_LONG
        ).show()
    }
}