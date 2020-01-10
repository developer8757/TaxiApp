package com.rontaxi.retrofit

import com.dizzipay.railsbank.base.BaseResponseModel
import com.dizzipay.railsbank.data.remote.ApiConstants
import com.rontaxi.model.CancellationReasonsResponseModel
import com.rontaxi.model.RateRequestModel
import com.rontaxi.model.SubmitQueryRequest
import com.rontaxi.model.driverrating.DriverRatingResponseModel
import com.rontaxi.model.*
import com.rontaxi.model.drivingearning.DriverEarningModel
import com.rontaxi.model.login.LoginResponseModel
import com.rontaxi.model.otp.OtpResponse
import com.rontaxi.model.registration.RegistrationResponseModel
import com.rontaxi.model.paymentdriver.AddBankModel
import com.rontaxi.view.home.rider.payment.AddCardModel
import com.rontaxi.view.home.rider.payment.DefaultPaymentModel
import com.rontaxi.view.home.rider.payment.PaymentListDriverModel
import com.rontaxi.view.home.rider.payment.PaymentListModel
import com.rontaxi.view.home.rider.ridehistory.BlockDriverModel
import com.rontaxi.view.initial.driver.login.LoginRequestModel
import com.rontaxi.view.initial.driver.register.PhoneRequestModel
import com.rontaxi.view.initial.driver.vehicletype.CarTypeResponse
import com.rontaxi.view.initial.rider.forgotpassword.ForgotRequest
import com.rontaxi.view.initial.rider.forgotpassword.ForgotResponse
import com.rontaxi.view.initial.rider.register.RegisterRequestModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET(ApiConstants.GET_CAR_TYPE)
    fun getCarType(): Observable<Response<CarTypeResponse>>

    @FormUrlEncoded
    @POST(ApiConstants.VALIDATE_DOCS)
    fun validateDocs(@Field("docNumber") docNumber: String, @Field("type") type: Int,@Field("phoneNumber")phoneNumber: String?): Observable<Response<BaseResponseModel>>


    @POST(ApiConstants.VALIDATE_PHONE)
    fun validatePhone(@Body request: PhoneRequestModel): Observable<Response<BaseResponseModel>>


    @Multipart
    @POST(ApiConstants.REGISTER)
    fun registerDriver(
        @Part("firstName") firstName: okhttp3.RequestBody,
        @Part("lastName") lastName: okhttp3.RequestBody,
        @Part("email") email: okhttp3.RequestBody,
        @Part("phone") phone: okhttp3.RequestBody,
        @Part("licenseNumber") licenseNumber: okhttp3.RequestBody,
        @Part("licenseIssuedOn") licenseIssuedOn: okhttp3.RequestBody,
        @Part("licenseExpiryDate") licenseExpiryDate: okhttp3.RequestBody,
        @Part("carNumberId") carNumberId: okhttp3.RequestBody,
        @Part("brand") brand: okhttp3.RequestBody,
        @Part("model") model: okhttp3.RequestBody,
        @Part("year") year: okhttp3.RequestBody,
        @Part("color") color: okhttp3.RequestBody,
        @Part("carId") carId: okhttp3.RequestBody,
        @Part("device") device: okhttp3.RequestBody,
        @Part("password") password: okhttp3.RequestBody,
        @Part("userActiveRole") userActiveRole: okhttp3.RequestBody,
        @Part driverImage: MultipartBody.Part,
        @Part permitDoc: MultipartBody.Part,
        @Part insuranceDoc: MultipartBody.Part,
        @Part licenseDoc: MultipartBody.Part,
        @Part regDoc: MultipartBody.Part,
        @Part vehicleImage: MultipartBody.Part

    ): Observable<Response<RegistrationResponseModel>>


    @POST(ApiConstants.LOGIN)
    fun login(@Body loginRequest: LoginRequestModel): Observable<Response<LoginResponseModel>>


    @Multipart
    @POST(ApiConstants.REGISTER)
    fun registerRider(
        @Part("firstName") firstName: okhttp3.RequestBody,
        @Part("lastName") lastName: okhttp3.RequestBody,
        @Part("email") email: okhttp3.RequestBody,
        @Part("phone") phone: okhttp3.RequestBody,
        @Part("password") password: okhttp3.RequestBody,
        @Part("device") device: okhttp3.RequestBody,
        @Part("userActiveRole") userActiveRole: okhttp3.RequestBody
    ): Observable<Response<RegistrationResponseModel>>


    @POST(ApiConstants.REGISTER)
    fun registerRider(@Body request: RegisterRequestModel): Observable<Response<RegistrationResponseModel>>

    @POST(ApiConstants.FORGOT_PASSWORD)
    fun forgotPassword(@Body phone: ForgotRequest): Observable<Response<ForgotResponse>>

    @FormUrlEncoded
    @POST(ApiConstants.OTP_VERIFICATIONS)
    fun otpVerification(@Field("otp") otp: Int, @Field("token") token: String?): Observable<Response<OtpResponse>>


    @FormUrlEncoded
    @PUT(ApiConstants.RESET_PASSWORD)
    fun resetPassword(@Field("password") password: String): Observable<Response<BaseResponseModel>>

    @FormUrlEncoded
    @PUT(ApiConstants.CHANGE_PASSWORD)
    fun changePassword(
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String
    ): Observable<Response<BaseResponseModel>>

    @POST(ApiConstants.RESEND_OTP)
    fun resendPassword(): Observable<Response<BaseResponseModel>>


    // for rider only
    @Multipart
    @PUT(ApiConstants.UPDATE_PROFILE)
    fun updateRiderProfile(
        @Part("firstName") firstName: okhttp3.RequestBody,
        @Part("lastName") lastName: okhttp3.RequestBody,
        @Part("email") email: okhttp3.RequestBody
    ): Observable<Response<LoginResponseModel>>


    // for rider only
    @Multipart
    @PUT(ApiConstants.UPDATE_PROFILE)
    fun updateRiderProfileWithPhoto(
        @Part profileImage: MultipartBody.Part,
        @Part("firstName") firstName: okhttp3.RequestBody,
        @Part("lastName") lastName: okhttp3.RequestBody,
        @Part("email") email: okhttp3.RequestBody
    ): Observable<Response<LoginResponseModel>>


    @GET(ApiConstants.GET_CANCEL_REASONS)
    fun getCancellationReasons(@Query("type") type: Int): Observable<Response<CancellationReasonsResponseModel>>


    @POST(ApiConstants.RATING)
    fun rating(@Body request: RateRequestModel): Observable<Response<BaseResponseModel>>

    @GET(ApiConstants.RIDE_HISTORY)
    fun rideHistory(@Query("type") type: Int,@Query("offSet") offset: Int): Observable<Response<RiderHistoryResponseModel>>

    @DELETE(ApiConstants.LOGOUT)
    fun logout(): Observable<Response<BaseResponseModel>>

    @POST(ApiConstants.CONTACT_US)
    fun sendQuery(@Body request: SubmitQueryRequest): Observable<Response<BaseResponseModel>>

    @Multipart
    @PUT(ApiConstants.UPDATE_PROFILE)
    fun updateDriverProfile(
        @Part("firstName") firstName: okhttp3.RequestBody?,
        @Part("lastName") lastName: okhttp3.RequestBody?,
        @Part("email") email: okhttp3.RequestBody?,
        @Part("phone") phone: okhttp3.RequestBody?,
        @Part("licenseNumber") licenseNumber: okhttp3.RequestBody?,
        @Part("licenseIssuedOn") licenseIssuedOn: okhttp3.RequestBody?,
        @Part("licenseExpiryDate") licenseExpiryDate: okhttp3.RequestBody?,
        @Part("carNumberId") carNumberId: okhttp3.RequestBody?,
        @Part("brand") brand: okhttp3.RequestBody?,
        @Part("model") model: okhttp3.RequestBody?,
        @Part("year") year: okhttp3.RequestBody?,
        @Part("color") color: okhttp3.RequestBody?,
        @Part("carId") carId: okhttp3.RequestBody?,
        @Part driverImage: MultipartBody.Part?,
        @Part permitDoc: MultipartBody.Part?,
        @Part insuranceDoc: MultipartBody.Part?,
        @Part licenseDoc: MultipartBody.Part?,
        @Part regDoc: MultipartBody.Part?,
        @Part vehicleImage: MultipartBody.Part?
    ): Observable<Response<LoginResponseModel>>


    @GET(ApiConstants.TODAY_EARNING)
    fun driverEarning(@Query("type") type: Int, @Query("offSet") offSet: Int): Observable<Response<DriverEarningModel>>

    @POST(ApiConstants.RAISE_ISSUE)
    fun raiseIssue(@Body request: RaiseIssueRequest): Observable<Response<BaseResponseModel>>


    @GET(ApiConstants.DRIVER_RATINGS)
    fun getDriverRatings(@Query("offset") offSet: Int)
            : Observable<Response<DriverRatingResponseModel>>


    @PUT(ApiConstants.CHANGE_PHONE)
    fun changePhone(
        @Body request: PhoneRequestModel

    ): Observable<Response<LoginResponseModel>>

    @POST(ApiConstants.ADD_CARD)
    fun addCard(@Body addCard: AddCardModel): Observable<Response<BaseResponseModel>>

    @GET(ApiConstants.GET_CARD_LIST)
    fun getCardList(): Observable<Response<PaymentListModel>>

    @GET(ApiConstants.GET_CARD_LIST)
    fun getCardListDriver(): Observable<Response<PaymentListDriverModel>>

    @PUT(ApiConstants.DEFAULT_PAYMENT)
    fun getDefaultPayment(@Body getPayment: DefaultPaymentModel): Observable<Response<BaseResponseModel>>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = ApiConstants.DELETE_CARD, hasBody = true)
    fun deleteCard(@Field("accountId") id: String): Observable<Response<LoginResponseModel>>

    @POST(ApiConstants.ADD_BANK)
    fun addBankAccount(@Body addBank: AddBankModel): Observable<Response<LoginResponseModel>>

    @PUT(ApiConstants.BLOCK_DRIVER)
    fun blockDriver(@Body blockDriver: BlockDriverModel): Observable<Response<BaseResponseModel>>

    @GET(ApiConstants.GET_BOOKING_DATA)
    fun getBookingData(@Query("bookingId") bookingId: String): Observable<Response<BookingResponseModel>>


    @POST(ApiConstants.SCHEDULE_BOOKING)
    fun scheduleBooking(@Body createBookingRequestModel: CreateBookingRequestModel): Observable<Response<BaseResponseModel>>

    @POST(ApiConstants.ADD_TIP)
    fun addTip(@Body addTip: AddTipModel): Observable<Response<BaseResponseModel>>


}