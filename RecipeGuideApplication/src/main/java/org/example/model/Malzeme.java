package org.example.model;

public class Malzeme {
    private int malzemeID;
    private String malzemeAdi;
    private String toplamMiktar;
    private String malzemeBirim;
    private double birimFiyat;



    public int getMalzemeID() {
        return malzemeID;
    }

    public void setMalzemeID(int malzemeID) {
        this.malzemeID = malzemeID;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public void setMalzemeAdi(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }

    public String getToplamMiktar() {
        return toplamMiktar;
    }

    public void setToplamMiktar(String toplamMiktar) {
        this.toplamMiktar = toplamMiktar;
    }

    public String getMalzemeBirim() {
        return malzemeBirim;
    }

    public void setMalzemeBirim(String malzemeBirim) {
        this.malzemeBirim = malzemeBirim;
    }

    public double getBirimFiyat() {
        return birimFiyat;
    }

    public void setBirimFiyat(double birimFiyat) {
        this.birimFiyat = birimFiyat;
    }

    // Getter ve Setter metodları
}
