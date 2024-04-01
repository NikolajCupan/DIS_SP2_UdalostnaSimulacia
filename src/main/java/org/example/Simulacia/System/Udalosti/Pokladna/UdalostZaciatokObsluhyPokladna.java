package org.example.Simulacia.System.Udalosti.Pokladna;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Objekty.Pokladna;
import org.example.Simulacia.System.SimulaciaSystem;

public class UdalostZaciatokObsluhyPokladna extends Udalost
{
    private final Pokladna pokladna;

    public UdalostZaciatokObsluhyPokladna(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, Pokladna pokladna)
    {
        super(simulacneJadro, casVykonania, agent, Konstanty.PRIORITA_ZACIATOK_OBSLUHY_POKLADNA);

        this.pokladna = pokladna;
        if (this.pokladna.getObsadena())
        {
            throw new RuntimeException("Bola naplanovana obsluha u obsadenej pokladne!");
        }
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

    public Pokladna getPokladna()
    {
        return this.pokladna;
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        Agent vykonavajuciAgent = this.getAgent();


        // Kontrola stavu simulacie
        if (this.pokladna.getObsadena())
        {
            throw new RuntimeException("Bola naplanovana obsluha u obsadenej pokladne!");
        }


        // Zmena stavu simulacie
        this.pokladna.setObsadena(true, this.getCasVykonania());


        // Nastavenia atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasZaciatokObsluhyPokladna(this.getCasVykonania());
        this.pokladna.pridajCakanieAgent(vykonavajuciAgent);


        // Naplanuj koniec obsluhy pri pokladni
        double dlzkaPlatenia = simulacia.getGeneratorDlzkaPlatenia().getDlzkaPlatenia();
        double casUkonceniaPlatenia = simulacia.getAktualnySimulacnyCas() + dlzkaPlatenia;

        UdalostKoniecObsluhyPokladna koniecPlatenia =
            new UdalostKoniecObsluhyPokladna(simulacia, casUkonceniaPlatenia, vykonavajuciAgent, this.pokladna);
        simulacia.naplanujUdalost(koniecPlatenia);
    }
}
