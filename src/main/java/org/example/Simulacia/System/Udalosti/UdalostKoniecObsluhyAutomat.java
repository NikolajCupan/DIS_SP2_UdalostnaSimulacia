package org.example.Simulacia.System.Udalosti;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.SimulaciaSystem;

public class UdalostKoniecObsluhyAutomat extends Udalost
{
    public UdalostKoniecObsluhyAutomat(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
    {
        super(simulacneJadro, casVykonania, agent);
    }

    private void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_UDALOST)
        {
            System.out.print("[UDALOST ");
            System.out.format("%6s", this.getAgent().getID());
            System.out.print("]   ");
            System.out.format("%-35s", "Koniec obsluhy automat");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        simulacia.setObsluhaAutomatPrebieha(false);

        // Nastavenie atributov agenta, ktory udalost vykonava
        Agent vykonavajuciAgent = this.getAgent();
        vykonavajuciAgent.setCasKoniecObsluhyAutomat(this.getCasVykonania());
        vykonavajuciAgent.vypis();

        // Zaznamenanie statistik
        simulacia.getStatistikaCasSystem().pridajHodnotu(vykonavajuciAgent.getCasKoniecObsluhyAutomat() - vykonavajuciAgent.getCasPrichod());

        // Naplanovanie dalsej obsluhy
        if (simulacia.getPocetAgentovVoFronteAutomat() == 0)
        {
            // Front je prazdny, nemozno naplanovat dalsiu obsluhu
        }
        else
        {
            Agent odobratyAgent = simulacia.odoberAgentaZFrontuAutomat();
            UdalostZaciatokObsluhyAutomat zaciatokObsluhy = new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), odobratyAgent);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
    }
}
