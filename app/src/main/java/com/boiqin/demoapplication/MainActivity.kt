package com.boiqin.demoapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.boiqin.screen.AutoScreenAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AutoScreenAdapter.match(this, 75f)

        setContentView(R.layout.activity_main)
    }
}
