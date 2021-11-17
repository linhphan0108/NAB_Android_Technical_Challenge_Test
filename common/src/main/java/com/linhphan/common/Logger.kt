package com.linhphan.common

import android.util.Log

/**
 * @author linhphan
 */
object Logger {

    private val enable = BuildConfig.DEBUG

    fun v(tag: String?, msg: String) {
        if (enable){
            Log.v(tag, msg)
        }
    }

    fun v(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            Log.v(tag, msg, tr)
        }
    }

    fun d(tag: String?, msg: String) {
        if (enable){
            Log.d(tag, msg)
        }
    }

    fun d(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            Log.d(tag, msg, tr)
        }
    }

    fun i(tag: String?, msg: String) {
        if (enable){
            Log.i(tag, msg)
        }
    }

    fun i(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            Log.i(tag, msg, tr)
        }
    }

    fun w(tag: String?, msg: String) {
        if (enable){
            Log.w(tag, msg)
        }
    }

    fun w(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            Log.w(tag, msg, tr)
        }
    }

    fun w(tag: String?, tr: Throwable?) {
        if (enable){
            Log.w(tag, tr)
        }
    }

    fun e(tag: String?, msg: String) {
        if (enable){
            Log.e(tag, msg)
        }
    }

    fun e(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            Log.e(tag, msg, tr)
        }
    }

    fun wtf(tag: String?, msg: String?) {
        if (enable){
            Log.wtf(tag, msg)
        }
    }

    fun wtf(tag: String?, tr: Throwable) {
        if (enable){
            Log.wtf(tag, tr)
        }
    }

    fun wtf(tag: String?, msg: String?, tr: Throwable?) {
        if (enable){
            Log.wtf(tag, msg, tr)
        }
    }
}