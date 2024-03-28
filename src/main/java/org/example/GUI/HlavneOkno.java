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
    private JLabel labelSimulacnyCas;

    private JSlider sliderRychlost;
    private JLabel labelRychlost;
    private Thread simulacneVlakno;

    private SimulaciaSystem simulacia;

    public HlavneOkno()
    {
        setTitle("Aplikácia - Nikolaj Cupan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1550, 750);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(this.panel);

        this.inicializujSlider();

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

        this.buttonPauza.addActionListener(e -> this.toggleSimulaciaPozastavena());
        this.buttonStop.addActionListener(e -> this.ukonciSimulaciu());

        this.sliderRychlost.addChangeListener(e -> {
            int rychlost = this.getRychlost();
            if (rychlost >= Konstanty.MAX_RYCHLOST)
            {
                this.labelRychlost.setText("MAX");
            }
            else
            {
                this.labelRychlost.setText(rychlost + "x");
            }

            if (this.simulacia != null)
            {
                this.simulacia.setRychlost(rychlost);
            }
        });
    }

    private void inicializujSimulaciu()
    {
        if (this.simulacia != null)
        {
            this.ukonciSimulaciu();
            this.resetujGUI();
        }

        int pocetReplikacii = Integer.parseInt(this.inputPocetReplikacii.getText());
        boolean nasadaZadana = !this.inputNasada.getText().isEmpty();
        int nasada = (nasadaZadana ? Integer.parseInt(this.inputNasada.getText()) : -1);

        int pocetObsluznychMiest = Integer.parseInt(this.inputPocetObsluznychMiest.getText());
        int pocetPokladni = Integer.parseInt(this.inputPocetPokladni.getText());

        if (pocetObsluznychMiest < 3)
        {
            throw new RuntimeException("Pocet okien nemoze byt mensi ako 3!");
        }

        this.simulacia = new SimulaciaSystem(pocetReplikacii, this.getRychlost(), Konstanty.OTVARACIA_DOBA_SEKUND,
        pocetObsluznychMiest, pocetPokladni, nasada, nasadaZadana);
        this.simulacia.pridajDelegata(this);

        this.simulacneVlakno = new Thread(() -> this.simulacia.simuluj());
        this.simulacneVlakno.setName("Simulacia");
        this.simulacneVlakno.setDaemon(true);
        this.simulacneVlakno.setPriority(Thread.MAX_PRIORITY);
        this.simulacneVlakno.start();
    }

    private void ukonciSimulaciu()
    {
        if (this.simulacia != null)
        {
            this.simulacia.ukonciSimulaciu();
        }

        try
        {
            if (this.simulacneVlakno != null)
            {
                this.simulacneVlakno.join();
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri cakani na ukoncenie simulacneho vlakna!");
        }
    }

    private void toggleSimulaciaPozastavena()
    {
        if (this.simulacia != null)
        {
           this.simulacia.toggleSimulaciaPozastavena();
        }
    }

    private void resetujGUI()
    {
        this.labelAktualnaReplikacia.setText("n/a");
        this.labelCelkovyPriemernyCasSystem.setText("n/a");
        this.labelSimulacnyCas.setText("n/a");
    }

    private int getRychlost()
    {
        int zadanaRychlost = this.sliderRychlost.getValue();
        if (zadanaRychlost == 0)
        {
            return 1;
        }

        return zadanaRychlost;
    }

    private void inicializujSlider()
    {
        this.sliderRychlost.setValueIsAdjusting(true);
        this.sliderRychlost.setValue(Konstanty.DEFAULT_RYCHLOST);
        this.sliderRychlost.setValueIsAdjusting(false);

        this.labelRychlost.setText(Konstanty.DEFAULT_RYCHLOST + "x");
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
        this.labelSimulacnyCas.setText(Prezenter.simulacnyCas(simulacia));
    }
}
