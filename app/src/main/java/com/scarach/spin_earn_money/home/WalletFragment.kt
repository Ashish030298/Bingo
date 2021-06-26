package com.scarach.spin_earn_money.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scarach.spin_earn_money.R
import com.scarach.spin_earn_money.TransactionFragment
import com.scarach.spin_earn_money.databinding.FragmentWalletBinding
import com.scarach.spin_earn_money.withdrwal.WithdrawalActivity
import com.startapp.sdk.adsbase.StartAppAd


class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding
    private lateinit var currentBalance: TextView
    private lateinit var redeemBtn: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private val TAG = "Wallet"
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentWalletBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val wallet = activity?.findViewById<ImageView>(R.id.action_wallet)
        wallet?.setColorFilter(ContextCompat.getColor(context as Activity, R.color.colorPrimary))
        currentBalance = binding.currentBalance
        redeemBtn = binding.redeem
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        binding.backBtn.setOnClickListener {
            (context as Activity).onBackPressed()
            StartAppAd.onBackPressed(context)
        }
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFrag(TransactionFragment(), "TRANSACTION")
        adapter.addFrag(RedeemFragment(), "REDEEM")

        viewPager.adapter = adapter


        binding.redeem.setOnClickListener {
            startActivity(Intent(context, WithdrawalActivity::class.java))
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab?.position!!
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }


        })
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        val user = auth.currentUser
        db.collection("users")
            .document(user?.uid.toString())
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    binding.currentBalance.text = "${it.getLong("userCoin")} COINS"

                    Log.d(TAG, "getUserData: ${it.data}")
                }
            }.addOnFailureListener { e ->
                Log.e(TAG, "Error writing document", e)
            }
    }


    override fun onStart() {
        super.onStart()
        getUserData()
    }


}