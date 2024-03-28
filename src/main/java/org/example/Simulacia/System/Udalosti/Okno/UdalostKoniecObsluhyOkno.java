package org.example.Simulacia.System.Udalosti.Okno;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.Okno;
import org.example.Simulacia.System.Agenti.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.System.Udalosti.Automat.UdalostZaciatokObsluhyAutomat;

import java.util.Queue;

public class UdalostKoniecObsluhyOkno extends Udalost
{
    private final Okno okno;

    public UdalostKoniecObsluhyOkno(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, Okno okno)
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
            System.out.format("%-35s", "Koniec obsluhy okno");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        Agent vykonavajuciAgent = this.getAgent();

        // Zmena stavu simulacie
        this.okno.setObsadene(false);

        // Nastavenie atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasKoniecObsluhyOkno(getCasVykonania());
        vykonavajuciAgent.vypis();


        // Pokus o naplanovanie zaciatku obsluhy dalsieho agenta u okna
        Queue<Agent> frontOkno = simulacia.getFrontOkno();
        if (frontOkno.size() > Konstanty.KAPACITA_FRONT_OKNO)
        {
            throw new RuntimeException("Front pred oknami prekrocil maximalnu velkost!");
        }

        if (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE
            && simulacia.frontOknoObsahujeOnlineAgenta())
        {
            Agent onlineAgent = simulacia.vyberPrvyOnlineAgent();
            UdalostZaciatokObsluhyOkno zaciatokObsluhy =
                new UdalostZaciatokObsluhyOkno(simulacia, this.getCasVykonania(), onlineAgent, this.okno);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
        else if ((vykonavajuciAgent.getTypAgenta() == TypAgenta.BEZNY || vykonavajuciAgent.getTypAgenta() == TypAgenta.ZMLUVNY)
                 && simulacia.frontOknoObsahujeObycajnehoAgenta())
        {
            Agent obycajnyAgent = simulacia.vyberPrvyObycajnyAgent();
            UdalostZaciatokObsluhyOkno zaciatokObsluhy =
                new UdalostZaciatokObsluhyOkno(simulacia, this.getCasVykonania(), obycajnyAgent, this.okno);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }

        if (frontOkno.size() < Konstanty.KAPACITA_FRONT_OKNO
            && simulacia.getAutomatVypnuty())
        {
            // Naplanuj pouzitie automatu
            simulacia.setAutomatVypnuty(false);
            
            if (simulacia.getPocetFrontAutomat() != 0)
            {
                Agent odobratyAgent = simulacia.odoberFrontAutomat();
                UdalostZaciatokObsluhyAutomat zaciatokObsluhyAutomat =
                    new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), odobratyAgent);
                simulacia.naplanujUdalost(zaciatokObsluhyAutomat);
            }
        }


        // Statistiky
        simulacia.getStatistikaCasSystem().pridajHodnotu(
        vykonavajuciAgent.getCasKoniecObsluhyOkno() - vykonavajuciAgent.getCasPrichodSystem());
    }
}
