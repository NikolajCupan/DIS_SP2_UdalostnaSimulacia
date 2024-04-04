package org.example.NewGUI.Okno;

import org.example.NewGUI.Graf;
import org.example.NewGUI.ISimulationDelegate;
import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.System.SimulaciaSystem;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExperimentOkno implements ISimulationDelegate
{
    private final JLabel labelAktualnaReplikacia;
    private final JPanel panelGraf;

    private final int minPocetPokladni;
    private final int maxPocetPokladni;

    private Graf grafExperiment;
    private Thread experimentVlakno;

    private final List<SimulaciaSystem> simulacie;

    public ExperimentOkno(JLabel labekAktualnaReplikacia, JPanel panelGraf, int minPocetPokladni, int maxPocetPokladni)
    {
        this.validujVstupy(minPocetPokladni, maxPocetPokladni);
        this.minPocetPokladni = minPocetPokladni;
        this.maxPocetPokladni = maxPocetPokladni;

        this.labelAktualnaReplikacia = labekAktualnaReplikacia;
        this.panelGraf = panelGraf;

        this.simulacie = Collections.synchronizedList(new ArrayList<>(this.maxPocetPokladni - this.minPocetPokladni + 1));

        this.inicializujGraf();
    }

    private void inicializujGraf()
    {
        this.grafExperiment = new Graf(Konstanty.MIN_POCET_POKLADNI, Konstanty.MAX_POCET_POKLADNI, Konstanty.HORNA_HRANICA_GRAF);

        int panelSirka = this.panelGraf.getWidth();
        int panelVyska = this.panelGraf.getHeight();
        ChartPanel chartPanel = new ChartPanel(this.grafExperiment.getGraf());
        chartPanel.setBounds(0, 0, panelSirka, panelVyska);

        this.panelGraf.add(chartPanel);
        this.panelGraf.repaint();
        this.panelGraf.revalidate();
    }

    public void spustiExperiment(int pocetReplikacii, double dlzkaTrvaniaSimulacie, int pocetObsluznychMiest,
                                 int nasada, boolean pouziNasadu)
    {
        for (int i = this.minPocetPokladni; i <= this.maxPocetPokladni; i++)
        {
            this.simulacie.add(i - this.minPocetPokladni, new SimulaciaSystem(pocetReplikacii, Konstanty.MAX_RYCHLOST, dlzkaTrvaniaSimulacie,
                pocetObsluznychMiest, i, nasada, pouziNasadu));
            this.simulacie.get(i - this.minPocetPokladni).pridajDelegata(this);
        }

        this.experimentVlakno = new Thread(() -> {
            for (int i = this.minPocetPokladni; i <= this.maxPocetPokladni; i++)
            {
                if (this.simulacie.get(i - this.minPocetPokladni) != null)
                {
                    this.simulacie.get(i - this.minPocetPokladni).simuluj();
                }
            }
        });
        this.experimentVlakno.setName("Experiment");
        this.experimentVlakno.setDaemon(true);
        this.experimentVlakno.setPriority(Thread.MAX_PRIORITY);
        this.experimentVlakno.start();

    }

    private void validujVstupy(int minHodnota, int maxHodnota)
    {
        if (minHodnota < 1)
        {
            throw new RuntimeException("Minimalna hodnota nemoze byt mensia ako 1!");
        }

        if (minHodnota >= maxHodnota)
        {
            throw new RuntimeException("Minimalna hodnota musi byt mensia ako maximalna hodnota!");
        }
    }

    public void ukonciExperiment()
    {
        for (int i = this.minPocetPokladni; i <= this.maxPocetPokladni; i++)
        {
            if (this.simulacie.get(i - this.minPocetPokladni) != null)
            {
                this.simulacie.get(i - this.minPocetPokladni).ukonciSimulaciu();
                this.simulacie.get(i - this.minPocetPokladni).odoberDelegata(this);
                this.simulacie.set(i - this.minPocetPokladni, null);
            }
        }
    }

    @Override
    public void aktualizujSa(SimulacneJadro simulacneJadro, boolean celkoveStatistiky, boolean priebezneStatistiky)
    {
        if (priebezneStatistiky)
        {
            // Okno experimentu neobsahuje priebezne statistiky
        }

        if (celkoveStatistiky)
        {
            SimulaciaSystem simulacia = (SimulaciaSystem)simulacneJadro;
            this.grafExperiment.aktualizujGraf(simulacia.getPocetPokladni(), simulacia.getCelkovaStatistikaDlzkaFrontAutomat().forceGetPriemer());
            this.labelAktualnaReplikacia.setText(simulacia.getAktualnaReplikacia() + "/" + simulacia.getPocetReplikacii());
        }
    }

    @Override
    public void aktualizujSimulacnyCas(SimulacneJadro simulacneJadro)
    {
        throw new RuntimeException("Okno experimentu neobsahuje simulacny cas!");
    }
}
