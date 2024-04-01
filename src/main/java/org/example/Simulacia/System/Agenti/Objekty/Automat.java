package org.example.Simulacia.System.Agenti.Objekty;

import org.example.Simulacia.System.Agenti.Zakaznik.Agent;

import java.util.LinkedList;
import java.util.Queue;

public class Automat
{
    private boolean obsluhaPrebieha;
    private boolean vypnuty;
    private Queue<Agent> front;

    public Automat()
    {
        this.obsluhaPrebieha = false;
        this.vypnuty = false;
        this.front = new LinkedList<>();
    }

    public void pridajFront(Agent agent)
    {
        this.front.add(agent);
    }

    public Agent odoberFront()
    {
        if (this.front.isEmpty())
        {
            throw new RuntimeException("Pokus o vybratie agenta z frontu pred automatom, ktory je prazdny!");
        }

        return this.front.poll();
    }

    public void vyprazdniAutomat()
    {
        this.front.clear();
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

    public void setObsluhaPrebieha(boolean obsluhaPrebieha)
    {
        this.obsluhaPrebieha = obsluhaPrebieha;
    }

    public void setVypnuty(boolean vypnuty)
    {
        this.vypnuty = vypnuty;
    }
}
