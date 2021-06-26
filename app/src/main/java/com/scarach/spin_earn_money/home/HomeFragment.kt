package com.scarach.spin_earn_money.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.startapp.sdk.ads.nativead.NativeAdDetails
import com.startapp.sdk.ads.nativead.NativeAdPreferences
import com.startapp.sdk.ads.nativead.StartAppNativeAd
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdEventListener


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var startAppNativeAd: StartAppNativeAd
    private lateinit var nativeAdList: List<NativeAdDetails>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
      //  loadNativeAd()
        val home = activity?.findViewById<ImageView>(R.id.action_home)
        home?.setColorFilter(ContextCompat.getColor(context as Activity, R.color.colorPrimary))
        clickListener()
        startAppNativeAd = StartAppNativeAd(activity as HomeActivity)

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
        binding.Refer.setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(AppPreferences.PrefKeys.OPEN_TASK_2,AppPreferences.PrefKeys.OPEN_TASK_2)
            activity?.startActivity(intent)

        }
        binding.Ad.setOnClickListener {
             startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/SMARTCASH21")))

 }


    }

    private fun loadNativeAd() {
        val nativeAd = StartAppNativeAd(context as Activity)
        nativeAd.loadAd(NativeAdPreferences()
            .setAdsNumber(1)
            .setAutoBitmapDownload(true)
            .setPrimaryImageSize(2), object : AdEventListener {
            @SuppressLint("SetTextI18n")
            override fun onReceiveAd(ad: Ad) {
                nativeAdList = nativeAd.nativeAds
                val get = nativeAdList[1]
               binding.icon.setImageBitmap(get.imageBitmap)
                binding.title.text = "${get.title}\n${get.description}\n ${if(get.isApp) "install" else "open"}"
            }

            override fun onFailedToReceiveAd(ad: Ad) {
                Log.v("TAG", "onFailedToReceiveAd: " + ad.errorMessage)
            }
        })
    }

    fun setNativeAd(){
        Log.d("TAG", "onCreateView: ${nativeAdList.size}")
        val get = nativeAdList[1]
        binding.icon.setImageBitmap(get.imageBitmap)
        binding.title.text = "${get.title}\n${get.description}\n ${if(get.isApp) "install" else "open"}"
    }


    companion object
}