package org.example.model;

public class TarifMalzeme {
    private int tarifID;
    private int malzemeID;
    private float malzemeMiktar;

    // Parametresiz Constructor (Varsayılan)
    public TarifMalzeme() {
    }

    // Constructor
    public TarifMalzeme(int tarifID, int malzemeID, float malzemeMiktar) {
        this.tarifID = tarifID;
        this.malzemeID = malzemeID;
        this.malzemeMiktar = malzemeMiktar;
    }


    public int getTarifID() {
        return tarifID;
    }

    public void setTarifID(int tarifID) {
        this.tarifID = tarifID;
    }

    public int getMalzemeID() {
        return malzemeID;
    }

    public void setMalzemeID(int malzemeID) {
        this.malzemeID = malzemeID;
    }

    public float getMalzemeMiktar() {
        return malzemeMiktar;
    }

    public void setMalzemeMiktar(float malzemeMiktar) {
        this.malzemeMiktar = malzemeMiktar;
    }



    // Getter ve Setter metodları
}
