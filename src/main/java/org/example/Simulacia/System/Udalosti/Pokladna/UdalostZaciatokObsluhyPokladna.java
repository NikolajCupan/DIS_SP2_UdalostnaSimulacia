package org.example.Simulacia.System.Udalosti.Pokladna;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.Pokladna;

public class UdalostZaciatokObsluhyPokladna extends Udalost
{
    private final Pokladna pokladna;

    public UdalostZaciatokObsluhyPokladna(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, Pokladna pokladna)
    {
        super(simulacneJadro, casVykonania, agent);

        this.pokladna = pokladna;
    }

    private void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_UDALOST)
        {
            System.out.print("[UDALOST ");
            System.out.format("%6s", this.getAgent().getID());
            System.out.print("]   ");
            System.out.format("%-35s", "Zaciatok obsluhy pokladna");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
    }
}
