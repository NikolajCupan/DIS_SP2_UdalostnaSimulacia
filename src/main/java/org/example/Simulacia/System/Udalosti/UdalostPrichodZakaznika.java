package org.example.Simulacia.System.Udalosti;

import org.example.Generatory.SpojityExponencialnyGenerator;
import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Zakaznik.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;

public class UdalostPrichodZakaznika extends Udalost
{
    public UdalostPrichodZakaznika(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
    {
        super(simulacneJadro, casVykonania, agent, Konstanty.PRIORITA_PRICHOD_ZAKAZNIKA);
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

    private SpojityExponencialnyGenerator getGenerator(SimulaciaSystem simulacia, Agent agent)
    {
        switch (agent.getTypAgenta())
        {
            case TypAgenta.BEZNY:
                return simulacia.getGeneratorDalsiPrichodBeznyZakaznik();
            case TypAgenta.ZMLUVNY:
                return simulacia.getGeneratorDalsiPrichodZmluvnyZakaznik();
            case TypAgenta.ONLINE:
                return simulacia.getGeneratorDalsiPrichodOnlineZakaznik();
            default:
                throw new RuntimeException("Neplatny typ agenta!");
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        Agent vykonavajuciAgent = this.getAgent();


        // Kontrola stavu simulacie
        if (simulacia.getAktualnySimulacnyCas() > simulacia.getDlzkaTrvaniaSimulacie())
        {
            throw new RuntimeException("Doslo k vykonaniu udalosti prichodu zakaznika po vyprsani simulacneho casu!");
        }


        // Nastavenie atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasPrichodSystem(this.getCasVykonania());


        // Naplanuj prichod dalsieho zakaznika
        SpojityExponencialnyGenerator generator = this.getGenerator(simulacia, vykonavajuciAgent);
        double dalsiPrichodPo = generator.sample();
        double casDalsiehoPrichodu = simulacia.getAktualnySimulacnyCas() + dalsiPrichodPo;

        if (casDalsiehoPrichodu <= simulacia.getDlzkaTrvaniaSimulacie())
        {
            // Udalost je naplanovana iba za predpokladu, ze nenastane po vyprsani simulacneho casu
            Agent dalsiPrichadzajuciAgent = new Agent(simulacia.getIdentifikator().getID(), vykonavajuciAgent.getTypAgenta());
            simulacia.pridajAgenta(dalsiPrichadzajuciAgent);
            UdalostPrichodZakaznika dalsiPrichod = new UdalostPrichodZakaznika(simulacia, casDalsiehoPrichodu, dalsiPrichadzajuciAgent);
            simulacia.naplanujUdalost(dalsiPrichod);
        }

        UdalostVstupZakaznika vstupZakaznika = new UdalostVstupZakaznika(simulacia, this.getCasVykonania(), vykonavajuciAgent);
        simulacia.naplanujUdalost(vstupZakaznika);
    }
}
