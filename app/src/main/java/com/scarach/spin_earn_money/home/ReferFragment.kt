package com.scarach.spin_earn_money.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.scarach.spin_earn_money.CoreBaseActivity
import com.scarach.spin_earn_money.CoreBaseActivity.userName
import com.scarach.spin_earn_money.CoreBaseActivity.userReferId
import com.scarach.spin_earn_money.R
import com.scarach.spin_earn_money.Transaction
import com.scarach.spin_earn_money.UserModel
import com.scarach.spin_earn_money.databinding.FragmentReferBinding
import com.startapp.sdk.adsbase.StartAppAd
import java.util.*


class ReferFragment : Fragment() {
    private lateinit var binding: FragmentReferBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userDetails: UserModel
    private val TAG = "ReferFdragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentReferBinding.inflate(inflater, container, false)
        binding.iHaveReferCode.text = Html.fromHtml("<u><strong>I have Refer code</strong></u>")
        binding.iHaveReferCode.setOnClickListener {
            if (CoreBaseActivity.joinReferId == "") {
                binding.referCodeEd.visibility = View.VISIBLE
            } else {
                Toast.makeText(context,"You all ready collect", Toast.LENGTH_SHORT).show()
            }
        }

        binding.referCodeEd.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if( s?.length == 6){
                    if (!binding.referCodeEd.text.equals(userReferId)){
                        referProcess()
                    }else{
                        Toast.makeText(context as Activity, "It's your code!!",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        val refer = activity?.findViewById<ImageView>(
            R.id.action_share)
        refer?.setColorFilter(ContextCompat.getColor(context as Activity, R.color.colorPrimary))
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        binding.backBtn.setOnClickListener {
            (context as Activity).onBackPressed()
            StartAppAd.onBackPressed(context)
        }

        binding.referBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareIntent.type = "text/plain"
            val text = "Download ${getString(R.string.app_name)} app to earn paytm cash,money !" +
                    "\nReferrer Code -${userDetails.userReferId}\nhttps://play.google.com/store/apps/details?id=${context?.packageName}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(shareIntent)
        }

        binding.copyReferId.setOnLongClickListener {
            (activity as HomeActivity).copyToClipboard(binding.setReferTv.text)
            true
        }

        return binding.root
    }

    private fun getUserData() {
        val user = auth.currentUser
        db.collection("users")
            .document(user?.uid.toString())
            .get()
            .addOnSuccessListener {
                if (it != null) {
                    userDetails = it.toObject(UserModel::class.java)!!
                    binding.setReferTv.text = userDetails.userReferId
                    binding.pb.visibility = View.GONE
                    binding.setReferTv.visibility = View.VISIBLE

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

    fun referProcess(){
        db.collection("users")
            .whereEqualTo("userReferId", binding.referCodeEd.text.toString().trim())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                        binding.referCodeEd.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(context as Activity ,R.drawable.ic_baseline_done_all_24), null)
                        db.collection("users")
                            .document(document.id)
                            .update("userCoin", FieldValue.increment(500))
                            .addOnSuccessListener {
                                val transaction = Transaction(
                                    title = "$userName use your refer code",
                                    time = Calendar.getInstance().timeInMillis.toString(),
                                    coin = "500"
                                )
                                db.collection("users")
                                    .document(document.id)
                                    .collection("transaction")
                                    .document(UUID.randomUUID().toString())
                                    .set(transaction)
                                    .addOnSuccessListener {
                                        db.collection("users")
                                            .document(auth.currentUser?.uid.toString())
                                            .update("userCoin",FieldValue.increment(250))
                                            .addOnSuccessListener {
                                                val transaction = Transaction(
                                                    title = "Refer code bonus",
                                                    time = Calendar.getInstance().timeInMillis.toString(),
                                                    coin = "250"
                                                )
                                                db.collection("users")
                                                    .document(auth.currentUser?.uid.toString())
                                                    .collection("transaction")
                                                    .document(UUID.randomUUID().toString())
                                                    .set(transaction)
                                            }
                                    }

                            }

                    }
                } else {
                    Log.d(TAG, "Refer code is invalid", task.exception)
                    binding.referCodeEd.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(context as Activity ,R.drawable.ic_baseline_close_24), null)

                }
            }

    }

    companion object
}