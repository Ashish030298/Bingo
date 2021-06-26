package com.scarach.spin_earn_money

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FieldValue
import com.scarach.spin_earn_money.SpinWheel.model.LuckyItem
import com.scarach.spin_earn_money.databinding.ActivitySpinBinding
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import java.util.*
import kotlin.collections.ArrayList


class SpinActivity : CoreBaseActivity() {
    private lateinit var binding: ActivitySpinBinding
    private lateinit var timer: CountDownTimer
    private var isTimerFinished: Boolean = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWheelItem()
        checkDailySpin()
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.spin.setOnClickListener {
            showAdInterstitial()
            spinCount()
        }
        // }
        binding.wheelview.setLuckyRoundItemSelectedListener { index -> updateCoins(index) }

    }

    private fun showAdInterstitial() {
        startAppAd.showAd(object : AdDisplayListener {
            override fun adHidden(ad: Ad) {}
            override fun adDisplayed(ad: Ad) {}
            override fun adClicked(ad: Ad) {
                Log.d("TAG", "adClicked: ${ad.type}")
                isTimerFinished = true
                if (AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_SPIN, 0) == getSpin) {
                    timer = object : CountDownTimer(60000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {

                        }

                        override fun onFinish() {
                            isTimerFinished = false
                            updateSpinCoin(25, "Win Spin Bonus")
                        }
                    }.start()
                }

            }

            override fun adNotDisplayed(ad: Ad) {}
        })
    }

    private fun updateSpinCoin(cash: Int, title: String) {
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .update("userCoin", FieldValue.increment(cash.toLong()))
            .addOnSuccessListener {
                db.collection("users")
                    .document(auth.currentUser?.uid.toString())
                    .update("userCoin", FieldValue.increment(100))
                    .addOnSuccessListener {
                        val transaction = Transaction(
                            title = title,
                            time = Calendar.getInstance().time.toString(),
                            coin = cash.toString()
                        )
                        db.collection("users")
                            .document(auth.currentUser?.uid.toString())
                            .collection("transaction")
                            .document(UUID.randomUUID().toString())
                            .set(transaction)

                    }
            }
    }

    private fun spinCount() {
        var totalSpin = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_SPIN, 0)
        when {
            totalSpin < getSpin -> {
                totalSpin++
                AppPreferences.putInt(AppPreferences.PrefKeys.TOTAL_SPIN, totalSpin)
                binding.showTime.text = "Total Spin: $totalSpin/$getSpin"
                val r = Random()
                val randomNumber: Int = r.nextInt(8)
                binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber)
                binding.spin.isClickable = false
            }
            totalSpin == getSpin -> {
                binding.spin.isClickable = false
                Toast.makeText(this,
                    "Click ad and wait 1 min. & earn Upto 50 coin ",
                    Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Today Spin is Complete", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("NewApi")
    fun checkDailySpin() {
        AppPreferences.initialize(this)
        val dbDate: Date
        val defaultSpinDate = "16/06/2021"
        AppPreferences.putString(AppPreferences.PrefKeys.SPIN_DATE, defaultSpinDate)
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        dbDate = if (!AppPreferences.hasKey(AppPreferences.PrefKeys.SPIN_DATE)) {
            dateFormat.parse(defaultSpinDate)
        } else {
            dateFormat.parse(AppPreferences.getString(AppPreferences.PrefKeys.SPIN_DATE))
        }
        val xDate = dateFormat.format(currentDate)
        val date = dateFormat.parse(xDate)
        if (date.after(dbDate) && date.compareTo(dbDate) != 0) {
            binding.showTime.text = "Total Spin: 0/$getSpin"
            AppPreferences.putString(AppPreferences.PrefKeys.SPIN_DATE, xDate)
            AppPreferences.putInt(AppPreferences.PrefKeys.TOTAL_SPIN, 0)
        } else {
            val totalSpin = AppPreferences.getInt(AppPreferences.PrefKeys.TOTAL_SPIN, 0)
            binding.showTime.text = "Total Spin: $totalSpin/$getSpin"

        }
    }

    private fun updateCoins(index: Int) {
        val random = Random()
        val cash = random.nextInt(10)
        when (index) {
            0 -> cash
            1 -> cash
            2 -> cash
            3 -> cash
            4 -> cash
            5 -> cash
            6 -> cash
            7 -> cash
        }

        Toast.makeText(this, "Awesome you win $cash", Toast.LENGTH_SHORT).show()
        binding.spin.isClickable = true
        updateSpinCoin(cash, "Win Spin Coin")

    }


    private val listOfWheelItem = ArrayList<LuckyItem>()

    private fun initWheelItem() {
        val item1 = LuckyItem()
        item1.topText = "\uD83C\uDF81"
        item1.secondaryText = "?"
        item1.color = Color.parseColor("#ffffff")
        item1.textColor = Color.parseColor("#000000")
        listOfWheelItem.add(item1)

        val item2 = LuckyItem()
        item2.topText = "\uD83C\uDF81"
        item2.secondaryText = "?"
        item2.color = Color.parseColor("#314169")
        item2.textColor = Color.parseColor("#ffffff")
        listOfWheelItem.add(item2)

        val item3 = LuckyItem()
        item3.topText = "\uD83C\uDF81"
        item3.secondaryText = "?"
        item3.color = Color.parseColor("#ffffff")
        item3.textColor = Color.parseColor("#000000")
        listOfWheelItem.add(item3)

        val item4 = LuckyItem()
        item4.topText = "\uD83C\uDF81"
        item4.secondaryText = "?"
        item4.color = Color.parseColor("#FF7F50")
        item4.textColor = Color.parseColor("#ffffff")
        listOfWheelItem.add(item4)

        val item5 = LuckyItem()
        item5.topText = "\uD83C\uDF81"
        item5.secondaryText = "?"
        item5.color = Color.parseColor("#ffffff")
        item5.textColor = Color.parseColor("#000000")
        listOfWheelItem.add(item5)

        val item6 = LuckyItem()
        item6.topText = "\uD83C\uDF81"
        item6.secondaryText = "?"
        item6.color = Color.parseColor("#314169")
        item6.textColor = Color.parseColor("#ffffff")
        listOfWheelItem.add(item6)

        val item7 = LuckyItem()
        item7.topText = "\uD83C\uDF81"
        item7.secondaryText = "?"
        item7.color = Color.parseColor("#ffffff")
        item7.textColor = Color.parseColor("#000000")
        listOfWheelItem.add(item7)

        val item8 = LuckyItem()
        item8.topText = "\uD83C\uDF81"
        item8.secondaryText = "?"
        item8.color = Color.parseColor("#FF7F50")
        item8.textColor = Color.parseColor("#ffffff")
        listOfWheelItem.add(item8)

        binding.wheelview.setData(listOfWheelItem)
        binding.wheelview.setRound(8)
    }

    override fun onBackPressed() {
        startAppAd.onBackPressed()
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        if (isTimerFinished) {
            timer.cancel()
            Toast.makeText(this, "Oops! you miss the Spin Bonus", Toast.LENGTH_SHORT).show()
        }
    }


}