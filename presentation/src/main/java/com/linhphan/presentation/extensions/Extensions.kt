package com.linhphan.presentation.extensions

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.linhphan.presentation.BuildConfig


fun View.temporaryLockView(time: Long = 200): Runnable {
    isEnabled = false
    val runnable = Runnable { isEnabled = true }
    postDelayed(runnable, time)
    return runnable
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.toast(@StringRes mesResId: Int, duration: Int = Toast.LENGTH_SHORT){
    context.toast(context.getString(mesResId), duration)
}

fun View.toast(message: String, duration: Int = Toast.LENGTH_SHORT){
    context.toast(message, duration)
}

fun View.enableTapToHideKeyboard() {
    this.setOnTouchListener { v, _ ->
        hideKeyboard()
        true
    }
}

fun View.hideKeyboard(){
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

////////////////////////////
fun ViewGroup.inflate(@LayoutRes l: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(l, this, attachToRoot)

////////////////////////////
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, duration).show()
}

fun Context.toastLong(message: String, duration: Int = Toast.LENGTH_LONG)
        = toast(message, duration)

////////////////////////////
fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> = Transformations.distinctUntilChanged(this)