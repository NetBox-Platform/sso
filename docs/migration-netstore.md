# Netbox SSO Migration Guide: Version 1.x.x to 2.0.0 (Launcher to Netstore)

In version 2.0.0, the SSO authentication flow has been migrated from the **Netbox Launcher** to **Netstore**. To ensure your application continues to work correctly, please update your integration by following the steps below.

## 1. Update Dependency
Update the Netbox SSO library version in your `build.gradle` file:

```gradle
dependencies {
    implementation("com.github.netbox-ir:sso-android-sdk:2.0.0")
}
```

## 2. Update Installation Checks
The SDK now checks for the presence of Netstore instead of the Netbox Launcher.

**Old (1.x.x):**
```kotlin
import ir.net_box.sso.core.AppManager

if (AppManager.isNetboxLauncherInstalled(this)) { ... }
```

**New (2.0.0):**
```kotlin
import ir.net_box.sso.core.AppManager

if (AppManager.isNetstoreInstalled(this)) { ... }
```

## 3. Update Sign-In Preparation Method
The method used to prepare the environment for sign-in has been renamed to reflect the shift to Netstore. The new method, `ensureNetstoreReadyForSignIn`, also handles prompting the user to update Netstore if it is outdated.

**Old (1.x.x):**
```kotlin
import ir.net_box.sso.core.NetboxClient

val netboxSignInIntent = NetboxClient.getSignInIntent(applicationContext)

NetboxClient.startLauncherSignIn(this@SampleActivity1) {
    try {
        resultLauncher.launch(netboxSignInIntent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}
--- OR ---
NetboxClient.startLauncherSignIn(this@SampleActivity2) {
    try {
        startActivityForResult(netboxSignInIntent, NetboxClient.NETBOX_SSO_REQ_CODE)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}
```

**New (2.0.0):**
```kotlin
import ir.net_box.sso.core.NetboxClient

val netboxSignInIntent = NetboxClient.getSignInIntent(applicationContext)

NetboxClient.ensureNetstoreReadyForSignIn(this@NetboxLoginSampleActivity) {
    try {
        resultLauncher.launch(netboxSignInIntent) 
    } catch (e: ActivityNotFoundException) {
        Log.e(TAG, "Netstore not found", e)
    }
}
--- OR ---
NetboxClient.ensureNetstoreReadyForSignIn(this@NetboxLoginSampleActivity2) {
    try {
        startActivityForResult(netboxSigningIntent, NetboxClient.NETBOX_SSO_REQ_CODE) 
    } catch (e: ActivityNotFoundException) {
        Log.e(TAG, "Netstore not found", e)
    }
}
```

## 4. Restructure Activity Result Handling (Recommended)
In version 2.0.0, it is highly recommended to verify the `statusCode` using `SSOConfirmationStatus` *before* attempting to parse the user's data (like the phone number and signature). User data will only be available if the status is `SSOConfirmationStatus.OK`.

**Updated Flow Example:**
```kotlin
import ir.net_box.sso.SSOConfirmationStatus
import ir.net_box.sso.findSSOConfirmationStatusByCode
import ir.net_box.sso.core.NetboxClient

// Inside onActivityResult or ActivityResultCallback
data?.let { resultIntent ->
    val statusCode = resultIntent.getIntExtra(NetboxClient.STATUS_CODE_ARG_KEY, -1)
    
    when (findSSOConfirmationStatusByCode(statusCode)) {
        SSOConfirmationStatus.OK -> { 
            // Successful login
            val phoneNumber = resultIntent.getStringExtra(NetboxClient.PHONE_NUMBER_ARG_KEY)
            val signature = resultIntent.getStringExtra(NetboxClient.SIGNATURE_ARG_KEY)
            val message = resultIntent.getStringExtra(NetboxClient.STATUS_CODE_MESSAGE_ARG_KEY)
            // Proceed with authenticated session
        }
        SSOConfirmationStatus.REJECT -> {
            // User canceled the login
        }
        SSOConfirmationStatus.REGULAR_PROFILE_WITH_OUT_PHONE_NUMBER -> {
            // Handle specific error case (e.g., guest profile)
        }
        else -> {
            // Handle other status codes
        }
    }
}
```

---

## Summary of Key Changes

| Feature | Version 1.x.x | Version 2.0.0 |
| :--- | :--- | :--- |
| **Target App** | Netbox Launcher | Netstore |
| **Check Installation** | `isNetboxLauncherInstalled` | `isNetstoreInstalled` |
| **Preparation Method** | `startLauncherSignIn` | `ensureNetstoreReadyForSignIn` |
| **Status Verification** | Manual Int check | `findSSOConfirmationStatusByCode` |
