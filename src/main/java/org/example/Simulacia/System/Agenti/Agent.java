package org.example.Simulacia.System.Agenti;

import org.example.Ostatne.Konstanty;

public class Agent
{
    private final long ID;
    private final TypAgenta typAgenta;

    private double casPrichod;
    private double casZaciatokObsluhyAutomat;
    private double casKoniecObsluhyAutomat;

    public Agent(long ID, TypAgenta typAgenta)
    {
        this.ID = ID;
        this.typAgenta = typAgenta;

        this.casPrichod = -1;
        this.casZaciatokObsluhyAutomat = -1;
        this.casKoniecObsluhyAutomat = -1;
    }

    public void vypis()
    {
        if (Konstanty.DEBUG_VYPIS_AGENT)
        {
            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Typ");
            System.out.format("%-20s%n", this.typAgenta);

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Prichod");
            System.out.format("%-20s%n", this.casPrichod);

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Zaciatok obsluhy automat");
            System.out.format("%-30s%n", this.casZaciatokObsluhyAutomat);

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Koniec obsluhy automat");
            System.out.format("%-20s%n", this.casKoniecObsluhyAutomat);
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

    public double getCasZaciatokObsluhyAutomat()
    {
        return this.casZaciatokObsluhyAutomat;
    }

    public double getCasKoniecObsluhyAutomat()
    {
        return this.casKoniecObsluhyAutomat;
    }

    public void setCasPrichod(double casPrichod)
    {
        this.casPrichod = casPrichod;
    }

    public void setCasZaciatokObsluhyAutomat(double casZaciatokObsluhyAutomat)
    {
        this.casZaciatokObsluhyAutomat = casZaciatokObsluhyAutomat;
    }

    public void setCasKoniecObsluhyAutomat(double casKoniecObsluhyAutomat)
    {
        this.casKoniecObsluhyAutomat = casKoniecObsluhyAutomat;
    }
}
