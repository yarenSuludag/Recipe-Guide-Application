package org.example.dao;

import org.example.database.Database;
import org.example.model.Malzeme;
import org.example.model.Tarif;
import org.example.model.TarifMalzeme;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TarifDAO {


    public int tarifEkle(Tarif tarif) {
        String sql = "INSERT INTO Tarifler (TarifAdi, Kategori, HazirlamaSuresi, Talimatlar) VALUES (?, ?, ?, ?)";
        int tarifID = -1;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, tarif.getTarifAdi());
            statement.setString(2, tarif.getKategori());
            statement.setInt(3, tarif.getHazirlamaSuresi());
            statement.setString(4, tarif.getTalimatlar());
            statement.executeUpdate();

            // Eklenen tarifin ID'si gerekli alıyoruz
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                tarifID = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                JOptionPane.showMessageDialog(null, "Bu tarif zaten mevcut!", "Hata", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
            }
        }
        return tarifID;
    }



    public void malzemeEkle(Malzeme malzeme) {
        String sql = "INSERT INTO Malzemeler (MalzemeAdi, ToplamMiktar, MalzemeBirim, BirimFiyat) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, malzeme.getMalzemeAdi());
            statement.setString(2, malzeme.getToplamMiktar());
            statement.setString(3, malzeme.getMalzemeBirim());
            statement.setDouble(4, malzeme.getBirimFiyat());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Malzeme> tumMalzemeleriGetir() {
        List<Malzeme> malzemeler = new ArrayList<>();
        String sql = "SELECT * FROM Malzemeler";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Malzeme malzeme = new Malzeme();
                malzeme.setMalzemeID(resultSet.getInt("MalzemeID"));
                malzeme.setMalzemeAdi(resultSet.getString("MalzemeAdi"));
                malzeme.setToplamMiktar(resultSet.getString("ToplamMiktar"));
                malzeme.setMalzemeBirim(resultSet.getString("MalzemeBirim"));
                malzeme.setBirimFiyat(resultSet.getDouble("BirimFiyat"));
                malzemeler.add(malzeme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return malzemeler;
    }

    public void tarifMalzemeleriEkle(int tarifID, List<TarifMalzeme> malzemeler) {
        for (TarifMalzeme malzeme : malzemeler) {
            tarifMalzemeEkle(tarifID, malzeme.getMalzemeID(), malzeme.getMalzemeMiktar());
        }
    }

    public void tarifMalzemeEkle(int tarifID, int malzemeID, float malzemeMiktar) {
        String sql = "INSERT INTO TarifMalzeme (TarifID, MalzemeID, MalzemeMiktar) VALUES (?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, tarifID);
            statement.setInt(2, malzemeID);
            statement.setFloat(3, malzemeMiktar);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getMalzemeAdiById(int malzemeID) {
        String sql = "SELECT MalzemeAdi FROM Malzemeler WHERE MalzemeID = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, malzemeID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("MalzemeAdi");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void tarifGuncelle(Tarif tarif) {
        String sql = "UPDATE Tarifler SET Kategori = ?, HazirlamaSuresi = ?, Talimatlar = ? WHERE TarifAdi = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tarif.getKategori());
            statement.setInt(2, tarif.getHazirlamaSuresi());
            statement.setString(3, tarif.getTalimatlar());
            statement.setString(4, tarif.getTarifAdi());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void tarifSil(String tarifAdi) {
        String deleteMalzemelerQuery = "DELETE FROM tarifmalzeme WHERE TarifID = ?";
        String deleteTarifQuery = "DELETE FROM tarifler WHERE TarifID = ?";

        try (Connection connection = Database.getConnection()) {
            if (connection == null) {
                throw new SQLException("Veritabanına bağlanılamadı!");
            }

            // Tarif ID'sini bul
            int tarifID = getTarifIdByName(tarifAdi);
            if (tarifID == -1) {
                System.out.println("Tarif bulunamadı.");
                return;
            }

            // İlişkili malzemeleri sil
            try (PreparedStatement ps = connection.prepareStatement(deleteMalzemelerQuery)) {
                ps.setInt(1, tarifID);
                ps.executeUpdate();
            }

            // Tarifi sil
            try (PreparedStatement ps = connection.prepareStatement(deleteTarifQuery)) {
                ps.setInt(1, tarifID);
                ps.executeUpdate();
            }

            System.out.println("Tarif başarıyla silindi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getTarifIdByName(String tarifAdi) {
        String sql = "SELECT TarifID FROM tarifler WHERE TarifAdi = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tarifAdi);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TarifID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Tarif bulunamazsa -1 döner
    }


    public List<Tarif> tarifAra(String aramaTerimi) {
        String sql = "SELECT * FROM Tarifler WHERE TarifAdi LIKE ?";
        List<Tarif> tarifListesi = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + aramaTerimi + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Tarif tarif = new Tarif(
                        rs.getString("TarifAdi"),
                        rs.getString("Kategori"),
                        rs.getInt("HazirlamaSuresi"),
                        rs.getString("Talimatlar")
                );
                tarifListesi.add(tarif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarifListesi;
    }


    public List<Tarif> siralaByHazirlamaSuresi(boolean artan) {
        String sql = "SELECT * FROM Tarifler ORDER BY HazirlamaSuresi " + (artan ? "ASC" : "DESC");
        List<Tarif> tarifListesi = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Tarif tarif = new Tarif(
                        rs.getString("TarifAdi"),
                        rs.getString("Kategori"),
                        rs.getInt("HazirlamaSuresi"),
                        rs.getString("Talimatlar")
                );
                tarifListesi.add(tarif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarifListesi;
    }

    public List<Map<String, Object>> siralaByMaliyet(boolean artan) {
        String tarifSql = "SELECT TarifID, TarifAdi, Kategori, HazirlamaSuresi, Talimatlar FROM Tarifler";
        String malzemeSql = "SELECT tm.MalzemeMiktar, m.BirimFiyat " +
                "FROM TarifMalzeme tm " +
                "JOIN Malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                "WHERE tm.TarifID = ?";

        List<Map<String, Object>> tarifler = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement tarifStmt = connection.prepareStatement(tarifSql);
             ResultSet tarifRs = tarifStmt.executeQuery()) {

            while (tarifRs.next()) {
                Map<String, Object> tarifBilgi = new HashMap<>();
                int tarifID = tarifRs.getInt("TarifID");
                tarifBilgi.put("TarifAdi", tarifRs.getString("TarifAdi"));
                tarifBilgi.put("Kategori", tarifRs.getString("Kategori"));
                tarifBilgi.put("HazirlamaSuresi", tarifRs.getInt("HazirlamaSuresi"));
                tarifBilgi.put("Talimatlar", tarifRs.getString("Talimatlar"));

                // Her tarifin malzemelerini getirmek ve maliyetlerini hesaplayabilme için
                float toplamMaliyet = 0;
                try (PreparedStatement malzemeStmt = connection.prepareStatement(malzemeSql)) {
                    malzemeStmt.setInt(1, tarifID);
                    try (ResultSet malzemeRs = malzemeStmt.executeQuery()) {
                        while (malzemeRs.next()) {
                            float malzemeMiktar = malzemeRs.getFloat("MalzemeMiktar");
                            float birimFiyat = malzemeRs.getFloat("BirimFiyat");
                            toplamMaliyet += malzemeMiktar * birimFiyat;
                        }
                    }
                }

                tarifBilgi.put("ToplamMaliyet", toplamMaliyet);
                tarifler.add(tarifBilgi);
            }

            // maliyetlerine uygun sıralama
            tarifler.sort((t1, t2) -> {
                float maliyet1 = (float) t1.get("ToplamMaliyet");
                float maliyet2 = (float) t2.get("ToplamMaliyet");
                return artan ? Float.compare(maliyet1, maliyet2) : Float.compare(maliyet2, maliyet1);
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tarifler;
    }


    public List<Tarif> tarifFiltrele(String kategori, int maxHazirlamaSuresi) {
        String sql = "SELECT * FROM Tarifler WHERE Kategori = ? AND HazirlamaSuresi <= ?";
        List<Tarif> tarifListesi = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, kategori);
            statement.setInt(2, maxHazirlamaSuresi);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Tarif tarif = new Tarif(
                        rs.getString("TarifAdi"),
                        rs.getString("Kategori"),
                        rs.getInt("HazirlamaSuresi"),
                        rs.getString("Talimatlar")
                );
                tarifListesi.add(tarif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarifListesi;
    }
    public Tarif tarifBul(String tarifAdi) {
        Tarif tarif = null;
        String query = "SELECT * FROM tarifler WHERE tarifAdi = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, tarifAdi);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                tarif = new Tarif(
                        resultSet.getInt("TarifID"),
                        resultSet.getString("tarifAdi"),
                        resultSet.getString("Kategori"),
                        resultSet.getInt("HazirlamaSuresi"),
                        resultSet.getString("Talimatlar")

                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tarif;
    }

    private Connection connection;



    public Map<Tarif, List<Malzeme>> getTariflerVeMalzemeleri() {
        Map<Tarif, List<Malzeme>> tariflerVeMalzemeleri = new HashMap<>();
        String sql = "SELECT t.id AS tarif_id, t.tarif_adi, m.id AS malzeme_id, m.malzeme_adi " +
                "FROM tarif t " +
                "JOIN tarif_malzeme tm ON t.id = tm.tarif_id " +
                "JOIN malzeme m ON tm.malzeme_id = m.id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int tarifId = resultSet.getInt("tarif_id");
                String tarifAdi = resultSet.getString("tarif_adi");

                Tarif tarif = new Tarif(tarifAdi, "", 0, tarifAdi);
                Malzeme malzeme = new Malzeme();
                malzeme.setMalzemeID(resultSet.getInt("malzeme_id"));
                malzeme.setMalzemeAdi(resultSet.getString("malzeme_adi"));

                tariflerVeMalzemeleri.computeIfAbsent(tarif, k -> new ArrayList<>()).add(malzeme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tariflerVeMalzemeleri;
    }

    public List<Malzeme> getTumMalzemeler() {
        List<Malzeme> malzemeler = new ArrayList<>();
        String sql = "SELECT id, malzeme_adi FROM malzeme";

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Malzeme malzeme = new Malzeme();
                malzeme.setMalzemeID(resultSet.getInt("id"));
                malzeme.setMalzemeAdi(resultSet.getString("malzeme_adi"));
                malzemeler.add(malzeme);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return malzemeler;
    }


    public List<Tarif> getTarifler() {
        String sql = "SELECT * FROM Tarifler";
        List<Tarif> tarifListesi = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Tarif tarif = new Tarif(
                        resultSet.getString("tarifAdi"),
                        resultSet.getString("kategori"),
                        resultSet.getInt("hazirlamaSuresi"),
                        resultSet.getString("talimatlar")
                );
                tarif.setTarifID(resultSet.getInt("tarifID"));
                tarifListesi.add(tarif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (tarifListesi.isEmpty()) {
            System.out.println("Veritabanından tarif bulunamadı.");
        }

        return tarifListesi;
    }

    public List<TarifMalzeme> getTarifMalzemeleri(int tarifID) {
        String sql = "SELECT * FROM TarifMalzeme WHERE tarifID = ?";
        List<TarifMalzeme> malzemeListesi = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, tarifID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int malzemeID = resultSet.getInt("malzemeID");
                    float miktar = resultSet.getFloat("malzemeMiktar");


                    System.out.println("TarifID: " + tarifID + " MalzemeID: " + malzemeID + " Miktar: " + miktar);

                    TarifMalzeme tarifMalzeme = new TarifMalzeme();
                    tarifMalzeme.setTarifID(tarifID);
                    tarifMalzeme.setMalzemeID(malzemeID);
                    tarifMalzeme.setMalzemeMiktar(miktar);

                    malzemeListesi.add(tarifMalzeme);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Toplam Malzeme Sayısı: " + malzemeListesi.size());
        return malzemeListesi;
    }

    public int getMalzemeIdByName(String malzemeAdi) {
        String sql = "SELECT MalzemeID FROM Malzemeler WHERE MalzemeAdi = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, malzemeAdi);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("MalzemeID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Malzeme bulunamazsa -1 döner
    }



    public void tarifMalzemeleriSil(int tarifID) {
        String sql = "DELETE FROM TarifMalzeme WHERE TarifID = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, tarifID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Malzeme> getMalzemeler() {
        String sql = "SELECT * FROM Malzemeler";
        List<Malzeme> malzemeListesi = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Malzeme malzeme = new Malzeme();
                malzeme.setMalzemeID(resultSet.getInt("malzemeID"));
                malzeme.setMalzemeAdi(resultSet.getString("malzemeAdi"));
                malzeme.setToplamMiktar(resultSet.getString("toplamMiktar"));
                malzeme.setMalzemeBirim(resultSet.getString("malzemeBirim"));
                malzeme.setBirimFiyat(resultSet.getDouble("birimFiyat"));
                malzemeListesi.add(malzeme);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return malzemeListesi;
    }


    public Malzeme getMalzemeById(int malzemeID) {
        String sql = "SELECT * FROM Malzemeler WHERE MalzemeID = ?";
        Malzeme malzeme = null;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, malzemeID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    malzeme = new Malzeme();
                    malzeme.setMalzemeID(resultSet.getInt("MalzemeID"));
                    malzeme.setMalzemeAdi(resultSet.getString("MalzemeAdi"));
                    malzeme.setToplamMiktar(resultSet.getString("toplamMiktar"));
                    malzeme.setMalzemeBirim(resultSet.getString("MalzemeBirim"));
                    malzeme.setBirimFiyat(resultSet.getDouble("birimFiyat"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return malzeme;
    }

    public List<Map<String, Object>> filtreleByMaliyetAraligi(float minMaliyet, float maxMaliyet) {
        String tarifSql = "SELECT TarifID, TarifAdi, Kategori, HazirlamaSuresi, Talimatlar FROM Tarifler";
        String malzemeSql = "SELECT tm.MalzemeMiktar, m.BirimFiyat " +
                "FROM TarifMalzeme tm " +
                "JOIN Malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                "WHERE tm.TarifID = ?";

        List<Map<String, Object>> uygunTarifler = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement tarifStmt = connection.prepareStatement(tarifSql);
             ResultSet tarifRs = tarifStmt.executeQuery()) {

            while (tarifRs.next()) {
                int tarifID = tarifRs.getInt("TarifID");
                float toplamMaliyet = 0;

                // Her tarifin toplam maliyetini hesapla
                try (PreparedStatement malzemeStmt = connection.prepareStatement(malzemeSql)) {
                    malzemeStmt.setInt(1, tarifID);
                    try (ResultSet malzemeRs = malzemeStmt.executeQuery()) {
                        while (malzemeRs.next()) {
                            float miktar = malzemeRs.getFloat("MalzemeMiktar");
                            float birimFiyat = malzemeRs.getFloat("BirimFiyat");
                            toplamMaliyet += miktar * birimFiyat;
                        }
                    }
                }

                // Maliyet aralığında olan tarifleri listeleyebilmek içini
                if (toplamMaliyet >= minMaliyet && toplamMaliyet <= maxMaliyet) {
                    Map<String, Object> tarifBilgi = new HashMap<>();
                    tarifBilgi.put("TarifAdi", tarifRs.getString("TarifAdi"));
                    tarifBilgi.put("Kategori", tarifRs.getString("Kategori"));
                    tarifBilgi.put("HazirlamaSuresi", tarifRs.getInt("HazirlamaSuresi"));
                    tarifBilgi.put("Talimatlar", tarifRs.getString("Talimatlar"));
                    tarifBilgi.put("ToplamMaliyet", toplamMaliyet);

                    uygunTarifler.add(tarifBilgi);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return uygunTarifler;
    }




}

