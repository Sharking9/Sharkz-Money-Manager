package com.Sharkz.Money_Manager.model;

import java.security.SecureRandom;

public class Data {


    private String id, name, jumlah, tanggal, label, type, aset, plusminus;

    private String name_aset, create_date, total;
    private int drawableId;



    public Data(String id, String name, String jumlah, String tanggal, String label, String type, String aset, String plusminus, String name_aset, String create_date, String total){
        this.id = id;
        this.name = name;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.label = label;
        this.type = type;
        this.aset = aset;
        this.plusminus = plusminus;
        this.name_aset = name_aset;
        this.create_date = create_date;
        this.total = total;
    }

    public Data(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) { this.label = label; }

    public int getDrawableId() { return drawableId; }
    public void setDrawableId(int drawableId) { this.drawableId = drawableId; }

    public String getTypeEI() { return type; }
    public void setTypeEI(String type) { this.type = type; }
    public String getAset() { return aset; }
    public void setAset(String aset) { this.aset = aset; }

    public String getPlusminus() { return plusminus; }
    public void setPlusminus(String plusminus) { this.plusminus = plusminus; }


    public String getName_aset() {
        return name_aset;
    }

    public void setName_aset(String name_aset) {
        this.name_aset = name_aset;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
