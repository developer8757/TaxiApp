package com.rontaxi.cache

import android.content.Context
import com.rontaxi.constants.CacheConstants
import com.rontaxi.model.map.Address
import com.rontaxi.model.map.RecentSearch
import com.rontaxi.model.registration.Phone
import com.rontaxi.model.user.User
import com.rontaxi.util.Prefs

fun getToken(context: Context): String? {
    return Prefs.with(context).getString(CacheConstants.USER_TOKEN, "")
}

fun saveToken(context: Context, token: String) {
    Prefs.with(context).save(CacheConstants.USER_TOKEN, token)
}


fun getUser(context: Context): User? {
    return Prefs.with(context).getObject(CacheConstants.USER_DATA, User::class.java)
}

fun saveUser(context: Context, user: User) {
    Prefs.with(context).save(CacheConstants.USER_DATA, user)
}


fun savePhone(context: Context, phone: Phone) {

    Prefs.with(context).save(CacheConstants.PHONE, phone)
}

fun getPhone(context: Context): Phone? {
    return Prefs.with(context).getObject(CacheConstants.PHONE, Phone::class.java)
}


fun saveWorkLocation(context: Context, officeAddress: Address) {
    Prefs.with(context).save(CacheConstants.WORK_LOCATION, officeAddress)
}

fun getWorkLocation(context: Context): Address? {
    return Prefs.with(context).getObject(CacheConstants.WORK_LOCATION, Address::class.java)
}

fun saveHomeLocation(context: Context, homeAddress: Address) {
    Prefs.with(context).save(CacheConstants.HOME_LOCATION, homeAddress)
}

fun getHomeLocation(context: Context): Address? {
    return Prefs.with(context).getObject(CacheConstants.HOME_LOCATION, Address::class.java)
}

fun getRecentSearch(context: Context): RecentSearch? {
    return Prefs.with(context).getObject(CacheConstants.RECENT_SEARCH, RecentSearch::class.java)
}

fun saveRecentSearch(context: Context, recentSearch: RecentSearch) {
    Prefs.with(context).save(CacheConstants.RECENT_SEARCH, recentSearch)
}

fun saveSOSNumber(context: Context, number: String) {
    Prefs.with(context).save(CacheConstants.SOS, number)

}

fun saveDriverOnlineStatus(context: Context, onlineStatus: Boolean) {
    Prefs.with(context).save(CacheConstants.DRIVER_ONLINE_STATUS, onlineStatus)

}

fun getDriverSavedOnlineStatus(context: Context): Boolean {
    return Prefs.with(context).getBoolean(CacheConstants.DRIVER_ONLINE_STATUS, false)

}

fun getSOSNumber(context: Context): String {

    return Prefs.with(context).getString(CacheConstants.SOS, "")
}


fun clearAllData(context: Context) {
    Prefs.with(context).removeAll()
}