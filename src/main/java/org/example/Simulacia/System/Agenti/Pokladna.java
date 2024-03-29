package org.example.Simulacia.System.Agenti;

import java.util.LinkedList;
import java.util.Queue;

public class Pokladna
{
    private boolean obsadena;
    private final Queue<Agent> front;

    public Pokladna()
    {
        this.obsadena = false;
        this.front = new LinkedList<>();
    }

    public int getPocetFront()
    {
        return this.front.size();
    }

    public void pridajDoFrontu(Agent agent)
    {
        if (this.front.contains(agent))
        {
            throw new RuntimeException("Front pred pokladnou uz obsahuje daneho zakaznika!");
        }

        this.front.add(agent);
    }

    public Agent odoberZFrontu()
    {
        if (this.front.isEmpty())
        {
            throw new RuntimeException("Front pred pokladnou je prazdny!");
        }

        return this.front.poll();
    }

    public boolean getObsadena()
    {
        return this.obsadena;
    }

    public void setObsadena(boolean obsadena)
    {
        this.obsadena = obsadena;
    }
}
