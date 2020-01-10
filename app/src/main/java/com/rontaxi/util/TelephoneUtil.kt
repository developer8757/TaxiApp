package com.rontaxi.util

import android.content.Context
import android.content.Intent
import android.net.Uri


fun makeTelephoneCall(context: Context,phoneNumber: String){

    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    context.startActivity(intent)

}