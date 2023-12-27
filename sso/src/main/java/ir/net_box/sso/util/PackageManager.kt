package ir.net_box.sso.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.core.content.pm.PackageInfoCompat
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.NET_STORE_PACKAGE_NAME
import java.util.Locale

internal fun getPackageInfo(context: Context, packageName: String, flags: Int = 0) = try {
    context.packageManager.getPackageInfo(packageName, flags)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

internal fun getAppName(context: Context) =
    getPackageInfo(context, context.packageName)?.appName(
        context,
        Locale("fa")
    )

private fun PackageInfo.appName(context: Context, locale: Locale): String? = try {
    val applicationInfo: ApplicationInfo = context.packageManager.getApplicationInfo(
        packageName,
        PackageManager.GET_META_DATA
    )
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

fun getLauncherVersion(context: Context): Int {
    val longVersionCode =
        getPackageInfo(context, LAUNCHER_PACKAGE_NAME)?.let {
            PackageInfoCompat.getLongVersionCode(
                it
            )
        }
    return longVersionCode?.toInt() ?: -1
}

fun getNetstoreVersion(context: Context): Int {
    val longVersionCode =
        getPackageInfo(context, NET_STORE_PACKAGE_NAME)?.let {
            PackageInfoCompat.getLongVersionCode(
                it
            )
        }
    return longVersionCode?.toInt() ?: -1
}