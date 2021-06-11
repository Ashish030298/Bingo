package com.scarach.spin_earn_money

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class WalletFragment : Fragment() {
    lateinit var currentBalance: TextView
    lateinit var redeemBtn: TextView
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewgroup = inflater.inflate(R.layout.fragment_wallet, container, false)
        initializeData(viewgroup)
        return viewgroup
    }

    private fun initializeData(viewgroup: View) {
        currentBalance = viewgroup.findViewById(R.id.currentBalance)
        redeemBtn = viewgroup.findViewById(R.id.redeem)
        tabLayout = viewgroup.findViewById(R.id.tabLayout)
        viewPager = viewgroup.findViewById(R.id.viewPager)

        tabLayout.addTab(tabLayout.newTab().setText("TRANSACTION"))
        tabLayout.addTab(tabLayout.newTab().setText("REDEEM"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = ViewPagerAdapter(fragmentManager)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab?.position!!
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }


        })

    }


}