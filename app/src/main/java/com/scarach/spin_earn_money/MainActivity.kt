package com.scarach.spin_earn_money

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.onesignal.OneSignal
import com.scarach.spin_earn_money.databinding.ActivityMainBinding
import com.scarach.spin_earn_money.home.HomeActivity
import java.util.*


class MainActivity : CoreBaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val TAG = "MainActivity"
    private val RC_SIGN_IN = 103
    private var isReferIdValid: Boolean = false
    private var documentId: String =""
    private val ONESIGNAL_APP_ID = "93e58b1b-86e6-45b3-9d3c-a1463dd9c92f"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        createGoogleEmail()

        binding.googleLogin.setOnClickListener {
            binding.pb.visibility = View.VISIBLE
            signIn()

        }







    }

    private fun createGoogleEmail() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val id = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
                    val referId = UUID.randomUUID().toString()
                    val user = auth.currentUser

                    val userModel = UserModel(
                        id = id,
                        userName = user?.displayName.toString(),
                        userEmail = user?.email.toString(),
                        userCoin = 0,
                        userImageUrl = user?.photoUrl.toString(),
                        userReferId = referId.substring(0, 6),
                        dailyBonus = "15/06/2021",
                        timeStamp = com.google.firebase.Timestamp.now()
                    )
                    db.collection("users")
                        .document(user?.uid.toString())
                        .set(userModel)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                            binding.pb.visibility = View.GONE
                            transaction("Refer code Bonus",
                                Calendar.getInstance().timeInMillis.toString(),
                                "250")
                            goToNewActivity()
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)

                }
            }
    }

    private fun goToNewActivity() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null) {
            goToNewActivity()

        }
    }





}