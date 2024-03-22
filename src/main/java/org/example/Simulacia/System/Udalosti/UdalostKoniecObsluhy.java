package org.example.Simulacia.System.Udalosti;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.ObsluznyZamestnanec;
import org.example.Simulacia.System.Agenti.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;

import java.util.Queue;

public class UdalostKoniecObsluhy extends Udalost
{
    private final ObsluznyZamestnanec okno;

    public UdalostKoniecObsluhy(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, ObsluznyZamestnanec okno)
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
            System.out.format("%-35s", "Koniec obsluhy");
            System.out.println(this.getCasVykonania());
        }
    }

    @Override
    public void vykonajUdalost()
    {
        this.vypis();
        SimulaciaSystem simulacia = (SimulaciaSystem)this.getSimulacneJadro();
        this.okno.setObsadeny(false);

        // Nastavenie atributov agenta, ktory udalost vykonava
        Agent vykonavajuciAgent = this.getAgent();
        vykonavajuciAgent.setCasKoniecObsluhy(getCasVykonania());
        vykonavajuciAgent.vypis();

        // Naplanuj dalsi zaciatok obsluhy
        Queue<Agent> frontObsluha = simulacia.getFrontObsluha();
        if (frontObsluha.size() > Konstanty.KAPACITA_FRONT_OBSLUHA)
        {
            throw new RuntimeException("Front prekrocil maximalnu velkost!");
        }

        if (frontObsluha.isEmpty())
        {
            // Front je prazdny
            return;
        }

        if (vykonavajuciAgent.getTypAgenta() == TypAgenta.ONLINE)
        {
            for (Agent agent : frontObsluha)
            {
                if (agent.getTypAgenta() != TypAgenta.ONLINE)
                {
                    continue;
                }

                // Naplanuj obsluhu daneho agenta
                frontObsluha.remove(agent);
                UdalostZaciatokObsluhy zaciatokObsluhy = new UdalostZaciatokObsluhy(simulacia, this.getCasVykonania(), agent, this.okno);
                simulacia.naplanujUdalost(zaciatokObsluhy);

                break;
            }
        }
        else
        {
            boolean obsahujeZmluvneho = false;
            for (Agent agent : frontObsluha)
            {
                if (agent.getTypAgenta() == TypAgenta.ZMLUVNY)
                {
                    obsahujeZmluvneho = true;
                    break;
                }
            }

            if (obsahujeZmluvneho)
            {
                // Front obsahuje zmluvneho zakaznika, tento ma prednost
                for (Agent agent : frontObsluha)
                {
                    if (agent.getTypAgenta() == TypAgenta.ZMLUVNY)
                    {
                        // Naplanuj obsluhu daneho zmluvneho agenta
                        frontObsluha.remove(agent);
                        UdalostZaciatokObsluhy zaciatokObsluhy = new UdalostZaciatokObsluhy(simulacia, this.getCasVykonania(), agent, this.okno);
                        simulacia.naplanujUdalost(zaciatokObsluhy);

                        break;
                    }
                }
            }
            else
            {
                // Front neobsahuje zmluvneho zakaznika
                for (Agent agent : frontObsluha)
                {
                    if (agent.getTypAgenta() == TypAgenta.BEZNY)
                    {
                        // Naplanuj obsluhu daneho obycajneho agenta
                        frontObsluha.remove(agent);
                        UdalostZaciatokObsluhy zaciatokObsluhy = new UdalostZaciatokObsluhy(simulacia, this.getCasVykonania(), agent, this.okno);
                        simulacia.naplanujUdalost(zaciatokObsluhy);

                        break;
                    }
                }
            }
        }

        // Moze ale nemusi byt naplanovana dalsia obsluha pri okienku
        if (frontObsluha.size() < Konstanty.KAPACITA_FRONT_OBSLUHA)
        {
            simulacia.setAutomatVypnuty(false);
        }
    }
}
