package com.rontaxi.base


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.fxn.utility.Utility
import com.rontaxi.R
import kotlinx.android.synthetic.main.dialog_cancelleation_reasons.view.*


/**
 * Created by harminder on 27/3/18.
 */
abstract class BaseDialogFragment : DialogFragment() {

    var baseView: View? = null

    @LayoutRes
    protected abstract fun getContentLayoutResId(): Int


    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (baseView == null) {
            baseView = inflater.inflate(getContentLayoutResId(), container, true)
        }

        return baseView
    }


    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT

            dialog?.window?.setLayout(width, height)
        }
    }

    fun setToolbarBackButton(toolbar: Toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }


    fun setBackIcon(ivBack: ImageView) {
        ivBack.setImageResource(R.drawable.ic_arrow_back_black_24dp)
        ivBack.setOnClickListener {
            // (activity as HomeActivity).onBackPressed()
        }
    }

    fun setHomeIcon(ivMenu: ImageView) {
        //ivMenu.setImageResource(R.drawable.menu)
        ivMenu.setOnClickListener {
            drawerOpenClose()
        }
    }


    fun setIcon(ivBack: ImageView) {
        ivBack.setImageResource(R.drawable.ic_arrow_back_black_24dp)
        ivBack.setOnClickListener {
            //  (activity as HomeActivity).onBackPressed()
        }
    }


    fun setTitle(tvTitle: TextView, title: String) {
        tvTitle.text = title
    }

    open fun backPress() {}


    private lateinit var baseActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.baseActivity = context
            //      this.baseActivity.onFragmentAttached();
        }
    }


//    protected fun replaceFragment(@NonNull fragment: Fragment) {
//        baseActivity?.replaceFragment(fragment)
//
//    }

    override fun onDestroyView() {
//        if (unbinder != null) {
//            unbinder.unbind()
//        }
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
    }

//    override fun showLoading(message: String) {
//        baseActivity.showLoading(message)
//    }


//    override fun hideLoading() {
//        baseActivity.hideLoading()
//    }

//    override fun onUnknownError(error: String) {
//        baseActivity.onUnknownError(error)
//    }

//    override fun onNetworkError() {
//        if (baseActivity != null) {
//            baseActivity.onNetworkError()
//        }
//    }

//    override fun onTimeout() {
//        if (baseActivity != null) {
//            baseActivity.onTimeout()
//        }
//    }

//    override fun isNetworkAvailable(): Boolean {
//        if (baseActivity != null) {
//            baseActivity.isNetworkAvailable()
//        }
//        return false
//    }

//    override fun onConnectionError() {
//
//        baseActivity?.onConnectionError()
//
//    }


    fun drawerOpenClose() {
        //  (baseActivity as HomeActivity).drawerOpenClose()
    }

    fun drawerOpenClose(open: Boolean) {
        // (baseActivity as HomeActivity).drawerOpenClose(open)
    }

    fun hideKeyBoard(input: EditText?) {

        input?.let {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(input.windowToken, 0)
        }
    }


//    fun setToolbar(name: String, drawable: Int?,toolbar2: View) {
//        toolbar2.toolbar?.apply {
//            navigationIcon?.setTint(ContextCompat.getColor(activity!!,R.color.white))
//            if (drawable != null)
//                setNavigationIcon(drawable)
//            setNavigationOnClickListener {
//                activity?.onBackPressed()
//            }
//        }
//        toolbar2.tvTitle.text=name
//    }


    fun setToolbar(name: String, drawable: Int?, toolbar: Toolbar) {
        // toolbar.title = addressFromUser
        if (drawable != null)
            toolbar.setNavigationIcon(drawable)

        toolbar.navigationIcon?.setTint(ContextCompat.getColor(activity!!,R.color.colorPrimary))
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        if(name.isNotEmpty())
            toolbar.tvTitle.setCompoundDrawables(null,null,null,null)
        toolbar.tvTitle.text = name


    }

//    override fun onDestroy() {
//        super.onDestroy()
//        hideLoading()
//    }

    fun setToolbarHome(name: String, toolbar: Toolbar) {
        // toolbar.title = addressFromUser
        //toolbar.setNavigationIcon(R.drawable.ic)

        toolbar.navigationIcon?.setTint(ContextCompat.getColor(activity!!,R.color.white))
        toolbar.setNavigationOnClickListener {
            //  (activity as HomeActivity).openCloseDrawer()
        }
        toolbar.tvTitle.text = name
    }

    private var mLastClickTime = 0L
    fun isValidClick(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
            return false
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
            return true
        }
    }

//    fun checkPermission(context: Context): Boolean {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    val alertBuilder = AlertDialog.Builder(context)
//                    alertBuilder.setCancelable(true)
//                    alertBuilder.setTitle(R.string.permission_neccassary)
//                    alertBuilder.setMessage(R.string.storage_permission)
//                    alertBuilder.setPositiveButton(android.R.string.yes) { dialog, which -> requestPermissions(arrayOf(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE), Utility.Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) }
//                    val alert = alertBuilder.create()
//                    alert.show()
//                } else {
//                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Utility.Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
//                }
//                return false
//            } else {
//                return true
//            }
//        } else {
//            return true
//        }
//
//    }
//    fun checkCameraPermission(context: Context): Boolean {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                    val alertBuilder = AlertDialog.Builder(context)
//                    alertBuilder.setCancelable(true)
//                    alertBuilder.setTitle(R.string.permission_neccassary)
//                    alertBuilder.setMessage(R.string.camera_permission)
//                    alertBuilder.setPositiveButton(android.R.string.yes) { dialog, which -> requestPermissions(arrayOf(
//                        Manifest.permission.CAMERA), Utility.Constants.MY_PERMISSIONS_REQUEST_CAMERA) }
//                    val alert = alertBuilder.create()
//                    alert.show()
//                } else {
//                    requestPermissions(arrayOf(Manifest.permission.CAMERA), Utility.Constants.MY_PERMISSIONS_REQUEST_CAMERA)
//                }
//                return false
//            } else {
//                return true
//            }
//        } else {
//            return true
//        }
//
//    }

}