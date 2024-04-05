package org.example.Simulacia.System.Agenti.Objekty;

import org.example.Simulacia.Statistiky.SpojitaStatistika;

public class Okno
{
    private static final int OKNO_OBSADENE = 1;
    private static final int OKNO_VOLNE = 0;

    private boolean obsadene;
    private final SpojitaStatistika statistikaVytazenie;

    public Okno()
    {
        this.obsadene = false;
        this.statistikaVytazenie = new SpojitaStatistika();
    }

    public boolean getObsadene()
    {
        return this.obsadene;
    }

    public void setObsadene(boolean obsadene, double simulacnyCas)
    {
        this.obsadene = obsadene;
        this.aktualizujStatistiku(simulacnyCas);
    }

    public double getVytazenie()
    {
        return this.statistikaVytazenie.getPriemer();
    }

    private void aktualizujStatistiku(double simulacnyCas)
    {
        if (this.obsadene)
        {
            this.statistikaVytazenie.pridajHodnotu(simulacnyCas, Okno.OKNO_OBSADENE);
        }
        else
        {
            this.statistikaVytazenie.pridajHodnotu(simulacnyCas, Okno.OKNO_VOLNE);
        }
    }
}
