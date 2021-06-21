package com.scarach.spin_earn_money

import android.os.Bundle
import android.util.Log
import com.scarach.spin_earn_money.databinding.ActivityTaskBinding
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener

class TaskActivity : CoreBaseActivity() {
    private lateinit var binding: ActivityTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AdBtn.setOnClickListener {
            showAdInterstitial()
        }

        binding.impression.text = "0/$impression"
        binding.impression.text = "0/$click"


    }

    private fun showAdInterstitial() {
        startAppAd.showAd(object : AdDisplayListener {
            override fun adHidden(ad: Ad) {

            }

            override fun adDisplayed(ad: Ad) {}
            override fun adClicked(ad: Ad) {
                Log.d("TAG", "adClicked: ${ad.type}")

            }

            override fun adNotDisplayed(ad: Ad) {}
        })
    }


}