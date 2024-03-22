package org.example.Simulacia.Stanok.Udalosti;

import org.example.Ostatne.Identifikator;
import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Stanok.SimulaciaStanok;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.Stanok.Agent;

public class UdalostPrichodZakaznika extends Udalost
{
    public UdalostPrichodZakaznika(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
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
            System.out.format("%-35s", "Prichod zakaznika");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaStanok simulacia = (SimulaciaStanok)this.getSimulacneJadro();

        // Nastavenie atributov agenta, ktory udalost vykonava
        Agent vykonavajuciAgent = this.getAgent();
        vykonavajuciAgent.setCasPrichod(this.getCasVykonania());

        // Naplanuj prichod dalsieho zakaznika
        double dalsiPrichodPo = simulacia.getSpojityExponencialnyGenerator().sample();
        double casDalsiehoPrichodu = simulacia.getAktualnySimulacnyCas() + dalsiPrichodPo;

        UdalostPrichodZakaznika dalsiPrichod = new UdalostPrichodZakaznika(simulacia, casDalsiehoPrichodu, new Agent(Identifikator.getID()));
        simulacia.naplanujUdalost(dalsiPrichod);

        if (simulacia.getObsluhaPrebieha())
        {
            // Niekto je obsluhovany, pridaj agenta do frontu
            simulacia.pridajAgentaDoFrontu(vykonavajuciAgent);
            simulacia.getStatistikaVelkostFrontu().pridajHodnotu(this.getCasVykonania(), simulacia.getPocetAgentovVoFronte());
        }
        else
        {
            // Nikto nie je obsluhovany, mozno obsluzit zakaznika
            UdalostZaciatokObsluhy zaciatokObsluhy = new UdalostZaciatokObsluhy(simulacia, this.getCasVykonania(), vykonavajuciAgent);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
    }
}
