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

    protected SimulacneJadro(int pocetReplikacii)
    {
        this.validujVstupy(pocetReplikacii);

        this.delegati = new ArrayList<>();
        this.pocetReplikacii = pocetReplikacii;
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

            while (!this.kalendarUdalosti.isEmpty())
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

    private void predReplikaciouJadro()
    {
        if (this.komparatorUdalosti == null)
        {
            throw new RuntimeException("Komparator udalosti nebol nastaveny!");
        }

        this.kalendarUdalosti = new PriorityQueue<>(this.komparatorUdalosti);
        this.aktualnySimulacnyCas = 0.0;
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
            throw new RuntimeException("Pri uspavani simulacia nastala chyba!");
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
