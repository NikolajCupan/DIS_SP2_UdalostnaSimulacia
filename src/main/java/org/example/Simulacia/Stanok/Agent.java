package org.example.Simulacia.Stanok;

import org.example.Ostatne.Konstanty;

public class Agent
{
    private final long ID;

    private double casPrichod;
    private double casZaciatokObsluhy;
    private double casKoniecObsluhy;

    public Agent(long ID)
    {
        this.ID = ID;

        this.casPrichod = -1;
        this.casZaciatokObsluhy = -1;
        this.casKoniecObsluhy = -1;
    }

    public void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_AGENT)
        {
            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Prichod");
            System.out.format("%-20s%n", this.casPrichod);

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Zaciatok obsluhy");
            System.out.format("%-30s%n", this.casZaciatokObsluhy);

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Koniec obsluhy");
            System.out.format("%-20s%n", this.casKoniecObsluhy);
        }
    }

    public long getID()
    {
        return this.ID;
    }

    public double getCasPrichod()
    {
        return this.casPrichod;
    }

    public double getCasZaciatokObsluhy()
    {
        return this.casZaciatokObsluhy;
    }

    public double getCasKoniecObsluhy()
    {
        return this.casKoniecObsluhy;
    }

    public void setCasPrichod(double casPrichod)
    {
        this.casPrichod = casPrichod;
    }

    public void setCasZaciatokObsluhy(double casZaciatokObsluhy)
    {
        this.casZaciatokObsluhy = casZaciatokObsluhy;
    }

    public void setCasKoniecObsluhy(double casKoniecObsluhy)
    {
        this.casKoniecObsluhy = casKoniecObsluhy;
    }
}
