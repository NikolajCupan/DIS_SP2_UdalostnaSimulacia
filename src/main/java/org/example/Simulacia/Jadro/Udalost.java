package org.example.Simulacia.Jadro;

import org.example.Simulacia.Stanok.Agent;

public abstract class Udalost
{
    private final SimulacneJadro simulacneJadro;
    private final double casVykonania;
    private final Agent agent;

    public Udalost(SimulacneJadro simulacneJadro, double casVykonania, Agent agent)
    {
        this.simulacneJadro = simulacneJadro;
        this.casVykonania = casVykonania;
        this.agent = agent;
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

    abstract public void vykonajUdalost();
}