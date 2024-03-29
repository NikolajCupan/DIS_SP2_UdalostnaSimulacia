package org.example.GUI;

import org.example.Ostatne.Konstanty;
import org.example.Ostatne.Prezenter;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.SimulaciaSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.SortedSet;

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

    private JTable tabulkaAgenti;

    private SimulaciaSystem simulacia;
    private Thread simulacneVlakno;

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

        this.sliderRychlost.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                super.mouseReleased(e);;

                int rychlost = HlavneOkno.this.getRychlost();
                if (rychlost >= Konstanty.MAX_RYCHLOST)
                {
                    HlavneOkno.this.labelRychlost.setText("MAX");
                }
                else
                {
                    HlavneOkno.this.labelRychlost.setText(rychlost + "x");
                }

                if (HlavneOkno.this.simulacia != null)
                {
                    HlavneOkno.this.simulacia.setRychlost(rychlost);
                }
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
            this.simulacia = null;
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
        ((DefaultTableModel)this.tabulkaAgenti.getModel()).setRowCount(0);
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
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Prichod system");
        model.addColumn("Zaciatok automat");
        model.addColumn("Koniec automat");
        model.addColumn("Zaciatok obsluha");
        model.addColumn("Koniec obsluha");
        this.tabulkaAgenti = new JTable(model);
    }

    @Override
    public void aktualizujSa(SimulacneJadro simulacneJadro)
    {
        SimulaciaSystem simulacia = (SimulaciaSystem)simulacneJadro;

        // Celkove informacie
        Prezenter.aktualnaReplikacia(simulacia, this.labelAktualnaReplikacia);
        Prezenter.celkovyPriemernyCasSystem(simulacia, this.labelCelkovyPriemernyCasSystem);

        // Informacie aktualnej replikacie
        Prezenter.simulacnyCas(simulacia, this.labelSimulacnyCas);
        Prezenter.tabulkaAgentov(simulacia, this.tabulkaAgenti);
    }
}
