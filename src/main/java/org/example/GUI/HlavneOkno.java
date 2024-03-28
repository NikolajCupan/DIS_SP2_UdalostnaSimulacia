package org.example.GUI;

import org.example.Ostatne.Konstanty;
import org.example.Ostatne.Prezenter;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.System.SimulaciaSystem;

import javax.swing.*;

public class HlavneOkno extends JFrame implements ISimulationDelegate
{
    private JPanel panel;

    private JTextField inputPocetReplikacii;
    private JTextField inputNasada;
    private JTextField inputPocetObsluznychMiest;
    private JTextField inputPocetPokladni;

    private JButton buttonStart;
    private JButton buttonPauza;
    private JButton buttonStop;

    private JLabel labelAktualnaReplikacia;
    private JLabel labelCelkovyPriemernyCasSystem;
    private SimulaciaSystem simulacia;

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
                this.inicializujSimulaciu();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(HlavneOkno.this, "Neplatne zadane parametre simulacie!");
            }
        });

        this.buttonPauza.addActionListener(e -> this.simulacia.toggleSimulaciaPozastavena());
        this.buttonStop.addActionListener(e -> this.simulacia.ukonciSimulaciu());
    }

    private void inicializujSimulaciu()
    {
        int pocetReplikacii = Integer.parseInt(this.inputPocetReplikacii.getText());
        boolean nasadaZadana = !this.inputNasada.getText().isEmpty();
        int nasada = (nasadaZadana ? Integer.parseInt(this.inputNasada.getText()) : -1);

        int pocetObsluznychMiest = Integer.parseInt(this.inputPocetObsluznychMiest.getText());
        int pocetPokladni = Integer.parseInt(this.inputPocetPokladni.getText());

        if (pocetObsluznychMiest < 3)
        {
            throw new RuntimeException("Pocet okien nemoze byt mensi ako 3!");
        }

        this.simulacia = new SimulaciaSystem(pocetReplikacii, Konstanty.OTVARACIA_DOBA_SEKUND,
        pocetObsluznychMiest, pocetPokladni, nasada, nasadaZadana);
        this.simulacia.pridajDelegata(this);

        Thread vlakno = new Thread(() -> this.simulacia.simuluj());
        vlakno.setName("Simulacia");
        vlakno.setDaemon(true);
        vlakno.setPriority(Thread.MAX_PRIORITY);
        vlakno.start();
    }

    public void createUIComponents()
    {
    }

    @Override
    public void aktualizujSa(SimulacneJadro simulacneJadro)
    {
        SimulaciaSystem simulacia = (SimulaciaSystem)simulacneJadro;

        // Celkove informacie
        this.labelAktualnaReplikacia.setText(String.valueOf(simulacia.getAktualnaReplikacia()));
        this.labelCelkovyPriemernyCasSystem.setText(Prezenter.celkovaStatistikaSystem(simulacia));

        // Informacie aktualnej replikacie
    }
}
