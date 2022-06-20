package com.arief.filmmanagementapps.ui.inputData;

import static android.app.Activity.RESULT_OK;
import static android.text.TextUtils.isEmpty;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;

import com.arief.filmmanagementapps.activity.InputTanggal;
import com.arief.filmmanagementapps.R;
import com.arief.filmmanagementapps.data_film;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class InputDataFragment extends Fragment {
//  {ADA IKLANNYA = INTERESTIAL}
    private InterstitialAd mInterstitialAd;
    //    Variable

    TextView ouputTanggal;
    CheckBox fhd,hd,qhd;
    EditText Judul, Durasi, Distributor;
    Spinner Genre;
    RadioButton radioButton;
    RadioGroup Rating;
    Button Simpan, inputTanggal;
    ImageView Gambar;
    ProgressBar progressBar;

    String getJudul,getTanggal,getDurasi,getDistributor
            ,getGenre,getRating,getGambar, getResolusi,cbox;

    //    Input Foto
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri fotoUrl;

    //   Firebase Connection
    DatabaseReference getReference;


    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_data, container, false);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

//        Test Ads:
//        ca-app-pub-3940256099942544/1033173712
//      INTERESTIAL ADS
        AdRequest adRequest = new AdRequest.Builder().build();
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




//        Get ID

        progressBar = view.findViewById(R.id.progres_simpan);
        progressBar.setVisibility(View.GONE);
        Judul = view.findViewById(R.id.input_judul);
        Durasi = view.findViewById(R.id.input_durasi);
        Distributor = view.findViewById(R.id.input_distributor);
        Genre = view.findViewById(R.id.input_genre);
        Rating = view.findViewById(R.id.opsi_rating);
        Simpan = view.findViewById(R.id.button_simpan);
        Gambar = view.findViewById(R.id.input_gambar);
        inputTanggal = view.findViewById(R.id.input_tanggal);
        ouputTanggal = view.findViewById(R.id.output_tanggal);
        fhd = view.findViewById(R.id.fhd);
        hd = view.findViewById(R.id.hd);
        qhd = view.findViewById(R.id.qhd);

        fhd.setChecked(true);
        cbox = "1080p";

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



        //      Mendapatkan Data dari Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        getReference = database.getReference();


//        Ketika Tanggal Dipencet
        inputTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TampilTanggal();
            }
        });


//        Ketika Gambar Di Klik
        Gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

//      Get Foto
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                  if (result.getResultCode() == RESULT_OK && result.getData() != null){
                      fotoUrl = result.getData().getData();
                      Gambar.setImageURI(fotoUrl);
                      Log.e("Data Lokasi Foto : ", String.valueOf(fotoUrl));

                      Simpan.setOnClickListener(new View.OnClickListener() {
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
                                              getResolusi = cbox;
                                              int selectedRating = Rating.getCheckedRadioButtonId();
                                              radioButton = Rating.findViewById(selectedRating);
                                              getRating = (String) radioButton.getText();

                                              if (checkInput() == true){
                                                  progressBar.setVisibility(View.GONE);
                                                  Toast.makeText(getContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
                                              }else{
                                                  checkUser();
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

      Simpan.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (checkInput() == true){
                  progressBar.setVisibility(View.GONE);
                  Toast.makeText(getContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
              }else{
                  progressBar.setVisibility(View.VISIBLE);
                  checkUser();
              }
          }
      });
        return view;
//
    }

    private boolean checkInput() {
        boolean emtpyInput = false;
        if (isEmpty(getGambar)||isEmpty(getJudul) ||isEmpty(getTanggal)||isEmpty(getGenre)
                ||isEmpty(getDurasi) |isEmpty(getDistributor)
                ||isEmpty(getRating)){
            emtpyInput = true;
        }
        return emtpyInput;
    }

    private void checkUser() {
        if (isEmpty(getGambar)||isEmpty(getJudul) ||isEmpty(getTanggal)||isEmpty(getGenre)
        ||isEmpty(getDurasi) |isEmpty(getDistributor)
        ||isEmpty(getRating)){
            Toast.makeText(getContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
        }else{
            getReference.child("Admin").child("DataFilm").push()
                    .setValue(new data_film(getJudul,getTanggal,getGenre,getDurasi,getDistributor,
                            getRating,getResolusi ,getGambar)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Data Berhasil Disimpan!", Toast.LENGTH_SHORT).show();
                    Gambar.setImageResource(R.mipmap.film_management);
                    Judul.setText("");
                    ouputTanggal.setText("01 - 01 - 2022");
                    Durasi.setText("");
                    Distributor.setText("");
                    progressBar.setVisibility(View.GONE);

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(getActivity());
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }

                }
            });

        }
    }

    private void TampilTanggal() {
        InputTanggal inputTanggal = new InputTanggal();
        inputTanggal.show(getActivity().getSupportFragmentManager(), "data");
        inputTanggal.setOnDateClickListener(new InputTanggal.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String tahun = ""+datePicker.getYear();
                String bulan = ""+(datePicker.getMonth()+1);
                String hari = ""+datePicker.getDayOfMonth();
                String text = hari+" - "+bulan+" - "+tahun;
                ouputTanggal.setText(text);
            }
        });

    }
}
