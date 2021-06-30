package com.scarach.spin_earn_money;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.scarach.spin_earn_money.home.HomeFragment;
import com.startapp.sdk.ads.nativead.NativeAdDetails;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CoreBaseActivity extends AppCompatActivity implements MaxAdListener {
    public static int getScratch;
    public static int getTaskOneClick;
    public static int getTaskTwoClick;
    public static int getSpin;
    public static int getTaskOneImpression;
    public static int getApplovinTaskCoin;
    public static int getStartAppTaskCoin;
    public static int getTaskTwoImpression;
    public static String ad_id = "205427187";
    public static String userName;
    public static String userEmail;
    public static boolean isUserFirstTimeWithdrawal;
    public static int userCoin;
    public static String userImageUrl;
    public static String userDailyBonus;
    public static Activity mActivity;
    public static Context mContext;
    public Admin admin;
    public FirebaseFirestore db;
    public FirebaseAuth auth;
    public StartAppAd startAppAd;
    public boolean isStartAppLoad = false;
    public UserModel userData;
    public static List<NativeAdDetails> nativeAdList;
    public ArrayList<AdminWithdrawal> withdrawalArrayList;
    public String IP_Address;
    public String IP_Address_Start_link = "http://proxycheck.io/v2/";
    public String IP_Address_end_link = "&vpn=1&asn=1&risk=1&port=1&seen=1&days=7&tag=msg";
    public String IP_key_query = "?key=";
    public String IP_Address_API_key = "50k108-05v179-19pl8u-2391d5";
    private String TAG = "CoreBaseActivity";
    public String Check_IP_Address_Link = "https://iplist.cc/api";
    private MaxInterstitialAd interstitialAd;
    public static int maxWithdraw;
    public static int minimumWithdraw;
    public static String joinReferId;
    public static String userReferId;


    public void setNativeAd(@Nullable List<NativeAdDetails> nativeAd) {
        this.nativeAdList = nativeAd;


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppAd.disableSplash();
        mActivity = this;
        mContext = getApplicationContext();
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
            }
        });
       // showApplovin();
        startAppAd = new StartAppAd(mContext);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        AppPreferences.Companion.initialize(mContext);
        withdrawalArrayList = new ArrayList();

    }

    public void showInterstitialAd() {
        startAppAd.loadAd(new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {

            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                isStartAppLoad = false;

            }

        });
    }

    private void getAdminData() {
        db.collection("Admin")
                .document("spinhub")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        admin = documentSnapshot.toObject(Admin.class);
                        if (admin != null) {
                            getTaskTwoClick = admin.getApplovinClick();
                            getTaskTwoImpression = admin.getApplovinImpression();
                            getScratch = admin.getScratch();
                            getSpin = admin.getSpin();
                            getTaskOneClick = admin.getStartAppClick();
                            getTaskOneImpression = admin.getStartAppImpression();
                            getApplovinTaskCoin = admin.getApplovinTaskCoin();
                            getStartAppTaskCoin = admin.getStartAppTaskCoin();
                            maxWithdraw = admin.getMaxWithdraw();
                            minimumWithdraw = admin.getMinimumWithdraw();
                        }
                    }
                });
    }

    private void getUserData() {
        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userData = documentSnapshot.toObject(UserModel.class);
                        userName = userData.getUserName();
                        userEmail = userData.getUserEmail();
                        userCoin = userData.getUserCoin();
                        userImageUrl = userData.getUserImageUrl();
                        userDailyBonus = userData.getDailyBonus();
                        joinReferId = userData.getJoinReferId();
                        userReferId = userData.getUserReferId();
                        isUserFirstTimeWithdrawal = userData.isOneTimeWithdrawal();
                    }
                });
    }

    public void showRewardedVideo(View view) {
        final StartAppAd rewardedVideo = new StartAppAd(this);

        rewardedVideo.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                Toast.makeText(getApplicationContext(), "Grant the reward to user", Toast.LENGTH_SHORT).show();
            }
        });

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                rewardedVideo.showAd();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                Toast.makeText(getApplicationContext(), "Can't show rewarded video", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            getUserData();
            getAdminData();
            showInterstitialAd();
            loadNativeAd();
            //getIpAddress();
            checkProxy();
            testing();
        }
    }

    private void loadNativeAd(){
        final StartAppNativeAd nativeAd = new StartAppNativeAd(mContext);
        try {
            nativeAd.loadAd(new NativeAdPreferences()
                    .setAdsNumber(1)
                    .setAutoBitmapDownload(true)
                    .setPrimaryImageSize(2), new AdEventListener() {
                @Override
                public void onReceiveAd(Ad ad) {
                    setNativeAd(nativeAd.getNativeAds());
                    Log.d("nativeAd", "onReceiveAd: "+ nativeAd.getNativeAds());
                    HomeFragment showNativeAd = new HomeFragment();
                    showNativeAd.setNativeAd();
                }

                @Override
                public void onFailedToReceiveAd(Ad ad) {
                    Log.v("TAG", "onFailedToReceiveAd: " + ad.getErrorMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getIpAddress(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlIp = "http://checkip.amazonaws.com/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlIp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                IP_Address = response;
                //checkProxy(response.replace("\n","").trim());
                Log.d(TAG, "onResponse: "+IP_Address);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                IP_Address ="didn't work";
                Log.d(TAG, "onResponse: "+error.getMessage());

            }
        });

        queue.add(stringRequest);
    }

    void checkProxy(){
//        String url = IP_Address_Start_link+ip+IP_key_query+IP_Address_end_link;
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Check_IP_Address_Link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    /*JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, "onResponse: "+jsonObject.getString("status"));
                    if (jsonObject.getString("status").equals("ok")) {
                        JSONObject object = jsonObject.getJSONObject(ip);
                        String country = object.getString("country");
                        if (country.equals("Bangladesh")) {
                            dialog();
                        } else if (country.equals("United States") || country.equals("India")) {

                        }
                    }else {

                    }*/
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("countryname").equals("India") || jsonObject.getString("countryname").equals("United States")
                    || jsonObject.getString("countrycode").equals("US")){
                        Log.d(TAG, "onResponse: "+jsonObject.getString("countryname"));

                    }else {
                        ViewDialog alert = new ViewDialog();
                        alert.showDialog(mActivity, "Please Connect to US VPN!!\nand Start work.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: "+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Anything you want
                Log.d(TAG, "onErrorResponse: "+ error.getMessage());
            }
        });
        queue.add(stringRequest);

    }

    private void showApplovin(){
        interstitialAd = new MaxInterstitialAd("274d1164d0b4a1f8", this);
        interstitialAd.setListener(this);

        // Load the first ad

        // Load the first ad
        interstitialAd.loadAd();
    }


    @Override
    public void onAdLoaded(MaxAd ad) {
        interstitialAd.showAd();
    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
        interstitialAd.loadAd();
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        interstitialAd.loadAd();
    }

    public void testing() {
        db.collection("users")
                .whereEqualTo("userReferId", "430157s")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d(TAG, "Failure getting documents: "+ e.getMessage());

            }
        });

    }

    public void transaction(String title, String time, String coin){
        Transaction transaction = new Transaction(coin,time, title);
        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                    .collection("transaction")
                .document(UUID.randomUUID().toString())
                .set(transaction);
    }
}
