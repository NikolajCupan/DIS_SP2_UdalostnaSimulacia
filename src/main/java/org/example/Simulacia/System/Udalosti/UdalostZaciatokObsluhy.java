package org.example.Simulacia.System.Udalosti;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.ObsluznyZamestnanec;
import org.example.Simulacia.System.Agenti.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;

public class UdalostZaciatokObsluhy extends Udalost
{
    private final ObsluznyZamestnanec okno;

    public UdalostZaciatokObsluhy(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, ObsluznyZamestnanec okno)
    {
        super(simulacneJadro, casVykonania, agent);

        this.okno = okno;
    }

    private void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_UDALOST)
        {
            System.out.print("[UDALOST ");
            System.out.format("%6s", this.getAgent().getID());
            System.out.print("]   ");
            System.out.format("%-35s", "Zaciatok obsluhy");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        this.okno.setObsadeny(true);

        // Nastavenie atributov agenta, ktory udalost vykonava
        Agent vykonavajuciAgent = this.getAgent();
        vykonavajuciAgent.setCasZaciatokObsluhy(this.getCasVykonania());

        // Naplanuj koniec obsluhy pri okne
        double dlzkaObsluhy;
        if (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE)
        {
            dlzkaObsluhy = simulacia.getGeneratorObsluhaOnline().sample();
        }
        else
        {
            dlzkaObsluhy = simulacia.getGeneratorObsluhaObycajni().sample();
        }
        double casKoncaObsluhy = simulacia.getAktualnySimulacnyCas() + dlzkaObsluhy;

        UdalostKoniecObsluhy koniecObsluhy = new UdalostKoniecObsluhy(simulacia, casKoncaObsluhy, vykonavajuciAgent, this.okno);
        simulacia.naplanujUdalost(koniecObsluhy);
    }
}
