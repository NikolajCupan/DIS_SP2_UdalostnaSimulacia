package org.example.Simulacia.System.Agenti.Objekty;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.Statistiky.SpojitaStatistika;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Zakaznik.TypAgenta;

import java.util.LinkedList;
import java.util.Queue;

public class ObsluhaOkna
{
    private final Queue<Agent> front;
    private final Okno[] oknaObycajni;
    private final Okno[] oknaOnline;

    private final SpojitaStatistika statistikaDlzkaFront;
    private final DiskretnaStatistika statistikaCakanieFront;

    public ObsluhaOkna(int pocetOnlineObsluznychMiest, int pocetObycajnychObsluznychMiest)
    {
        this.front = new LinkedList<>();

        this.oknaObycajni = new Okno[pocetObycajnychObsluznychMiest];
        for (int i = 0; i < this.oknaObycajni.length; i++)
        {
            this.oknaObycajni[i] = new Okno();
        }

        this.oknaOnline = new Okno[pocetOnlineObsluznychMiest];
        for (int i = 0; i < this.oknaOnline.length; i++)
        {
            this.oknaOnline[i] = new Okno();
        }

        this.statistikaDlzkaFront = new SpojitaStatistika();
        this.statistikaCakanieFront = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
    }

    public Agent vyberPrvyOnlineAgent(double simulacnyCas)
    {
        for (Agent agent : this.front)
        {
            if (agent.getTypAgenta() == TypAgenta.ONLINE)
            {
                this.front.remove(agent);
                this.statistikaDlzkaFront.pridajHodnotu(simulacnyCas, this.getPocetFront());
                return agent;
            }
        }

        throw new RuntimeException("Front pred oknami neobsahuje online agenta!");
    }

    public Agent vyberPrvyObycajnyAgent(double simulacnyCas)
    {
        boolean obsahujeZmluvneho = this.frontOknoObsahujeZmluvnehoAgenta();
        TypAgenta vyberanyTyp = (obsahujeZmluvneho ? TypAgenta.ZMLUVNY : TypAgenta.BEZNY);

        for (Agent agent : this.front)
        {
            if (agent.getTypAgenta() == vyberanyTyp)
            {
                this.front.remove(agent);
                this.statistikaDlzkaFront.pridajHodnotu(simulacnyCas, this.getPocetFront());
                return agent;
            }
        }

        throw new RuntimeException("Front pred oknami neobsahuje obycajneho agenta!");
    }

    private boolean frontOknoObsahujeZmluvnehoAgenta()
    {
        for (Agent agent : this.front)
        {
            if (agent.getTypAgenta() == TypAgenta.ZMLUVNY)
            {
                return true;
            }
        }

        return false;
    }

    public boolean frontOknoObsahujeObycajnehoAgenta()
    {
        for (Agent agent : this.front)
        {
            if (agent.getTypAgenta() == TypAgenta.ZMLUVNY || agent.getTypAgenta() == TypAgenta.BEZNY)
            {
                return true;
            }
        }

        return false;
    }

    public boolean frontOknoObsahujeOnlineAgenta()
    {
        for (Agent agent : this.front)
        {
            if (agent.getTypAgenta() == TypAgenta.ONLINE)
            {
                return true;
            }
        }

        return false;
    }

    public void pridajFront(Agent agent, double simulacnyCas)
    {
        if (this.front.size() >= Konstanty.KAPACITA_FRONT_OKNO)
        {
            throw new RuntimeException("Prekrocenie kapacity frontu pred obsluznymi oknami!");
        }
        if (this.front.contains(agent))
        {
            throw new RuntimeException("Front pred obsluznymi oknami uz obsahuje daneho agenta!");
        }

        this.front.add(agent);
        this.statistikaDlzkaFront.pridajHodnotu(simulacnyCas, this.getPocetFront());
    }

    public void pridajCakanieAgent(Agent agent)
    {
        if (agent.getCasZaciatokObsluhyOkno() == -1
            || agent.getCasKoniecObsluhyAutomat() == -1)
        {
            throw new RuntimeException("Agent nema nastavene casy tykajuce sa obsluzneho okna!");
        }

        this.statistikaCakanieFront.pridajHodnotu(agent.getCasZaciatokObsluhyOkno() - agent.getCasKoniecObsluhyAutomat());
    }

    public int getPocetFront()
    {
        return this.front.size();
    }

    public Queue<Agent> getFront()
    {
        return this.front;
    }

    public Okno[] getOknaObycajni()
    {
        return this.oknaObycajni;
    }

    public Okno[] getOknaOnline()
    {
        return this.oknaOnline;
    }

    public double getPriemernaDlzkaFrontu()
    {
        return this.statistikaDlzkaFront.getPriemer();
    }

    public double getPriemerneCakenieFront()
    {
        return this.statistikaCakanieFront.forceGetPriemer();
    }
}
