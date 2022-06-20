package com.arief.filmmanagementapps.activity;

import static android.text.TextUtils.isEmpty;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arief.filmmanagementapps.R;
import com.arief.filmmanagementapps.data_film;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.google.android.gms.ads.rewarded.RewardedAd;

public class ActivityUpdate extends AppCompatActivity {

    //  { ADA IKLANNYA = Reward }
    private RewardedAd mRewardedAd;


    //    Variable
    TextView ouputTanggal;
    EditText Judul, Durasi, Distributor;
    Spinner Genre;
    RadioButton radioButton;
    CheckBox fhd,hd,qhd;
    RadioGroup Rating;
    Button Update, inputTanggal;
    ImageView Gambar;
    ProgressBar progressBar;

    String getJudul, getTanggal, getDurasi, getDistributor, getGenre, getRating, getGambar, getResolusi,cbox;

    //    Input Foto
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri fotoUrl;

    //   Firebase Connection
    DatabaseReference getReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_data);
        //        Get ID
        progressBar = findViewById(R.id.progres_update);
        progressBar.setVisibility(View.GONE);
        Judul = findViewById(R.id.new_input_judul);
        Durasi = findViewById(R.id.new_input_durasi);
        Distributor = findViewById(R.id.new_input_distributor);
        Genre = findViewById(R.id.new_input_genre);
        Rating = findViewById(R.id.new_opsi_rating);
        Update = findViewById(R.id.button_update);
        Gambar = findViewById(R.id.new_input_gambar);
        inputTanggal = findViewById(R.id.new_input_tanggal);
        ouputTanggal = findViewById(R.id.new_output_tanggal);
        qhd = (CheckBox) findViewById(R.id.new_qhd);
        hd = (CheckBox) findViewById(R.id.new_hd);
        fhd = (CheckBox) findViewById(R.id.new_fhd);


        fhd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbox = "1080p";
                qhd.setChecked(false);
                hd.setChecked(false);
            }
        });

        qhd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbox = "480p";
                fhd.setChecked(false);
                hd.setChecked(false);
            }
        });

        hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbox = "720p";
                fhd.setChecked(false);
                qhd.setChecked(false);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//        Test Ads:
//        ca-app-pub-3940256099942544/5224354917
//      REWARD ADS
        AdRequest adRequest = new AdRequest.Builder().build();
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



        //      Mendapatkan Data dari Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        getReference = database.getReference();

        getData();


// Input Tanggal
        inputTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TampilTanggal();
            }
        });

        Gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });
//        Input Gambar
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    fotoUrl = result.getData().getData();
                    Gambar.setImageURI(fotoUrl);
                    Log.e("Data Foto : ", String.valueOf(fotoUrl));

                    Update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressBar.setVisibility(View.VISIBLE);
                            StorageReference filePath = storage.getReference().child("ImagePost").child(fotoUrl.getLastPathSegment());
                            filePath.putFile(fotoUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> dowload = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            getGambar = task.getResult().toString();
                                            getJudul = Judul.getText().toString();
                                            getTanggal = ouputTanggal.getText().toString();
                                            getGenre = Genre.getSelectedItem().toString();
                                            getDurasi = Durasi.getText().toString();
                                            getDistributor = Distributor.getText().toString();
                                            int selectedRating = Rating.getCheckedRadioButtonId();
                                            radioButton = Rating.findViewById(selectedRating);
                                            getRating = (String) radioButton.getText();
                                            if (cbox == null){
                                                final String data_resolusi = getIntent().getExtras().getString("dataResolusi");
                                                getResolusi= data_resolusi;
                                            }else{
                                                getResolusi = cbox;
                                            }

                                            if (checkInput() == true){
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
                                            }else{

//                                             Proses Update
                                                data_film setFilm = new data_film();
                                                setFilm.setImage(getGambar);
                                                setFilm.setJudul(getJudul);
                                                setFilm.setTanggal(getTanggal);
                                                setFilm.setGenre(getGenre);
                                                setFilm.setDurasi(getDurasi);
                                                setFilm.setDistributor(getDistributor);
                                                setFilm.setRating(getRating);
                                                setFilm.setResolusi(getResolusi);
                                                updateDataFilm(setFilm);
                                            }



                                        }
                                    });
                                }
                            });
                        }
                    });

                }
            }
        });

