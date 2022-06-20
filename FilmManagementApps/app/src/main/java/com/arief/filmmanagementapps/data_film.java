package com.arief.filmmanagementapps;

public class data_film {

    //    Variable
    private String judul;
    private String tanggal;
    private String genre;
    private String durasi;
    private String distributor;
    private String rating;
    private String resolusi;
    private String image;
    private String key;

//    Getter and Setter


    public String getResolusi() {
        return resolusi;
    }

    public void setResolusi(String resolusi) {
        this.resolusi = resolusi;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

//    Konstruktor
    public data_film() {
    }

//    Konstruktor


    public data_film(String judul, String tanggal, String genre, String durasi, String distributor, String rating, String resolusi, String image) {
        this.judul = judul;
        this.tanggal = tanggal;
        this.genre = genre;
        this.durasi = durasi;
        this.distributor = distributor;
        this.rating = rating;
        this.resolusi = resolusi;
        this.image = image;

    }
}
