package ir.net_box.sso.security

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import ir.net_box.sso.BuildConfig
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import ir.net_box.sso.NET_STORE_PACKAGE_NAME
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.PublicKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*

object Security {
    fun verifyLauncherIsInstalled(context: Context): Boolean {
        val packageManager: PackageManager = context.packageManager
        val packageName = LAUNCHER_PACKAGE_NAME

        val signatures: Array<Signature> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            packageInfo.signingInfo.apkContentsSigners
        } else {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            packageInfo.signatures
        }

        for (sig in signatures) {
            val input: InputStream = ByteArrayInputStream(sig.toByteArray())
            val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X509")
            val certificate: X509Certificate =
                certificateFactory.generateCertificate(input) as X509Certificate
            val publicKey: PublicKey = certificate.publicKey
            val certificateHex = byte2HexFormatted(publicKey.encoded)
            if (BuildConfig.NETBOX_CERTIFICATE != certificateHex) {
                return false
            }
        }
        return true
    }

    fun verifyNetstoreIsInstalled(context: Context): Boolean {
        val packageManager: PackageManager = context.packageManager
        val packageName = NET_STORE_PACKAGE_NAME

        val signatures: Array<Signature> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            packageInfo.signingInfo.apkContentsSigners
        } else {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            packageInfo.signatures
        }

        for (sig in signatures) {
            val input: InputStream = ByteArrayInputStream(sig.toByteArray())
            val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X509")
            val certificate: X509Certificate =
                certificateFactory.generateCertificate(input) as X509Certificate
            val publicKey: PublicKey = certificate.publicKey
            val certificateHex = byte2HexFormatted(publicKey.encoded)
            if (BuildConfig.NETBOX_CERTIFICATE != certificateHex) {
                return false
            }
        }
        return true
    }

    private fun byte2HexFormatted(array: ByteArray): String {
        val stringBuilder = StringBuilder(array.size * 2)
        for (index in array.indices) {
            var suggestedHex = Integer.toHexString(array[index].toInt())
            val length = suggestedHex.length
            if (length == 1) {
                suggestedHex = "0$suggestedHex"
            } else if (length > 2) {
                suggestedHex = suggestedHex.substring(length - 2, length)
            }
            stringBuilder.append(suggestedHex.uppercase(Locale.getDefault()))
            if (index < array.size - 1) {
                stringBuilder.append(':')
            }
        }
        return stringBuilder.toString()
    }
}