package org.example.Simulacia.System.Udalosti;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.Okno;
import org.example.Simulacia.System.Agenti.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;

import java.util.Queue;

public class UdalostKoniecObsluhyAutomat extends Udalost
{
    public UdalostKoniecObsluhyAutomat(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
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
            System.out.format("%-35s", "Koniec obsluhy automat");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        simulacia.setObsluhaAutomatPrebieha(false);

        // Nastavenie atributov agenta, ktory udalost vykonava
        Agent vykonavajuciAgent = this.getAgent();
        vykonavajuciAgent.setCasKoniecObsluhyAutomat(this.getCasVykonania());

        // Naplanovanie obsluhy u okienka
        Queue<Agent> frontObsluha = simulacia.getFrontOkno();
        if (frontObsluha.size() >= Konstanty.KAPACITA_FRONT_OBSLUHA)
        {
            throw new RuntimeException("Doslo k vydaniu listka hoci je front pred obsluhou plny!");
        }

        // Skontroluj, ci nemoze byt zacata obsluha daneho agenta
        boolean obsluhaNaplanovana = false;
        if (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE)
        {
            Okno[] oknaOnline = simulacia.getOknaOnline();
            for (Okno okno : oknaOnline)
            {
                if (!okno.getObsadene())
                {
                    obsluhaNaplanovana = true;

                    UdalostZaciatokObsluhy zaciatokObsluhyOkno = new UdalostZaciatokObsluhy(simulacia, this.getCasVykonania(),
                        vykonavajuciAgent, okno);
                    simulacia.naplanujUdalost(zaciatokObsluhyOkno);

                    break;
                }
            }
        }
        else
        {
            Okno[] oknaOstatne = simulacia.getOknaObycajni();
            for (Okno okno : oknaOstatne)
            {
                if (!okno.getObsadene())
                {
                    obsluhaNaplanovana = true;

                    UdalostZaciatokObsluhy zaciatokObsluhyOkno = new UdalostZaciatokObsluhy(simulacia, this.getCasVykonania(),
                            vykonavajuciAgent, okno);
                    simulacia.naplanujUdalost(zaciatokObsluhyOkno);

                    break;
                }
            }
        }

        if (!obsluhaNaplanovana)
        {
            frontObsluha.add(vykonavajuciAgent);
            if (frontObsluha.size() == Konstanty.KAPACITA_FRONT_OBSLUHA)
            {
                // Front sa naplnil, vypni automat
                simulacia.setAutomatVypnuty(true);
            }
        }

        // Naplanovanie dalsej obsluhy u automatu
        if (simulacia.getPocetFrontAutomat() == 0 || simulacia.getAutomatVypnuty())
        {
            // Front je prazdny alebo automat je vypnuty, nemozno naplanovat dalsiu obsluhu u automatu
        }
        else
        {
            Agent odobratyAgent = simulacia.odoberFrontAutomat();
            UdalostZaciatokObsluhyAutomat zaciatokObsluhy = new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), odobratyAgent);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
    }
}
