package com.rontaxi.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.rontaxi.BuildConfig
import com.rontaxi.R
import com.rontaxi.cache.getUser
import com.rontaxi.util.getFileNameFromUrl
import com.rontaxi.view.home.driver.HomeDriverActivity
import com.rontaxi.view.home.rider.HomeRiderActivity
import com.rontaxi.view.initial.driver.InitialDriverActivity
import com.rontaxi.view.initial.rider.InitialRiderActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //supportActionBar!!.hide()

        Handler().postDelayed(Runnable {
            var intent: Intent? = null
            if (BuildConfig.ROLE == 0) {// rider{
                if (getUser(this) == null) {
                    intent = Intent(this, InitialRiderActivity::class.java)
                } else {
                    intent = Intent(this, HomeRiderActivity::class.java)
                }
            } else {
                if (getUser(this) == null) {
                    intent = Intent(this, InitialDriverActivity::class.java)
                } else {
                    intent = Intent(this, HomeDriverActivity::class.java)
                }
            }
            startActivity(intent)
            finish()

        }, 2000)
    }
}