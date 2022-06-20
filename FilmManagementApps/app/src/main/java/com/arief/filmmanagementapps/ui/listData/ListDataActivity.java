package com.arief.filmmanagementapps.ui.listData;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arief.filmmanagementapps.R;
import com.arief.filmmanagementapps.activity.ListDataGuest;
import com.arief.filmmanagementapps.adapter.RecyclerViewAdapter;
import com.arief.filmmanagementapps.data_film;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListDataActivity extends Fragment implements RecyclerViewAdapter.dataListener {

//    {ADA IKLANNYA = BANNER}
private AdView mAdView;
    //  Mendapatkan Data dari Adapter RecicleView

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //    Firebase
    private DatabaseReference reference;
    private ArrayList<data_film> dataFilm;
    private Context context;

    //    Search
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_data,container,false);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//      BANNER ADS
        mAdView = view.findViewById(R.id.adView3);
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



        //      Inisiasi ID
        recyclerView = view.findViewById(R.id.datalist);
        searchView = view.findViewById(R.id.etSearch);

        getData();
        MyRecyclerView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    if (TextUtils.isEmpty(newText)) {
                        getData();
                    } else {
                        getDataSearch(newText);
                    }
                    return true;
                }
            });
        }

        return view;
    }

    //    Ketika Posisi IDle
    private void getData() {
        Toast.makeText(context, "Sedang Memuat Data...", Toast.LENGTH_LONG).show();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("DataFilm").orderByChild("tanggal")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Inisialisasi Arraylist
                        dataFilm = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.exists()) {
                                data_film film = snapshot.getValue(data_film.class);
//                            Mengambil Primary Key untuk Update dan Delete
                                film.setKey(snapshot.getKey());
                                dataFilm.add(film);

                            }
                        }
//                        Inisiasi adapter dan data mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(dataFilm, context, ListDataActivity.this);

//                        Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(context, "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails() + "" + databaseError.getMessage());
                    }

                });
    }


    //  Get Data pada saat searchView dimasukan String
    private void getDataSearch(String data) {
        //        Get 'Referensi DataBase
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Admin").child("DataFilm").orderByChild("judul").startAt(data).endAt(data)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Inisialisasi Arraylist
                        dataFilm = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.exists()) {
                                data_film film = snapshot.getValue(data_film.class);
//                            Mengambil Primary Key untuk Update dan Delete
                                film.setKey(snapshot.getKey());
                                dataFilm.add(film);
                            }

                        }

//                        Inisiasi adapter dan data mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(dataFilm, context, ListDataActivity.this);

//                        Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(context, "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails() + "" + databaseError.getMessage());
                    }
                });
    }

    private void MyRecyclerView() {
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


//        Membuat Underline pada setiap item didalam list
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDeleteData(data_film data, int position) {
        if (reference != null) {
            reference.child("Admin")
                    .child("DataFilm")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
