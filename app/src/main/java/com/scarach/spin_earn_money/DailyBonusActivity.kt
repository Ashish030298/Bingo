package com.scarach.spin_earn_money

import android.animation.Animator
import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.scarach.spin_earn_money.databinding.ActivityDailyBonusBinding
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.adlisteners.AdEventListener
import java.util.*

class DailyBonusActivity : CoreBaseActivity() {
    private lateinit var binding: ActivityDailyBonusBinding
    private val TAG = "DailyBonusActivity"
    private var dailyBonusDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyBonusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        binding.addBonusBtn.setOnClickListener {
            showRewardedVideo(it)
            binding.addBonusBtn.isClickable = false

        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }




        binding.lottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                binding.lottie.postDelayed({
                    binding.bonusImage.visibility = View.VISIBLE
                    binding.lottie.visibility = View.GONE
                }, 2300)
            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })

    }

    @SuppressLint("NewApi")
    fun checkDailyBonus() {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dbDate = dateFormat.parse(dailyBonusDate)
        val xDate = dateFormat.format(currentDate)
        val date = dateFormat.parse(xDate)
        if (date.after(dbDate) && date.compareTo(dbDate) != 0) {
            updateDailyBonusDate(xDate)
            binding.bonusImage.visibility = View.GONE
            binding.lottie.visibility = View.VISIBLE
            binding.lottie.playAnimation()
            binding.noBonus.visibility = View.GONE

        } else {
            binding.noBonus.visibility = View.VISIBLE
        }
    }

    private fun updateDailyBonusDate(xDate: String) {
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .update("dailyBonus", xDate)
            .addOnSuccessListener {
                db.collection("users")
                    .document(auth.currentUser?.uid.toString())
                    .update("userCoin", FieldValue.increment(100))
                    .addOnSuccessListener {
                        val transaction = Transaction(
                            title = "Big Bonus",
                            time = Calendar.getInstance().time.toString(),
                            coin = "100"
                        )
                        db.collection("users")
                            .document(auth.currentUser?.uid.toString())
                            .collection("transaction")
                            .document(UUID.randomUUID().toString())
                            .set(transaction)
                        getUserData()
                    }
            }
    }

    private fun getUserData() {
        val user = auth.currentUser
        db.collection("users")
            .document(user?.uid.toString())
            .get()
            .addOnSuccessListener {
                if (it != null) {

                    Log.d(TAG, "getUserData: ${it.data}")
                    dailyBonusDate = it.getString("dailyBonus").toString()
                    binding.pb.visibility = View.GONE
                    binding.addBonusBtn.visibility = View.VISIBLE

                }
            }.addOnFailureListener { e ->
                Log.e(TAG, "Error writing document", e)
            }
    }

    override fun showRewardedVideo(view: View?) {
        val rewardedVideo = StartAppAd(this)
        rewardedVideo.setVideoListener {
            Toast.makeText(applicationContext,
                "Grant the reward to user",
                Toast.LENGTH_SHORT).show()

            checkDailyBonus()
        }
        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
                rewardedVideo.showAd()
            }

            override fun onFailedToReceiveAd(ad: Ad) {
                Toast.makeText(applicationContext, "Can't show rewarded video", Toast.LENGTH_SHORT)
                    .show()
                binding.addBonusBtn.isClickable = true
            }
        })
    }

    override fun onStart() {
        super.onStart()
        getUserData()

    }

}