package com.scarach.spin_earn_money

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HomeActivity : AppCompatActivity() {

     private var home:ImageView? = null
     private var share:ImageView? = null
     private var wallet:ImageView? = null
     private var profile: ImageView? = null
    private var backBtn: ImageView? = null
    private var title: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        home = findViewById(R.id.action_home)
        share = findViewById(R.id.action_share)
        wallet = findViewById(R.id.action_wallet)
        profile = findViewById(R.id.action_profile)
        backBtn = findViewById(R.id.backBtn)
        title = findViewById(R.id.title_tv)


        clickListener()
        homeFragment()


    }

    private fun clickListener() {
       home?.setOnClickListener {
            normal()
           homeFragment()
       }

        share?.setOnClickListener {
            normal()
            share?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))

        }

        wallet?.setOnClickListener {
            normal()
            wallet?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
            val fragment = WalletFragment()
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                .replace(R.id.frameLayout, fragment, "profileFragment")
                .commit()
        }

        profile?.setOnClickListener {
            normal()
            profile?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
            val fragment = ProfileFragment()
            backBtn?.visibility = View.VISIBLE
            title?.text = "Profile"
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                    .replace(R.id.frameLayout, fragment, "profileFragment")
                    .commit()
        }

        backBtn?.setOnClickListener {
            normal()
            homeFragment()        }

    }

    private fun normal(){
        home?.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        share?.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        wallet?.setColorFilter(ContextCompat.getColor(this, R.color.gray))
        profile?.setColorFilter(ContextCompat.getColor(this, R.color.gray))
    }

    private fun homeFragment(){
        backBtn?.visibility = View.GONE
        title?.text = "Dashboard"
        val fragment = HomeFragment()
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                    .replace(R.id.frameLayout, fragment, "homeFragment")
                    .commit()
        home?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))


    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }

    }


}

class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {

    val totalPager = 2
    override fun getCount(): Int {
        return totalPager
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = TransactionFragment()
            1 -> fragment =RedeemFragment()
        }
        return fragment!!
    }
    override fun getPageTitle(position: Int): CharSequence {
        return "Tab " + (position + 1)
    }



}
