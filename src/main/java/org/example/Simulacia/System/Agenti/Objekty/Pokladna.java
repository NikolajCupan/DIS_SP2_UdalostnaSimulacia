package org.example.Simulacia.System.Agenti.Objekty;

import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.Statistiky.SpojitaStatistika;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;

import java.util.LinkedList;
import java.util.Queue;

public class Pokladna
{
    private static final int POKLADNA_OBSADENA = 1;
    private static final int POKLADNA_VOLNA = 0;

    private boolean obsadena;
    private final Queue<Agent> front;

    private final SpojitaStatistika statistikaVytazenie;
    private final SpojitaStatistika statistikaDlzkaFront;
    private final DiskretnaStatistika statistikaCakanieFront;

    public Pokladna()
    {
        this.obsadena = false;
        this.front = new LinkedList<>();

        this.statistikaVytazenie = new SpojitaStatistika();
        this.statistikaDlzkaFront = new SpojitaStatistika();
        this.statistikaCakanieFront = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
    }

    public int getPocetFront()
    {
        return this.front.size();
    }

    public void pridajDoFrontu(Agent agent, double simulacnyCas, Pokladna[] pokladne)
    {
        if (!this.obsadena)
        {
            throw new RuntimeException("Pokus o pridanie agenta do frontu pred pokladnou, hoci pokladna nie je obsadena!");
        }

        Pokladna[] ostatnePokladne = new Pokladna[pokladne.length - 1];
        int index = 0;
        for (Pokladna pokladna : pokladne)
        {
            if (pokladna != this)
            {
                ostatnePokladne[index] = pokladna;
                index++;
            }
        }

        if (ostatnePokladne.length != pokladne.length - 1)
        {
            throw new RuntimeException("Neplatna velkost pola ostatnych pokladni!");
        }

        for (Pokladna pokladna : ostatnePokladne)
        {
            if (!pokladna.getObsadena())
            {
                throw new RuntimeException("Agent bol priradeny do frontu pred pokladnou, hoci existuje ina pokladna, ktora nie je obsadena!");
            }

            if (pokladna.getPocetFront() < this.getPocetFront())
            {
                throw new RuntimeException("Agent bol priradeny do frontu pred pokladnou, hoci existuje ina pokladna, ktora ma mensi front!");
            }
        }

        if (this.front.contains(agent))
        {
            throw new RuntimeException("Front pred pokladnou uz obsahuje daneho agenta!");
        }

        this.front.add(agent);
        this.statistikaDlzkaFront.pridajHodnotu(simulacnyCas, this.getPocetFront());
    }

    public Agent odoberZFrontu(double simulacnyCas)
    {
        if (this.front.isEmpty())
        {
            throw new RuntimeException("Front pred pokladnou je prazdny!");
        }

        Agent odobratyAgent = this.front.poll();
        this.statistikaDlzkaFront.pridajHodnotu(simulacnyCas, this.getPocetFront());
        return odobratyAgent;
    }

    public void pridajCakanieAgent(Agent agent)
    {
        if (agent.getCasZaciatokObsluhyPokladna() == -1
            || agent.getCasKoniecObsluhyOkno() == -1)
        {
            throw new RuntimeException("Agent nema nastavene casy tykajuce sa pokladne!");
        }

        this.statistikaCakanieFront.pridajHodnotu(agent.getCasZaciatokObsluhyPokladna() - agent.getCasKoniecObsluhyOkno());
    }

    public double getPriemerneCakenieFront()
    {
        return this.statistikaCakanieFront.forceGetPriemer();
    }

    public double getPriemernaDlzkaFrontu()
    {
        return this.statistikaDlzkaFront.getPriemer();
    }

    public boolean getObsadena()
    {
        return this.obsadena;
    }

    public void setObsadena(boolean obsadena, double simulacnyCas)
    {
        this.obsadena = obsadena;
        this.aktualizujStatistiku(simulacnyCas);
    }

    public double getVytazenie()
    {
        return this.statistikaVytazenie.getPriemer();
    }

    private void aktualizujStatistiku(double simulacnyCas)
    {
        if (this.obsadena)
        {
            this.statistikaVytazenie.pridajHodnotu(simulacnyCas, Pokladna.POKLADNA_OBSADENA);
        }
        else
        {
            this.statistikaVytazenie.pridajHodnotu(simulacnyCas, Pokladna.POKLADNA_VOLNA);
        }
    }
}
