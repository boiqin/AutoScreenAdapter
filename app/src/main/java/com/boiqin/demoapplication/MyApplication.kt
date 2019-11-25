package com.boiqin.demoapplication

import android.app.Application
import com.boiqin.screen.AutoScreenAdapter

class MyApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        //AutoScreenAdapter.register(this, 375f)
    }



}