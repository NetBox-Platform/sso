# Netbox SSO Migration Guide: Version 1.x.x to 2.0.0 (Launcher to Netstore)

In version 2.0.0, the SSO authentication flow has been migrated from the Netbox Launcher to **Netstore**. To ensure your application continues to work correctly, please update your integration by following the steps below.

## 1. Update Installation Checks
Previously, the SDK checked if the Netbox Launcher was installed. You must update this to check for **Netstore**.

**Old (1.x.x):**
```kotlin
if (AppManager.isNetboxLauncherInstalled(this)) { ... }
```
**New (2.0.0):**
```kotlin
if (AppManager.isNetstoreInstalled(this)) { ... }
```
## 2. Update Sign-In Preparation Method
The method used to prepare the environment for sign-in has been renamed to reflect the shift to Netstore.

**Old (1.x.x):**
```kotlin
NetboxClient.startLauncherSignIn(this@SampleActivity1) {
    try {
        resultLauncher.launch(netboxSigningIntent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}
 --- OR ---
NetboxClient.startLauncherSignIn(this@SampleActivity2) {
     try {
         startActivityForResult(netboxSigningIntent, NetboxClient.NETBOX_SSO_REQ_CODE)
     } catch (e: ActivityNotFoundException) {
         e.printStackTrace()
     }
}
```
**New (2.0.0):**
```kotlin
NetboxClient.ensureNetstoreReadyForSignIn(this@NetboxLoginSampleActivity) {
    try {
        resultLauncher.launch(netboxSignInIntent) // Note: renamed variable for readability
    } catch (e: ActivityNotFoundException) {
        Log.e(TAG, "Netstore not found", e)
    }
}
--- OR ---
NetboxClient.ensureNetstoreReadyForSignIn(this@NetboxLoginSampleActivity2) {
    try {
        startActivityForResult(netboxSigningIntent, NetboxClient.NETBOX_SSO_REQ_CODE) // Note: renamed variable for readability
    } catch (e: ActivityNotFoundException) {
        Log.e(TAG, "Netstore not found", e)
    }
}
```
## 3. Restructure Activity Result Handling (Recommended)
As a best practice in 2.0.0, you should verify the `statusCode` *before* attempting to parse the user's data (like the phone number and signature). User data will only be available if the status is `SSOConfirmationStatus.OK`.

**Updated Flow Example:**
```kotlin
SSOConfirmationStatus.OK -> { // Successful login
    val phoneNumber = resultIntent.getStringExtra(NetboxClient.PHONE_NUMBER_ARG_KEY)
    val signature = resultIntent.getStringExtra(NetboxClient.SIGNATURE_ARG_KEY)
}
```