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


import com.arief.filmmanagementapps.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.ads.rewarded.RewardedAd;

public class RegisterActivity extends AppCompatActivity {

//    {ADA IKLANNYA = REWARD dan BANNER}
    private RewardedAd mRewardedAd;


    //  Variable
    EditText emailReg, passwordReg;
    Button registerButton;
    ProgressBar progressBar;
    FirebaseAuth akunAuth;
    TextView loginButton;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        getSupportActionBar().hide();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//      BANNER ADS
        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
//      Test Ads Banner:
//      ca-app-pub-3940256099942544/6300978111
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

        //        Test Ads:
//        ca-app-pub-3940256099942544/5224354917
//      REWARD ADS
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


//      Mendapatkan Data Firebase Auth
        akunAuth = FirebaseAuth.getInstance();

//      Inisiasi ID
        emailReg = findViewById(R.id.email_register);
        passwordReg = findViewById(R.id.password_register);
        registerButton = findViewById(R.id.register_btn);
        loginButton = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progres_regis);
        progressBar.setVisibility(View.GONE);


//        Fungsi Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emptyInput() == true) {
                    String email = emailReg.getText().toString();
                    String password = passwordReg.getText().toString();
                    if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                        emailReg.setError("Email tidak boleh Kosong!");
                        passwordReg.setError("Password tidak boleh Kosong!");
                        Toast.makeText(RegisterActivity.this, "Email dan Password mohon di Isi!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(email)) {

                        emailReg.setError("Email tidak boleh Kosong!");
                        emailReg.requestFocus();
                        Toast.makeText(RegisterActivity.this, "Email mohon di Isi!", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(password)) {
                        passwordReg.setError("Password tidak boleh Kosong!");
                        passwordReg.requestFocus();
                        Toast.makeText(RegisterActivity.this, "Password mohon di Isi!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    createUser();
                    progressBar.setVisibility(View.VISIBLE);

                }

            }
        });

//      Fungsi Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    //    Check Input Form
    private boolean emptyInput() {
        boolean inputKosong = false;
        String email = emailReg.getText().toString();
        String password = passwordReg.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            inputKosong = true;

        }
        return inputKosong;
    }

    //    Buat User
    private void createUser() {
        String email = emailReg.getText().toString();
        String password = passwordReg.getText().toString();

        akunAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User Berhasil Registrasi", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    progressBar.setVisibility(View.GONE);
                    if (mRewardedAd != null) {
                        Activity activityContext = RegisterActivity.this;
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
                    Toast.makeText(RegisterActivity.this, "Registrasi Akun Gagal!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}