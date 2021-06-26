 package com.scarach.spin_earn_money

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scarach.spin_earn_money.databinding.FragmentTransactionBinding


class TransactionFragment : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var transactionList: ArrayList<Transaction>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        transactionList = ArrayList()
        transactionList.clear()
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .collection("transaction")
            .get().addOnSuccessListener {
                for (snapshot in it) {
                    val transaction = snapshot.toObject(Transaction::class.java)
                    transactionList.add(transaction)
                }
                val adapter = TransactionAdapter(activity as Activity, transactionList)
                binding.transactionRV.adapter = adapter
                binding.transactionRV.layoutManager = LinearLayoutManager(context)

            }
        return binding.root
    }

    companion object
}