package org.example.Simulacia.Stanok.Udalosti;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Stanok.Agent;
import org.example.Simulacia.Stanok.SimulaciaStanok;
import org.example.Simulacia.Jadro.Udalost;

public class UdalostKoniecObsluhy extends Udalost
{
    public UdalostKoniecObsluhy(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
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
            System.out.format("%-35s", "Koniec obsluhy zakaznika");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaStanok simulacia = (SimulaciaStanok)this.getSimulacneJadro();
        simulacia.setObsluhaPrebieha(false);

        // Nastavenie atributov agenta, ktory udalost vykonava
        Agent vykonavajuciAgent = this.getAgent();
        vykonavajuciAgent.setCasKoniecObsluhy(this.getCasVykonania());
        vykonavajuciAgent.vypis();

        // Zaznamenanie statistik
        simulacia.getStatistikaCasSystem().pridajHodnotu(vykonavajuciAgent.getCasKoniecObsluhy() - vykonavajuciAgent.getCasPrichod());

        // Naplanovanie dalsej obsluhy
        if (simulacia.getPocetAgentovVoFronte() == 0)
        {
            // Front je prazdny, nemozno naplanovat dalsiu obsluhu
        }
        else
        {
            Agent odobratyAgent = simulacia.odoberAgentaZFrontu();
            UdalostZaciatokObsluhy zaciatokObsluhy = new UdalostZaciatokObsluhy(simulacia, this.getCasVykonania(), odobratyAgent);
            simulacia.naplanujUdalost(zaciatokObsluhy);

            simulacia.getStatistikaVelkostFrontu().pridajHodnotu(this.getCasVykonania(), simulacia.getPocetAgentovVoFronte());
        }
    }
}
