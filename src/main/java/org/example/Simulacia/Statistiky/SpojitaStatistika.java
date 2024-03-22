package org.example.Simulacia.Statistiky;

import java.util.ArrayList;

public class SpojitaStatistika
{
    private static class Stav
    {
        protected double vaha;
        protected int hodnota;

        protected Stav(double vaha, int hodnota)
        {
            this.vaha = vaha;
            this.hodnota = hodnota;
        }
    }

    private final ArrayList<Stav> data;

    public SpojitaStatistika()
    {
        this.data = new ArrayList<>();

        // Prvotny stav
        this.data.add(new Stav(0.0, 0));
    }

    public void pridajHodnotu(double vaha, int hodnota)
    {
        this.data.add(new Stav(vaha, hodnota));
    }

    public double getPriemer()
    {
        this.skontrolujData();

        if (this.data.size() == 1)
        {
            // Statistika neobsahuje ziadne zaznamy
            return 0;
        }

        double citatel = 0.0;
        double menovatel = this.data.getLast().vaha;

        for (int i = 0; i < this.data.size() - 1; i++)
        {
            Stav curStav = this.data.get(i);
            Stav nextStav = this.data.get(i + 1);
            citatel += (nextStav.vaha - curStav.vaha) * curStav.hodnota;
        }

        if (menovatel == 0.0)
        {
            return 0.0;
        }

        return citatel / menovatel;
    }

    private void skontrolujData()
    {
        Stav prvy = this.data.getFirst();
        if (prvy.vaha != 0.0 || prvy.hodnota != 0)
        {
            throw new RuntimeException("Prvy element spojitej statistiky je neplatny!");
        }

        if (this.data.size() == 1)
        {
            return;
        }

        Stav predposledny = this.data.get(this.data.size() - 2);
        Stav posledny = this.data.getLast();
        if (predposledny.hodnota != posledny.hodnota)
        {
            throw new RuntimeException("Posledny element spojitej statistiky je neplatny!");
        }
    }
}
