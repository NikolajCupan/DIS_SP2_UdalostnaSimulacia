package org.example.Simulacia.System.Udalosti.Okno;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Generovania.VelkostTovaru;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Objekty.Automat;
import org.example.Simulacia.System.Agenti.Objekty.ObsluhaOkna;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Objekty.Okno;
import org.example.Simulacia.System.Agenti.Objekty.Pokladna;
import org.example.Simulacia.System.Agenti.Zakaznik.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.System.Udalosti.Automat.UdalostZaciatokObsluhyAutomat;
import org.example.Simulacia.System.Udalosti.Pokladna.UdalostZaciatokObsluhyPokladna;

import java.util.Queue;

public class UdalostKoniecObsluhyOkno extends Udalost
{
    private final Okno okno;

    public UdalostKoniecObsluhyOkno(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, Okno okno)
    {
        super(simulacneJadro, casVykonania, agent, Konstanty.PRIORITA_KONIEC_OBSLUHY_OKNO);

        this.okno = okno;
        if (!this.okno.getObsadene())
        {
            throw new RuntimeException("Okno obsahuje agenta, hoci ma nastavene, ze nikoho neobsahuje!");
        }
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
        ObsluhaOkna obsluhaOkna = simulacia.getObsluhaOkna();


        // Kontrola stavu simulacie
        if (!this.okno.getObsadene())
        {
            throw new RuntimeException("Okno obsahuje agenta, hoci ma nastavene, ze nikoho neobsahuje!");
        }


        // Zmena stavu simulacie
        VelkostTovaru velkost = simulacia.getGeneratorVelkostTovaru().getVelkostTovaru();
        if (velkost == VelkostTovaru.MALY)
        {
            // Uvolni okno len za predpokladnu, ze sa jedna o maly tovar
            this.okno.setObsadene(false, this.getCasVykonania());
        }


        // Nastavenie atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasKoniecObsluhyOkno(this.getCasVykonania());

        // Skontroluj odlozenie tovaru
        if (this.okno.getObsadene())
        {
            // Doslo k odlozeniu tovaru pri okne, nastavenie agenta
            vykonavajuciAgent.setOdlozenyTovar(true);
            vykonavajuciAgent.setOdlozenyTovarOkno(this.okno);
        }


        // Pokus o naplanovanie zaciatku obsluhy dalsieho agenta u okna
        Queue<Agent> frontOkno = obsluhaOkna.getFront();
        if (frontOkno.size() > Konstanty.KAPACITA_FRONT_OKNO)
        {
            throw new RuntimeException("Front pred oknami prekrocil maximalnu velkost!");
        }

        if (!this.okno.getObsadene())
        {
            if (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE
                && obsluhaOkna.frontOknoObsahujeOnlineAgenta())
            {
                Agent onlineAgent = obsluhaOkna.vyberPrvyOnlineAgent(this.getCasVykonania());

                UdalostZaciatokObsluhyOkno zaciatokObsluhy =
                    new UdalostZaciatokObsluhyOkno(simulacia, this.getCasVykonania(), onlineAgent, this.okno);
                simulacia.naplanujUdalost(zaciatokObsluhy);
            }
            else if ((vykonavajuciAgent.getTypAgenta() == TypAgenta.BEZNY || vykonavajuciAgent.getTypAgenta() == TypAgenta.ZMLUVNY)
                     && obsluhaOkna.frontOknoObsahujeObycajnehoAgenta())
            {
                Agent obycajnyAgent = obsluhaOkna.vyberPrvyObycajnyAgent(this.getCasVykonania());

                UdalostZaciatokObsluhyOkno zaciatokObsluhy =
                    new UdalostZaciatokObsluhyOkno(simulacia, this.getCasVykonania(), obycajnyAgent, this.okno);
                simulacia.naplanujUdalost(zaciatokObsluhy);
            }
        }


        // Pokus o naplanovanie zaciatku obsluhy u pokladne
        Pokladna pokladna = simulacia.getPokladna();
        if (pokladna.getPocetFront() == 0 && !pokladna.getObsadena())
        {
            // Pred pokladnou nie je ziadny front a nie je obsadena, moze zacat obsluha agenta
            UdalostZaciatokObsluhyPokladna zaciatokObsluhyPokladna =
                new UdalostZaciatokObsluhyPokladna(simulacia, this.getCasVykonania(), vykonavajuciAgent, pokladna);
            simulacia.naplanujUdalost(zaciatokObsluhyPokladna);
        }
        else
        {
            // Pred pokladnou je front, umiestni agenta do frontu
            pokladna.pridajDoFrontu(vykonavajuciAgent, this.getCasVykonania());
        }
    }
}
