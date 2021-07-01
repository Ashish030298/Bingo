package com.scarach.spin_earn_money.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scarach.spin_earn_money.*
import com.scarach.spin_earn_money.databinding.FragmentRedeemBinding

class RedeemFragment : Fragment() {
   private lateinit var binding : FragmentRedeemBinding
   private lateinit var db : FirebaseFirestore
   private lateinit var auth : FirebaseAuth
    private lateinit var withdrawalList: ArrayList<Withdrawal>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRedeemBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        withdrawalList = ArrayList()
        withdrawalList.clear()
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("withdraw")
            .orderBy("timeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(25)
            .get()
            .addOnSuccessListener {
                for (snapshot in it) {
                    val withdrawal = snapshot.toObject(Withdrawal::class.java)
                    withdrawalList.add(withdrawal)
                }
                val adapter = WithdrawAdapter(activity as Activity, withdrawalList)
                binding.withdrawRecyclerview.adapter = adapter
                binding.withdrawRecyclerview.layoutManager = LinearLayoutManager(context)

            }

        return binding.root
    }

    companion object {

    }
}