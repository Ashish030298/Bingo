@file:Suppress("DEPRECATION")

package com.scarach.spin_earn_money.home

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scarach.spin_earn_money.CoreBaseActivity
import com.scarach.spin_earn_money.R
import com.startapp.sdk.ads.nativead.NativeAdDetails
import com.startapp.sdk.ads.nativead.NativeAdPreferences
import com.startapp.sdk.ads.nativead.StartAppNativeAd
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdEventListener
import java.lang.ref.WeakReference
import java.util.*


class HomeActivity : CoreBaseActivity() {

    private var home: ImageView? = null
    private var share: ImageView? = null
    private var wallet: ImageView? = null
    private var profile: ImageView? = null
    private var backBtn: ImageView? = null
    private var title: TextView? = null
    private val TAG = "HomeActivity"
    private var weakActivity = WeakReference(this@HomeActivity)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        home = findViewById(R.id.action_home)
        share = findViewById(R.id.action_share)
        wallet = findViewById(R.id.action_wallet)
        profile = findViewById(R.id.action_profile)
        backBtn = findViewById(R.id.backBtn)
        title = findViewById(R.id.title_tv)
        ad()
        //getUserData()
        clickListener()
        homeFragment()

    }

    private fun ad() {
        val startAppNativeAd = StartAppNativeAd(this)
        // Declare Native Ad Preferences
        // Declare Native Ad Preferences
        val nativePrefs = NativeAdPreferences()
            .setAdsNumber(3) // Load 3 Native Ads
            .setAutoBitmapDownload(true) // Retrieve Images object
            .setPrimaryImageSize(2) // 150x150 image


// Declare Ad Callbacks Listener

// Declare Ad Callbacks Listener
        val adListener: AdEventListener = object : AdEventListener {
            // Callback Listener
            override fun onReceiveAd(arg0: Ad) {
                // Native Ad received
                val ads: ArrayList<NativeAdDetails> = startAppNativeAd.nativeAds // get NativeAds list

                // Print all ads details to log
                val iterator: Iterator<*> = ads.iterator()
                while (iterator.hasNext()) {
                    Log.d("MyApplication", iterator.next().toString())
                }

                Log.d(TAG, "onReceiveAd: ${ads[1].title}")
            }

            override fun onFailedToReceiveAd(arg0: Ad) {
                // Native Ad failed to receive
                Log.e("MyApplication", "Error while loading Ad")
            }
        }

// Load Native Ads

// Load Native Ads
        startAppNativeAd.loadAd(nativePrefs, adListener)
    }

    fun copyToClipboard(text: CharSequence) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("refer_id", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copy to Clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun clickListener() {
        home?.setOnClickListener {
            normal()
            homeFragment()
        }

        share?.setOnClickListener {
            normal()
            share?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
            val fragment = ReferFragment()
            changeFragment(fragment, "ReferFragment")
            val tag = supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1
            ).name
            Log.d(TAG, "clickListener: $tag")
        }

        wallet?.setOnClickListener {
            normal()
            wallet?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
            val fragment = WalletFragment()
            changeFragment(fragment, "WalletFragment")
        }

        profile?.setOnClickListener {
            normal()
            profile?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
            val fragment = ProfileFragment()
            backBtn?.visibility = View.VISIBLE
            changeFragment(fragment, "ProfileFragment")
        }

        backBtn?.setOnClickListener {
            normal()
            homeFragment()
        }

    }


    private fun changeFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
            .replace(R.id.frameLayout, fragment, fragmentTag)
            .addToBackStack(fragmentTag)
            .commit()
    }

    fun normal() {
        home?.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        share?.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        wallet?.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        profile?.setColorFilter(ContextCompat.getColor(this, R.color.gray))
    }

    private fun homeFragment() {
        backBtn?.visibility = View.GONE
        title?.text = "Dashboard"
        val fragment = HomeFragment()
        changeFragment(fragment, "HomeFragment")
        home?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))


    }

    override fun onBackPressed() {
        val tag = supportFragmentManager.getBackStackEntryAt(
            supportFragmentManager.backStackEntryCount - 1
        ).name

        if (tag != null && tag == "HomeFragment") {
            finish()
        } else {
            normal()
            supportFragmentManager.popBackStack()

        }


    }

    fun getmInstanceActivity(): HomeActivity? {
        return weakActivity.get()
    }



}

class ViewPagerAdapter(manager: FragmentManager?) :
    FragmentStatePagerAdapter(manager!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }


    fun addFrag(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }




}
