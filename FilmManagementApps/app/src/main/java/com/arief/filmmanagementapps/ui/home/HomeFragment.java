package com.arief.filmmanagementapps.ui.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arief.filmmanagementapps.ActivityUpdate;
import com.arief.filmmanagementapps.activity.ListDataGuest;
import com.arief.filmmanagementapps.activity.LoginActivity;
import com.arief.filmmanagementapps.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class HomeFragment extends Fragment {

//  {ADA IKLANNYA = BANNER DAN INTERESTIAL}
    private InterstitialAd mInterstitialAd;

    //    Variable
    Button logOut;
    FirebaseAuth akunAuth;
    TextView dataEmailLogin;
    private AdView mAdView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

//      BANNER ADS
        mAdView = view.findViewById(R.id.adView5);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
//        Test Ads Banner:
//        ca-app-pub-3940256099942544/6300978111
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
//        ca-app-pub-3940256099942544/1033173712
//      INTERESTIAL ADS
        InterstitialAd.load(getContext(),"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(getTag(), "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(getTag(), loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

        if (mInterstitialAd != null) {
            mInterstitialAd.show(getActivity());
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

//        Mendapatkan data dari Firebase Auth
        akunAuth = FirebaseAuth.getInstance();

//        Inisiasi ID

        logOut = view.findViewById(R.id.log_out);
        dataEmailLogin = view.findViewById(R.id.emailLogin);




//        Mendapatkan Data Email yang sudah Login
        if (akunAuth.getCurrentUser() != null) {
            dataEmailLogin.setText(akunAuth.getCurrentUser().getEmail());

        }

//        Fungsi Logout Button
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutButton();

            }
        });

        return view;
    }
    //    Sign Out
    private void logOutButton() {
        akunAuth.signOut();

        startActivity(new Intent(getContext(), LoginActivity.class));
    }
}



