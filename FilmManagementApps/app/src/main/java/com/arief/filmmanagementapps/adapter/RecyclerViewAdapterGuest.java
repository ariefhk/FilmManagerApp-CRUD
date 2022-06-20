package com.arief.filmmanagementapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arief.filmmanagementapps.R;
import com.arief.filmmanagementapps.data_film;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapterGuest extends RecyclerView.Adapter<RecyclerViewAdapterGuest.ViewHolder>{
    //    Variable
    private ArrayList<data_film> listFilm;
    private Context context;

    public RecyclerViewAdapterGuest(ArrayList<data_film> listFilm, Context context) {
        this.listFilm = listFilm;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterGuest.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_guest,parent,false);
        RecyclerViewAdapterGuest.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterGuest.ViewHolder holder, int position) {
        final String Poster = listFilm.get(position).getImage();
        final String Judul = listFilm.get(position).getJudul();
        final String Tanggal = listFilm.get(position).getTanggal();
        final String Genre = listFilm.get(position).getGenre();
        final String Durasi = listFilm.get(position).getDurasi();
        final String Distributor = listFilm.get(position).getDistributor();
        final String Rating = listFilm.get(position).getRating();

        //        Memasukan Kedalam Holder
        Picasso.get().load(Poster).into(holder.Gambar);
        holder.Judul.setText(Judul);
        holder.Tanggal.setText(Tanggal);
        holder.Genre.setText(Genre);
        holder.Durasi.setText(Durasi+" menit");
        holder.Distributor.setText(Distributor);
        holder.Rating.setText(Rating);
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
        private final TextView Distributor;
        private final TextView Rating;
        private final LinearLayout ListItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Gambar = itemView.findViewById(R.id.show_gambar_guest);
            Judul = itemView.findViewById(R.id.show_judul_guest);
            Tanggal = itemView.findViewById(R.id.show_tanggal_guest);
            Genre = itemView.findViewById(R.id.show_genre_guest);
            Durasi = itemView.findViewById(R.id.show_durasi_guest);
            Distributor = itemView.findViewById(R.id.show_distributor_guest);
            Rating = itemView.findViewById(R.id.show_rating_guest);
            ListItem = itemView.findViewById(R.id.list_item_guest);
        }
    }
}
