---
id: introduction
title: 1. معرفی
sidebar_position: 1
custom_edit_url: null
---
# Netbox SSO (Single Sign-on)

## Introduction

The Single Sign-On (SSO) SDK is a software development kit designed by the Netbox team for third-party applications. It enables these apps to implement a seamless authentication experience for their users using 'netbox'. With this SDK, developers can integrate a secure and efficient login process, allowing users to access multiple applications without the need to repeatedly log in.

This enhances user convenience and streamlines the authentication process across diverse third-party applications through the use of 'netbox' as the authentication provider. In essence, our SDK allows users to either create a fully customized login button or utilize our pre-defined `LoginButton` with additional customization options, such as changing the text font and size, providing flexibility in adapting the authentication experience to their application's unique design.

> **NOTE:** If you want to use the Netbox SSO in your apps you should be on our whitelist! Please contact us to start our cooperation.

## How the system works

First, you need to add our SSO dependency to your Android project. (Documentation and examples are available in the project's GitHub repo).
**https://github.com/NetBox-Platform/sso**

---

### 1. Initiate Login

After the user presses the “Login with Netbox” button in your app, our `SsoActivity` will be started.

### 2. App Verification

In the activity, we verify your app. To do this, we will call an `sso-confirmation` API, sending your app's package name, APK signing public key, and our user info to the Netbox server.

> **Important note:** This is our server-side call. **YOU DON’T NEED TO CALL ANY NETBOX APIs YOURSELF.** The sample responses are shown here for clarification on what happens in the background.

#### Request
*   **Method:** `POST`
*   **URL:** `api.netbox/**/sso-confirmation/`
*   **Headers:**
    *   `Content-Type: application/json`
    *   `session-key: <netbox-user-session-key>`
*   **Payload:**
    *   `packageName: <your-apk-package-name>`
    *   `publicKey: <your apk public key SHA-256 digest>`

#### Sample Successful Response
```json
{
"username": "9123456789",
"status": 1,
"name": "نام اپ شما",
"nameEn": "Your app name",
"signature": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InNzbyJ9.eyJ1c2VybmFtZSI6IjkwMzUwMDQzNDIiLCJwYWNrYWdlTmFtZSI6ImlyLmZpbG1uZXQuYW5kcm9pZC50diIsImlhdCI6MTcyODQ3NDc0NiwiZXhwIjoxNzI4NDc1OTQ2fQ.EY3d77PLEDlN2yR2gV1oLOfG_auVo8LnCYRlFnbp6lCX0eQlBIU-TGT43_vz2EcXQHhV2kVpfM-5UVJryqpDCA-sDZRvtuech1Bk4dlXj92WNndyBhEFTfh9F0-lq-u9WUpVYmJpc99OILmexOPABm2Nn4he0S4KqVxRh7b_6gGB3Ix_xppEXzyhYje2BzYlHB7hBR-1ow-Ke5-ekaTeoEiW9ldKIMYt5kofgoPQQ8xsX9o1d2ygknAaolvqgI2--mv72qTetXifPQPiVXhD9JWB3niBS4s5MB-UQ5m4fuSJAjVjcBotNUBidyLmmlkm9WJ6BZwojLm1krTdsu6vgg"
}

```

#### Sample Failed Response
```json
{
"username": "9123456789",
"status": 2,
"signature": "..."
}
```

### 3. Receive the Result

We will pass the results back to your application as an `Intent`. You can receive this using `registerForActivityResult` (recommended) or the deprecated `startActivityForResult`.

#### Get user phone number
```kotlin
resultIntent.getStringExtra(NetboxClient.PHONE_NUMBER_ARG_KEY)?.let {
    Log.d("NetboxClient", "Phone number: $it")
}
```
#### Get status message
```kotlin
resultIntent.getStringExtra(NetboxClient.STATUS_CODE_MESSAGE_ARG_KEY)?.let {
    Log.d("NetboxClient", "Status message: $it")
}
```
#### Get status code
```kotlin
resultIntent.getIntExtra(NetboxClient.STATUS_CODE_ARG_KEY, -1).let {
    Log.d("NetboxClient", "Status code: $it")

    when (findSSOConfirmationStatusByCode(it)) {
        SSOConfirmationStatus.OK -> TODO("Handle successful login")
        SSOConfirmationStatus.PACKAGE_NAME_NOT_FOUND -> TODO()
        SSOConfirmationStatus.PUBLIC_KEY_INVALID -> TODO()
        SSOConfirmationStatus.KID_PROFILE_NOT_ACCESS -> TODO()
        SSOConfirmationStatus.REGULAR_PROFILE_WITH_OUT_PHONE_NUMBER -> TODO()
        SSOConfirmationStatus.REJECT -> TODO("User rejected the request")
        SSOConfirmationStatus.NOT_ACCESS -> TODO()
        SSOConfirmationStatus.BACK_PRESSED -> TODO("User pressed back")
        else -> TODO("Handle other cases")
    }
}
```
#### Get the signature
The `signature` is a JSON Web Token (JWT) that contains the `username(phone number)`, `packagename`, `issued_at` time, and `expiration` time.
```kotlin
resultIntent.getStringExtra(NetboxClient.SIGNATURE_ARG_KEY)?.let {
    Log.d("NetboxClient", "signature: $it")
}
```
### 4. Verify the Token

Now you can verify the signature token you received using our public key.

#### Request
*   **Method:** `GET`
*   **URL:** `https://netstore.net-box.ir/.well-known/jwks.json`

#### Response
This endpoint provides the JSON Web Key Set (JWKS) needed to verify the JWT signature.
```json
{
"keys": [
    {
    "kty": "RSA",
    "n": "q3CutOtrlW4_R60dawKgBmNNz0ObFJlvF3Rit-iJbd_MTDXG-ZLWGEx0enpDewc8UYNOL5XJefoOD5IovCD7Uib7YZDKfRXrtz2Wfq3xKmmWkAIjb7Lkv0Gx-oBe7FQN2Fv27-jZHuAu2b6zUTndpfpfyDdD7GvdVKG9gLS_6RW0g_lxI8SDlm70vN5mxsDyDz-CStVmC6xoLX3Ji--9HhVdMGNQos3haRrUnuarUyTTpOG_o69ieBunYvceJOAeep5_FPzDrRBk3K1jp2QQQYzrR5MsAkK_F8-WowloOoFQEOxX_yJvP7FNvS5eajiOtPMGR98MjuRBcgjrHZFJhQ",
    "e": "AQAB",
    "kid": "sso"
    }
  ]
}
```
### 5. Create User Session

After your backend successfully verifies the token, you can create a user session in your system with the provided phone number.

