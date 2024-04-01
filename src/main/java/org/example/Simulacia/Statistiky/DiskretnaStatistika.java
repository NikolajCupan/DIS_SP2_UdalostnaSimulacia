package org.example.Simulacia.Statistiky;

import org.example.Ostatne.Konstanty;

import java.util.ArrayList;

public class DiskretnaStatistika
{
    private final ArrayList<Double> data;

    private int pocetHodnot;
    private double sucet;
    private double sucetDruheMocniny;

    private double priemer;
    private double smerodajnaOdchylka;

    private final double kvantil;
    private final double kvantilHodnota;
    private double dolnaHranicaIS;
    private double hornaHranicaIS;

    private boolean statistikyVypocitane;

    public DiskretnaStatistika(double kvantil, double kvantilHodnota)
    {
        this.data = new ArrayList<>();

        this.pocetHodnot = 0;
        this.sucet = 0.0;
        this.sucetDruheMocniny = 0.0;

        this.priemer = -1.0;
        this.smerodajnaOdchylka = -1.0;

        this.kvantil = kvantil;
        this.kvantilHodnota = kvantilHodnota;
        this.dolnaHranicaIS = -1.0;
        this.hornaHranicaIS = -1.0;

        this.statistikyVypocitane = false;
    }

    public void pridajHodnotu(double hodnota)
    {
        this.pocetHodnot++;
        this.sucet += hodnota;
        this.sucetDruheMocniny += Math.pow(hodnota, 2);

        if (Konstanty.STATISTIKY_ZOZNAM_DAT)
        {
            this.data.add(hodnota);
        }
    }

    public void vypisHodnoty()
    {
        if (!Konstanty.STATISTIKY_ZOZNAM_DAT)
        {
            throw new RuntimeException("Nie je aktivovane zaznamenavanie jednotlivych hodnot!");
        }

        for (double hodnota : this.data)
        {
            System.out.println(hodnota);
        }
    }

    public void skusPrepocitatStatistiky()
    {
        if (this.pocetHodnot < 2)
        {
            // Nie je mozne vypocitat statistiky, nakolko nie je k dispozicii dostatok hodnot
            return;
        }

        this.vypocitajPriemer();
        this.vypocitajSmerodajnuOdchylku();
        this.vypocitajIntervalSpolahlivosti();

        this.statistikyVypocitane = true;
    }

    private void vypocitajPriemer()
    {
        this.priemer = this.sucet / this.pocetHodnot;
    }

    private void vypocitajSmerodajnuOdchylku()
    {
        if (this.priemer == -1)
        {
            throw new RuntimeException("Nie je mozne vypocitat smerodajnu odchylku ak nie je vypocitany priemer!");
        }

        double citatel = this.sucetDruheMocniny - (Math.pow(this.sucet, 2) / this.pocetHodnot);
        double menovatel = this.pocetHodnot - 1;

        this.smerodajnaOdchylka = Math.sqrt(citatel / menovatel);
    }

    private void vypocitajIntervalSpolahlivosti()
    {
        if (this.priemer == -1 || this.smerodajnaOdchylka == -1)
        {
            throw new RuntimeException("Nie je mozne vypocitat interval spolahlivosti ak priemer a smerodajna odchylka nie su vypocitane!");
        }

        this.dolnaHranicaIS = this.priemer - (this.kvantilHodnota * this.smerodajnaOdchylka) / Math.sqrt(this.pocetHodnot);
        this.hornaHranicaIS = this.priemer + (this.kvantilHodnota * this.smerodajnaOdchylka) / Math.sqrt(this.pocetHodnot);
    }

    public double getKvantil()
    {
        return this.kvantil;
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

    public double forceGetPriemer()
    {
        if (this.pocetHodnot == 0)
        {
            return -1;
        }
        else
        {
            return this.sucet / this.pocetHodnot;
        }
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
