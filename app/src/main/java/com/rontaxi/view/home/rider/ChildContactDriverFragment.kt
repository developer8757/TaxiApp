package com.rontaxi.view.home.rider

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rontaxi.R

class ChildContactDriverFragment : Fragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
         return inflater.inflate(R.layout.child_fragment_contact_driver, container, false)
    }

}