package org.example.Simulacia.Statistiky;

import java.util.ArrayList;

public class SpojitaStatistika
{
    private static class Stav
    {
        private final double vaha;
        private final int hodnota;

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
        Stav najnovsiStav = this.data.getLast();
        if (vaha < najnovsiStav.vaha)
        {
            throw new RuntimeException("Element pridavany do spojitej statistiky nemoze mat mensiu vahu!");
        }

        this.data.add(new Stav(vaha, hodnota));
    }

    public double getPriemer(double vaha, int hodnota)
    {
        this.data.add(new Stav(vaha, hodnota));
        this.skontrolujData();

        double menovatel = this.data.getLast().vaha;
        if (menovatel == 0.0)
        {
            return 0.0;
        }

        double citatel = 0.0;
        for (int i = 0; i < this.data.size() - 1; i++)
        {
            Stav curStav = this.data.get(i);
            Stav nextStav = this.data.get(i + 1);
            citatel += (nextStav.vaha - curStav.vaha) * curStav.hodnota;
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

        if (this.data.size() < 2)
        {
            throw new RuntimeException("Spojita statistika neobsahuje uzatvaraci element!");
        }

        Stav predposledny = this.data.get(this.data.size() - 2);
        Stav posledny = this.data.getLast();
        if (predposledny.hodnota != posledny.hodnota)
        {
            throw new RuntimeException("Posledny element spojitej statistiky je neplatny!");
        }
    }
}
