package com.scarach.spin_earn_money

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import com.scarach.spin_earn_money.databinding.ActivityExtraBinding
import com.startapp.sdk.adsbase.StartAppAd

class ExtraActivity : CoreBaseActivity() {
    private lateinit var binding : ActivityExtraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            startAppAd.onBackPressed()
            onBackPressed()
        }
        howToWork()
    }

    fun howToWork(){
        binding.howToWork.visibility = View.VISIBLE
        binding.howToWorkDetails.text = getString(R.string.how_to_work)
    }

    override fun onBackPressed() {
        startAppAd.onBackPressed()
        super.onBackPressed()
    }
}