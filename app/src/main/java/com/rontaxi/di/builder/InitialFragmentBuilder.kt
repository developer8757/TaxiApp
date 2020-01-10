package com.rontaxi.di.builder

import com.rontaxi.di.modules.home.AddBankModule
import com.rontaxi.di.modules.home.BankDetailModule
import com.rontaxi.di.modules.initial.*
import com.rontaxi.view.home.driver.home.payment.AddBankFragment
import com.rontaxi.view.home.driver.home.payment.BankDetailFragment
import com.rontaxi.view.initial.driver.forgotpassword.DriverForgotPasswordFragment
import com.rontaxi.view.initial.driver.landing.LandingFragment
import com.rontaxi.view.initial.driver.license.LicenseDetailFragment
import com.rontaxi.view.initial.driver.login.DriverLoginFragment
import com.rontaxi.view.initial.driver.otp.DriverOtpFragment
import com.rontaxi.view.initial.driver.register.DriverRegisterFragment
import com.rontaxi.view.initial.driver.resetpassword.DriverResetPasswordFragment
import com.rontaxi.view.initial.driver.upload.UploadDocumentFragment
import com.rontaxi.view.initial.driver.vehicledetails.VehicleDetailsFragment
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeFragment
import com.rontaxi.view.initial.rider.forgotpassword.ForgotPasswordFragment
import com.rontaxi.view.initial.rider.login.LoginFragment
import com.rontaxi.view.initial.rider.otp.OtpFragment
import com.rontaxi.view.initial.rider.register.RegisterFragment
import com.rontaxi.view.initial.rider.resetpassword.ResetPasswordFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module

abstract class InitialFragmentBuilder {


    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun bindRiderLoginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [RegisterModule::class])
    abstract fun bindRiderRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector(modules = [OtpModule::class])
    abstract fun bindRiderOTPFragment(): OtpFragment

    @ContributesAndroidInjector(modules = [LandingModule::class])
    abstract fun bindLandingFragment(): LandingFragment

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun bindDriverLoginFragment(): DriverLoginFragment

    @ContributesAndroidInjector(modules = [RegisterModule::class])
    abstract fun bindDriverRegisterFragment(): DriverRegisterFragment

    @ContributesAndroidInjector(modules = [LicenseModule::class])
    abstract fun bindLicenseDetailsFragment(): LicenseDetailFragment

    @ContributesAndroidInjector(modules = [VehicleTypeModule::class])
    abstract fun bindVehicleTypeFragment(): VehicleTypeFragment

    @ContributesAndroidInjector(modules = [VehicleDetailsModule::class])
    abstract fun bindVehicleDetailsFragment(): VehicleDetailsFragment

    @ContributesAndroidInjector(modules = [UploadModule::class])
    abstract fun bindUploadFragment(): UploadDocumentFragment

    @ContributesAndroidInjector(modules = [ForgotModule::class])
    abstract fun bindForgotPasswordFragment(): ForgotPasswordFragment

    @ContributesAndroidInjector(modules = [ForgotModule::class])
    abstract fun bindDriverForgotPasswordFragment(): DriverForgotPasswordFragment

    @ContributesAndroidInjector(modules = [OtpModule::class])
    abstract fun bindDriverOtpFragment(): DriverOtpFragment

    @ContributesAndroidInjector(modules = [ResetPasswordModule::class])
    abstract fun bindDriverResetPasswordFragment(): DriverResetPasswordFragment

    @ContributesAndroidInjector(modules = [ResetPasswordModule::class])
    abstract fun bindResetPasswordFragment(): ResetPasswordFragment

    @ContributesAndroidInjector (modules = [BankDetailModule::class])
    abstract fun bindBankDetailFragment():BankDetailFragment

    @ContributesAndroidInjector(modules = [AddBankModule::class])
    abstract fun bindAddBankFragment(): AddBankFragment


}