//        Tanpa Input Gambar
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getJudul = Judul.getText().toString();
                getTanggal = ouputTanggal.getText().toString();
                getGenre = Genre.getSelectedItem().toString();
                if (cbox == null){
                    final String data_resolusi = getIntent().getExtras().getString("dataResolusi");
                    getResolusi= data_resolusi;
                }else{
                    getResolusi = cbox;
                }
                getDurasi = Durasi.getText().toString();
                getDistributor = Distributor.getText().toString();
                int selectedRating = Rating.getCheckedRadioButtonId();
                radioButton = Rating.findViewById(selectedRating);
                getRating = (String) radioButton.getText();
                if (checkInput() == true){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
                }else{
//              Proses Update
                data_film setFilm = new data_film();
                setFilm.setImage(getGambar);
                setFilm.setJudul(getJudul);
                setFilm.setTanggal(getTanggal);
                setFilm.setGenre(getGenre);
                setFilm.setResolusi(getResolusi);
                setFilm.setDurasi(getDurasi);
                setFilm.setDistributor(getDistributor);
                setFilm.setRating(getRating);
                final String getImage = getIntent().getExtras().getString("dataImage");
                setFilm.setImage(getImage);
                updateDataFilm(setFilm);}
            }
        });

    }

    private boolean checkInput() {
        boolean emtpyInput = false;
        if (isEmpty(getJudul) ||isEmpty(getTanggal)||isEmpty(getGenre)
                ||isEmpty(getDurasi) |isEmpty(getDistributor)
                ||isEmpty(getRating)){
            emtpyInput = true;
        }
        return emtpyInput;
    }

    private void updateDataFilm(data_film setFilm) {
        String getKey = getIntent().getExtras().getString("dataKey");
        getReference.child("Admin")
                .child("DataFilm")
                .child(getKey)
                .setValue(setFilm)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Gambar.setImageResource(R.mipmap.mv);
                        Judul.setText("");
                        ouputTanggal.setText("01 - 01 - 2022");
                        Durasi.setText("");
                        Distributor.setText("");
                        if (mRewardedAd != null) {
                            Activity activityContext = ActivityUpdate.this;
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

                        Toast.makeText(ActivityUpdate.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        finish();
                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    private void getData() {
        final String getImage = getIntent().getExtras().getString("dataImage");
        final String getResolusi = getIntent().getExtras().getString("dataResolusi");
        final String getJudul = getIntent().getExtras().getString("dataJudul");
        final String getTanggal = getIntent().getExtras().getString("dataTanggal");
        final String getDurasi = getIntent().getExtras().getString("dataDurasi");
        final String getGenre = getIntent().getExtras().getString("dataGenre");
        final String getDistributor = getIntent().getExtras().getString("dataDistributor");
        final String getRating = getIntent().getExtras().getString("dataRating");

        Picasso.get().load(getImage).into(Gambar);
        Judul.setText(getJudul);
        ouputTanggal.setText(getTanggal);
        Genre.setSelection(((ArrayAdapter<String>) Genre.getAdapter()).getPosition(getGenre));
        Durasi.setText(getDurasi);
        Distributor.setText(getDistributor);
        if (getRating.equalsIgnoreCase("Semua Umur")) {
            Rating.check(R.id.new_rating_su);
        } else if (getRating.equalsIgnoreCase("Remaja")) {
            Rating.check(R.id.new_rating_r);
        } else if (getRating.equalsIgnoreCase("Dewasa")) {
            Rating.check(R.id.new_rating_dewasa);
        }

        if (getResolusi.equalsIgnoreCase("1080p")) {
            fhd.setChecked(true);
            hd.setChecked(false);
            qhd.setChecked(false);
        } else if (getResolusi.equalsIgnoreCase("720p")) {
            hd.setChecked(true);
            fhd.setChecked(false);
            qhd.setChecked(false);
        } else if (getResolusi.equalsIgnoreCase("480p")) {
            qhd.setChecked(true);
            hd.setChecked(false);
            fhd.setChecked(false);
        }

    }

    private void TampilTanggal() {
        InputTanggal inputTanggal = new InputTanggal();
        inputTanggal.show(getSupportFragmentManager(), "data");
        inputTanggal.setOnDateClickListener(new InputTanggal.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String tahun = "" + datePicker.getYear();
                String bulan = "" + (datePicker.getMonth() + 1);
                String hari = "" + datePicker.getDayOfMonth();
                String text = hari + " - " + bulan + " - " + tahun;
                ouputTanggal.setText(text);
            }
        });

    }
}

