package com.example.illergi_opencv_java_test.utills

enum class ErrorText(val text: String) {
    INVALID_EMAIL("Please enter a valid email address"),
    EMPTY_EMAIL("Please enter an email address"),
    EMPTY_PASS("Please enter a password"),
    INVALID_FIRSTNAME("Please enter a valid firstname"),
    EMPTY_FIRSTNAME("Please enter a firstname"),
    INVALID_LASTNAME("Please enter a valid lastname"),
    EMPTY_LASTNAME("Please enter a lastname"),
    EMPTY_COMFIRMPASS("Please enter comfirm password"),
    PASSWORD_NOT_MATCH("password not match"),

    EMPTY_EMAIL_PASSWORD("Please enter a valid email address and password")
}
