package com.scarach.spin_earn_money.withdrwal

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.scarach.spin_earn_money.AdminWithdrawal
import com.scarach.spin_earn_money.CoreBaseActivity
import com.scarach.spin_earn_money.databinding.ActivityWithdrawalBinding

class WithdrawalActivity : CoreBaseActivity() {
    private lateinit var binding: ActivityWithdrawalBinding
    private var withdrawalList:ArrayList<AdminWithdrawal> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        withdrawalList = withdrawalArrayList
//        val adapter = WithdrawalAdapter(this, withdrawalArrayList = withdrawalArrayList)
//        binding.withdrawalRv.adapter = adapter
//        binding.withdrawalRv.layoutManager = LinearLayoutManager(this)
        getAdminWithdrawalData()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }



    }

    fun getAdminWithdrawalData() {
        db.collection("Admin")
            .document("withdrawal")
            .collection("paytmType")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    for (documentSnapshot in queryDocumentSnapshots) {
                        val withdrawal = documentSnapshot.toObject(
                            AdminWithdrawal::class.java)
                        withdrawalList.add(withdrawal)
                        val adapter = WithdrawalAdapter(this, withdrawalArrayList = withdrawalList)
                        binding.withdrawalRv.adapter = adapter
                        binding.withdrawalRv.layoutManager = LinearLayoutManager(this)
                    }
                }
            }
    }
}