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
import java.util.Locale

object Security {
    fun verifyAppIsInstalled(context: Context, packageName: String): Boolean {
        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageInfo = context.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            packageInfo.signingInfo.apkContentsSigners
        } else {
            val packageInfo = context.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            packageInfo.signatures
        }

        for (signature in signatures) {
            val input = ByteArrayInputStream(signature.toByteArray())
            val certificateFactory = CertificateFactory.getInstance("X509")
            val certificate =
                certificateFactory.generateCertificate(input) as X509Certificate
            val certificateHex = byte2HexFormatted(certificate.publicKey.encoded)

            val validCertificates = if (packageName == NET_STORE_PACKAGE_NAME) {
                listOf(
                    BuildConfig.NETBOX_CERTIFICATE,
                    BuildConfig.NETBOX_PUBLIC_CERTIFICATE
                )
            } else {
                listOf(
                    BuildConfig.NETBOX_CERTIFICATE
                )
            }

            if (certificateHex !in validCertificates) {
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