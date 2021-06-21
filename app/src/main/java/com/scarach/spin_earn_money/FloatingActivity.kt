package com.scarach.spin_earn_money

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class FloatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.floating_dialog)
    }
}