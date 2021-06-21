package com.scarach.spin_earn_money

import android.animation.Animator
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView
import com.anupkumarpanwar.scratchview.ScratchView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FieldValue
import com.scarach.spin_earn_money.databinding.ActivityScratchBinding
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import java.util.*

class ScratchActivity : CoreBaseActivity(), ScratchView.IRevealListener {
    private lateinit var binding: ActivityScratchBinding
    private lateinit var scratchCard: ScratchView
    private lateinit var timer: CountDownTimer
    private var isTimerFinished: Boolean = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScratchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scratchCard = binding.scratchCard
        checkDailyScratch()
        setScratch()
        scratchCard.setRevealListener(this)
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        Log.d("TAG", "onCreate: $scratch")

    }


    private fun showAddMoneyDialog() {
        val mBottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.scrach_bottom, null)
        mBottomSheetDialog.setContentView(view)
        val lotti = view.findViewById<LottieAnimationView>(R.id.scratchLotti)
        lotti.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                lotti.postDelayed({
                    lotti.pauseAnimation()
                }, 2500)
            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })
        val addBtn = view.findViewById<Button>(R.id.addScratchBtn)
        addBtn.setOnClickListener {
            lotti.playAnimation()
            mBottomSheetDialog.dismissWithAnimation
            //addCoin
        }
        mBottomSheetDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkDailyScratch() {
        AppPreferences.initialize(this)
        val dbDate: Date
        val defaultScratchDate = "16/06/2021"
        //AppPreferences.putString(AppPreferences.PrefKeys.SCRATCH_DATE,defaultScratchDate)
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        dbDate = if (!AppPreferences.hasKey(AppPreferences.PrefKeys.SCRATCH_DATE)) {
            dateFormat.parse(defaultScratchDate)
        } else {
            dateFormat.parse(AppPreferences.getString(AppPreferences.PrefKeys.SCRATCH_DATE))
        }
        val xDate = dateFormat.format(currentDate)
        val date = dateFormat.parse(xDate)
        if (date.after(dbDate) && date.compareTo(dbDate) != 0) {
            binding.totalScratchCard.text = "Total Scratch: 0/$scratch"
            AppPreferences.putString(AppPreferences.PrefKeys.SCRATCH_DATE, xDate)
            AppPreferences.putInt(AppPreferences.PrefKeys.TOTAL_SCRATCH, 0)
        } else {
            val totalScratch = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_SCRATCH, 0)
            if (totalScratch == scratch) {
                binding.totalScratchCard.text = "Today Scratch is completed!"
            } else {
                binding.totalScratchCard.text = "Total Scratch: $totalScratch/$scratch"
            }

        }
    }

    private fun setScratch() {
        val r = Random()
        val randomNumber: Int = r.nextInt(10)
        binding.randomScratch.text = randomNumber.toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
//        getAdminData()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onRevealed(scratchView: ScratchView?) {
        scratchView?.reveal()
        showAdInterstitial()
        countScratch(scratchView)
    }

    private fun showAdInterstitial() {
        startAppAd.showAd(object : AdDisplayListener {
            override fun adHidden(ad: Ad) {}
            override fun adDisplayed(ad: Ad) {}
            override fun adClicked(ad: Ad) {
                Log.d("TAG", "adClicked: ${ad.type}")
                isTimerFinished = true
                if (AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_SCRATCH, 0) == scratch) {
                    timer = object : CountDownTimer(60000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {

                        }

                        override fun onFinish() {
                            isTimerFinished = false
                            updateScratchCoin()
                        }
                    }.start()
                }

            }

            override fun adNotDisplayed(ad: Ad) {}
        })
    }

    private fun updateScratchCoin() {
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .update("userCoin", FieldValue.increment(25))
    }

    private fun countScratch(scratchView: ScratchView?) {
        var totalScratch = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_SCRATCH, 0)
        totalScratch++
        when {
            totalScratch < scratch -> {
                AppPreferences.putInt(AppPreferences.PrefKeys.TOTAL_SCRATCH, totalScratch)
                binding.totalScratchCard.text = "Total Scratch: $totalScratch/$scratch"
                scratchView?.postDelayed({
                    scratchView.mask()
                }, 5000)
            }
            totalScratch == scratch -> {
                AppPreferences.putInt(AppPreferences.PrefKeys.TOTAL_SCRATCH, totalScratch)
                binding.totalScratchCard.text = "Today Scratch is completed!"
            }
            else -> {
                Toast.makeText(this, "Today Scratch is Complete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRevealPercentChangedListener(scratchView: ScratchView?, percent: Float) {
        if (percent >= 20) {
            scratchView?.reveal()
            Log.d("Reveal Percentage", "onRevealPercentChangedListener: $percent")
        }
    }

    override fun onBackPressed() {
        startAppAd.onBackPressed()
        super.onBackPressed()
    }

}