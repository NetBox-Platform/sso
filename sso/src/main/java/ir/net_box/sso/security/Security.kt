package ir.net_box.sso.security

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import ir.net_box.sso.LAUNCHER_PACKAGE_NAME
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.PublicKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*

object Security {

    private val launcherCertificateHex: String =
        ("30:82:01:20:30:0d:06:09:2a:86:48:86:f7:0d:01:01:01:05:00:03:82:01:0d:00:30:82:01:08:02" +
                ":82:01:01:00:b6:8b:df:e5:89:f7:2b:5e:ce:99:ab:0f:0c:42:95:64:3b:53:4b:55:71:21" +
                ":40:79:b0:a2:c3:ff:9e:ca:39:a5:e3:b1:a6:34:1c:c7:0b:05:06:86:0b:70:58:09:be:6a" +
                ":9b:8b:73:c5:84:8f:c7:ca:ec:d0:b1:2b:63:6e:03:71:d1:df:ea:91:b8:e1:7b:73:f4:a3" +
                ":20:28:50:1f:d3:dc:9e:9e:fc:2e:29:30:b1:69:de:c3:65:80:44:90:fd:ad:2b:c6:df:7a" +
                ":0b:3e:15:62:f6:1f:65:1a:88:29:77:8c:7a:c1:dc:68:36:72:c1:ed:7d:04:3a:f3:b1:3f" +
                ":3b:f9:56:16:1a:6b:22:42:22:30:96:ca:fe:08:7b:41:a4:2b:b7:9f:8c:62:be:1b:11:fc" +
                ":6e:be:e8:d6:1b:17:da:a2:24:01:14:52:40:db:da:10:32:cf:24:04:8f:29:51:19:e0:f7" +
                ":32:d2:d6:b5:b1:43:db:01:d7:ef:ab:86:fe:7f:68:7f:58:6a:0f:04:72:9d:2a:21:37:ee" +
                ":01:5e:da:90:b4:85:ff:65:af:0f:0e:fd:cf:a7:eb:31:9a:54:0f:8e:d6:2e:f2:10:b2:6e" +
                ":cb:1e:39:84:ae:9b:27:c6:f3:48:c3:7c:2b:c6:f8:7e:82:59:29:1b:74:e1:e4:17:c0:25" +
                ":02:01:03").uppercase()

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
            if (launcherCertificateHex != certificateHex) {
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
            stringBuilder.append(suggestedHex.toUpperCase(Locale.getDefault()))
            if (index < array.size - 1) {
                stringBuilder.append(':')
            }
        }
        return stringBuilder.toString()
    }
}