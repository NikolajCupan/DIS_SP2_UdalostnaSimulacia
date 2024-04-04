package org.example.Simulacia.Jadro;

import org.example.NewGUI.ISimulationDelegate;
import org.example.Ostatne.Konstanty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public abstract class SimulacneJadro
{
    // Prepojenie s GUI
    private final List<ISimulationDelegate> delegati;

    private final long pocetReplikacii;
    private int aktualnaReplikacia;

    private double aktualnySimulacnyCas;

    private PriorityBlockingQueue<Udalost> kalendarUdalosti;
    private Comparator komparatorUdalosti;

    private volatile boolean simulaciaPozastavena;
    private volatile boolean simulaciaUkoncena;

    private volatile boolean udalostPrebieha;

    private volatile int rychlost;

    protected SimulacneJadro(int pocetReplikacii, int rychlost)
    {
        this.validujVstupy(pocetReplikacii, rychlost);

        this.delegati = Collections.synchronizedList(new ArrayList<>());
        this.pocetReplikacii = pocetReplikacii;
        this.rychlost = rychlost;
        this.aktualnaReplikacia = -1;
    }

    private void validujVstupy(int pocetReplikacii, int rychlost)
    {
        if (pocetReplikacii < 1)
        {
            throw new RuntimeException("Pocet replikacii nemoze byt mensi ako 1!");
        }

        if (rychlost < 1)
        {
            throw new RuntimeException("Rychlost nemoze byt mensia ako 1!");
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

                // Priebeh 1 udalosti
                this.udalostPrebieha = true;

                Udalost aktualnaUdalost = this.kalendarUdalosti.poll();
                this.aktualizujSimulacnyCas(aktualnaUdalost);

                this.predVykonanimUdalosti(aktualnaUdalost);
                aktualnaUdalost.vykonajUdalost();
                this.udalostPrebieha = false;

                this.poVykonaniUdalosti(aktualnaUdalost);
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

        this.kalendarUdalosti = new PriorityBlockingQueue<Udalost>(1, this.komparatorUdalosti);
        this.aktualnySimulacnyCas = 0.0;

        // Naplanovanie prvej systemovej udalosti v case 0,
        // za podmienky, ze nie je nastavena maximalna rychlost
        if (this.rychlost < Konstanty.MAX_RYCHLOST)
        {
            SystemovaUdalost systemovaUdalost = new SystemovaUdalost(this, 0.0);
            this.naplanujUdalost(systemovaUdalost);
        }
    }

    private void aktualizujSimulacnyCas(Udalost vykonavanaUdalost)
    {
        // Kontrola podmienky, ze simulacny cas nemoze klesat
        if (vykonavanaUdalost.getCasVykonania() < this.aktualnySimulacnyCas)
        {
            throw new RuntimeException("Vykonanie udalosti sposobilo pokles simulacneho casu!");
        }

        this.aktualnySimulacnyCas = vykonavanaUdalost.getCasVykonania();
    }

    public void naplanujUdalost(Udalost planovanaUdalost)
    {
        // Kontrola podmienky, ze simulacny cas nemoze klesat
        if (planovanaUdalost.getCasVykonania() < this.aktualnySimulacnyCas)
        {
            throw new RuntimeException("Planovana udalost ma mensi cas vykonania ako aktualny siulacny cas!");
        }

        this.kalendarUdalosti.add(planovanaUdalost);
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
        boolean simulaciaBolaZastavena = this.simulaciaPozastavena;

        this.simulaciaPozastavena = true;
        while (this.udalostPrebieha)
        {
            // Pockaj kym skonci vykonavanie aktualnej udalosti
        }

        int pocetSystemovychUdalosti = 0;
        for (Udalost udalost : this.kalendarUdalosti)
        {
            if (udalost instanceof SystemovaUdalost)
            {
                pocetSystemovychUdalosti++;
            }
        }

        if (pocetSystemovychUdalosti > 1)
        {
            throw new RuntimeException("Kalendar udalosti obsahuje viac ako 1 systemovu udalost!");
        }
        else if (pocetSystemovychUdalosti == 0)
        {
            // Kalendar udalosti neobsahuje systemovu udalost je nutne ju naplanovat
            Udalost nasledujucaUdalost = this.kalendarUdalosti.peek();
            if (nasledujucaUdalost != null)
            {
                SystemovaUdalost systemovaUdalost = new SystemovaUdalost(this, nasledujucaUdalost.getCasVykonania());
                this.naplanujUdalost(systemovaUdalost);
            }
        }
        this.rychlost = rychlost;

        this.simulaciaPozastavena = simulaciaBolaZastavena;
    }

    public long getPocetReplikacii()
    {
        return this.pocetReplikacii;
    }

    public int getRychlost()
    {
        return this.rychlost;
    }

    public BlockingQueue<Udalost> getKalendarUdalosti()
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

    public void aktualizujGUI(boolean celkoveStatistiky, boolean priebezneStatistiky)
    {
        int velkost = this.delegati.size();
        for (int i = 0; i < velkost; i++)
        {
            this.delegati.get(i).aktualizujSa(this, celkoveStatistiky, priebezneStatistiky);
        }
    }

    public void aktualizujSimulacnyCasGUI()
    {
        for (ISimulationDelegate delegat : this.delegati)
        {
            delegat.aktualizujSimulacnyCas(this);
        }
    }

    protected abstract void predReplikaciami();
    protected abstract void poReplikaciach();
    protected abstract void predReplikaciou();
    protected abstract void poReplikacii();
    protected abstract void predVykonanimUdalosti(Udalost vykonavanaUdalost);
    protected abstract void poVykonaniUdalosti(Udalost vykonanaUdalost);
}
