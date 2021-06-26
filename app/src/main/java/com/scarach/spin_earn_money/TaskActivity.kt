package com.scarach.spin_earn_money

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdkUtils
import com.scarach.spin_earn_money.databinding.ActivityTaskBinding
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import java.util.*

class TaskActivity : CoreBaseActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var timer:CountDownTimer
    private var isTimerEnabled:Boolean= false
    private var bundle: Bundle? = null
    private var adView: MaxAdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bundle = intent.extras

        binding.AdBtn.setOnClickListener {
            AppPreferences.initialize(this)
            showAdInterstitial()
            if (bundle != null && bundle?.containsKey(AppPreferences.PrefKeys.OPEN_TASK_2) == true) {
                val impressionCount = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_One,0)
                val impression = getTaskOneImpression
                val preferences = AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_One
                taskOneClick(impressionCount,impression,preferences)
            } else {
                val impressionCount = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_Two,0)
                val impression = getTaskTwoImpression
                val preferences = AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_Two
                taskOneClick(impressionCount,impression,preferences)
            }
        }



    }

    private fun showAdInterstitial() {
        startAppAd.showAd(object : AdDisplayListener {
            override fun adHidden(ad: Ad) {}
            override fun adDisplayed(ad: Ad) {}
            override fun adClicked(ad: Ad) {
                Log.d("TAG", "adClicked: ${ad.type}")
                if (bundle != null && bundle?.containsKey(AppPreferences.PrefKeys.OPEN_TASK_2) == true) {
                    val impressionCount = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_One,0)
                    val impression = getTaskOneImpression
                    val click = getTaskOneClick
                    val preferences = AppPreferences.PrefKeys.TOTAL_CLICk_TASK_One
                    val nextTask = AppPreferences.PrefKeys.NEXT_TASK
                    val clickPrefrance = AppPreferences.PrefKeys.TOTAL_CLICk_TASK_One
                    adClickProcess(impressionCount, impression, preferences,nextTask, clickPrefrance,click)
                } else {
                    val impressionCount = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_Two,0)
                    val impression = getTaskTwoImpression
                    val click = getTaskTwoClick
                    val preferences = AppPreferences.PrefKeys.TOTAL_CLICk_TASK_Two
                    val nextTask = AppPreferences.PrefKeys.NEXT_TASK
                    val clickPrefrance = AppPreferences.PrefKeys.TOTAL_CLICk_TASK_Two
                    adClickProcess(impressionCount, impression, preferences,nextTask, clickPrefrance,click)

                }
            }

            override fun adNotDisplayed(ad: Ad) {}
        })
    }

    override fun onResume() {
        super.onResume()
        if (isTimerEnabled){
            timer.cancel()
            Toast.makeText(applicationContext,"Please complete your task",Toast.LENGTH_SHORT).show()
        }
        check()
    }

   fun countDown(time:Long){
       binding.nextTaskTv.visibility = View.VISIBLE
       timer = object : CountDownTimer(time, 1000) {
           override fun onTick(millisUntilFinished: Long) {
               val Days = millisUntilFinished / (24 * 60 * 60 * 1000)
               val Hours = millisUntilFinished / (60 * 60 * 1000) % 24
               val Minutes = millisUntilFinished / (60 * 1000) % 60
               val Seconds = millisUntilFinished / 1000 % 60
               binding.nextTaskTv.text = Html.fromHtml("<normal>Next task available in </normal>\n<strong>$Hours : $Minutes : $Seconds</strong>")

           }

           override fun onFinish() {
               if (bundle != null && bundle?.containsKey(AppPreferences.PrefKeys.OPEN_TASK_2) == true) {

                   AppPreferences.putInt(AppPreferences.PrefKeys.TOTAL_CLICk_TASK_One,0)
                   AppPreferences.putInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_One,0)
               } else {
                   AppPreferences.putInt(AppPreferences.PrefKeys.TOTAL_CLICk_TASK_Two,0)
                   AppPreferences.putInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_Two,0)
               }

           }

       }.start()
   }

   fun check(){
       AppPreferences.initialize(this)
       if (bundle != null && bundle?.containsKey(AppPreferences.PrefKeys.OPEN_TASK_2) == true) {
           val task = AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_One
           val click = AppPreferences.PrefKeys.TOTAL_CLICk_TASK_One
           val nextTask = AppPreferences.PrefKeys.NEXT_TASK
           val getTaskImpression = getTaskOneImpression
           val getClick = getTaskOneClick
           checkprocess(task, click, nextTask, getTaskImpression, getClick)
       } else {
           val task = AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_Two
           val click = AppPreferences.PrefKeys.TOTAL_CLICk_TASK_Two
           val nextTask = AppPreferences.PrefKeys.NEXT_TASK
           val getTaskImpression = getTaskTwoImpression
           val getClick = getTaskTwoClick
           checkprocess(task, click, nextTask, getTaskImpression, getClick)
       }
   }

    private fun checkprocess(
        task1: String,
        click1: String,
        nextTask: String,
        getTaskImpression: Int,
        getClick: Int
    ) {
        binding.impression.text = "${AppPreferences.getInt(task1, 0)}/$getTaskImpression"
        binding.click.text = "${AppPreferences.getInt(click1, 0)}/$getClick"
        val currentTaskTime = Calendar.getInstance().timeInMillis
        val nextTaskTime = AppPreferences.getInt(nextTask,-1)
        if (nextTaskTime != -1 && currentTaskTime >= nextTaskTime){
            AppPreferences.putInt(click1,0)
            AppPreferences.putInt(task1,0)
        }else if (nextTaskTime != -1 && currentTaskTime < nextTaskTime) {
            val newTime = AppPreferences.getInt(nextTask, 0) - currentTaskTime
            countDown(newTime)
        }
    }

    fun taskOneClick(impressionCounts: Int, impression: Int, preferences: String) {
        var impressionCount = impressionCounts
        if (impressionCount < impression) {
            impressionCount++
            AppPreferences.putInt(preferences, impressionCount)
            binding.impression.text = "$impressionCount/$impression"

        }else if (impression == impressionCount){
            Toast.makeText(applicationContext,"Click ad and wait 1 min",Toast.LENGTH_LONG).show()
        }
    }

    fun adClickProcess(
        impressionCount: Int,
        impression: Int,
        preferences: String,
        nextTask: String,
        clickPreferences: String,
        click: Int
    ) {
        if (impressionCount >= impression) {
            isTimerEnabled = true
            timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Toast.makeText(applicationContext,"wait ${millisUntilFinished/1000}s",Toast.LENGTH_SHORT).show()
                }

                override fun onFinish() {
                    isTimerEnabled = false
                    AppPreferences.putInt(preferences,1)
                    if (impressionCount >= impression &&
                        AppPreferences.getInt(preferences,0) >= click){
                        val currentTaskTime = Calendar.getInstance().timeInMillis
                        val nextTaskTime = currentTaskTime + 600000
                        AppPreferences.putLong(nextTask, nextTaskTime)
                        countDown(nextTaskTime)
                        binding.click.text = "${AppPreferences.getInt(clickPreferences, 0)}/$click"


                    }
                }

            }.start()
        }
    }

    override fun onBackPressed() {
        finish()
    }




}