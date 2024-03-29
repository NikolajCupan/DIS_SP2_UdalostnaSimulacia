package org.example.Simulacia.System.Udalosti.Vyzdvihnutie;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.Okno;
import org.example.Simulacia.System.Agenti.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.System.Udalosti.Okno.UdalostZaciatokObsluhyOkno;

import java.util.Queue;

public class UdalostKoniecVyzdvihnutia extends Udalost
{
    public UdalostKoniecVyzdvihnutia(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
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


        // Pokus o naplanovanie dalsej oblushy u uvolneneho okna
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
                new UdalostZaciatokObsluhyOkno(simulacia, this.getCasVykonania(), onlineAgent, okno);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
        else if ((vykonavajuciAgent.getTypAgenta() == TypAgenta.BEZNY || vykonavajuciAgent.getTypAgenta() == TypAgenta.ZMLUVNY)
                 && simulacia.frontOknoObsahujeObycajnehoAgenta())
        {
            Agent obycajnyAgent = simulacia.vyberPrvyObycajnyAgent();

            UdalostZaciatokObsluhyOkno zaciatokObsluhy =
                    new UdalostZaciatokObsluhyOkno(simulacia, this.getCasVykonania(), obycajnyAgent, okno);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }


        // Statistiky
        simulacia.getStatistikaCasSystem().pridajHodnotu(
        vykonavajuciAgent.getCasKoniecVyzdvihnutie() - vykonavajuciAgent.getCasPrichodSystem());
    }
}
