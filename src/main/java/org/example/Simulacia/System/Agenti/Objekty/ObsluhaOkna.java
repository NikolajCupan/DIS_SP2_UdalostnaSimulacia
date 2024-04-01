package org.example.Simulacia.System.Agenti.Objekty;

import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Zakaznik.TypAgenta;

import java.util.LinkedList;
import java.util.Queue;

public class ObsluhaOkna
{
    private final Queue<Agent> front;
    private final Okno[] oknaObycajni;
    private final Okno[] oknaOnline;

    public ObsluhaOkna(int pocetObsluznychMiest)
    {
        this.front = new LinkedList<>();

        int pocetOkienOnline = (int)Math.floor(pocetObsluznychMiest / 3.0);
        int pocetOkienObycajni = pocetObsluznychMiest - pocetOkienOnline;

        this.oknaObycajni = new Okno[pocetOkienObycajni];
        for (int i = 0; i < this.oknaObycajni.length; i++)
        {
            this.oknaObycajni[i] = new Okno();
        }

        this.oknaOnline = new Okno[pocetOkienOnline];
        for (int i = 0; i < this.oknaOnline.length; i++)
        {
            this.oknaOnline[i] = new Okno();
        }
    }

    public Agent vyberPrvyOnlineAgent()
    {
        for (Agent agent : this.front)
        {
            if (agent.getTypAgenta() == TypAgenta.ONLINE)
            {
                this.front.remove(agent);
                return agent;
            }
        }

        throw new RuntimeException("Front pred oknami neobsahuje online agenta!");
    }

    public Agent vyberPrvyObycajnyAgent()
    {
        boolean obsahujeZmluvneho = this.frontOknoObsahujeZmluvnehoAgenta();
        TypAgenta vyberanyTyp = (obsahujeZmluvneho ? TypAgenta.ZMLUVNY : TypAgenta.BEZNY);

        for (Agent agent : this.front)
        {
            if (agent.getTypAgenta() == vyberanyTyp)
            {
                this.front.remove(agent);
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
}
