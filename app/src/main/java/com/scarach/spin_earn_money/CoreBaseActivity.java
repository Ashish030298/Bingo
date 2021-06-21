package com.scarach.spin_earn_money;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;

public class CoreBaseActivity extends AppCompatActivity {
    public static int scratch;
    public static int click;
    public static int spin;
    public static int impression;
    public static String ad_id = "205427187";
    public static String userName;
    public static String userEmail;
    public static int userCoin;
    public static String userImageUrl;
    public static String userDailyBonus;
    public Activity mActivity;
    public Context mContext;
    public Admin admin;
    public FirebaseFirestore db;
    public FirebaseAuth auth;
    public StartAppAd startAppAd;
    public boolean isStartAppLoad = false;
    public UserModel userData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = getApplicationContext();
        StartAppSDK.init(mContext, ad_id, false);
        StartAppAd.disableSplash();
        startAppAd = new StartAppAd(mContext);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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

    private void getData() {
        db.collection("Admin")
                .document("spinhub")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        admin = documentSnapshot.toObject(Admin.class);
                        if (admin != null) {
                            scratch = admin.getScratch();
                            spin = admin.getSpin();
                            click = admin.getClick();
                            impression = admin.getImpression();
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
        getUserData();
        getData();
        showInterstitialAd();
    }
}
