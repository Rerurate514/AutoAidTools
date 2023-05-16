package com.example.autoaidtools

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Space
import android.widget.TableRow
import android.widget.TextView

val mAPI = MyAppBaseAPI()

class MyAppBaseAPI {
    fun logTest(msg: String = "log is outed"){
        Log.d("test",msg)
    }

    fun visibleView(view: View, mode: Boolean){
        when(mode){
            true -> view.visibility = View.VISIBLE
            false -> view.visibility = View.INVISIBLE
        }
    }

    fun runCompleted(callback: () -> Unit) {
        callback.invoke()
    }

    fun countClosure(mode :String = "plus") : () -> Int{
        var count = 0
        val counter : () -> Int = {
            when (mode) {
                "plus" -> count++
                "minus" -> count--
            }
            count
        }
        return counter
    }
}