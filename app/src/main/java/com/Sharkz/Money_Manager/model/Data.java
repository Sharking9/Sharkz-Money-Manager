package com.Sharkz.Money_Manager.model;

public class Data {


    private String id, name, jumlah, tanggal, label;
    private int drawableId;

    public Data(String id, String name, String jumlah, String tanggal, String label){
        this.id = id;
        this.name = name;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.label = label;
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

}
