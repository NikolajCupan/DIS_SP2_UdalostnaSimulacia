package org.example.Simulacia.System.Udalosti;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.System.Agenti.Objekty.Automat;
import org.example.Simulacia.System.SimulaciaSystem;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Udalosti.Automat.UdalostZaciatokObsluhyAutomat;

public class UdalostVstupZakaznika extends Udalost
{
    public UdalostVstupZakaznika(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
    {
        super(simulacneJadro, casVykonania, agent, Konstanty.PRIORITA_VSTUP_ZAKAZNIKA);
    }

    private void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_UDALOST)
        {
            System.out.print("[UDALOST ");
            System.out.format("%6s", this.getAgent().getID());
            System.out.print("]   ");
            System.out.format("%-35s", "Vstup zakaznika");
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
            throw new RuntimeException("Doslo k vykonaniu udalosti vstupu zakaznika po vyprsani simulacneho casu!");
        }


        // Naplanuj zaciatok obsluhy u automatu, ak je to mozne
        if (automat.getObsluhaPrebieha() || automat.getVypnuty())
        {
            // Niekto je obsluhovany alebo je automat vypnuty, pridaj agenta do frontu pred automatom
            automat.pridajFront(vykonavajuciAgent, this.getCasVykonania());
        }
        else
        {
            // Obsluha u automatu moze byt naplanovana
            if (automat.getPocetFront() == 0)
            {
                // Front pred automatom je prazdny, vykonavajuci agent bude obsluhovany
                UdalostZaciatokObsluhyAutomat zaciatokObsluhy =
                    new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), vykonavajuciAgent);
                simulacia.naplanujUdalost(zaciatokObsluhy);
            }
            else
            {
                throw new RuntimeException("Nikto nepouziva automat a front pred nim nie je prazdny!");

                /*
                // Povodny kod

                // Front pred automatom nie je prazdny, vyber z neho prveho agenta
                // a naplanuj jeho obsluhu
                Agent odobratyAgent = simulacia.odoberFrontAutomat();
                UdalostZaciatokObsluhyAutomat zaciatokObsluhy =
                    new UdalostZaciatokObsluhyAutomat(simulacia, this.getCasVykonania(), odobratyAgent);
                simulacia.naplanujUdalost(zaciatokObsluhy);
                */
            }
        }
    }
}
