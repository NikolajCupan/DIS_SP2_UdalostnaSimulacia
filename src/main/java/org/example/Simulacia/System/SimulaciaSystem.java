package org.example.Simulacia.System;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.SpojityExponencialnyGenerator;
import org.example.Generatory.SpojityRovnomernyGenerator;
import org.example.Generatory.SpojityTrojuholnikovyGenerator;
import org.example.Ostatne.Identifikator;
import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Generovania.GenerovanieTypuZakaznika;
import org.example.Simulacia.System.Agenti.AgentKomparator;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.Okno;
import org.example.Simulacia.System.Agenti.TypAgenta;
import org.example.Simulacia.System.Udalosti.UdalostKomparator;
import org.example.Simulacia.System.Udalosti.UdalostPrichodZakaznika;
import org.example.Simulacia.Jadro.Udalost;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class SimulaciaSystem extends SimulacneJadro
{
    private final GeneratorNasad generatorNasad;
    private final double dlzkaTrvaniaSimulacie;
    private boolean prichodyZrusene;
    private ConcurrentSkipListSet<Agent> agenti;


    // Automat
    private boolean obsluhaAutomatPrebieha;
    private boolean automatVypnuty;
    private Queue<Agent> frontAutomat;
    // Koniec automat


    // Obsluha okno
    private final int pocetObsluznychMiest;
    private Queue<Agent> frontOkno;
    private Okno[] oknaObycajni;
    private Okno[] oknaOnline;
    // Koniec obsluha okno


    // Generatory
    private SpojityExponencialnyGenerator generatorDalsiPrichod;
    private GenerovanieTypuZakaznika generatorTypZakaznika;
    private SpojityRovnomernyGenerator generatorVydanieListka;

    private SpojityRovnomernyGenerator generatorObsluhaObycajni;
    private SpojityTrojuholnikovyGenerator generatorObsluhaOnline;

    // Statistiky jednej replikacie
    private DiskretnaStatistika statistikaCasSystem;

    // Celkove statistiky
    private DiskretnaStatistika celkovaStatistikaCasSystem;

    public SimulaciaSystem(int pocetReplikacii, int rychlost, double dlzkaTrvaniaSimulacie,
                           int pocetObsluznychMiest, int pocetPokladni, int nasada, boolean pouziNasadu)
    {
        super(pocetReplikacii, rychlost);
        this.validujVstupy(pocetObsluznychMiest, pocetPokladni, dlzkaTrvaniaSimulacie);

        GeneratorNasad.inicializujGeneratorNasad(nasada, pouziNasadu);
        this.generatorNasad = new GeneratorNasad();

        this.dlzkaTrvaniaSimulacie = dlzkaTrvaniaSimulacie;
        this.prichodyZrusene = false;

        // Obsluha okno
        this.pocetObsluznychMiest = pocetObsluznychMiest;
    }

    private void validujVstupy(int pocetObsluznychMiest, int pocetPokladni, double dlzkaTrvaniaSimulacie)
    {
        if (pocetObsluznychMiest < 3)
        {
            throw new RuntimeException("Pocet obsluznych miest nemoze byt mensi ako 3!");
        }

        if (pocetPokladni < 1)
        {
            throw new RuntimeException("Pocet pokladni nemoze byt mensi ako 1!");
        }

        if (dlzkaTrvaniaSimulacie <= 0.0)
        {
            throw new RuntimeException("Dlzka trvania simulacie musi byt vacsia ako 0!");
        }
    }

    @Override
    protected void predReplikaciami()
    {
        Comparator<Udalost> komparator = new UdalostKomparator();
        this.nastavKomparator(komparator);

        // Generatory
        this.generatorDalsiPrichod = new SpojityExponencialnyGenerator(1.0 / 120.0, this.generatorNasad);
        this.generatorTypZakaznika = new GenerovanieTypuZakaznika(this.generatorNasad);
        this.generatorVydanieListka = new SpojityRovnomernyGenerator(30.0, 180.0, this.generatorNasad);

        this.generatorObsluhaObycajni = new SpojityRovnomernyGenerator(60.0, 900.0, this.generatorNasad);
        this.generatorObsluhaOnline = new SpojityTrojuholnikovyGenerator(60.0, 480.0, 120.0, this.generatorNasad);

        // Statistiky
        this.celkovaStatistikaCasSystem = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
    }

    @Override
    protected void poReplikaciach() {}

    @Override
    protected void predReplikaciou()
    {
        // Ostatne
        this.prichodyZrusene = false;
        this.agenti = new ConcurrentSkipListSet<>(new AgentKomparator());
        Identifikator.resetID();
        // Koniec ostatne


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
        this.statistikaCasSystem = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        // Koniec statistiky


        // Naplanovanie prichodu 1. zakaznika
        Agent vykonavajuciAgent = new Agent(Identifikator.getID(), this.generatorTypZakaznika.getTypAgenta());
        double casPrichodu = this.generatorDalsiPrichod.sample();
        if (casPrichodu <= this.dlzkaTrvaniaSimulacie)
        {
            // Udalost je naplanovana iba za predpokladu, ze nenastane po vyprsani simulacneho casu
            this.pridajAgenta(vykonavajuciAgent);
            UdalostPrichodZakaznika prichod = new UdalostPrichodZakaznika(this, casPrichodu, vykonavajuciAgent);
            this.naplanujUdalost(prichod);
        }
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

    @Override
    protected void predVykonanimUdalosti()
    {
        this.skontrolujVyprsanieSimulacnehoCasu();
    }

    @Override
    protected void poVykonaniUdalosti()
    {
        this.skontrolujVyprsanieSimulacnehoCasu();
    }

    private void skontrolujVyprsanieSimulacnehoCasu()
    {
        if (this.getAktualnySimulacnyCas() > this.dlzkaTrvaniaSimulacie
            && !this.prichodyZrusene)
        {
            this.prichodyZrusene = true;

            // Doslo k prekroceniu simulacneho casu, vyprazdni front pred automatom
            this.frontAutomat.clear();

            // Kontrola stavu kalendara udalosti
            for (Udalost udalost : this.getKalendarUdalosti())
            {
                if (udalost instanceof UdalostPrichodZakaznika)
                {
                    throw new RuntimeException("Doslo k naplanovaniu udalosti prichodu zakaznika po otvaracej dobe!");
                }
            }
        }
    }


    // Ostatne
    public double getDlzkaTrvaniaSimulacie()
    {
        return this.dlzkaTrvaniaSimulacie;
    }

    public void pridajAgenta(Agent agent)
    {
        boolean obsahujeAgenta = this.agenti.contains(agent);
        if (obsahujeAgenta)
        {
            throw new RuntimeException("Zoznam uz daneho agenta obsahuje!");
        }

        this.agenti.add(agent);
    }

    public SortedSet<Agent> getAgenti()
    {
        return this.agenti;
    }
    // Koniec ostatne


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

    public Agent vyberPrvyOnlineAgent()
    {
        for (Agent agent : this.frontOkno)
        {
            if (agent.getTypAgenta() == TypAgenta.ONLINE)
            {
                this.frontOkno.remove(agent);
                return agent;
            }
        }

        throw new RuntimeException("Front pred oknami neobsahuje online agenta!");
    }

    public Agent vyberPrvyObycajnyAgent()
    {
        boolean obsahujeZmluvneho = this.frontOknoObsahujeZmluvnehoAgenta();
        TypAgenta vyberanyTyp = (obsahujeZmluvneho ? TypAgenta.ZMLUVNY : TypAgenta.BEZNY);

        for (Agent agent : this.frontOkno)
        {
            if (agent.getTypAgenta() == vyberanyTyp)
            {
                this.frontOkno.remove(agent);
                return agent;
            }
        }

        throw new RuntimeException("Front pred oknami neobsahuje obycajneho agenta!");
    }

    private boolean frontOknoObsahujeZmluvnehoAgenta()
    {
        for (Agent agent : this.frontOkno)
        {
            if (agent.getTypAgenta() == TypAgenta.ZMLUVNY)
            {
                return true;
            }
        }

        return false;
    }

    public boolean frontOknoObsahujeObycajnehoAgenta()
    {
        for (Agent agent : this.frontOkno)
        {
            if (agent.getTypAgenta() == TypAgenta.ZMLUVNY || agent.getTypAgenta() == TypAgenta.BEZNY)
            {
                return true;
            }
        }

        return false;
    }

    public boolean frontOknoObsahujeOnlineAgenta()
    {
        for (Agent agent : this.frontOkno)
        {
            if (agent.getTypAgenta() == TypAgenta.ONLINE)
            {
                return true;
            }
        }

        return false;
    }
    // Koniec okno


    // Generatory
    public GenerovanieTypuZakaznika getGeneratorTypZakaznika()
    {
        return this.generatorTypZakaznika;
    }

    public SpojityExponencialnyGenerator getGeneratorDalsiPrichod()
    {
        return this.generatorDalsiPrichod;
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

    public DiskretnaStatistika getCelkovaStatistikaCasSystem()
    {
        return this.celkovaStatistikaCasSystem;
    }
    // Koniec statistiky
}
