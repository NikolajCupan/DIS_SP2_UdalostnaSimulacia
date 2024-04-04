package org.example.Simulacia.System.Udalosti.Okno;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Objekty.Automat;
import org.example.Simulacia.System.Agenti.Objekty.ObsluhaOkna;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Objekty.Okno;
import org.example.Simulacia.System.Agenti.Zakaznik.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.System.Udalosti.Automat.UdalostZaciatokObsluhyAutomat;

import java.util.Queue;

public class UdalostZaciatokObsluhyOkno extends Udalost
{
    private final Okno okno;

    public UdalostZaciatokObsluhyOkno(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, Okno okno)
    {
        super(simulacneJadro, casVykonania, agent, Konstanty.PRIORITA_ZACIATOK_OBSLUHY_OKNO);

        this.okno = okno;
        if (this.okno.getObsadene())
        {
            throw new RuntimeException("Bola naplanovana obsluha u obsadeneho okna!");
        }
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
        ObsluhaOkna obsluhaOkna = simulacia.getObsluhaOkna();
        Automat automat = simulacia.getAutomat();


        // Kontrola stavu simulacie
        if (this.okno.getObsadene())
        {
            throw new RuntimeException("Bola naplanovana obsluha u obsadeneho okna!");
        }


        // Zmena stavu simulacie
        this.okno.setObsadene(true, this.getCasVykonania());


        // Nastavenie atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasZaciatokObsluhyOkno(this.getCasVykonania());
        obsluhaOkna.pridajCakanieAgent(vykonavajuciAgent);


        // Pokus o naplanovanie dalsej obsluhy zakaznika u automatu
        Queue<Agent> frontOkno = obsluhaOkna.getFront();
        if (frontOkno.size() > Konstanty.KAPACITA_FRONT_OKNO)
        {
            throw new RuntimeException("Front pred oknami prekrocil maximalnu velkost!");
        }

        if (frontOkno.size() < (Konstanty.KAPACITA_FRONT_OKNO - 1)
            && automat.getVypnuty())
        {
            throw new RuntimeException("Automat je vypnuty hoci front pred oknom nie je plny!");
        }

        if (frontOkno.size() < Konstanty.KAPACITA_FRONT_OKNO
            && automat.getVypnuty())
        {
            // Naplanuj pouzitie automatu
            automat.setVypnuty(false);

            if (automat.getPocetFront() != 0)
            {
                Agent odobratyAgent = automat.odoberFront(this.getCasVykonania());
                UdalostZaciatokObsluhyAutomat zaciatokObsluhyAutomat =
                    new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), odobratyAgent);
                simulacia.naplanujUdalost(zaciatokObsluhyAutomat);
            }
        }


        // Naplanuj koniec obsluhy pri okne
        double dlzkaObsluhy;
        if (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE)
        {
            // Online zakaznik iba nadiktuje objednavku
            dlzkaObsluhy = simulacia.getGeneratorObsluhaOnline().sample();
        }
        else
        {
            // Nadiktovanie objednavky plus pripravanie objednavky
            dlzkaObsluhy = simulacia.getGeneratorObsluhaObycajni().sample();
            dlzkaObsluhy += simulacia.getGeneratorTrvaniePripravy().getDlzkaPripravy();
        }

        double casKoncaObsluhy = simulacia.getAktualnySimulacnyCas() + dlzkaObsluhy;

        UdalostKoniecObsluhyOkno koniecObsluhy = new UdalostKoniecObsluhyOkno(simulacia, casKoncaObsluhy, vykonavajuciAgent, this.okno);
        simulacia.naplanujUdalost(koniecObsluhy);
    }
}
