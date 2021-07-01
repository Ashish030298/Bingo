package com.scarach.spin_earn_money.withdrwal

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.scarach.spin_earn_money.*
import com.scarach.spin_earn_money.databinding.WithdrawalItemBinding
import java.util.*
import kotlin.collections.ArrayList


class WithdrawalAdapter(val context: Context, val withdrawalArrayList:ArrayList<AdminWithdrawal>):
    RecyclerView.Adapter<WithdrawalAdapter.WithdrawalViewHolder>() {
    class WithdrawalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = WithdrawalItemBinding.bind(itemView)
        val img = binding.withdrawalImg
        val tv = binding.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WithdrawalViewHolder {
        return WithdrawalViewHolder(LayoutInflater.from(context).inflate(R.layout.withdrawal_item, parent, false))
    }

    override fun onBindViewHolder(holder: WithdrawalViewHolder, position: Int) {
       val listData = withdrawalArrayList[position]
        val uri:Uri = Uri.parse(listData.imgurl)
        Glide.with(context)
            .load(listData.imgurl)
            .into(holder.img)
        //holder.img.setImageURI(uri)
        holder.tv.text = "${listData.name}\n minimum withdrawal:0.5$ = ${listData.minimum}"

        holder.itemView.setOnClickListener {
            if (CoreBaseActivity.isUserFirstTimeWithdrawal){
                paymentDetails(listData.name,listData.max)
            }else {
                paymentDetails(listData.name, listData.minimum)
            }
        }

    }

    override fun getItemCount(): Int {
       return withdrawalArrayList.size
    }


    private fun paymentDetails(name: String, coin: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Withdraw by $name")
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.text_inpu_password, null)
        // Set up the input
        // Set up the input
        val input = viewInflated.findViewById<View>(R.id.input) as EditText
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated)

        // Set up the buttons

        // Set up the buttons
        builder.setPositiveButton("Submit",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
        // m_Text = input.text.toString()
                val withdrawal = Withdrawal(
                    userName = CoreBaseActivity.userName,
                    userEmail = CoreBaseActivity.userEmail,
                    withdrawalCoin = coin,
                    withdrawalType = name,
                    withdrawalAccount = input.text.toString(),
                    timeStamp = Calendar.getInstance().timeInMillis.toString()
                )

                val collection = UUID.randomUUID().toString()
                val db = FirebaseFirestore.getInstance()
                val auth = FirebaseAuth.getInstance()
                db.collection("users")
                    .document(auth.currentUser?.uid.toString())
                    .collection("withdraw")
                    .add(withdrawal)
                    .addOnSuccessListener {
                        db.collection("users")
                            .document(auth.currentUser?.uid.toString())
                            .update("userCoin", FieldValue.increment(-coin.toLong()))
                            .addOnSuccessListener {
                                val transaction = Transaction(
                                    title = "Withdraw",
                                    time = Calendar.getInstance().timeInMillis.toString(),
                                    coin = coin.toString()
                                )
                                db.collection("users")
                                    .document(auth.currentUser?.uid.toString())
                                    .collection("transaction")
                                    .document(UUID.randomUUID().toString())
                                    .set(transaction)
                                Toast.makeText(context,"Your withdrawal request is successfully submit",Toast.LENGTH_SHORT).show()
                                (context as Activity).finish()
                            }
                    }
            })
        builder.setNegativeButton(android.R.string.cancel,
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }



}


