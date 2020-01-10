package com.rontaxi.util

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rontaxi.R
import java.io.File


//RequestOptions requestOptions = new RequestOptions();
//requestOptions.placeholder(R.drawable.ic_placeholder);
//requestOptions.error(R.drawable.ic_error);

val requestOptions: RequestOptions = RequestOptions()

fun ImageView.loadProfileImageFromURL(context: Context, url: String) {

    requestOptions.placeholder(R.drawable.ic_crop_original_black_24dp)
    requestOptions.error((R.drawable.ic_crop_original_black_24dp))
    requestOptions.circleCrop()

    Glide.with(context)
        .load(url)
        .apply(requestOptions)
        .into(this)
}

fun ImageView.loadProfileImageFromURI(context: Context, path: String) {
    Glide.with(context)
        .load(Uri.fromFile(File(path)))
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun ImageView.loadImage(context: Context, url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadImageFromUri(context: Context, path: String) {
    Glide.with(context)
        .load(Uri.fromFile(File(path)))
        .into(this)
}







