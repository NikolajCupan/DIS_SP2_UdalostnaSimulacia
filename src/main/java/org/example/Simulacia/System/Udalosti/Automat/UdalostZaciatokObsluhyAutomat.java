package org.example.Simulacia.System.Udalosti.Automat;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Objekty.Automat;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.SimulaciaSystem;

import java.util.Queue;

public class UdalostZaciatokObsluhyAutomat extends Udalost
{
    public UdalostZaciatokObsluhyAutomat(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
    {
        super(simulacneJadro, casVykonania, agent, Konstanty.PRIORITA_ZACIATOK_OBSLUHY_AUTOMAT);
    }

    private void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_UDALOST)
        {
            System.out.print("[UDALOST ");
            System.out.format("%6s", this.getAgent().getID());
            System.out.print("]   ");
            System.out.format("%-35s", "Zaciatok obsluhy automat");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        Agent vykonavajuciAgent = this.getAgent();
        Automat automat = simulacia.getAutomat();


        // Kontrola stavu simulacie
        if (simulacia.getAktualnySimulacnyCas() > simulacia.getDlzkaTrvaniaSimulacie())
        {
            throw new RuntimeException("Doslo k vykonaniu udalosti zaciatku obsluhy u automatu po vyprsani simulacneho casu!");
        }
        if (automat.getVypnuty() || automat.getObsluhaPrebieha())
        {
            throw new RuntimeException("Bol naplanovany zaciatok obsluhy u automatu, ktory je vypnuty alebo uz obsluha prebieha!");
        }

        Queue<Agent> frontOkno = simulacia.getFrontOkno();
        if (frontOkno.size() >= Konstanty.KAPACITA_FRONT_OKNO)
        {
            throw new RuntimeException("Doslo k vydaniu listka hoci je front pred oknom plny!");
        }


        // Zmena stavu simulacie
        automat.setObsluhaPrebieha(true);


        // Nastavenie atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasZaciatokObsluhyAutomat(this.getCasVykonania());


        // Naplanuj koniec obsluhy pri automate
        double dlzkaObsluhy = simulacia.getGeneratorVydanieListka().sample();
        double casKoncaObsluhy = simulacia.getAktualnySimulacnyCas() + dlzkaObsluhy;

        UdalostKoniecObsluhyAutomat koniecObsluhy = new UdalostKoniecObsluhyAutomat(simulacia, casKoncaObsluhy, vykonavajuciAgent);
        simulacia.naplanujUdalost(koniecObsluhy);
    }
}
