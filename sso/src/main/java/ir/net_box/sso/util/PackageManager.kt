package ir.net_box.sso.util

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import ir.net_box.sso.NET_STORE_PACKAGE_NAME

internal fun getPackageInfo(context: Context, packageName: String, flags: Int = 0) = try {
    context.packageManager.getPackageInfo(packageName, flags)
} catch (e: Exception) {
    e.printStackTrace()
    null
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