package com.scarach.spin_earn_money.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scarach.spin_earn_money.R
import com.scarach.spin_earn_money.UserModel
import com.scarach.spin_earn_money.databinding.FragmentReferBinding
import com.startapp.sdk.adsbase.StartAppAd


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

    companion object
}