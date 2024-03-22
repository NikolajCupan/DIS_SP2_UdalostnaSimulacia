package org.example.Simulacia.System.Udalosti;

import org.example.Ostatne.Identifikator;
import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Udalosti.Automat.UdalostZaciatokObsluhyAutomat;

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
        Agent vykonavajuciAgent = this.getAgent();

        // Nastavenie atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasPrichodSystem(this.getCasVykonania());


        // Naplanuj prichod dalsieho zakaznika
        double dalsiPrichodPo = simulacia.getGeneratorDalsiPrichod().sample();
        double casDalsiehoPrichodu = simulacia.getAktualnySimulacnyCas() + dalsiPrichodPo;

        Agent dalsiPrichadzajuciAgent = new Agent(Identifikator.getID(), simulacia.getGeneratorTypZakaznika().getTypAgenta());
        UdalostPrichodZakaznika dalsiPrichod = new UdalostPrichodZakaznika(simulacia, casDalsiehoPrichodu, dalsiPrichadzajuciAgent);
        simulacia.naplanujUdalost(dalsiPrichod);


        // Naplanuj zaciatok obsluhy u automatu, ak je to mozne
        if (simulacia.getObsluhaAutomatPrebieha() || simulacia.getAutomatVypnuty())
        {
            // Niekto je obsluhovany alebo je automat vypnuty, pridaj agenta do frontu pred automatom
            simulacia.pridajFrontAutomat(vykonavajuciAgent);
        }
        else
        {
            // Nikto nie je obsluhovany, mozno obsluzit daneho agenta
            UdalostZaciatokObsluhyAutomat zaciatokObsluhy =
                new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), vykonavajuciAgent);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
    }
}
