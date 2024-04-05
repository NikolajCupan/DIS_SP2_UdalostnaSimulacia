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

    public double getNajnovsiaVaha()
    {
        return this.data.getLast().vaha;
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

    public double getPriemer()
    {
        double menovatel = this.data.getLast().vaha;
        if (menovatel == 0.0 || this.data.size() == 1)
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
}
