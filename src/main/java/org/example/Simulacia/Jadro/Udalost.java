package org.example.Simulacia.Jadro;

import org.example.Simulacia.System.Agenti.Agent;

public abstract class Udalost
{
    private final SimulacneJadro simulacneJadro;
    private final double casVykonania;
    private final Agent agent;
    private final int priorita;

    public Udalost(SimulacneJadro simulacneJadro, double casVykonania, Agent agent, int priorita)
    {
        this.simulacneJadro = simulacneJadro;
        this.casVykonania = casVykonania;
        this.agent = agent;
        this.priorita = priorita;
    }

    public SimulacneJadro getSimulacneJadro()
    {
        return this.simulacneJadro;
    }

    public double getCasVykonania()
    {
        return this.casVykonania;
    }

    public Agent getAgent()
    {
        return this.agent;
    }

    public int getPriorita()
    {
        return this.priorita;
    }

    abstract public void vykonajUdalost();
}