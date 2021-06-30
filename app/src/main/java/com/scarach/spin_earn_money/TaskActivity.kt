package com.scarach.spin_earn_money

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.applovin.mediation.*
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinSdkUtils
import com.google.firebase.firestore.FieldValue
import com.scarach.spin_earn_money.databinding.ActivityTaskBinding
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import java.util.*

class TaskActivity : CoreBaseActivity(), MaxAdListener, MaxAdViewAdListener {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var timer:CountDownTimer
    private var isTimerEnabled:Boolean= false
    private var bundle: Bundle? = null
    private var adView: MaxAdView? = null
    private var interstitialAd: MaxInterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bundle = intent.extras

        createMrecAd()

        binding.AdBtn.setOnClickListener {
            AppPreferences.initialize(this)
            if (bundle != null && bundle?.containsKey(AppPreferences.PrefKeys.OPEN_TASK_2) == true) {
                showAdInterstitial()

            } else {
                showApplovinAd()
            }

            binding.AdBtn.isClickable = false
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

    }

    private fun showAdInterstitial() {
        startAppAd.showAd(object : AdDisplayListener {
            override fun adHidden(ad: Ad) {
                binding.AdBtn.isClickable = true
            }
            override fun adDisplayed(ad: Ad) {
                val impressionCount = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_One,0)
                val impression = getTaskOneImpression
                val preferences = AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_One
                taskOneClick(impressionCount,impression,preferences, "Click ad and wait 1 min")
            }
            override fun adClicked(ad: Ad) {
                Log.d("TAG", "adClicked: ${ad.type}")
                if (bundle != null && bundle?.containsKey(AppPreferences.PrefKeys.OPEN_TASK_2) == true) {
                    val impressionCount = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_One,0)
                    val impression = getTaskOneImpression
                    val click = getTaskOneClick
                    val preferences = AppPreferences.PrefKeys.TOTAL_CLICk_TASK_One
                    val nextTask = AppPreferences.PrefKeys.NEXT_TASK
                    adClickProcess(impressionCount, impression, preferences,nextTask, click)
                }
            }

            override fun adNotDisplayed(ad: Ad) {
                binding.AdBtn.isClickable = true
            }


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
               binding.nextTaskTv.text = Html.fromHtml("<normal>Next task available in </normal><br><strong> $Minutes : $Seconds</strong>")

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

        val currentTaskTime = Calendar.getInstance().timeInMillis
        val newTimeInString = AppPreferences.getString(nextTask, "0")?.toLong()

        if (AppPreferences.getInt(task1, 0)>=getTaskImpression
            && AppPreferences.getInt(click1, 0) >= getClick) {
            if (currentTaskTime >= newTimeInString!!){
                AppPreferences.putInt(click1,0)
                AppPreferences.putInt(task1,0)
                AppPreferences.putString(nextTask,"0")
            }else if (currentTaskTime < newTimeInString) {
                val newTime = newTimeInString.minus(currentTaskTime)
                countDown(newTime)
            }
        }

        binding.impression.text = "${AppPreferences.getInt(task1, 0)}/$getTaskImpression"
        binding.click.text = "${AppPreferences.getInt(click1, 0)}/$getClick"
    }

    private fun taskOneClick(impressionCounts: Int, impression: Int, preferences: String, toastMsg: String) {
        var impressionCount = impressionCounts
        if (impressionCount < impression) {
            impressionCount++
            AppPreferences.putInt(preferences, impressionCount)
            binding.impression.text = "$impressionCount/$impression"

        }else if (impression == impressionCount){
            Toast.makeText(applicationContext,toastMsg,Toast.LENGTH_LONG).show()
        }
    }

