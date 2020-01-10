package com.rontaxi.di.builder

import com.rontaxi.di.modules.home.*
import com.rontaxi.di.modules.home.ChangePasswordModule
import com.rontaxi.di.modules.initial.*
import com.rontaxi.view.home.driver.home.HomeDriverFragment
import com.rontaxi.view.home.driver.home.earnings.DriverEarningTodayFragment
import com.rontaxi.view.home.driver.home.earnings.EarningsFragment
import com.rontaxi.view.home.driver.home.map.MapViewFragment
import com.rontaxi.view.home.driver.home.profile.AccountFragment
import com.rontaxi.view.home.driver.home.profile.basicinfo.BasicInformationFragment
import com.rontaxi.view.home.driver.home.profile.updatephone.ChangePhoneFragment
import com.rontaxi.view.home.driver.home.rating.DriverRatingFragment
import com.rontaxi.view.home.driver.home.payment.AddBankFragment
import com.rontaxi.view.home.driver.home.payment.BankDetailFragment
import com.rontaxi.view.home.rider.addtip.AddTipFragment
import com.rontaxi.view.home.rider.changepassword.ChangePasswordFragment
import com.rontaxi.view.home.rider.godview.GodViewFragment
import com.rontaxi.view.home.rider.help.HelpFragment
import com.rontaxi.view.home.rider.payment.AddCardFragment
import com.rontaxi.view.home.rider.payment.PaymentFragment
import com.rontaxi.view.home.rider.profile.RiderProfileFragment
import com.rontaxi.view.home.rider.rate.RateDriverFragment
import com.rontaxi.view.home.rider.ridehistory.*
import com.rontaxi.view.home.rider.schedulebooking.ScheduleBookingFragment
import com.rontaxi.view.initial.driver.license.LicenseDetailFragment
import com.rontaxi.view.initial.driver.otp.DriverOtpFragment
import com.rontaxi.view.initial.driver.upload.UploadDocumentFragment
import com.rontaxi.view.initial.driver.vehicledetails.VehicleDetailsFragment
import com.rontaxi.view.initial.driver.vehicletype.VehicleTypeFragment
import com.rontaxi.view.initial.rider.otp.OtpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class HomeFragmentBuilder {

    @ContributesAndroidInjector(modules = [GodViewModule::class])
    abstract fun bindHomeFragment(): GodViewFragment

    @ContributesAndroidInjector(modules = [ProfileModule::class])
    abstract fun bindProfileFragment(): RiderProfileFragment

    @ContributesAndroidInjector(modules = [HomeDriverModule::class])
    abstract fun bindHomeDriverFragment(): HomeDriverFragment

    @ContributesAndroidInjector(modules = [MapViewModule::class])
    abstract fun bindMapViewFragment(): MapViewFragment

    @ContributesAndroidInjector(modules = [RateViewModule::class])
    abstract fun bindDriverRateFragment(): RateDriverFragment

    @ContributesAndroidInjector(modules = [HelpModule::class])
    abstract fun bindHelpFragment(): HelpFragment

    @ContributesAndroidInjector(modules = [OtpModule::class])
    abstract fun bindOtpFragment(): OtpFragment

    @ContributesAndroidInjector(modules = [OtpModule::class])
    abstract fun bindDriverOtpFragment(): DriverOtpFragment

    @ContributesAndroidInjector(modules = [AccountModule::class])
    abstract fun bindAccountFragment(): AccountFragment

    @ContributesAndroidInjector(modules = [BasicInformationModule::class])
    abstract fun bindBasicInformationFragment(): BasicInformationFragment

    @ContributesAndroidInjector(modules = [DriverRatingModule::class])
    abstract fun bindDriverRatingFragment(): DriverRatingFragment

    @ContributesAndroidInjector(modules = [PastRidesModule::class])
    abstract fun bindTripHistoryFragment(): RideHistoryFragment

    @ContributesAndroidInjector(modules = [DriverEarningModule::class])
    abstract fun bindDriverEarningFragment(): EarningsFragment

    @ContributesAndroidInjector(modules = [DriverEarningTodayModule::class])
    abstract fun bindDirverEarningTodayFragment(): DriverEarningTodayFragment

    @ContributesAndroidInjector(modules = [ChangePasswordModule::class])
    abstract fun bindChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector(modules = [ChangePhoneModule::class])
    abstract fun bindUpdateDriverPhoneFragment(): ChangePhoneFragment


    @ContributesAndroidInjector(modules = [UploadModule::class])
    abstract fun bindUploadFragment(): UploadDocumentFragment

    @ContributesAndroidInjector(modules = [LicenseModule::class])
    abstract fun bindLicenseDetailsFragment(): LicenseDetailFragment

    @ContributesAndroidInjector(modules = [VehicleDetailsModule::class])
    abstract fun bindVehicleDetailsFragment(): VehicleDetailsFragment

    @ContributesAndroidInjector(modules = [VehicleTypeModule::class])
    abstract fun bindVehicleTypeFragment(): VehicleTypeFragment

    @ContributesAndroidInjector(modules = [PaymentModule::class])
    abstract fun bindPaymentFragment(): PaymentFragment

    @ContributesAndroidInjector(modules = [AddCardModule::class])
    abstract fun bindAddCardFragment(): AddCardFragment

    @ContributesAndroidInjector(modules = [AddBankModule::class])
    abstract fun bindAddBankFragment(): AddBankFragment

    @ContributesAndroidInjector(modules = [RideHistoryDetailModule::class])
    abstract fun bindRideHistoryDetailFragment(): RideHistoryDetailFragment

    @ContributesAndroidInjector(modules = [BankDetailModule::class])
    abstract fun bindBankDetailFragment(): BankDetailFragment

    @ContributesAndroidInjector (modules = [AddTipModule::class])
    abstract fun bindAddTipFragment(): AddTipFragment

    @ContributesAndroidInjector(modules = [ScheduleBookingViewModule::class])
    abstract fun bindScheduleBookingFragment(): ScheduleBookingFragment

    @ContributesAndroidInjector(modules = [RideHistoryModule::class])
    abstract fun bindRidesContainerFragment(): RidesContainerFragment

    @ContributesAndroidInjector(modules = [UpcomingRidesModule::class])
    abstract fun bindUpcomingRidesFragment(): UpcomingRidesFragment
}