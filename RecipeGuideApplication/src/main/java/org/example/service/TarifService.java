package org.example.service;

import org.example.dao.TarifDAO;
import org.example.model.Malzeme;
import org.example.model.Tarif;
import org.example.model.TarifMalzeme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TarifService {

    private TarifDAO tarifDAO = new TarifDAO();
    private List<Tarif> tarifList = new ArrayList<>();

    // Constructor ile tarifleri yükleme
    public TarifService() {
        tarifList = tarifDAO.getTarifler(); // Tarifleri veritabanından al ve listeyi doldur
    }
    // Yeni bir tarif ekleme
    public int tarifEkle(Tarif tarif) {
        return tarifDAO.tarifEkle(tarif);  // Tarif eklendiğinde ID'sini döndür
    }
    // Tarife malzemeleri ekleme
    public void tarifMalzemeleriEkle(int tarifID, List<TarifMalzeme> malzemeler) {
        tarifDAO.tarifMalzemeleriEkle(tarifID, malzemeler);
    }

    public int getMalzemeIdByName(String malzemeAdi) {
        return tarifDAO.getMalzemeIdByName(malzemeAdi);
    }

    public void tarifMalzemeleriGuncelle(int tarifID, List<TarifMalzeme> yeniMalzemeler) {
        // Önce eski malzemeleri sil
        tarifDAO.tarifMalzemeleriSil(tarifID);

        // Yeni malzemeleri ekle
        for (TarifMalzeme malzeme : yeniMalzemeler) {
            tarifDAO.tarifMalzemeEkle(malzeme.getTarifID(), malzeme.getMalzemeID(), malzeme.getMalzemeMiktar());
        }
    }

    public String getMalzemeAdiById(int malzemeID) {
        return tarifDAO.getMalzemeAdiById(malzemeID);
    }


    // yeni malzeme ekleme metodu
    public void malzemeEkle(Malzeme malzeme) {
        tarifDAO.malzemeEkle(malzeme);
    }

    // Malzemeleri veritabanından getirme metodu
    public List<Malzeme> tumMalzemeleriGetir() {


        return tarifDAO.tumMalzemeleriGetir();
    }



    public void tarifGuncelle(Tarif tarif) {
        tarifDAO.tarifGuncelle(tarif);
    }

    public void tarifSil(String tarifAdi) {
        tarifDAO.tarifSil(tarifAdi);
    }

    public List<Tarif> tarifAra(String aramaTerimi) {
        return tarifDAO.tarifAra(aramaTerimi);
    }

    public List<Tarif> siralaByHazirlamaSuresi(boolean artan) {
        return tarifDAO.siralaByHazirlamaSuresi(artan);
    }
    // Maliyete göre tarifleri sıralama metodu
    public List<Map<String, Object>> siralaByMaliyet(boolean artan) {
        return tarifDAO.siralaByMaliyet(artan);
    }

    // Tarifleri getir metodu
    public List<Tarif> getTarifList() {
        return tarifList;
    }


    public List<Tarif> tarifFiltrele(String kategori, int maxHazirlamaSuresi) {
        return tarifDAO.tarifFiltrele(kategori, maxHazirlamaSuresi);
    }
    public Tarif tarifBul(String tarifAdi) {
        return tarifDAO.tarifBul(tarifAdi); // Belirtilen tarif adını bul
    }

    public Map<Tarif, List<Malzeme>> getTariflerVeMalzemeleri() {
        return tarifDAO.getTariflerVeMalzemeleri();
    }

    public Map<Tarif, Integer> malzemeyeGoreTarifAra(List<String> girilenMalzemeler) {
        Map<Tarif, List<Malzeme>> tariflerVeMalzemeleri = tarifDAO.getTariflerVeMalzemeleri();
        Map<Tarif, Integer> tarifEslasmeSayilari = new HashMap<>();

        for (Map.Entry<Tarif, List<Malzeme>> entry : tariflerVeMalzemeleri.entrySet()) {
            Tarif tarif = entry.getKey();
            List<Malzeme> tarifMalzemeleri = entry.getValue();

            // Eşleşen malzeme sayısını hesapla
            int eslesenMalzemeSayisi = (int) tarifMalzemeleri.stream()
                    .filter(malzeme -> girilenMalzemeler.contains(malzeme.getMalzemeAdi()))
                    .count();

            // Eşleşme varsa haritaya ekle
            if (eslesenMalzemeSayisi > 0) {
                tarifEslasmeSayilari.put(tarif, eslesenMalzemeSayisi);
            }
        }

        return tarifEslasmeSayilari;
    }

    public List<Tarif> getTarifler() {
        List<Tarif> tarifler = tarifDAO.getTarifler();

        for (Tarif tarif : tarifler) {
            List<TarifMalzeme> malzemeler = tarifDAO.getTarifMalzemeleri(tarif.getTarifID());
            System.out.println("Tarif: " + tarif.getTarifAdi() + " için malzemeler: " + malzemeler.size());
            tarif.setTarifMalzemeleri(malzemeler);
        }

        return tarifler;
    }


    public List<Tarif> malzemeyeGoreAra(String malzemeAdi) {
        return tarifList1.stream()
                .filter(tarif -> tarif.getTarifMalzemeleri().stream()
                        .anyMatch(tarifMalzeme -> tarifMalzeme.getMalzemeID() == malzemeAdiIleIDBul(malzemeAdi)))
                .collect(Collectors.toList());
    }
    private List<Tarif> tarifList1;  // Tarif listesi
    private List<Malzeme> malzemeler; // Malzeme listesi

    // Malzeme adını kullanarak ilgili malzeme ID'sini bulur
    private int malzemeAdiIleIDBul(String malzemeAdi) {
        return malzemeler.stream()
                .filter(malzeme -> malzeme.getMalzemeAdi().equalsIgnoreCase(malzemeAdi))
                .findFirst()
                .map(Malzeme::getMalzemeID)
                .orElse(-1); // Bulunamazsa -1 döndür
    }

    public List<Tarif> tarifleriMalzemeyeGoreGetir(List<String> secilenMalzemeler) {
        List<Tarif> tumTarifler = getTarifler(); // Veritabanından tarifleri getir

        if (tumTarifler == null || tumTarifler.isEmpty()) {
            System.out.println("Tarif listesi boş veya null.");
            return new ArrayList<>(); // Boş liste döndür
        }

        return tumTarifler.stream()
                .sorted((t1, t2) -> Double.compare(
                        hesaplaEslesmeYuzdesi(t2, secilenMalzemeler),
                        hesaplaEslesmeYuzdesi(t1, secilenMalzemeler)))
                .collect(Collectors.toList());
    }



    private Malzeme getMalzemeById(int malzemeID) {
        return tarifDAO.getMalzemeById(malzemeID);
    }

    // DAO'dan tarif malzemelerini getir
    public List<TarifMalzeme> getTarifMalzemeleri(int tarifID) {
        return tarifDAO.getTarifMalzemeleri(tarifID);
    }


    public double hesaplaEslesmeYuzdesi(Tarif tarif, List<String> secilenMalzemeler) {
        List<TarifMalzeme> tarifMalzemeleri = tarif.getTarifMalzemeleri();

        // Null veya boş liste kontrolü
        if (tarifMalzemeleri == null || tarifMalzemeleri.isEmpty()) {

            return 0.0;
        }

        // Tarif malzemelerinin adlarını topluyoruz
        List<String> malzemeAdlari = tarifMalzemeleri.stream()
                .map(tarifMalzeme -> getMalzemeById(tarifMalzeme.getMalzemeID()).getMalzemeAdi().toLowerCase().trim())
                .collect(Collectors.toList());

        // Seçilen malzemeleri de aynı şekilde normalize ediyoruz
        List<String> normalizasyonluSecilenMalzemeler = secilenMalzemeler.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .collect(Collectors.toList());

        System.out.println("Tarif Malzemeleri: " + malzemeAdlari);
        System.out.println("Seçilen Malzemeler: " + normalizasyonluSecilenMalzemeler);

        // Eşleşen malzeme sayısını buluyoruz
        long eslesmeSayisi = normalizasyonluSecilenMalzemeler.stream()
                .filter(malzemeAdlari::contains)
                .count();

        System.out.println("Eşleşme Sayısı: " + eslesmeSayisi);

        if (malzemeAdlari.isEmpty()) {
            return 0.0;  // Bölme hatası olmaması için kontrol
        }

        return (double) eslesmeSayisi / malzemeAdlari.size() * 100;
    }


    public List<Malzeme> eksikMalzemeleriBul(Tarif tarif) {
        List<Malzeme> eksikMalzemeler = new ArrayList<>();

        for (TarifMalzeme tarifMalzeme : tarif.getTarifMalzemeleri()) {
            Malzeme malzeme = getMalzemeById(tarifMalzeme.getMalzemeID());

            // Toplam miktarı almak için string'ten sayıyı ayıklıyoruz
            float toplamMiktar = parseMiktar(malzeme.getToplamMiktar()); // Bu kısım doğru çalışacak

            // Tarif malzemesi miktarı zaten float olduğu için direkt kullanıyoruz
            float tarifMiktar = tarifMalzeme.getMalzemeMiktar(); // Bu zaten float

            if (toplamMiktar < tarifMiktar) {
                eksikMalzemeler.add(malzeme);
            }
        }

        return eksikMalzemeler;
    }

    // Yardımcı metot: Miktardan sayısal değeri ayıklamak için
    private float parseMiktar(String miktarStr) {
        // Sayı dışında her şeyi kaldır
        String sayisalKisim = miktarStr.replaceAll("[^0-9.]", "");
        return Float.parseFloat(sayisalKisim);
    }



    // Eksik malzemelerin toplam maliyetini hesaplar
    public double eksikMalzemeMaliyeti(Tarif tarif) {
        List<Malzeme> eksikMalzemeler = eksikMalzemeleriBul(tarif);
        return eksikMalzemeler.stream()
                .mapToDouble(malzeme -> malzeme.getBirimFiyat())
                .sum();
    }

    // Tarifin yapılabilirliğini kontrol eder
    public boolean tarifYapilabilir(Tarif tarif) {
        return eksikMalzemeleriBul(tarif).isEmpty();
    }

    public List<Map<String, Object>> tarifleriMaliyetAraliginaGoreFiltrele(float minMaliyet, float maxMaliyet) {
        return tarifDAO.filtreleByMaliyetAraligi(minMaliyet, maxMaliyet);
    }


}
