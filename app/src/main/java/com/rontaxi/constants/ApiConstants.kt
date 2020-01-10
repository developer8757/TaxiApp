package com.dizzipay.railsbank.data.remote

object ApiConstants {

     const val BASE_URL = "https://rontaxiapp.yapits.com/"
    //const val BASE_URL = "http://192.168.2.194:3007/"

    //new base url
    // const val BASE_URL ="http://192.168.2.194:3007/"

    const val CONNECT_TIMEOUT: Long = 10000
    const val READ_TIMEOUT: Long = 30000
    const val WRITE_TIMEOUT: Long = 30000


    const val GET_CAR_TYPE = "/api/v1/user/getcartypes"

    const val VALIDATE_DOCS = "/api/v1/user/validatedocs"

    const val VALIDATE_PHONE = "/api/v1/user/validatephone"

    const val REGISTER = "/api/v1/user/register"

    const val LOGIN = "/api/v1/user/login"

    const val FORGOT_PASSWORD = "/api/v1/user/forgetpassword"

    const val OTP_VERIFICATIONS = "/api/v1/user/otpverification"

    const val RESET_PASSWORD = "/api/v1/user/resetpassword"

    const val CHANGE_PASSWORD = "/api/v1/user/password/change"

    const val RESEND_OTP = "/api/v1/user/resendotp"

    const val UPDATE_PROFILE = "/api/v1/user/updateprofile"

    const val GET_CANCEL_REASONS = "/api/v1/user/getreasons"

    const val RATING = "/api/v1/user/rating"

    const val CONTACT_US = "/api/v1/user/contactus"

    const val DRIVER_RATINGS = "/api/v1/user/rating/get/all"

    const val REPORT_ISSUE = "/api/v1/user/reportissue"
    const val RAISE_ISSUE = "/api/v1/user/report/issue"

    const val RIDE_HISTORY = "api/v1/user/ridehistory"

    const val LOGOUT = "api/v1/user/logout"

    const val TODAY_EARNING = "/api/v1/user/earnings/get/all"

    const val Weekly_EARNING = "/api/v1/user/earnings/get/all"

    const val ADD_CARD = "/api/v1/stripe/card/add"

    const val GET_CARD_LIST = "/api/v1/stripe/account/get"

    const val DEFAULT_PAYMENT = "api/v1/stripe/change/default/source"

    const val DELETE_CARD = "/api/v1/stripe/delete/account"

    const val CHANGE_PHONE = "/api/v1/user/phone/change"

    const val ADD_BANK = "/api/v1/stripe/customer/bankaccount/add"

    const val STATIC_MAP = "https://maps.googleapis.com/maps/api/staticmap"

    const val BLOCK_DRIVER = "/api/v1/booking/blockDriver"

    const val GET_BOOKING_DATA = "/api/v1/user/get/booking/data"

    const val ADD_TIP="/api/v1/stripe/makepayment"

    const val SCHEDULE_BOOKING = "/api/v1/booking/scheduled"

}