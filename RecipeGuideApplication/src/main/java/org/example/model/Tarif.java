package org.example.model;

import java.util.List;

public class Tarif {
    private int tarifID;
    private String tarifAdi;
    private String kategori;
    private int hazirlamaSuresi;
    private String talimatlar;
    private List<TarifMalzeme> tarifMalzemeleri; // TarifMalzeme listesini tutar


    public Tarif(String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar) {
        this.tarifAdi = tarifAdi;
        this.kategori = kategori;
        this.hazirlamaSuresi = hazirlamaSuresi;
        this.talimatlar = talimatlar;
    }

    public Tarif(int tarifID, String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar) {
        this.tarifID = tarifID;
        this.tarifAdi = tarifAdi;
        this.kategori = kategori;
        this.hazirlamaSuresi = hazirlamaSuresi;
        this.talimatlar = talimatlar;
    }

    public int getTarifID() {
        return tarifID;
    }

    public void setTarifID(int tarifID) {
        this.tarifID = tarifID;
    }

    public String getTarifAdi() {
        return tarifAdi;
    }

    public void setTarifAdi(String tarifAdi) {
        this.tarifAdi = tarifAdi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getHazirlamaSuresi() {
        return hazirlamaSuresi;
    }

    public void setHazirlamaSuresi(int hazirlamaSuresi) {
        this.hazirlamaSuresi = hazirlamaSuresi;
    }

    public String getTalimatlar() {
        return talimatlar;
    }

    public void setTalimatlar(String talimatlar) {
        this.talimatlar = talimatlar;
    }

    public List<TarifMalzeme> getTarifMalzemeleri() {
        return tarifMalzemeleri;
    }

    public void setTarifMalzemeleri(List<TarifMalzeme> tarifMalzemeleri) {
        this.tarifMalzemeleri = tarifMalzemeleri;
    }

    // Toplam maliyeti hesaplamak için getMaliyet metodu
    public float getMaliyet(List<Malzeme> malzemeler) {
        float toplamMaliyet = 0.0f;

        // Check if tarifMalzemeleri is initialized
        if (tarifMalzemeleri == null) {
            return toplamMaliyet; // Return 0 if no ingredients are present
        }

        // Check if malzemeler list is not null
        if (malzemeler == null) {
            throw new IllegalArgumentException("Malzeme listesi boş olamaz."); // Handle invalid input
        }

        for (TarifMalzeme tarifMalzeme : tarifMalzemeleri) {
            int malzemeID = tarifMalzeme.getMalzemeID();
            float malzemeMiktar = tarifMalzeme.getMalzemeMiktar();

            // Malzeme listesinden ilgili malzemenin maliyetini bul
            for (Malzeme malzeme : malzemeler) {
                if (malzeme.getMalzemeID() == malzemeID) {
                    toplamMaliyet += malzeme.getBirimFiyat() * malzemeMiktar; // Maliyet * Miktar
                    break; // İlgili malzeme bulundu, döngüyü kır
                }
            }
        }

        return toplamMaliyet; // Toplam maliyeti döndür
    }

}
