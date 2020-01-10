package com.rontaxi.base

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.rontaxi.R
import com.util.FragmentUtil
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

 abstract class BaseActivity : AppCompatActivity(),HasSupportFragmentInjector {


    @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)


        super.onCreate(savedInstanceState)
        //AndroidInjection.inject(this)

    }


//     fun replaceFragment(fragment: Fragment, keepInStack: Boolean) {
//         FragmentUtil.replaceFragment(this, fragment, R.id.container, keepInStack, FragmentUtil.TRANSITION_NONE)
//     }

     fun showActionBar(bool: Boolean){
       /*  if(bool)
             supportActionBar?.show()
         else
             supportActionBar?.hide()*/
     }



    override fun supportFragmentInjector(): AndroidInjector<Fragment> {

        return supportFragmentInjector
    }

     fun setTitle(title: String){
        // supportActionBar?.title=title
     }

     fun popBackStack(){
         supportFragmentManager.popBackStack()
     }

     fun replaceFragmentBase(fragment: Fragment,containerId: Int,addToStack: Boolean,@FragmentUtil.FragmentAnimation animation:Int){
         FragmentUtil.replaceFragment(this,fragment,containerId,addToStack,animation)
     }




}