    fun adClickProcess(
        impressionCount: Int,
        impression: Int,
        preferences: String,
        nextTask: String,
        click: Int
    ) {
        if (impressionCount >= impression) {
            isTimerEnabled = true
            timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Toast.makeText(applicationContext,"wait ${millisUntilFinished/1000}s",Toast.LENGTH_SHORT).show()
                }

                override fun onFinish() {
                    Toast.makeText(applicationContext,"Awesome your task is complete",Toast.LENGTH_SHORT).show()
                    isTimerEnabled = false
                    AppPreferences.putInt(preferences,1)

                    if (impressionCount >= impression &&
                        AppPreferences.getInt(preferences,0) >= click){
                        val currentTaskTime = Calendar.getInstance().timeInMillis
                        val nextTaskTime = currentTaskTime + 600000
                        AppPreferences.putString(nextTask, nextTaskTime.toString())
                        countDown(nextTaskTime)
                        binding.click.text = "${AppPreferences.getInt(preferences, 0)}/$click"
                        if (bundle != null && bundle?.containsKey(AppPreferences.PrefKeys.OPEN_TASK_2) == true) {
                            updateSpinCoin(getStartAppTaskCoin,"Task Complete")
                        }else{
                            updateSpinCoin(getApplovinTaskCoin,"Task Complete")
                        }


                    }
                }

            }.start()
        }
    }

    private fun updateSpinCoin(cash: Int, title: String) {
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .update("userCoin", FieldValue.increment(cash.toLong()))
            .addOnSuccessListener {
                val transaction = Transaction(
                    title = title,
                    time = Calendar.getInstance().timeInMillis.toString(),
                    coin = cash.toString()
                )
                db.collection("users")
                    .document(auth.currentUser?.uid.toString())
                    .collection("transaction")
                    .document(UUID.randomUUID().toString())
                    .set(transaction)


            }
    }

    private fun showApplovinAd(){
        interstitialAd = MaxInterstitialAd("274d1164d0b4a1f8", this)
        interstitialAd?.setListener(this)

        // Load the first ad

        // Load the first ad
        interstitialAd?.loadAd()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onAdLoaded(ad: MaxAd?) {
        interstitialAd?.showAd()
    }

    override fun onAdDisplayed(ad: MaxAd?) {
        val impressionCount = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_Two,0)
        val impression = getTaskTwoImpression
        val preferences = AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_Two
        taskOneClick(impressionCount,impression,preferences,"Click ad and download")

    }

    override fun onAdHidden(ad: MaxAd?) {
        binding.AdBtn.isClickable = true
    }

    override fun onAdClicked(ad: MaxAd?) {
        val impressionCount = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_IMPRESSION_TASK_Two,0)
        val impression = getTaskTwoImpression
        val click = getTaskTwoClick
        val preferences = AppPreferences.PrefKeys.TOTAL_CLICk_TASK_Two
        val nextTask = AppPreferences.PrefKeys.NEXT_TASK
        adClickProcess(impressionCount, impression, preferences,nextTask, click)
    }

    fun createMrecAd() {
        adView = MaxAdView("ca8dad739f78e1e3", MaxAdFormat.MREC, this)
        adView!!.setListener(this)

        // MREC width and height are 300 and 250 respectively, on phones and tablets
        val widthPx = AppLovinSdkUtils.dpToPx(this, 300)
        val heightPx = AppLovinSdkUtils.dpToPx(this, 250)

        val params = FrameLayout.LayoutParams(widthPx, heightPx)
        params.gravity = Gravity.BOTTOM or Gravity.CENTER
        adView!!.layoutParams = params

        // Set background or background color for MRECs to be fully functional

        val rootView = findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(adView)

        // Load the ad
        adView!!.loadAd()
        adView!!.visibility = View.VISIBLE
        adView!!.startAutoRefresh()
    }


    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
        binding.AdBtn.isClickable = true
    }

    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        binding.AdBtn.isClickable = true
    }

    override fun onAdExpanded(ad: MaxAd?) {
    }

    override fun onAdCollapsed(ad: MaxAd?) {
    }


}