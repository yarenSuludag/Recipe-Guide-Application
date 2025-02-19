package org.example.ui;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.model.Malzeme;
import org.example.model.Tarif;
import org.example.model.TarifMalzeme;
import org.example.service.TarifService;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;


public class TarifRehberiGUI {

    private JFrame frame;
    private JTextField aramaAlani;
    private JTextArea tarifListesi;
    private TarifService tarifService;
    private JTextField tarifAdiField, kategoriField, hazirlamaSuresiField;
    private JTextArea talimatlarField;
    private JPanel eklemePaneli, guncellemePaneli;


    private JTextField kategoriFiltreField;
    private JTextField maxHazirlamaSuresiField;
    private JButton filtreleButonu;


    private JComboBox<String> malzemeSecimiComboBox;
    private JList<String> malzemeListesi;
    private DefaultListModel<String> malzemeListesiModel;

    private JButton ekleButonu, silButonu, guncelleButonu;
    private JTextField minMaliyetField, maxMaliyetField;
    private JButton maliyetFiltreleButonu;





    public TarifRehberiGUI() {
        tarifService = new TarifService();

        frame = new JFrame("Tarif Rehberi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());




        ImageIcon originalIcon = new ImageIcon("C:\\Users\\Yaren\\Downloads\\kitchen_image_300x300.jpg");  // Resmin yolu
        Image resizedImage = originalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(resizedIcon);


        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int imageX = 10; // Soldan 10 piksel boşluk
                int imageY = frame.getHeight() - imageLabel.getHeight() - 50;
                imageLabel.setBounds(imageX, imageY, 300, 300);
            }
        });

        frame.add(imageLabel);

        frame.setVisible(true);



        aramaAlani = new JTextField(20);
        JButton araButonu = new JButton("Ara");

        JPanel aramaPaneli = new JPanel(new FlowLayout());
        aramaPaneli.setBackground(Color.decode("#F4F4F9"));
        aramaPaneli.add(aramaAlani);
        aramaAlani.setBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2"), 2));
        araButonu.setBackground(Color.decode("#4A90E2"));
        araButonu.setForeground(Color.WHITE);
        araButonu.setBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2")));
        aramaPaneli.add(araButonu);




        List<JCheckBox> malzemeCheckboxList = new ArrayList<>();


        JPanel malzemePanel = new JPanel();
        malzemePanel.setLayout(new BoxLayout(malzemePanel, BoxLayout.Y_AXIS));

        // Tüm malzemeleri getirip Checkbox olarak ekliyoruz
        List<Malzeme> tumMalzemeler = tarifService.tumMalzemeleriGetir();
        for (Malzeme malzeme : tumMalzemeler) {
            JCheckBox checkbox = new JCheckBox(malzeme.getMalzemeAdi().toLowerCase().trim());
            malzemeCheckboxList.add(checkbox);
            malzemePanel.add(checkbox);
        }


        JButton malzemeAraButonu = new JButton("Malzemeye Göre Ara");
        malzemeAraButonu.setBackground(Color.decode("#4A90E2"));
        malzemeAraButonu.setForeground(Color.WHITE);


        malzemeAraButonu.addActionListener(e -> {
            // Seçilen malzemeleri topla
            List<String> secilenMalzemeler = new ArrayList<>();
            for (JCheckBox checkbox : malzemeCheckboxList) {
                if (checkbox.isSelected()) {
                    secilenMalzemeler.add(checkbox.getText().toLowerCase().trim());
                }
            }


            System.out.println("Seçilen Malzemeler: " + secilenMalzemeler);


            List<Tarif> tarifler = tarifService.tarifleriMalzemeyeGoreGetir(secilenMalzemeler);
            tarifListesi.setText("");


            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            for (Tarif tarif : tarifler) {
                double yuzde = tarifService.hesaplaEslesmeYuzdesi(tarif, secilenMalzemeler);
                double eksikMaliyet = tarifService.eksikMalzemeMaliyeti(tarif);
                String tarifBilgisi;

                if (tarifService.tarifYapilabilir(tarif)) {
                    tarifBilgisi = tarif.getTarifAdi() + " - Eşleşme: %" + String.format("%.2f", yuzde);
                    JLabel label = new JLabel(tarifBilgisi);
                    label.setOpaque(true);
                    label.setBackground(Color.GREEN);
                    panel.add(label);
                } else {
                    tarifBilgisi = tarif.getTarifAdi() + " - Eşleşme: %" + String.format("%.2f", yuzde) +
                            " - Eksik Malzeme Maliyeti: " + eksikMaliyet + " TL";
                    JLabel label = new JLabel(tarifBilgisi);
                    label.setOpaque(true);
                    label.setBackground(Color.RED);
                    panel.add(label);
                }
            }


            JScrollPane scrollPane = new JScrollPane(panel);
            JFrame frame = new JFrame("Tarif Sonuçları");
            frame.add(scrollPane);
            frame.setSize(500, 400);
            frame.setVisible(true);
        });


        JScrollPane malzemeScrollPane = new JScrollPane(malzemePanel);
        malzemeScrollPane.setBorder(BorderFactory.createTitledBorder("Malzeme Seçimi"));


        JPanel anaMalzemePaneli = new JPanel(new BorderLayout());
        anaMalzemePaneli.add(malzemeScrollPane, BorderLayout.CENTER);
        anaMalzemePaneli.add(malzemeAraButonu, BorderLayout.SOUTH);

        frame.add(anaMalzemePaneli, BorderLayout.WEST);


        tarifListesi = new JTextArea();
        tarifListesi.setEditable(false);
        tarifListesi.setFont(new Font("Arial", Font.PLAIN, 14));
        tarifListesi.setBackground(Color.WHITE);
        tarifListesi.setBorder(BorderFactory.createLineBorder(Color.decode("#CCCCCC"), 2));
        JScrollPane tarifScrollPane = new JScrollPane(tarifListesi);
        tarifScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2"), 2),
                "Tarif Listesi", 0, 0, new Font("Arial", Font.BOLD, 14), Color.decode("#4A90E2")));



        eklemePaneli = new JPanel(new GridBagLayout());
        eklemePaneli.setBackground(Color.decode("#F4F4F9"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; eklemePaneli.add(new JLabel("Tarif Adı:"), gbc);
        tarifAdiField = new JTextField(15);
        tarifAdiField.setBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2"), 2));
        gbc.gridx = 1; eklemePaneli.add(tarifAdiField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; eklemePaneli.add(new JLabel("Kategori:"), gbc);
        kategoriField = new JTextField(15);
        kategoriField.setBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2"), 2));
        gbc.gridx = 1; eklemePaneli.add(kategoriField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; eklemePaneli.add(new JLabel("Hazırlama Süresi (dakika):"), gbc);
        hazirlamaSuresiField = new JTextField(15);
        hazirlamaSuresiField.setBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2"), 2));
        gbc.gridx = 1; eklemePaneli.add(hazirlamaSuresiField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; eklemePaneli.add(new JLabel("Talimatlar:"), gbc);
        talimatlarField = new JTextArea(4, 15);
        talimatlarField.setBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2"), 2));
        gbc.gridx = 1; gbc.gridheight = 2; eklemePaneli.add(new JScrollPane(talimatlarField), gbc);
        gbc.gridheight = 1;

        malzemeListesiModel = new DefaultListModel<>();
        malzemeListesi = new JList<>(malzemeListesiModel);
        gbc.gridx = 0; gbc.gridy = 5; eklemePaneli.add(new JLabel("Malzemeler:"), gbc);
        malzemeListesi.setBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2"), 2));
        gbc.gridx = 1; eklemePaneli.add(new JScrollPane(malzemeListesi), gbc);

        JPanel malzemePaneli = new JPanel(new FlowLayout());
        malzemeSecimiComboBox = new JComboBox<>();
        JTextField miktarTextField = new JTextField(5);
        miktarTextField.setBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2"), 2));
        JButton malzemeEkleButonu = new JButton("Malzeme Ekle");
        malzemeEkleButonu.setBackground(Color.decode("#4A90E2"));
        malzemeEkleButonu.setForeground(Color.WHITE);
        JButton yeniMalzemeEkleButonu = new JButton("Yeni Malzeme Ekle");
        yeniMalzemeEkleButonu.setBackground(Color.decode("#4A90E2"));
        yeniMalzemeEkleButonu.setForeground(Color.WHITE);

        JButton malzemeCikarButonu = new JButton("Malzeme Çıkar");
        malzemeCikarButonu.setBackground(Color.decode("#D0021B"));
        malzemeCikarButonu.setForeground(Color.WHITE);


        malzemeCikarButonu.addActionListener(e -> {
            int selectedIndex = malzemeListesi.getSelectedIndex();
            if (selectedIndex != -1) {
                malzemeListesiModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen çıkarmak için bir malzeme seçin.",
                        "Uyarı", JOptionPane.WARNING_MESSAGE);
            }
        });


        malzemePaneli.add(malzemeCikarButonu);


        malzemePaneli.add(malzemeSecimiComboBox);
        malzemePaneli.add(new JLabel("Miktar:"));
        malzemePaneli.add(miktarTextField);
        malzemePaneli.add(malzemeEkleButonu);
        malzemePaneli.add(yeniMalzemeEkleButonu);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; eklemePaneli.add(malzemePaneli, gbc);

        List<Malzeme> malzemeler = tarifService.tumMalzemeleriGetir();
        for (Malzeme malzeme : malzemeler) {
            malzemeSecimiComboBox.addItem(malzeme.getMalzemeAdi());
        }

        ekleButonu = new JButton("Tarif Ekle");
        ekleButonu.setBackground(Color.decode("#4A90E2"));
        ekleButonu.setForeground(Color.WHITE);
        ekleButonu.addActionListener(e -> tarifEkle());

        silButonu = new JButton("Tarif Sil");
        silButonu.setBackground(Color.decode("#D0021B"));
        silButonu.setForeground(Color.WHITE);
        silButonu.addActionListener(e -> tarifSil());

        guncelleButonu = new JButton("Tarif Güncelle");
        guncelleButonu.setBackground(Color.decode("#F5A623"));
        guncelleButonu.setForeground(Color.WHITE);
        guncelleButonu.addActionListener(e -> tarifGuncelle());


        JPanel butonPaneli = new JPanel(new FlowLayout());
        butonPaneli.add(ekleButonu);
        butonPaneli.add(silButonu);
        butonPaneli.add(guncelleButonu);


        ekleButonu.setToolTipText("Yeni tarif eklemek için tıklayın.");
        guncelleButonu.setToolTipText("Mevcut bir tarifi güncellemek için tıklayın.");
        silButonu.setToolTipText("Bir tarifi silmek için tıklayın.");


        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        eklemePaneli.add(butonPaneli, gbc);

        araButonu.addActionListener(e -> tarifAra(aramaAlani.getText()));

        malzemeEkleButonu.addActionListener(e -> {
            String malzeme = (String) malzemeSecimiComboBox.getSelectedItem();
            String miktar = miktarTextField.getText();

            if (malzeme != null && !miktar.isEmpty()) {
                malzemeListesiModel.addElement(malzeme + " - " + miktar);
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen malzeme ve miktar giriniz.");
            }
        });

        yeniMalzemeEkleButonu.addActionListener(e -> {
            String yeniMalzeme = JOptionPane.showInputDialog("Yeni malzeme adını girin:");
            if (yeniMalzeme != null && !yeniMalzeme.isEmpty()) {
                malzemeSecimiComboBox.addItem(yeniMalzeme);
                JOptionPane.showMessageDialog(null, "Yeni malzeme başarıyla eklendi.");
            } else {
                JOptionPane.showMessageDialog(null, "Malzeme adı boş olamaz!");
            }
        });



        JTabbedPane accordion = new JTabbedPane();
        JPanel sortPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton sortByTimeAsc = new JButton("Hazırlama Süresine Göre Artan");
        JButton sortByTimeDesc = new JButton("Hazırlama Süresine Göre Azalan");
        JButton sortByCostAsc = new JButton("Maliyete Göre Artan");
        JButton sortByCostDesc = new JButton("Maliyete Göre Azalan");

        sortByTimeAsc.addActionListener(e -> sortTariflerByTime(true));
        sortByTimeDesc.addActionListener(e -> sortTariflerByTime(false));
        sortByCostAsc.addActionListener(e -> {sortTariflerByCost(true);
        });
        sortByCostDesc.addActionListener(e -> {sortTariflerByCost(false);
        });

        sortPanel.add(sortByTimeAsc);
        sortPanel.add(sortByTimeDesc);
        sortPanel.add(sortByCostAsc);
        sortPanel.add(sortByCostDesc);

        accordion.addTab("Sıralama", sortPanel);

        JPanel filtrelemePaneli = new JPanel(new FlowLayout());
        kategoriFiltreField = new JTextField(10);
        maxHazirlamaSuresiField = new JTextField(5);
        filtreleButonu = new JButton("Filtrele");

        filtrelemePaneli.add(new JLabel("Kategori:"));
        filtrelemePaneli.add(kategoriFiltreField);
        filtrelemePaneli.add(new JLabel("Maks. Hazırlama Süresi:"));
        filtrelemePaneli.add(maxHazirlamaSuresiField);
        filtrelemePaneli.add(filtreleButonu);

        filtreleButonu.addActionListener(e -> filtreleTarifler());

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(filtrelemePaneli);
        rightPanel.add(accordion);

        frame.add(aramaPaneli, BorderLayout.NORTH);

        frame.add(tarifScrollPane, BorderLayout.CENTER);
        frame.add(eklemePaneli, BorderLayout.SOUTH);
        frame.add(rightPanel, BorderLayout.EAST);

        tarifListesi.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 1) {
                    try {
                        // İmlecin bulunduğu satırın başlangıç ve bitiş konumlarını al
                        int caretPosition = tarifListesi.getCaretPosition();
                        int start = Utilities.getRowStart(tarifListesi, caretPosition);
                        int end = Utilities.getRowEnd(tarifListesi, caretPosition);

                        // Seçilen satırı alıp trim ile baştaki ve sondaki boşlukları temizlemek için
                        String seciliSatir = tarifListesi.getText(start, end - start).trim();

                        if (seciliSatir.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Lütfen geçerli bir tarif satırına çift tıklayın.");
                            return;
                        }


                        if (seciliSatir.startsWith("Tarif Adı: ")) {
                            String seciliTarifAdi = seciliSatir.substring(11).trim(); // "Tarif Adı: " kısmını kes
                            if (!seciliTarifAdi.isEmpty()) {
                                tarifDetaylariniGoster(seciliTarifAdi);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Geçerli bir tarif adı bulunamadı.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Lütfen geçerli bir tarif satırına çift tıklayın.");
                        }

                    } catch (BadLocationException ex) {
                        JOptionPane.showMessageDialog(frame, "Tarif seçiminde bir hata oluştu: " + ex.getMessage(),
                                "Hata", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });


        JPanel maliyetPaneli = new JPanel(new FlowLayout());
        maliyetPaneli.setBorder(BorderFactory.createTitledBorder("Maliyet Aralığı"));

        minMaliyetField = new JTextField(5);
        maxMaliyetField = new JTextField(5);
        maliyetFiltreleButonu = new JButton("Maliyet'e göre Filtrele");

        maliyetPaneli.add(new JLabel("Min Maliyet:"));
        maliyetPaneli.add(minMaliyetField);
        maliyetPaneli.add(new JLabel("Max Maliyet:"));
        maliyetPaneli.add(maxMaliyetField);
        maliyetPaneli.add(maliyetFiltreleButonu);


        maliyetFiltreleButonu.addActionListener(e -> maliyetAraligiFiltrele());


        rightPanel.add(maliyetPaneli);

        frame.add(tarifScrollPane, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);


        tarifAra("");
        frame.setVisible(true);
    }


    private void tarifleriGoster(List<Tarif> tarifler, List<String> secilenMalzemeler) {
        tarifListesi.setText("");

        for (Tarif tarif : tarifler) {
            boolean yapilabilir = tarifService.tarifYapilabilir(tarif);
            double eksikMaliyet = tarifService.eksikMalzemeMaliyeti(tarif);

            String tarifBilgisi = tarif.getTarifAdi() + " - Eşleşme: %" +
                    String.format("%.2f", tarifService.hesaplaEslesmeYuzdesi(tarif, secilenMalzemeler)) +
                    (yapilabilir ? " (Tam Malzeme)" : " - Eksik Maliyet: " + eksikMaliyet + " TL") + "\n";

            appendTarifBilgisi(tarifBilgisi, yapilabilir);
        }
    }


    private void appendTarifBilgisi(String bilgi, boolean yapilabilir) {
        int start = tarifListesi.getDocument().getLength();
        try {
            tarifListesi.getDocument().insertString(start, bilgi, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        // Yapılabilir tarifler yeşil, eksik malzemeli tarifler kırmızı
        tarifListesi.setForeground(yapilabilir ? Color.GREEN : Color.RED);
    }

    private void tarifAra(String aramaTerimi) {
        List<Tarif> tarifler = tarifService.tarifAra(aramaTerimi);

        if (tarifler.isEmpty()) {
            tarifListesi.setText("Aradığınız kriterlerde tarif bulunamadı.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Tarif tarif : tarifler) {
                sb.append("Tarif Adı: ").append(tarif.getTarifAdi()).append("\n");
                sb.append("Kategori: ").append(tarif.getKategori()).append("\n");
                sb.append("Hazırlama Süresi: ").append(tarif.getHazirlamaSuresi()).append(" dakika\n");
                sb.append("Talimatlar: ").append(tarif.getTalimatlar()).append("\n");
                sb.append("--------------------------------------------------\n");
            }
            tarifListesi.setText(sb.toString());
        }
    }

    private void filtreleTarifler() {
        String kategori = kategoriFiltreField.getText();
        int maxHazirlamaSuresi;

        try {
            maxHazirlamaSuresi = Integer.parseInt(maxHazirlamaSuresiField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Maksimum hazırlama süresi geçerli bir sayı olmalıdır.");
            return;
        }

        List<Tarif> tarifler = tarifService.tarifFiltrele(kategori, maxHazirlamaSuresi);

        if (tarifler.isEmpty()) {
            tarifListesi.setText("Belirtilen kriterlere uyan tarif bulunamadı.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Tarif tarif : tarifler) {
                sb.append("Tarif Adı: ").append(tarif.getTarifAdi()).append("\n");
                sb.append("Kategori: ").append(tarif.getKategori()).append("\n");
                sb.append("Hazırlama Süresi: ").append(tarif.getHazirlamaSuresi()).append(" dakika\n");
                sb.append("Talimatlar: ").append(tarif.getTalimatlar()).append("\n");
                sb.append("--------------------------------------------------\n");
            }
            tarifListesi.setText(sb.toString());
        }
    }

    private void sortTariflerByTime(boolean artan) {
        List<Tarif> sortedTarifler = tarifService.siralaByHazirlamaSuresi(artan);
        displaySortedTarifler(sortedTarifler, false);
    }

    private void sortTariflerByCost(boolean artan) {
        try {
            List<Map<String, Object>> sortedTarifler = tarifService.siralaByMaliyet(artan);
            displaySortedTariflerWithCost(sortedTarifler);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Maliyet sıralaması yapılırken hata oluştu: " + e.getMessage());
        }
    }

    private void displaySortedTariflerWithCost(List<Map<String, Object>> sortedTarifler) {
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> tarif : sortedTarifler) {
            sb.append("Tarif Adı: ").append(tarif.get("TarifAdi")).append("\n");
            sb.append("Kategori: ").append(tarif.get("Kategori")).append("\n");
            sb.append("Hazırlama Süresi: ").append(tarif.get("HazirlamaSuresi")).append(" dakika\n");
            sb.append("Toplam Maliyet: ").append(tarif.get("ToplamMaliyet")).append(" TL\n");
            sb.append("Talimatlar: ").append(tarif.get("Talimatlar")).append("\n");
            sb.append("--------------------------------------------------\n");
        }
        tarifListesi.setText(sb.toString());
    }


    private void displaySortedTarifler(List<Tarif> sortedTarifler, boolean showCost) {
        StringBuilder sb = new StringBuilder();
        for (Tarif tarif : sortedTarifler) {
            sb.append("Tarif Adı: ").append(tarif.getTarifAdi()).append("\n");
            sb.append("Kategori: ").append(tarif.getKategori()).append("\n");
            sb.append("Hazırlama Süresi: ").append(tarif.getHazirlamaSuresi()).append(" dakika\n");
            sb.append("Talimatlar: ").append(tarif.getTalimatlar()).append("\n");
            sb.append("--------------------------------------------------\n");
        }
        tarifListesi.setText(sb.toString());
    }

    private void tarifEkle() {
        try {
            String tarifAdi = tarifAdiField.getText();
            String kategori = kategoriField.getText();
            int hazirlamaSuresi = Integer.parseInt(hazirlamaSuresiField.getText());
            String talimatlar = talimatlarField.getText();


            Tarif yeniTarif = new Tarif(tarifAdi, kategori, hazirlamaSuresi, talimatlar);
            int tarifID = tarifService.tarifEkle(yeniTarif);


            List<TarifMalzeme> malzemeler = new ArrayList<>();
            for (int i = 0; i < malzemeListesiModel.size(); i++) {
                String malzemeBilgi = malzemeListesiModel.getElementAt(i);
                String[] parts = malzemeBilgi.split(" - ");
                String malzemeAdi = parts[0];
                float miktar = Float.parseFloat(parts[1]);

                int malzemeID = tarifService.getMalzemeIdByName(malzemeAdi);
                if (malzemeID != -1) {
                    malzemeler.add(new TarifMalzeme(tarifID, malzemeID, miktar));
                }
            }

            // Tarifin malzemeleri ekleniyor
            tarifService.tarifMalzemeleriEkle(tarifID, malzemeler);

            JOptionPane.showMessageDialog(frame, "Tarif başarıyla eklendi!");
            temizleForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Tarif eklenirken bir hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createGuncellemePanel() {
        guncellemePaneli = new JPanel(new GridBagLayout());
        guncellemePaneli.setBackground(Color.decode("#F4F4F9"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; guncellemePaneli.add(new JLabel("Tarif Adı:"), gbc);
        tarifAdiField = new JTextField(15);
        gbc.gridx = 1; guncellemePaneli.add(tarifAdiField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; guncellemePaneli.add(new JLabel("Kategori:"), gbc);
        kategoriField = new JTextField(15);
        gbc.gridx = 1; guncellemePaneli.add(kategoriField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; guncellemePaneli.add(new JLabel("Hazırlama Süresi (dakika):"), gbc);
        hazirlamaSuresiField = new JTextField(15);
        gbc.gridx = 1; guncellemePaneli.add(hazirlamaSuresiField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; guncellemePaneli.add(new JLabel("Talimatlar:"), gbc);
        talimatlarField = new JTextArea(4, 15);
        gbc.gridx = 1; gbc.gridheight = 2;
        guncellemePaneli.add(new JScrollPane(talimatlarField), gbc);
        gbc.gridheight = 1;

        malzemeListesiModel = new DefaultListModel<>();
        malzemeListesi = new JList<>(malzemeListesiModel);
        gbc.gridx = 0; gbc.gridy = 5; guncellemePaneli.add(new JLabel("Malzemeler:"), gbc);
        gbc.gridx = 1; guncellemePaneli.add(new JScrollPane(malzemeListesi), gbc);

        JPanel malzemePaneli = new JPanel(new FlowLayout());
        malzemeSecimiComboBox = new JComboBox<>();
        JTextField miktarTextField = new JTextField(5);

        JButton malzemeEkleButonu = new JButton("Malzeme Ekle");
        malzemeEkleButonu.setBackground(Color.decode("#4A90E2"));
        malzemeEkleButonu.setForeground(Color.WHITE);

        JButton malzemeCikarButonu = new JButton("Malzeme Çıkar");
        malzemeCikarButonu.setBackground(Color.decode("#D0021B"));
        malzemeCikarButonu.setForeground(Color.WHITE);

        JButton yeniMalzemeEkleButonu = new JButton("Yeni Malzeme Ekle");
        yeniMalzemeEkleButonu.setBackground(Color.decode("#4A90E2"));
        yeniMalzemeEkleButonu.setForeground(Color.WHITE);

        // Malzeme ekleme işlemi
        malzemeEkleButonu.addActionListener(e -> {
            String malzeme = (String) malzemeSecimiComboBox.getSelectedItem();
            String miktar = miktarTextField.getText();

            if (malzeme != null && !miktar.isEmpty()) {
                malzemeListesiModel.addElement(malzeme + " - " + miktar);
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen malzeme ve miktar giriniz.");
            }
        });

        // Malzeme çıkarma işlemi
        malzemeCikarButonu.addActionListener(e -> {
            int selectedIndex = malzemeListesi.getSelectedIndex();
            if (selectedIndex != -1) {
                malzemeListesiModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen çıkarmak için bir malzeme seçin.",
                        "Uyarı", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Yeni malzeme ekleme işlemi
        yeniMalzemeEkleButonu.addActionListener(e -> {
            String yeniMalzeme = JOptionPane.showInputDialog("Yeni malzeme adını girin:");
            if (yeniMalzeme != null && !yeniMalzeme.isEmpty()) {
                malzemeSecimiComboBox.addItem(yeniMalzeme);
                JOptionPane.showMessageDialog(null, "Yeni malzeme başarıyla eklendi.");
            } else {
                JOptionPane.showMessageDialog(null, "Malzeme adı boş olamaz!");
            }
        });

        // Malzeme paneline bileşenleri ekle
        malzemePaneli.add(malzemeSecimiComboBox);
        malzemePaneli.add(new JLabel("Miktar:"));
        malzemePaneli.add(miktarTextField);
        malzemePaneli.add(malzemeEkleButonu);
        malzemePaneli.add(malzemeCikarButonu);
        malzemePaneli.add(yeniMalzemeEkleButonu);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        guncellemePaneli.add(malzemePaneli, gbc);

        // Mevcut malzemeleri doldur
        List<Malzeme> malzemeler = tarifService.tumMalzemeleriGetir();
        for (Malzeme malzeme : malzemeler) {
            malzemeSecimiComboBox.addItem(malzeme.getMalzemeAdi());
        }
    }


    private void tarifGuncelle() {
        String tarifAdi = JOptionPane.showInputDialog(frame, "Güncellemek istediğiniz tarifin adını girin:");
        Tarif mevcutTarif = tarifService.tarifBul(tarifAdi);

        if (mevcutTarif != null) {
            createGuncellemePanel(); // Güncelleme panelini oluştur.

            // Mevcut tarif bilgilerini GUI'ye yükle
            tarifAdiField.setText(mevcutTarif.getTarifAdi());
            kategoriField.setText(mevcutTarif.getKategori());
            hazirlamaSuresiField.setText(String.valueOf(mevcutTarif.getHazirlamaSuresi()));
            talimatlarField.setText(mevcutTarif.getTalimatlar());

            // Mevcut tarifin malzemelerini GUI'ye yükle
            List<TarifMalzeme> malzemeler = tarifService.getTarifMalzemeleri(mevcutTarif.getTarifID());
            malzemeListesiModel.clear();
            for (TarifMalzeme malzeme : malzemeler) {
                String malzemeAdi = tarifService.getMalzemeAdiById(malzeme.getMalzemeID());
                malzemeListesiModel.addElement(malzemeAdi + " - " + malzeme.getMalzemeMiktar());
            }

            // Güncelleme onayı için diyalog
            int onay = JOptionPane.showConfirmDialog(frame, guncellemePaneli, "Tarifi Güncelle", JOptionPane.OK_CANCEL_OPTION);

            if (onay == JOptionPane.OK_OPTION) {
                try {
                    // Tarif bilgilerini güncelle
                    mevcutTarif.setTarifAdi(tarifAdiField.getText());
                    mevcutTarif.setKategori(kategoriField.getText());
                    mevcutTarif.setHazirlamaSuresi(Integer.parseInt(hazirlamaSuresiField.getText()));
                    mevcutTarif.setTalimatlar(talimatlarField.getText());

                    // Veritabanında tarif bilgilerini güncelle
                    tarifService.tarifGuncelle(mevcutTarif);

                    // Yeni malzeme listesini oluştur ve GUI'den al
                    List<TarifMalzeme> yeniMalzemeler = new ArrayList<>();
                    for (int i = 0; i < malzemeListesiModel.size(); i++) {
                        String malzemeBilgi = malzemeListesiModel.getElementAt(i);
                        String[] parts = malzemeBilgi.split(" - ");
                        String malzemeAdi = parts[0];
                        float miktar = Float.parseFloat(parts[1]);

                        int malzemeID = tarifService.getMalzemeIdByName(malzemeAdi);
                        if (malzemeID != -1) {
                            yeniMalzemeler.add(new TarifMalzeme(mevcutTarif.getTarifID(), malzemeID, miktar));
                        }
                    }

                    // Malzemeleri güncelle
                    tarifService.tarifMalzemeleriGuncelle(mevcutTarif.getTarifID(), yeniMalzemeler);

                    JOptionPane.showMessageDialog(frame, "Tarif ve malzemeler başarıyla güncellendi!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Güncelleme sırasında hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Tarif bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void tarifSil() {
        String tarifAdi = JOptionPane.showInputDialog(frame, "Silmek istediğiniz tarifin adını girin:");
        tarifService.tarifSil(tarifAdi);

        JOptionPane.showMessageDialog(frame, "Tarif başarıyla silindi!");
    }

    private void tarifDetaylariniGoster(String tarifAdi) {
        // Seçilen tarifin detaylarını bulabilmek içib
        Tarif tarif = tarifService.tarifBul(tarifAdi);

        if (tarif != null) {
            StringBuilder detaylar = new StringBuilder();
            detaylar.append("Tarif Adı: ").append(tarif.getTarifAdi()).append("\n");
            detaylar.append("Kategori: ").append(tarif.getKategori()).append("\n");
            detaylar.append("Hazırlama Süresi: ").append(tarif.getHazirlamaSuresi()).append(" dakika\n");
            detaylar.append("Talimatlar: ").append(tarif.getTalimatlar()).append("\n");

            // Tarif malzemelerini göstermek için
            List<TarifMalzeme> malzemeler = tarifService.getTarifMalzemeleri(tarif.getTarifID());
            if (!malzemeler.isEmpty()) {
                detaylar.append("Malzemeler:\n");
                for (TarifMalzeme tarifMalzeme : malzemeler) {

                    String malzemeAdi = tarifService.getMalzemeAdiById(tarifMalzeme.getMalzemeID());
                    detaylar.append("- ").append(malzemeAdi)
                            .append(": ").append(tarifMalzeme.getMalzemeMiktar()).append("\n");
                }
            } else {
                detaylar.append("Bu tarif için malzeme bulunamadı.\n");
            }


            JOptionPane.showMessageDialog(frame, detaylar.toString(), "Tarif Detayları", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Tarif bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void maliyetAraligiFiltrele() {
        try {
            float minMaliyet = Float.parseFloat(minMaliyetField.getText());
            float maxMaliyet = Float.parseFloat(maxMaliyetField.getText());

            List<Map<String, Object>> tarifler = tarifService.tarifleriMaliyetAraliginaGoreFiltrele(minMaliyet, maxMaliyet);

            if (tarifler.isEmpty()) {
                tarifListesi.setText("Belirtilen maliyet aralığında tarif bulunamadı.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Map<String, Object> tarif : tarifler) {
                    sb.append("Tarif Adı: ").append(tarif.get("TarifAdi")).append("\n");
                    sb.append("Kategori: ").append(tarif.get("Kategori")).append("\n");
                    sb.append("Hazırlama Süresi: ").append(tarif.get("HazirlamaSuresi")).append(" dakika\n");
                    sb.append("Toplam Maliyet: ").append(tarif.get("ToplamMaliyet")).append(" TL\n");
                    sb.append("Talimatlar: ").append(tarif.get("Talimatlar")).append("\n");
                    sb.append("--------------------------------------------------\n");
                }
                tarifListesi.setText(sb.toString());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Lütfen geçerli bir sayı giriniz!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void temizleForm() {
        // Tarif bilgilerini temizlemek için
        tarifAdiField.setText("");
        kategoriField.setText("");
        hazirlamaSuresiField.setText("");
        talimatlarField.setText("");

        // Malzeme listesini temizlek için
        malzemeListesiModel.clear();
        malzemeSecimiComboBox.setSelectedIndex(-1);
    }

    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel(new FlatCyanLightIJTheme());


            UIManager.put("frame.background", new Color(113, 177, 236)); // Alice Blue (Yumuşak mavi)
            UIManager.put("Button.background", new Color(221, 189, 245)); // Papaya Whip (Pastel turuncu)
            UIManager.put("Button.foreground", Color.DARK_GRAY); // Düğme yazı rengi

            UIManager.put("ComboBox.background", new Color(224, 255, 255)); // Light Cyan
            UIManager.put("ComboBox.foreground", Color.DARK_GRAY);


            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(TarifRehberiGUI::new);
    }

}

