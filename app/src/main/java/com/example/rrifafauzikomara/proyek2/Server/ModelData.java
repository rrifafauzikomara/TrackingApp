package com.example.rrifafauzikomara.proyek2.Server;

/**
 * Created by R Rifa Fauzi Komara on 14/01/2018.
 */

public class ModelData {
    String id, nama, lng, lat, idk, date, time;
    public ModelData(){}

    public ModelData(String id, String nama, String lng, String lat, String idk, String date, String time) {
        this.id = id;
        this.nama = nama;
        this.lng = lng;
        this.lat = lat;
        this.idk = idk;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getIdk() {
        return idk;
    }

    public void setIdk(String idk) {
        this.idk = idk;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
