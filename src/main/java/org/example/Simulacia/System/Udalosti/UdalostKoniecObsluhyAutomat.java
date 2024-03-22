package org.example.Simulacia.System.Udalosti;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.ObsluznyZamestnanec;
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
        Queue<Agent> frontObsluha = simulacia.getFrontObsluha();
        if (frontObsluha.size() >= Konstanty.KAPACITA_FRONT_OBSLUHA)
        {
            throw new RuntimeException("Doslo k vydaniu listka hoci je front pred obsluhou plny!");
        }

        // Skontroluj, ci nemoze byt zacata obsluha daneho agenta
        boolean obsluhaNaplanovana = false;
        if (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE)
        {
            ObsluznyZamestnanec[] oknaOnline = simulacia.getOknaOnline();
            for (ObsluznyZamestnanec okno : oknaOnline)
            {
                if (!okno.getObsadeny())
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
            ObsluznyZamestnanec[] oknaOstatne = simulacia.getOknaObycajni();
            for (ObsluznyZamestnanec okno : oknaOstatne)
            {
                if (!okno.getObsadeny())
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
        if (simulacia.getPocetAgentovVoFronteAutomat() == 0 || simulacia.getAutomatVypnuty())
        {
            // Front je prazdny alebo automat je vypnuty, nemozno naplanovat dalsiu obsluhu u automatu
        }
        else
        {
            Agent odobratyAgent = simulacia.odoberAgentaZFrontuAutomat();
            UdalostZaciatokObsluhyAutomat zaciatokObsluhy = new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), odobratyAgent);
            simulacia.naplanujUdalost(zaciatokObsluhy);
        }
    }
}
