package org.example.Simulacia.System.Udalosti.Vyzdvihnutie;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Objekty.ObsluhaOkna;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Objekty.Okno;
import org.example.Simulacia.System.Agenti.Zakaznik.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.System.Udalosti.Okno.UdalostZaciatokObsluhyOkno;

import java.util.Queue;

public class UdalostKoniecVyzdvihnutia extends Udalost
{
    public UdalostKoniecVyzdvihnutia(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
    {
        super(simulacneJadro, casVykonania, agent, Konstanty.PRIORITA_KONIEC_VYZDVIHNUTIA);

        if (!agent.getOdlozenyTovar() || agent.getOdlozenyTovarOkno() == null)
        {
            throw new RuntimeException("Agent vyzdvihujuci tovar nema odlozeny tovar!");
        }
        if (!agent.getOdlozenyTovarOkno().getObsadene())
        {
            throw new RuntimeException("Okno, pri ktorom agent nechal tovar nie je obsadene!");
        }
    }

    private void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_UDALOST)
        {
            System.out.print("[UDALOST ");
            System.out.format("%6s", this.getAgent().getID());
            System.out.print("]   ");
            System.out.format("%-35s", "Koniec vyzdvihnutia tovaru");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        Agent vykonavajuciAgent = this.getAgent();
        Okno okno = vykonavajuciAgent.getOdlozenyTovarOkno();
        ObsluhaOkna obsluhaOkna = simulacia.getObsluhaOkna();


        // Kontrola stavu simulacie
        if (!vykonavajuciAgent.getOdlozenyTovar() || vykonavajuciAgent.getOdlozenyTovarOkno() == null)
        {
            throw new RuntimeException("Agent vyzdvihujuci tovar nema odlozeny tovar!");
        }


        // Zmena stavu simulacie
        okno.setObsadene(false, this.getCasVykonania());


        // Nastavenia atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasKoniecVyzdvihnutie(this.getCasVykonania());
        vykonavajuciAgent.setOdlozenyTovar(false);


        // Pokus o naplanovanie dalsej obsluhy u uvolneneho okna
        Queue<Agent> frontOkno = obsluhaOkna.getFront();
        if (frontOkno.size() > Konstanty.KAPACITA_FRONT_OKNO)
        {
            throw new RuntimeException("Front pred oknami prekrocil maximalnu velkost!");
        }

        if (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE
            && obsluhaOkna.frontOknoObsahujeOnlineAgenta())
        {
            Agent onlineAgent = obsluhaOkna.vyberPrvyOnlineAgent();

            UdalostZaciatokObsluhyOkno zaciatokObsluhy =
                new UdalostZaciatokObsluhyOkno(simulacia, this.getCasVykonania(), onlineAgent, okno);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
        else if ((vykonavajuciAgent.getTypAgenta() == TypAgenta.BEZNY || vykonavajuciAgent.getTypAgenta() == TypAgenta.ZMLUVNY)
                 && obsluhaOkna.frontOknoObsahujeObycajnehoAgenta())
        {
            Agent obycajnyAgent = obsluhaOkna.vyberPrvyObycajnyAgent();

            UdalostZaciatokObsluhyOkno zaciatokObsluhy =
                    new UdalostZaciatokObsluhyOkno(simulacia, this.getCasVykonania(), obycajnyAgent, okno);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }


        // Statistiky
        simulacia.getStatistikaCasSystem().pridajHodnotu(
        vykonavajuciAgent.getCasKoniecVyzdvihnutie() - vykonavajuciAgent.getCasPrichodSystem());
    }
}
