package com.boiqin.demoapplication

import android.app.Application
import com.boiqin.screen.AutoScreenAdapter

class MyApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        AutoScreenAdapter.setup(this)
        AutoScreenAdapter.register(this, 37f)
    }



}