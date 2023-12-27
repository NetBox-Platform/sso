package ir.net_box.sso

/**
 * for each status code:
 * 1 -> "ok" : Represents a successful response code.
 * 2 -> "invalid package name" : Indicates an invalid package name.
 * 3 -> "invalid key" : Indicates an invalid public key.
 * 4 -> "not access to the kid profile" : Indicates no access to the kid profile.
 * 5 -> "not access due to not having a mobile number" : Indicates no access due to missing mobile number.
 * 6 -> "rejected" : Indicates a rejection(cancellation).
 * 7 -> "not access" : Indicates general access denial.
 * 8 -> "back pressed" : Indicates user pressed back.
 **/
enum class SSOConfirmationStatus(val status: Int, val message: String) {
    OK(1, "ok"),
    PACKAGE_NAME_NOT_FOUND(2, "invalid package name"),
    PUBLIC_KEY_INVALID(3, "invalid key"),
    KID_PROFILE_NOT_ACCESS(4, "not access to the kid profile"),
    REGULAR_PROFILE_WITH_OUT_PHONE_NUMBER(5, "not access due to not having a mobile number"),
    REJECT(6, "rejected"),
    NOT_ACCESS(7, "not access"),
    BACK_PRESSED(8, "back pressed");
}

fun findSSOConfirmationStatusByCode(code: Int): SSOConfirmationStatus? {
    return SSOConfirmationStatus.values().find { it.status == code }
}