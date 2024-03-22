package org.example.Simulacia.Stanok.Udalosti;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Stanok.Agent;
import org.example.Simulacia.Stanok.SimulaciaStanok;
import org.example.Simulacia.Jadro.Udalost;

public class UdalostZaciatokObsluhy extends Udalost
{
    public UdalostZaciatokObsluhy(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
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
            System.out.format("%-35s", "Zaciatok obsluhy zakaznika");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaStanok simulacia = (SimulaciaStanok)this.getSimulacneJadro();
        simulacia.setObsluhaPrebieha(true);

        // Nastavenie atributov agenta, ktory udalost vykonava
        Agent vykonavajuciAgent = this.getAgent();
        vykonavajuciAgent.setCasZaciatokObsluhy(this.getCasVykonania());

        // Zaznamenanie statistik
        simulacia.getStatistikaCasFront().pridajHodnotu(vykonavajuciAgent.getCasZaciatokObsluhy() - vykonavajuciAgent.getCasPrichod());

        // Naplanovanie konca obsluhy
        double dlzkaObsluhy = simulacia.getSpojityTrojuholnikovyGenerator().sample();
        double casKoncaObsluhy = this.getCasVykonania() + dlzkaObsluhy;

        UdalostKoniecObsluhy koniecObsluhy = new UdalostKoniecObsluhy(simulacia, casKoncaObsluhy, vykonavajuciAgent);
        simulacia.naplanujUdalost(koniecObsluhy);
    }
}
