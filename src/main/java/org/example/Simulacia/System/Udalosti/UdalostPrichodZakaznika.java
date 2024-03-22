package org.example.Simulacia.System.Udalosti;

import org.example.Ostatne.Identifikator;
import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;

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
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();

        // Nastavenie atributov agenta, ktory udalost vykonava
        Agent vykonavajuciAgent = this.getAgent();
        vykonavajuciAgent.setCasPrichod(this.getCasVykonania());

        // Naplanuj prichod dalsieho zakaznika
        double dalsiPrichodPo = simulacia.getGeneratorDalsiehoVstupu().sample();
        double casDalsiehoPrichodu = simulacia.getAktualnySimulacnyCas() + dalsiPrichodPo;

        Agent dalsiPrichadzajuciAgent = new Agent(Identifikator.getID(), simulacia.getGeneratorTypuZakaznika().getTypAgenta());
        UdalostPrichodZakaznika dalsiPrichod = new UdalostPrichodZakaznika(simulacia, casDalsiehoPrichodu, dalsiPrichadzajuciAgent);
        simulacia.naplanujUdalost(dalsiPrichod);

        if (simulacia.getObsluhaAutomatPrebieha())
        {
            // Niekto je obsluhovany, pridaj agenta do frontu pred automatom
            simulacia.pridajAgentaDoFrontuAutomat(vykonavajuciAgent);
        }
        else
        {
            // Nikto nie je obsluhovany, mozno obsluzit zakaznika
            UdalostZaciatokObsluhyAutomat zaciatokObsluhy = new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), vykonavajuciAgent);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
    }
}