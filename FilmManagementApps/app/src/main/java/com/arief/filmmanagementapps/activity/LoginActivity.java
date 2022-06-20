package com.arief.filmmanagementapps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.arief.filmmanagementapps.MainActivity;
import com.arief.filmmanagementapps.R;
import com.arief.filmmanagementapps.ui.home.HomeFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.gms.ads.rewarded.RewardedAd;

public class LoginActivity extends AppCompatActivity {

//  { ADA IKLANNYA = REWARD DAN BANNER  }
    private RewardedAd mRewardedAd;

    //  Variable
    EditText emailLog, passwordLog;
    Button loginButton, guestMode;
    TextView registerButton;
    FirebaseAuth akunAuth;
    ProgressBar progressBar;
    private AdView mAdView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdView adView = new AdView(this);
//      Test Ads Banner:
//      ca-app-pub-3940256099942544/6300978111
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        //        AdRequest adRequest = new AdRequest.Builder().build();
//        Test Ads:
//        ca-app-pub-3940256099942544/5224354917
        RewardedAd.load(this,"ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
//                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
//                        Log.d(TAG, "Ad was loaded.");
                    }
                });


//        Mendapatkan data Firebase Auth
        akunAuth = FirebaseAuth.getInstance();

//        Insiasi ID
        guestMode = findViewById(R.id.guest);
        emailLog = findViewById(R.id.email_login);
        passwordLog = findViewById(R.id.password_login);
        registerButton = findViewById(R.id.register);
        loginButton = findViewById(R.id.login);
        progressBar = findViewById(R.id.progres_login);
        progressBar.setVisibility(View.GONE);

        guestMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), ListDataGuest.class));

            }
        });

        //     Fungsi Button Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emptyInput() == true) {
                    String email = emailLog.getText().toString();
                    String password = passwordLog.getText().toString();
                    if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                        emailLog.setError("Email tidak boleh Kosong!");
                        passwordLog.setError("Password tidak boleh Kosong!");
                        Toast.makeText(LoginActivity.this, "Email dan Password mohon di Isi!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(email)) {

                        emailLog.setError("Email tidak boleh Kosong!");
                        emailLog.requestFocus();
                        Toast.makeText(LoginActivity.this, "Email mohon di Isi!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(password)) {
                        passwordLog.setError("Password tidak boleh Kosong!");
                        passwordLog.requestFocus();
                        Toast.makeText(LoginActivity.this, "Password mohon di Isi!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser();
                }
            }
        });

//        Fungsi Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    //    Check Input Form
    private boolean emptyInput() {
        boolean inputKosong = false;
        String email = emailLog.getText().toString();
        String password = passwordLog.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            inputKosong = true;

        }
        return inputKosong;
    }

    //    Proses Login
    private void loginUser() {
//        Get data dari Form Box
        String email = emailLog.getText().toString();
        String password = passwordLog.getText().toString();

        akunAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "User Berhasil Login", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("dataEmail", email);
                    Intent intent = new Intent(LoginActivity.this, HomeFragment.class);
                    intent.putExtras(bundle);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    if (mRewardedAd != null) {
                    Activity activityContext = LoginActivity.this;
                        mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                // Handle the reward.
//                                    Log.d(TAG, "The user earned the reward.");
                                int rewardAmount = rewardItem.getAmount();
                                String rewardType = rewardItem.getType();
                            }
                        });
                    } else {
//                            Log.d(TAG, "The rewarded ad wasn't ready yet.");
                    }
                }
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Gagal!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


    }

    // Keluar Aplikasi
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }
}