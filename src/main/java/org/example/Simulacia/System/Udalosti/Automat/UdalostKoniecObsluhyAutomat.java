package org.example.Simulacia.System.Udalosti.Automat;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Objekty.Automat;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Objekty.Okno;
import org.example.Simulacia.System.Agenti.Zakaznik.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.System.Udalosti.Okno.UdalostZaciatokObsluhyOkno;

import java.util.Queue;

public class UdalostKoniecObsluhyAutomat extends Udalost
{
    public UdalostKoniecObsluhyAutomat(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
    {
        super(simulacneJadro, casVykonania, agent, Konstanty.PRIORITA_KONIEC_OBSLUHY_AUTOMAT);

        Queue<Agent> frontOkno = ((SimulaciaSystem)simulacneJadro).getFrontOkno();
        if (frontOkno.size() >= Konstanty.KAPACITA_FRONT_OKNO)
        {
            throw new RuntimeException("Doslo k vydaniu listka hoci je front pred oknom plny!");
        }
    }

    private void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_UDALOST)
        {
            System.out.print("[UDALOST ");
            System.out.format("%6s", this.getAgent().getID());
            System.out.print("]   ");
            System.out.format("%-35s", "Koniec obsluhy automat");
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
        if (!automat.getObsluhaPrebieha())
        {
            throw new RuntimeException("Automat obsluhuje agenta, hoci ma nastavene, ze nikoho neobsluhuje!");
        }
        if (automat.getVypnuty())
        {
            throw new RuntimeException("Agent bol obsluhovany u vypnuteho automatu!");
        }


        // Zmena stavu simulacie
        automat.setObsluhaPrebieha(false);


        // Nastavenie atributov agenta, ktory udalost vykonava
        vykonavajuciAgent.setCasKoniecObsluhyAutomat(this.getCasVykonania());


        // Pokus o naplanovanie obsluhy vykonavajuceho agenta u okienka
        Queue<Agent> frontOkno = simulacia.getFrontOkno();
        if (frontOkno.size() >= Konstanty.KAPACITA_FRONT_OKNO)
        {
            throw new RuntimeException("Doslo k vydaniu listka hoci je front pred oknom plny!");
        }

        boolean obsluhaNaplanovana = false;
        Okno[] okna = (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE
            ? simulacia.getOknaOnline() : simulacia.getOknaObycajni());

        for (Okno okno : okna)
        {
            if (!okno.getObsadene())
            {
                // Bolo najdene okno, ku ktoremu moze byt agent priradeny
                obsluhaNaplanovana = true;

                UdalostZaciatokObsluhyOkno zaciatokObsluhyOkno =
                    new UdalostZaciatokObsluhyOkno(simulacia, this.getCasVykonania(), vykonavajuciAgent, okno);
                simulacia.naplanujUdalost(zaciatokObsluhyOkno);

                break;
            }
        }

        if (!obsluhaNaplanovana)
        {
            // Nebolo mozne agenta priradit ku ziadnemu oknu, agent je pridany do frontu pred oknami
            frontOkno.add(vykonavajuciAgent);

            if (frontOkno.size() > Konstanty.KAPACITA_FRONT_OKNO)
            {
                throw new RuntimeException("Pridanie agenta do frontu pred oknami zapricinilo prekrocenie maximalnej velkosti frontu!");
            }

            if (frontOkno.size() == Konstanty.KAPACITA_FRONT_OKNO)
            {
                // Front sa naplnil, vypni automat
                automat.setVypnuty(true);
            }
        }


        // Pokus o naplanovanie dalsej obsluhy u automatu
        if (automat.getPocetFront() == 0 || automat.getVypnuty())
        {
            // Front je prazdny alebo automat je vypnuty, nemozno naplanovat dalsiu obsluhu u automatu
        }
        else
        {
            Agent odobratyAgent = automat.odoberFront(this.getCasVykonania());
            UdalostZaciatokObsluhyAutomat zaciatokObsluhy = new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), odobratyAgent);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
    }
}
