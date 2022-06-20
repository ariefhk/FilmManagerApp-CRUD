package com.arief.filmmanagementapps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

//    Variable
    private ArrayList<data_film> listFilm;
    private Context context;

    //    Transfer data
    dataListener listener;

    //  Interface
    public interface dataListener {
        void onDeleteData(data_film data, int position);

    }

//    Konstruktor
    public RecyclerViewAdapter(ArrayList<data_film> listFilm, Context context, dataListener listener) {
        this.listFilm = listFilm;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final String Poster = listFilm.get(position).getImage();
        final String Judul = listFilm.get(position).getJudul();
        final String Tanggal = listFilm.get(position).getTanggal();
        final String Genre = listFilm.get(position).getGenre();
        final String Durasi = listFilm.get(position).getDurasi();
//        final String Casting = listFilm.get(position).getCasting();
        final String Distributor = listFilm.get(position).getDistributor();
        final String Rating = listFilm.get(position).getRating();


//        Memasukan Kedalam Holder
        Picasso.get().load(Poster).into(holder.Gambar);
        holder.Judul.setText(Judul);
        holder.Tanggal.setText(Tanggal);
        holder.Genre.setText(Genre);
        holder.Durasi.setText(Durasi);
//        holder.Casting.setText(Casting);
        holder.Distributor.setText(Distributor);
        holder.Rating.setText(Rating);

//        SetOnClickListener
        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pilihan) {
                       switch (pilihan){
                           case 0:
                               Bundle bundle = new Bundle();
                               bundle.putString("dataImage",listFilm.get(position).getImage());
                               bundle.putString("dataJudul",listFilm.get(position).getJudul());
                               bundle.putString("dataTanggal",listFilm.get(position).getTanggal());
                               bundle.putString("dataGenre",listFilm.get(position).getGenre());
                               bundle.putString("dataDurasi",listFilm.get(position).getDurasi());
//                               bundle.putString("dataCasting",listFilm.get(position).getCasting());
                               bundle.putString("dataDistributor",listFilm.get(position).getDistributor());
                               bundle.putString("dataRating",listFilm.get(position).getRating());
                               bundle.putString("dataKey",listFilm.get(position).getKey());
                               Intent intent = new Intent(view.getContext(), ActivityUpdate.class);
                               intent.putExtras(bundle);
                               context.startActivity(intent);
                               break;
                           case 1:
                               listener.onDeleteData(listFilm.get(position),position);
                               break;
                       }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFilm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView Gambar;
        private final TextView Judul;
        private final TextView Tanggal;
        private final TextView Genre;
        private final TextView Durasi;
//        private final TextView Casting;
        private final TextView Distributor;
        private final TextView Rating;
        private final LinearLayout ListItem;
        dataListener listener;


        public ViewHolder(@NonNull View itemView, dataListener listener) {
            super(itemView);
            Gambar = itemView.findViewById(R.id.show_gambar);
            Judul = itemView.findViewById(R.id.show_judul);
            Tanggal = itemView.findViewById(R.id.show_tanggal);
            Genre = itemView.findViewById(R.id.show_genre);
            Durasi = itemView.findViewById(R.id.show_durasi);
//            Casting = itemView.findViewById(R.id.show_casting);
            Distributor = itemView.findViewById(R.id.show_distributor);
            Rating = itemView.findViewById(R.id.show_rating);
            ListItem = itemView.findViewById(R.id.list_item);
        }
    }
}
