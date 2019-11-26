package com.boiqin.demoapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.boiqin.screen.AutoScreenAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AutoScreenAdapter.match(this@MainActivity, 37f)

        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv).setOnClickListener{
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        AutoScreenAdapter.cancelMatch(this)
    }

}
