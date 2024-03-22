package org.example.Simulacia.System.Agenti;

import org.example.Ostatne.Konstanty;

public class Agent
{
    private final long ID;
    private final TypAgenta typAgenta;

    private double casPrichodSystem;

    // Automat
    private double casZaciatokObsluhyAutomat;
    private double casKoniecObsluhyAutomat;

    // Okno
    private double casZaciatokObsluhyOkno;
    private double casKoniecObsluhyOkno;

    public Agent(long ID, TypAgenta typAgenta)
    {
        this.ID = ID;
        this.typAgenta = typAgenta;

        this.casPrichodSystem = -1;

        // Automat
        this.casZaciatokObsluhyAutomat = -1;
        this.casKoniecObsluhyAutomat = -1;

        // Okno
        this.casZaciatokObsluhyOkno = -1;
        this.casKoniecObsluhyOkno = -1;
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
            System.out.format("%-39s", "]   Prichod system");
            System.out.format("%-20s%n", this.casPrichodSystem);

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
            System.out.format("%-39s", "]   Zaciatok obsluhy okno");
            System.out.format("%-20s%n", this.casZaciatokObsluhyOkno);

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Koniec obsluhy okno");
            System.out.format("%-20s%n", this.casKoniecObsluhyOkno);
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

    public double getCasPrichodSystem()
    {
        return this.casPrichodSystem;
    }

    public double getCasZaciatokObsluhyAutomat()
    {
        return this.casZaciatokObsluhyAutomat;
    }

    public double getCasKoniecObsluhyAutomat()
    {
        return this.casKoniecObsluhyAutomat;
    }

    public double getCasZaciatokObsluhyOkno()
    {
        return this.casZaciatokObsluhyOkno;
    }

    public double getCasKoniecObsluhyOkno()
    {
        return this.casKoniecObsluhyOkno;
    }

    public void setCasPrichodSystem(double casPrichodSystem)
    {
        this.casPrichodSystem = casPrichodSystem;
    }

    public void setCasZaciatokObsluhyAutomat(double casZaciatokObsluhyAutomat)
    {
        this.casZaciatokObsluhyAutomat = casZaciatokObsluhyAutomat;
    }

    public void setCasKoniecObsluhyAutomat(double casKoniecObsluhyAutomat)
    {
        this.casKoniecObsluhyAutomat = casKoniecObsluhyAutomat;
    }

    public void setCasZaciatokObsluhyOkno(double casZaciatokObsluhyOkno)
    {
        this.casZaciatokObsluhyOkno = casZaciatokObsluhyOkno;
    }

    public void setCasKoniecObsluhyOkno(double casKoniecObsluhyOkno)
    {
        this.casKoniecObsluhyOkno = casKoniecObsluhyOkno;
    }
}
