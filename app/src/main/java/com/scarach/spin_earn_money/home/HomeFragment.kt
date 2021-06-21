package com.scarach.spin_earn_money.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.scarach.spin_earn_money.*
import com.scarach.spin_earn_money.databinding.FragmentHomeBinding
import com.startapp.sdk.ads.nativead.NativeAdPreferences
import com.startapp.sdk.ads.nativead.StartAppNativeAd
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdEventListener


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var startAppNativeAd: StartAppNativeAd


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val home = activity?.findViewById<ImageView>(R.id.action_home)
        home?.setColorFilter(ContextCompat.getColor(context as Activity, R.color.colorPrimary))
        clickListener()
        startAppNativeAd = StartAppNativeAd(activity as HomeActivity)


        val nativeAdPreferences = NativeAdPreferences()
        nativeAdPreferences.adsNumber = 1
        nativeAdPreferences.isAutoBitmapDownload = true
        nativeAdPreferences.primaryImageSize = 2

        val adListener: AdEventListener = object : AdEventListener {
            // Callback Listener
            override fun onReceiveAd(arg0: Ad) {
                // Native Ad received
                val ads = startAppNativeAd.nativeAds // get NativeAds list

                // Print all ads details to log
                val iterator: Iterator<*> = ads.iterator()
                while (iterator.hasNext()) {
                    Log.d("MyApplication", iterator.next().toString())
                }
            }

            override fun onFailedToReceiveAd(arg0: Ad) {
                // Native Ad failed to receive
                Log.e("MyApplication", "Error while loading Ad")
            }
        }
        startAppNativeAd.loadAd(nativeAdPreferences, adListener)


        return binding.root

    }


    private fun clickListener() {
        binding.bonus.setOnClickListener {
            activity?.startActivity(Intent(activity, DailyBonusActivity::class.java))
        }

        binding.Scratch.setOnClickListener {
            activity?.startActivity(Intent(activity, ScratchActivity::class.java))
        }

        binding.spin.setOnClickListener {
            activity?.startActivity(Intent(activity, SpinActivity::class.java))

        }
        binding.task.setOnClickListener {
            activity?.startActivity(Intent(activity, TaskActivity::class.java))

        }


    }


    companion object
}