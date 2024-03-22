package org.example.Simulacia.System;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.SpojityExponencialnyGenerator;
import org.example.Generatory.SpojityRovnomernyGenerator;
import org.example.Generatory.SpojityTrojuholnikovyGenerator;
import org.example.Ostatne.Identifikator;
import org.example.Simulacia.Generovania.GenerovanieTypuZakaznika;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.Okno;
import org.example.Simulacia.System.Udalosti.UdalostKomparator;
import org.example.Simulacia.System.Udalosti.UdalostPrichodZakaznika;
import org.example.Simulacia.Jadro.Udalost;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class SimulaciaSystem extends SimulacneJadro
{
    private final GeneratorNasad generatorNasad;

    // Automat
    private boolean obsluhaAutomatPrebieha;
    private boolean automatVypnuty;
    private Queue<Agent> frontAutomat;

    // Obsluha okno
    private final int pocetObsluznychMiest;
    private Queue<Agent> frontOkno;
    private Okno[] oknaObycajni;
    private Okno[] oknaOnline;

    // Generatory
    private SpojityExponencialnyGenerator generatorDalsiVstup;
    private GenerovanieTypuZakaznika generatorTypZakaznika;
    private SpojityRovnomernyGenerator generatorVydanieListka;

    private SpojityRovnomernyGenerator generatorObsluhaObycajni;
    private SpojityTrojuholnikovyGenerator generatorObsluhaOnline;

    // Statistiky jednej replikacie
    private DiskretnaStatistika statistikaCasSystem;

    // Celkove statistiky
    private DiskretnaStatistika celkovaStatistikaCasSystem;

    public SimulaciaSystem(int pocetReplikacii, double dlzkaTrvaniaSimulacie, int pocetObsluznychMiest, int nasada, boolean pouziNasadu)
    {
        super(pocetReplikacii, dlzkaTrvaniaSimulacie);
        this.validujVstupy(pocetObsluznychMiest);

        GeneratorNasad.inicializujGeneratorNasad(nasada, pouziNasadu);
        this.generatorNasad = new GeneratorNasad();

        this.pocetObsluznychMiest = pocetObsluznychMiest;
    }

    private void validujVstupy(int pocetObsluznychMiest)
    {
        if (pocetObsluznychMiest < 3)
        {
            throw new RuntimeException("Pocet obsluznych miest nemoze byt mensi ako 3!");
        }
    }

    @Override
    protected void predReplikaciami()
    {
        Comparator<Udalost> komparator = new UdalostKomparator();
        this.nastavKomparator(komparator);

        // Generatory
        this.generatorDalsiVstup = new SpojityExponencialnyGenerator(1.0 / 120.0, this.generatorNasad);
        this.generatorTypZakaznika = new GenerovanieTypuZakaznika(this.generatorNasad);
        this.generatorVydanieListka = new SpojityRovnomernyGenerator(30.0, 180.0, this.generatorNasad);

        this.generatorObsluhaObycajni = new SpojityRovnomernyGenerator(60.0, 900.0, this.generatorNasad);
        this.generatorObsluhaOnline = new SpojityTrojuholnikovyGenerator(60.0, 480.0, 120.0, this.generatorNasad);

        // Statistiky
        this.celkovaStatistikaCasSystem = new DiskretnaStatistika();
    }

    @Override
    protected void poReplikaciach()
    {
        this.celkovaStatistikaCasSystem.skusPrepocitatStatistiky();

        System.out.println("[STATISTIKA] Priemerna doba v systeme: "
            + this.celkovaStatistikaCasSystem.getPriemer() + " [" + this.celkovaStatistikaCasSystem.getDolnaHranicaIS()
            + ", " + this.celkovaStatistikaCasSystem.getHornaHranicaIS() + "]");
    }

    @Override
    protected void predReplikaciou()
    {
        // Automat
        this.obsluhaAutomatPrebieha = false;
        this.automatVypnuty = false;
        this.frontAutomat = new LinkedList<>();
        // Koniec automat


        // Obsluha
        this.frontOkno = new LinkedList<>();

        int pocetOkienOnline = (int)Math.floor(this.pocetObsluznychMiest / 3.0);
        int pocetOkienObycajni = this.pocetObsluznychMiest - pocetOkienOnline;

        this.oknaObycajni = new Okno[pocetOkienObycajni];
        for (int i = 0; i < this.oknaObycajni.length; i++)
        {
            this.oknaObycajni[i] = new Okno();
        }

        this.oknaOnline = new Okno[pocetOkienOnline];
        for (int i = 0; i < this.oknaOnline.length; i++)
        {
            this.oknaOnline[i] = new Okno();
        }
        // Koniec obsluha


        // Statistiky
        this.statistikaCasSystem = new DiskretnaStatistika();
        // Koniec statistiky


        // Naplanovanie prichodu 1. zakaznika v case 0.0
        Agent vykonavajuciAgent = new Agent(Identifikator.getID(), this.generatorTypZakaznika.getTypAgenta());
        UdalostPrichodZakaznika prichod = new UdalostPrichodZakaznika(this, 0.0, vykonavajuciAgent);
        this.naplanujUdalost(prichod);
    }

    @Override
    protected void poReplikacii()
    {
        this.statistikaCasSystem.skusPrepocitatStatistiky();
        if (this.statistikaCasSystem.getStatistikyVypocitane())
        {
            this.celkovaStatistikaCasSystem.pridajHodnotu(this.statistikaCasSystem.getPriemer());
        }
    }


    // Automat
    public void pridajFrontAutomat(Agent agent)
    {
        this.frontAutomat.add(agent);
    }

    public Agent odoberFrontAutomat()
    {
        if (this.frontAutomat.isEmpty())
        {
            throw new RuntimeException("Pokus o vybratie agenta z frontu pred automatom, ktory je prazdny!");
        }

        return this.frontAutomat.poll();
    }

    public int getPocetFrontAutomat()
    {
        return this.frontAutomat.size();
    }

    public boolean getObsluhaAutomatPrebieha()
    {
        return this.obsluhaAutomatPrebieha;
    }

    public boolean getAutomatVypnuty()
    {
        return this.automatVypnuty;
    }

    public void setObsluhaAutomatPrebieha(boolean obsluhaAutomatPrebieha)
    {
        this.obsluhaAutomatPrebieha = obsluhaAutomatPrebieha;
    }

    public void setAutomatVypnuty(boolean automatVypnuty)
    {
        this.automatVypnuty = automatVypnuty;
    }
    // Koniec automat


    // Okno
    public Queue<Agent> getFrontOkno()
    {
        return this.frontOkno;
    }

    public Okno[] getOknaObycajni()
    {
        return this.oknaObycajni;
    }

    public Okno[] getOknaOnline()
    {
        return this.oknaOnline;
    }
    // Koniec okno


    // Generatory
    public GenerovanieTypuZakaznika getGeneratorTypZakaznika()
    {
        return this.generatorTypZakaznika;
    }

    public SpojityExponencialnyGenerator getGeneratorDalsiVstup()
    {
        return this.generatorDalsiVstup;
    }

    public SpojityRovnomernyGenerator getGeneratorVydanieListka()
    {
        return this.generatorVydanieListka;
    }

    public SpojityRovnomernyGenerator getGeneratorObsluhaObycajni()
    {
        return this.generatorObsluhaObycajni;
    }

    public SpojityTrojuholnikovyGenerator getGeneratorObsluhaOnline()
    {
        return this.generatorObsluhaOnline;
    }
    // Koniec generatory


    // Statistiky
    public DiskretnaStatistika getStatistikaCasSystem()
    {
        return this.statistikaCasSystem;
    }
    // Koniec statistiky
}
