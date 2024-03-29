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

    private boolean odlozenyTovar;
    private Okno odlozenyTovarOkno;

    // Pokladna
    private double casZaciatokObsluhyPokladna;
    private double casKoniecObsluhyPokladna;

    // Vyzdvihnutie tovaru
    private double casZaciatokVyzdvihnutie;
    private double casKoniecVyzdvihnutie;

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
        this.odlozenyTovar = false;
        this.odlozenyTovarOkno = null;

        // Pokladna
        this.casZaciatokObsluhyPokladna = -1;
        this.casKoniecObsluhyPokladna = -1;

        // Vyzdvihnutie tovaru
        this.casZaciatokVyzdvihnutie = -1;
        this.casKoniecVyzdvihnutie = -1;
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

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Zaciatok obsluhy pokladna");
            System.out.format("%-20s%n", this.casZaciatokObsluhyPokladna);

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Koniec obsluhy pokladna");
            System.out.format("%-20s%n", this.casKoniecObsluhyPokladna);

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Zaciatok vyzdvihnutie tovaru");
            System.out.format("%-20s%n", this.casZaciatokVyzdvihnutie);

            System.out.print("    [AGENT ");
            System.out.format("%4s", this.ID);
            System.out.format("%-39s", "]   Koniec vyzdvihnutie tovaru");
            System.out.format("%-20s%n", this.casKoniecVyzdvihnutie);
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

    public boolean getOdlozenyTovar()
    {
        return this.odlozenyTovar;
    }

    public Okno getOdlozenyTovarOkno()
    {
        if (!this.odlozenyTovarOkno.getObsadene())
        {
            throw new RuntimeException("Okno, pri ktorom agent zanechal tovar je oznacene ako neobsadene!");
        }

        return this.odlozenyTovarOkno;
    }

    public double getCasZaciatokObsluhyPokladna()
    {
        return this.casZaciatokObsluhyPokladna;
    }

    public double getCasKoniecObsluhyPokladna()
    {
        return this.casKoniecObsluhyPokladna;
    }

    public double getCasZaciatokVyzdvihnutie()
    {
        return casZaciatokVyzdvihnutie;
    }

    public double getCasKoniecVyzdvihnutie()
    {
        return casKoniecVyzdvihnutie;
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

    public void setOdlozenyTovar(boolean odlozenyTovar)
    {
        this.odlozenyTovar = odlozenyTovar;
    }

    public void setOdlozenyTovarOkno(Okno odlozenyTovarOkno)
    {
        if (!odlozenyTovarOkno.getObsadene())
        {
            throw new RuntimeException("Okno, pri ktorom agent zanechal tovar je oznacene ako neobsadene!");
        }

        this.odlozenyTovarOkno = odlozenyTovarOkno;
    }

    public void setCasZaciatokObsluhyPokladna(double casZaciatokObsluhyPokladna)
    {
        this.casZaciatokObsluhyPokladna = casZaciatokObsluhyPokladna;
    }

    public void setCasKoniecObsluhyPokladna(double casKoniecObsluhyPokladna)
    {
        this.casKoniecObsluhyPokladna = casKoniecObsluhyPokladna;
    }

    public void setCasZaciatokVyzdvihnutie(double casZaciatokVyzdvihnutie)
    {
        this.casZaciatokVyzdvihnutie = casZaciatokVyzdvihnutie;
    }

    public void setCasKoniecVyzdvihnutie(double casKoniecVyzdvihnutie)
    {
        this.casKoniecVyzdvihnutie = casKoniecVyzdvihnutie;
    }
}
