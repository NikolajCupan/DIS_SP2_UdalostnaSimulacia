package org.example.Simulacia.System.Udalosti.Vyzdvihnutie;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.SimulaciaSystem;

public class UdalostZaciatokVyzdvihnutia extends Udalost
{
    public UdalostZaciatokVyzdvihnutia(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
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
            System.out.format("%-35s", "Zaciatok vyzdvihnutia tovaru");
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
        if (!vykonavajuciAgent.getOdlozenyTovar() || vykonavajuciAgent.getOdlozenyTovarOkno() == null)
        {
            throw new RuntimeException("Agent vyzdvihujuci tovar nema odlozeny tovar!");
        }


        // Nastavenia atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasZaciatokVyzdvihnutie(this.getCasVykonania());


        // Naplanovanie konca vyzdvihnutia tovaru
        double dlzkaVyzdvihnutia = simulacia.getGeneratorVyzdvihnutieTovaru().sample();
        double casVykonania = simulacia.getAktualnySimulacnyCas() + dlzkaVyzdvihnutia;

        UdalostKoniecVyzdvihnutia koniecVyzdvihnutia =
            new UdalostKoniecVyzdvihnutia(simulacia, casVykonania, vykonavajuciAgent);
        simulacia.naplanujUdalost(koniecVyzdvihnutia);
    }
}
