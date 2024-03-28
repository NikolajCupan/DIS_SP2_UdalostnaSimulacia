package org.example.Simulacia.Jadro;

import org.example.GUI.ISimulationDelegate;
import org.example.Ostatne.Konstanty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class SimulacneJadro
{
    // Prepojenie s GUI
    private final ArrayList<ISimulationDelegate> delegati;

    private final long pocetReplikacii;
    private int aktualnaReplikacia;

    private double aktualnySimulacnyCas;

    private PriorityQueue<Udalost> kalendarUdalosti;
    private Comparator komparatorUdalosti;

    private volatile boolean simulaciaPozastavena;
    private volatile boolean simulaciaUkoncena;

    // Rychlost rovna -1 znaci real time
    private volatile int rychlost;

    protected SimulacneJadro(int pocetReplikacii, int rychlost)
    {
        this.validujVstupy(pocetReplikacii);

        this.delegati = new ArrayList<>();
        this.pocetReplikacii = pocetReplikacii;
        this.rychlost = rychlost;
        this.aktualnaReplikacia = -1;
    }

    private void validujVstupy(int pocetReplikacii)
    {
        if (pocetReplikacii < 1)
        {
            throw new RuntimeException("Pocet replikacii nemoze byt mensi ako 1!");
        }
    }

    public void simuluj()
    {
        this.simulaciaPozastavena = false;
        this.simulaciaUkoncena = false;
        this.aktualnaReplikacia = 1;

        this.predReplikaciami();

        while (this.aktualnaReplikacia <= this.pocetReplikacii)
        {
            if (this.simulaciaUkoncena)
            {
                break;
            }

            this.predReplikaciouJadro();
            this.predReplikaciou();

            while (!this.kalendarUdalostiPrazdny())
            {
                if (this.simulaciaUkoncena)
                {
                    break;
                }

                if (this.simulaciaPozastavena)
                {
                    this.uspiSimulaciu();
                    continue;
                }

                Udalost aktualnaUdalost = this.kalendarUdalosti.poll();
                this.aktualnySimulacnyCas = aktualnaUdalost.getCasVykonania();

                this.predVykonanimUdalosti();
                aktualnaUdalost.vykonajUdalost();
                this.poVykonaniUdalosti();

                this.aktualizujGUI();
            }

            this.poReplikacii();
            this.aktualnaReplikacia++;
        }

        this.poReplikaciach();
    }

    // Skontroluje, ci kalendar udalosti obsahuje inu udalost ako systemovu
    private boolean kalendarUdalostiPrazdny()
    {
        for (Udalost udalost : this.kalendarUdalosti)
        {
            if (!(udalost instanceof SystemovaUdalost))
            {
                return false;
            }
        }

        return true;
    }

    private void predReplikaciouJadro()
    {
        if (this.komparatorUdalosti == null)
        {
            throw new RuntimeException("Komparator udalosti nebol nastaveny!");
        }

        this.kalendarUdalosti = new PriorityQueue<>(this.komparatorUdalosti);
        this.aktualnySimulacnyCas = 0.0;

        // Naplanovanie prvej systemovej udalosti v case 0
        SystemovaUdalost systemovaUdalost = new SystemovaUdalost(this, 0.0);
        this.naplanujUdalost(systemovaUdalost);
    }

    public void naplanujUdalost(Udalost udalost)
    {
        this.kalendarUdalosti.add(udalost);
    }

    public void nastavKomparator(Comparator komparator)
    {
        this.komparatorUdalosti = komparator;
    }

    public void toggleSimulaciaPozastavena()
    {
        if (this.simulaciaPozastavena)
        {
            this.simulaciaPozastavena = false;
        }
        else
        {
            this.simulaciaPozastavena = true;
        }
    }

    public void ukonciSimulaciu()
    {
        this.simulaciaUkoncena = true;
    }

    public void setRychlost(int rychlost)
    {
        if (this.simulaciaPozastavena)
        {
            // Rychlost nemozno menit ak je simulacia pozastavena
            return;
        }

        if (rychlost >= Konstanty.MAX_RYCHLOST)
        {
            // Nutnost opetovneho naplanovania systemovej udalosti
            this.simulaciaPozastavena = true;
            Udalost nasledujucaUdalost = this.kalendarUdalosti.peek();
            if (nasledujucaUdalost != null)
            {
                SystemovaUdalost systemovaUdalost = new SystemovaUdalost(this, nasledujucaUdalost.getCasVykonania());
                this.naplanujUdalost(systemovaUdalost);
            }
            this.simulaciaPozastavena = false;
        }

        this.rychlost = rychlost;
    }

    public int getRychlost()
    {
        return this.rychlost;
    }

    public PriorityQueue<Udalost> getKalendarUdalosti()
    {
        return this.kalendarUdalosti;
    }

    public int getAktualnaReplikacia()
    {
        return this.aktualnaReplikacia;
    }

    public double getAktualnySimulacnyCas()
    {
        return this.aktualnySimulacnyCas;
    }

    private void uspiSimulaciu()
    {
        try
        {
            Thread.sleep(Konstanty.DLZKA_PAUZY_MS);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Pri uspavani simulacie nastala chyba!");
        }
    }

    public void pridajDelegata(ISimulationDelegate delegat)
    {
        this.delegati.add(delegat);
    }

    public void odoberDelegata(ISimulationDelegate delegat)
    {
        this.delegati.remove(delegat);
    }

    private void aktualizujGUI()
    {
        for (ISimulationDelegate delegat : this.delegati)
        {
            delegat.aktualizujSa(this);
        }
    }

    protected abstract void predReplikaciami();
    protected abstract void poReplikaciach();
    protected abstract void predReplikaciou();
    protected abstract void poReplikacii();
    protected abstract void predVykonanimUdalosti();
    protected abstract void poVykonaniUdalosti();
}
