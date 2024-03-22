package org.example.Simulacia.System.Agenti;

import org.example.Ostatne.Konstanty;

public class Agent
{
    private final long ID;
    private final TypAgenta typAgenta;

    // Automat
    private double casPrichod;
    private double casZaciatokObsluhyAutomat;
    private double casKoniecObsluhyAutomat;

    // Obsluha
    private double casZaciatokObsluhy;
    private double casKoniecObsluhy;

    public Agent(long ID, TypAgenta typAgenta)
    {
        this.ID = ID;
        this.typAgenta = typAgenta;

        // Automat
        this.casPrichod = -1;
        this.casZaciatokObsluhyAutomat = -1;
        this.casKoniecObsluhyAutomat = -1;

        // Obsluha
        this.casZaciatokObsluhy = -1;
        this.casKoniecObsluhy = -1;
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

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Zaciatok obsluhy");
            System.out.format("%-20s%n", this.casZaciatokObsluhy);

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

    public TypAgenta getTypAgenta()
    {
        return this.typAgenta;
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

    public void setCasZaciatokObsluhy(double casZaciatokObsluhy)
    {
        this.casZaciatokObsluhy = casZaciatokObsluhy;
    }

    public void setCasKoniecObsluhy(double casKoniecObsluhy)
    {
        this.casKoniecObsluhy = casKoniecObsluhy;
    }
}
