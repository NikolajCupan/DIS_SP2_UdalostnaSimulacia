package org.example.Simulacia.Statistiky;

import org.example.Ostatne.Konstanty;

import java.util.ArrayList;

public class DiskretnaStatistika
{
    private final ArrayList<Double> data;

    private double priemer;
    private double smerodajnaOdchylka;

    private double dolnaHranicaIS;
    private double hornaHranicaIS;

    private boolean statistikyVypocitane;

    public DiskretnaStatistika()
    {
        this.data = new ArrayList<>();

        this.priemer = -1;
        this.smerodajnaOdchylka = -1;

        this.dolnaHranicaIS = -1;
        this.hornaHranicaIS = -1;

        this.statistikyVypocitane = false;
    }

    public void pridajHodnotu(double cas)
    {
        this.data.add(cas);
    }

    public void vypisHodnoty()
    {
        if (!this.statistikyVypocitane)
        {
            throw new RuntimeException("Statistiky neboli vypocitane!");
        }

        for (double hodnota : this.data)
        {
            System.out.println(hodnota);
        }
    }

    public void skusPrepocitatStatistiky()
    {
        if (this.data.isEmpty())
        {
            return;
        }

        this.vypocitajPriemer();
        this.vypocitajSmerodajnuOdchylku();
        this.vypocitajIntervalSpolahlivosti();

        this.statistikyVypocitane = true;
    }

    private void vypocitajPriemer()
    {
        final int pocetZaznamov = this.data.size();
        double sucetHodnot = 0.0;

        for (double hodnota : this.data)
        {
            sucetHodnot += hodnota;
        }

        this.priemer = sucetHodnot / pocetZaznamov;
    }

    private void vypocitajSmerodajnuOdchylku()
    {
        if (this.priemer == -1)
        {
            throw new RuntimeException("Nie je mozne vypocitat smerodajnu odchylku ak nie je vypocitane priemer!");
        }

        double citatel = 0.0;
        for (Double hodnota : this.data)
        {
            citatel += Math.pow(hodnota - this.priemer, 2);
        }

        int menovatel = this.data.size() - 1;
        this.smerodajnaOdchylka = Math.sqrt(citatel / menovatel);
    }

    private void vypocitajIntervalSpolahlivosti()
    {
        if (this.priemer == -1 || this.smerodajnaOdchylka == -1)
        {
            throw new RuntimeException("Nie je mozne vypocitat IS ak nie je vypocitane priemer a smerodajna odchylka!");
        }

        this.dolnaHranicaIS = this.priemer - Konstanty.KVANTIL_99_PERCENT
            * (this.smerodajnaOdchylka / Math.sqrt(this.data.size()));
        this.hornaHranicaIS = this.priemer + Konstanty.KVANTIL_99_PERCENT
            * (this.smerodajnaOdchylka / Math.sqrt(this.data.size()));
    }

    public boolean getStatistikyVypocitane()
    {
        return this.statistikyVypocitane;
    }

    public double getPriemer()
    {
        if (!this.statistikyVypocitane)
        {
            throw new RuntimeException("Statistiky neboli vypocitane!");
        }

        return this.priemer;
    }

    public double getDolnaHranicaIS()
    {
        if (!this.statistikyVypocitane)
        {
            throw new RuntimeException("Statistiky neboli vypocitane!");
        }

        return this.dolnaHranicaIS;
    }

    public double getHornaHranicaIS()
    {
        if (!this.statistikyVypocitane)
        {
            throw new RuntimeException("Statistiky neboli vypocitane!");
        }

        return this.hornaHranicaIS;
    }
}
