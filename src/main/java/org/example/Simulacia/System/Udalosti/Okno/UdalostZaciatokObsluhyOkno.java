package org.example.Simulacia.System.Udalosti.Okno;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.Okno;
import org.example.Simulacia.System.Agenti.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;

public class UdalostZaciatokObsluhyOkno extends Udalost
{
    private final Okno okno;

    public UdalostZaciatokObsluhyOkno(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, Okno okno)
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
            System.out.format("%-35s", "Zaciatok obsluhy okno");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        Agent vykonavajuciAgent = this.getAgent();


        // Kontrola stavu simulacie
        if (this.okno.getObsadene())
        {
            throw new RuntimeException("Bola naplanovana obsluha u obsadeneho okna!");
        }


        // Zmena stavu simulacie
        this.okno.setObsadene(true);


        // Nastavenie atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasZaciatokObsluhyOkno(this.getCasVykonania());


        // Naplanuj koniec obsluhy pri okne
        double dlzkaObsluhy = (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE
            ? simulacia.getGeneratorObsluhaOnline().sample() : simulacia.getGeneratorObsluhaObycajni().sample());
        double casKoncaObsluhy = simulacia.getAktualnySimulacnyCas() + dlzkaObsluhy;

        UdalostKoniecObsluhyOkno koniecObsluhy = new UdalostKoniecObsluhyOkno(simulacia, casKoncaObsluhy, vykonavajuciAgent, this.okno);
        simulacia.naplanujUdalost(koniecObsluhy);
    }
}
