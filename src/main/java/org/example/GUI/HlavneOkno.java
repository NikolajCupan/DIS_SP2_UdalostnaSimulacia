package org.example.GUI;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Stanok.SimulaciaStanok;

import javax.swing.*;

public class HlavneOkno extends JFrame
{
    private JPanel panel;

    private JTextField inputPocetReplikacii;
    private JTextField inputNasada;

    private JButton buttonStart;
    private JButton buttonPauza;
    private JButton buttonStop;

    private SimulaciaStanok simulacia;

    public HlavneOkno()
    {
        setTitle("Aplikácia - Nikolaj Cupan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1550, 750);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(this.panel);

        this.buttonStart.addActionListener(e -> {
            try
            {
                int pocetReplikacii = Integer.parseInt(this.inputPocetReplikacii.getText());
                boolean nasadaZadana = !this.inputNasada.getText().isEmpty();
                int nasada = (nasadaZadana ? Integer.parseInt(this.inputNasada.getText()) : -1);

                this.simulacia = new SimulaciaStanok(pocetReplikacii, Konstanty.KONIEC_SEKUND - Konstanty.ZACIATOK_SEKUND,
                    nasada, nasadaZadana);
                new Thread(() ->  this.simulacia.simuluj()).start();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(HlavneOkno.this, "Neplatne zadane parametre simulacie!");
            }
        });

        this.buttonPauza.addActionListener(e -> this.simulacia.toggleSimulaciaPozastavena());
        this.buttonStop.addActionListener(e -> this.simulacia.ukonciSimulaciu());
    }

    public void createUIComponents()
    {
    }
}
