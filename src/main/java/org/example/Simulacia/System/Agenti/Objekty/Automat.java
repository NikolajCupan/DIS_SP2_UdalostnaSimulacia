package org.example.Simulacia.System.Agenti.Objekty;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.Statistiky.SpojitaStatistika;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;

import java.util.LinkedList;
import java.util.Queue;

public class Automat
{
    private static final int AUTOMAT_OBSADENY = 1;
    private static final int AUTOMAT_VOLNY = 0;

    private boolean obsluhaPrebieha;
    private boolean vypnuty;
    private final Queue<Agent> front;

    private final SpojitaStatistika statistikaDlzkaFront;
    private final DiskretnaStatistika statistikaCakanieFront;
    private final SpojitaStatistika statistikaVytazenie;

    public Automat()
    {
        this.obsluhaPrebieha = false;
        this.vypnuty = false;
        this.front = new LinkedList<>();

        this.statistikaDlzkaFront = new SpojitaStatistika();
        this.statistikaCakanieFront = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        this.statistikaVytazenie = new SpojitaStatistika();
    }

    public void pridajFront(Agent agent, double simulacnyCas)
    {
        if (this.front.contains(agent))
        {
            throw new RuntimeException("Front pred automatom uz obsahuje daneho agenta!");
        }

        this.front.add(agent);
        this.statistikaDlzkaFront.pridajHodnotu(simulacnyCas, this.getPocetFront());
    }

    public Agent odoberFront(double simulacnyCas)
    {
        if (this.front.isEmpty())
        {
            throw new RuntimeException("Pokus o vybratie agenta z frontu pred automatom, ktory je prazdny!");
        }

        Agent odobratyAgent = this.front.poll();
        this.statistikaDlzkaFront.pridajHodnotu(simulacnyCas, this.getPocetFront());
        return odobratyAgent;
    }

    public double getPriemernaDlzkaFrontu(double simulacnyCas)
    {
        return this.statistikaDlzkaFront.getPriemer(simulacnyCas, this.getPocetFront());
    }

    public void pridajCakanieAgent(Agent agent)
    {
        if (agent.getCasZaciatokObsluhyAutomat() == -1
            || agent.getCasPrichodSystem() == -1)
        {
            throw new RuntimeException("Agent nema nastavene casy tykajuce sa automatu!");
        }

        this.statistikaCakanieFront.pridajHodnotu(agent.getCasZaciatokObsluhyAutomat() - agent.getCasPrichodSystem());
    }

    public double getPriemerneCakenieFront()
    {
        return this.statistikaCakanieFront.forceGetPriemer();
    }

    public void vyprazdniAutomat(double simulacnyCas, DiskretnaStatistika statistikaCasSystem)
    {
        for (Agent agent : this.front)
        {
            agent.setCasZaciatokObsluhyAutomat(simulacnyCas);
            this.pridajCakanieAgent(agent);

            statistikaCasSystem.pridajHodnotu(agent.getCasZaciatokObsluhyAutomat() - agent.getCasPrichodSystem());
        }

        this.front.clear();
        this.statistikaDlzkaFront.pridajHodnotu(simulacnyCas, 0);
    }

    public int getPocetFront()
    {
        return this.front.size();
    }

    public boolean getObsluhaPrebieha()
    {
        return this.obsluhaPrebieha;
    }

    public boolean getVypnuty()
    {
        return this.vypnuty;
    }

    public void setVypnuty(boolean vypnuty)
    {
        this.vypnuty = vypnuty;
    }

    public void setObsluhaPrebieha(boolean obsluhaPrebieha, double simulacnyCas)
    {
        this.obsluhaPrebieha = obsluhaPrebieha;
        this.aktualizujStatistiku(simulacnyCas);
    }

    public double getVytazenie(double simulacnyCas)
    {
        int aktualnyStav = (this.obsluhaPrebieha) ? Automat.AUTOMAT_OBSADENY : Automat.AUTOMAT_VOLNY;
        return this.statistikaVytazenie.getPriemer(simulacnyCas, aktualnyStav);
    }

    private void aktualizujStatistiku(double simulacnyCas)
    {
        if (this.obsluhaPrebieha)
        {
            this.statistikaVytazenie.pridajHodnotu(simulacnyCas, Automat.AUTOMAT_OBSADENY);
        }
        else
        {
            this.statistikaVytazenie.pridajHodnotu(simulacnyCas, Automat.AUTOMAT_VOLNY);
        }
    }
}
