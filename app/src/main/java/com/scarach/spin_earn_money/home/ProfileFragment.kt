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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.scarach.spin_earn_money.MainActivity
import com.scarach.spin_earn_money.R
import com.scarach.spin_earn_money.databinding.FragmentProfileBinding
import com.startapp.sdk.adsbase.StartAppAd


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val TAG = "Profile"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val profile = activity?.findViewById<ImageView>(R.id.action_profile)
        profile?.setColorFilter(ContextCompat.getColor(context as Activity, R.color.colorPrimary))
        binding.backBtn.setOnClickListener {
            (context as Activity).onBackPressed()
            StartAppAd.onBackPressed(context)
        }

        binding.logout.setOnClickListener {
            auth.signOut()
            activity?.startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
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
                    binding.nameText.text = it.getString("userName")
                    binding.emailText.text = it.getString("userEmail")
                    val uri = it.getString("userImageUrl")
                    activity?.let { it1 ->
                        Glide
                            .with(it1)
                            .load(uri)
                            .centerCrop()
                            .placeholder(R.drawable.ic_man)
                            .into(binding.profileImg)
                    }
                    // binding.profileImg.setImageURI(Uri.parse(uri))
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