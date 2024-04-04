package org.example.Simulacia.System;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.SpojityExponencialnyGenerator;
import org.example.Generatory.SpojityRovnomernyGenerator;
import org.example.Generatory.SpojityTrojuholnikovyGenerator;
import org.example.Ostatne.Identifikator;
import org.example.Ostatne.Konstanty;
import org.example.Simulacia.Generovania.*;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.Agenti.Objekty.Automat;
import org.example.Simulacia.System.Agenti.Objekty.ObsluhaOkna;
import org.example.Simulacia.System.Agenti.Objekty.Okno;
import org.example.Simulacia.System.Agenti.Objekty.Pokladna;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Zakaznik.AgentKomparator;
import org.example.Simulacia.System.Udalosti.Automat.UdalostZaciatokObsluhyAutomat;
import org.example.Simulacia.System.Udalosti.UdalostKomparator;
import org.example.Simulacia.System.Udalosti.UdalostPrichodZakaznika;
import org.example.Simulacia.Jadro.Udalost;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class SimulaciaSystem extends SimulacneJadro
{
    // Ostatne
    private final GeneratorNasad generatorNasad;
    private final double dlzkaTrvaniaSimulacie;
    private boolean prichodyZrusene;
    private ConcurrentSkipListSet<Agent> agenti;
    private Identifikator identifikator;
    // Koniec ostatne


    // Automat
    private Automat automat;

    private GenerovanieTypuZakaznika generatorTypZakaznika;
    private SpojityExponencialnyGenerator generatorDalsiPrichod;

    private SpojityRovnomernyGenerator generatorVydanieListka;
    // Koniec automat


    // Obsluha okno
    private final int pocetObycajnychObsluznychMiest;
    private final int pocetOnlineObsluznychMiest;
    private ObsluhaOkna obsluhaOkna;

    private SpojityRovnomernyGenerator generatorObsluhaObycajni;
    private SpojityTrojuholnikovyGenerator generatorObsluhaOnline;

    private GenerovanieTrvaniaPripravy generatorTrvaniePripravy;
    private GenerovanieVelkostiTovaru generatorVelkostTovaru;
    // Koniec obsluha okno


    // Pokladna
    private final int pocetPokladni;
    private Pokladna[] pokladne;

    private GenerovanieVyberFrontu generatorVyberFrontu;
    private GenerovanieDlzkyPlatenia generatorDlzkaPlatenia;
    // Koniec pokladna


    // Vyzdvihnutie tovaru
    private SpojityRovnomernyGenerator generatorVyzdvihnutieTovaru;
    // Koniec vyzdvihnutie tovaru


    // Statistiky 1 replikacie
    private DiskretnaStatistika statistikaCasSystem;
    // Koniec statistiky 1 replikacie


    // Celkove statistiky
    private DiskretnaStatistika celkovaStatistikaCasSystem;

    private DiskretnaStatistika celkovaStatistikaDlzkaFrontAutomat;
    private DiskretnaStatistika celkovaStatistikaCasFrontAutomat;
    private DiskretnaStatistika celkovaStatistikaVytazenieAutomat;

    private DiskretnaStatistika celkovaStatistikaCasPoslednyOdchod;
    private DiskretnaStatistika celkovaStatistikaPocetObsluzenychAgentov;

    private DiskretnaStatistika celkovaStatistikaDlzkaFrontOkno;
    private DiskretnaStatistika celkovaStatistikaCasFrontOkno;

    private DiskretnaStatistika[] celkovaStatistikaVytazenieObycajneOkna;
    private DiskretnaStatistika[] celkovaStatistikaVytazenieOnlineOkna;

    private DiskretnaStatistika[] celkovaStatistikaVytazeniePokladne;
    private DiskretnaStatistika[] celkovaStatistikaDlzkaFrontPokladne;
    private DiskretnaStatistika[] celkovaStatistikaCakanieFrontPokladne;
    // Koniec celkove statistiky


    public SimulaciaSystem(int pocetReplikacii, int rychlost, double dlzkaTrvaniaSimulacie,
                           int pocetObsluznychMiest, int pocetPokladni, int nasada, boolean pouziNasadu)
    {
        super(pocetReplikacii, rychlost);
        this.validujVstupy(pocetObsluznychMiest, pocetPokladni, dlzkaTrvaniaSimulacie);

        GeneratorNasad.inicializujGeneratorNasad(nasada, pouziNasadu);
        this.generatorNasad = new GeneratorNasad();
        this.dlzkaTrvaniaSimulacie = dlzkaTrvaniaSimulacie;

        // Obsluha okno
        this.pocetOnlineObsluznychMiest = (int)Math.floor(pocetObsluznychMiest / 3.0);
        this.pocetObycajnychObsluznychMiest = pocetObsluznychMiest - this.pocetOnlineObsluznychMiest;

        // Obsluha pokladne
        this.pocetPokladni = pocetPokladni;
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
        this.generatorTypZakaznika = new GenerovanieTypuZakaznika(this.generatorNasad);
        this.generatorDalsiPrichod = new SpojityExponencialnyGenerator(1.0 / 120.0, this.generatorNasad);
        this.generatorVydanieListka = new SpojityRovnomernyGenerator(30.0, 180.0, this.generatorNasad);

        this.generatorObsluhaObycajni = new SpojityRovnomernyGenerator(60.0, 900.0, this.generatorNasad);
        this.generatorObsluhaOnline = new SpojityTrojuholnikovyGenerator(60.0, 480.0, 120.0, this.generatorNasad);

        this.generatorTrvaniePripravy = new GenerovanieTrvaniaPripravy(this.generatorNasad);
        this.generatorVelkostTovaru = new GenerovanieVelkostiTovaru(this.generatorNasad);

        this.generatorVyberFrontu = new GenerovanieVyberFrontu(this.generatorNasad);
        this.generatorDlzkaPlatenia = new GenerovanieDlzkyPlatenia(this.generatorNasad);

        this.generatorVyzdvihnutieTovaru = new SpojityRovnomernyGenerator(30.0, 70.0, this.generatorNasad);

        // Celkove statistiky
        this.celkovaStatistikaCasSystem = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);

        this.celkovaStatistikaDlzkaFrontAutomat = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        this.celkovaStatistikaCasFrontAutomat = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        this.celkovaStatistikaVytazenieAutomat = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);

        this.celkovaStatistikaCasPoslednyOdchod = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        this.celkovaStatistikaPocetObsluzenychAgentov = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);

        this.celkovaStatistikaDlzkaFrontOkno = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        this.celkovaStatistikaCasFrontOkno = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);

        this.celkovaStatistikaVytazenieObycajneOkna = new DiskretnaStatistika[this.pocetObycajnychObsluznychMiest];
        for (int i = 0; i < this.pocetObycajnychObsluznychMiest; i++)
        {
            this.celkovaStatistikaVytazenieObycajneOkna[i] = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        }

        this.celkovaStatistikaVytazenieOnlineOkna = new DiskretnaStatistika[this.pocetOnlineObsluznychMiest];
        for (int i = 0; i < this.pocetOnlineObsluznychMiest; i++)
        {
            this.celkovaStatistikaVytazenieOnlineOkna[i] = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        }

        this.celkovaStatistikaVytazeniePokladne = new DiskretnaStatistika[this.pocetPokladni];
        this.celkovaStatistikaCakanieFrontPokladne = new DiskretnaStatistika[this.pocetPokladni];
        this.celkovaStatistikaDlzkaFrontPokladne = new DiskretnaStatistika[this.pocetPokladni];
        for (int i = 0; i < this.pocetPokladni; i++)
        {
            this.celkovaStatistikaVytazeniePokladne[i] = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
            this.celkovaStatistikaCakanieFrontPokladne[i] = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
            this.celkovaStatistikaDlzkaFrontPokladne[i] = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        }
    }

    @Override
    protected void poReplikaciach() {}

    @Override
    protected void predReplikaciou()
    {
        // Ostatne
        this.prichodyZrusene = false;
        this.agenti = new ConcurrentSkipListSet<>(new AgentKomparator());
        this.identifikator = new Identifikator();
        // Koniec ostatne


        // Automat
        this.automat = new Automat();
        // Koniec automat


        // Obsluha
        this.obsluhaOkna = new ObsluhaOkna(this.pocetOnlineObsluznychMiest, this.pocetObycajnychObsluznychMiest);
        // Koniec obsluha


        // Pokladna
        this.pokladne = new Pokladna[this.pocetPokladni];
        for (int i = 0; i < this.pokladne.length; i++)
        {
            this.pokladne[i] = new Pokladna();
        }
        // Koniec pokladna


        // Statistiky 1 replikacie
        this.statistikaCasSystem = new DiskretnaStatistika(95, Konstanty.KVANTIL_95_PERCENT);
        // Koniec statistiky 1 replikacie


        // Naplanovanie prichodu 1. zakaznika
        Agent vykonavajuciAgent = new Agent(this.identifikator.getID(), this.generatorTypZakaznika.getTypAgenta());
        double casPrichodu = this.generatorDalsiPrichod.sample();
        if (casPrichodu <= this.dlzkaTrvaniaSimulacie)
        {
            // Udalost je naplanovana iba za predpokladu, ze nenastane po vyprsani simulacneho casu
            this.pridajAgenta(vykonavajuciAgent);
            UdalostPrichodZakaznika prichod = new UdalostPrichodZakaznika(this, casPrichodu, vykonavajuciAgent);
            this.naplanujUdalost(prichod);
        }

        // Inicializacia GUI
        this.aktualizujGUI(true, false);
    }

    @Override
    protected void poReplikacii()
    {
        double casSystem = this.statistikaCasSystem.forceGetPriemer();
        if (casSystem != -1)
        {
            this.celkovaStatistikaCasSystem.pridajHodnotu(casSystem);
        }

        double casFrontAutomat = this.automat.getPriemerneCakenieFront();
        if (casFrontAutomat != -1)
        {
            this.celkovaStatistikaCasFrontAutomat.pridajHodnotu(casFrontAutomat);
        }
        this.celkovaStatistikaDlzkaFrontAutomat.pridajHodnotu(this.automat.getPriemernaDlzkaFrontu(this.getAktualnySimulacnyCas()));
        this.celkovaStatistikaVytazenieAutomat.pridajHodnotu(this.automat.getVytazenie(this.getAktualnySimulacnyCas()));

        double casFrontOkno = this.obsluhaOkna.getPriemerneCakenieFront();
        if (casFrontOkno != -1)
        {
            this.celkovaStatistikaCasFrontOkno.pridajHodnotu(casFrontOkno);
        }
        this.celkovaStatistikaDlzkaFrontOkno.pridajHodnotu(this.obsluhaOkna.getPriemernaDlzkaFrontu(this.getAktualnySimulacnyCas()));

        // Cas posledneho odchodu
        if (!this.agenti.isEmpty())
        {
            Agent poslednyOdchadzajuci = null;
            double poslednyCasOdchodu = Double.MIN_VALUE;
            for (Agent agent : this.agenti)
            {
                double cas = (agent.getCasKoniecVyzdvihnutie() == -1) ?
                    agent.getCasKoniecObsluhyPokladna() : agent.getCasKoniecVyzdvihnutie();

                if (cas > poslednyCasOdchodu)
                {
                    poslednyOdchadzajuci = agent;
                    poslednyCasOdchodu = cas;
                }
            }

            if (poslednyOdchadzajuci != null
                && poslednyCasOdchodu != -1
                && poslednyCasOdchodu != Double.MIN_VALUE)
            {
                this.celkovaStatistikaCasPoslednyOdchod.pridajHodnotu(poslednyCasOdchodu);
            }
        }

        // Pocet obsluzenych agentov
        int pocetObsluzenychAgentov = 0;
        for (Agent agent : this.agenti)
        {
            if (agent.getCasZaciatokObsluhyAutomat() != -1)
            {
                pocetObsluzenychAgentov++;
            }
        }
        this.celkovaStatistikaPocetObsluzenychAgentov.pridajHodnotu(pocetObsluzenychAgentov);

        // Vytazenie obsluznych okien
        Okno[] oknaObycajni = this.obsluhaOkna.getOknaObycajni();
        for (int i = 0; i < oknaObycajni.length; i++)
        {
            this.celkovaStatistikaVytazenieObycajneOkna[i].pridajHodnotu(oknaObycajni[i].getVytazenie(this.getAktualnySimulacnyCas()));
        }

        Okno[] oknaOnline = this.obsluhaOkna.getOknaOnline();
        for (int i = 0; i < oknaOnline.length; i++)
        {
            this.celkovaStatistikaVytazenieOnlineOkna[i].pridajHodnotu(oknaOnline[i].getVytazenie(this.getAktualnySimulacnyCas()));
        }

        // Statistiky pokladni
        for (int i = 0; i < this.pocetPokladni; i++)
        {
            Pokladna pokladna = this.pokladne[i];
            this.celkovaStatistikaVytazeniePokladne[i].pridajHodnotu(pokladna.getVytazenie(this.getAktualnySimulacnyCas()));
            this.celkovaStatistikaDlzkaFrontPokladne[i].pridajHodnotu(pokladna.getPriemernaDlzkaFrontu(this.getAktualnySimulacnyCas()));

            double priemerneCakanie = pokladna.getPriemerneCakenieFront();
            if (priemerneCakanie != -1)
            {
                this.celkovaStatistikaCakanieFrontPokladne[i].pridajHodnotu(priemerneCakanie);
            }
        }

        // Aktualizacia GUI
        this.aktualizujGUI(true, false);
    }

    @Override
    protected void predVykonanimUdalosti()
    {
        this.kontrola();
        this.skontrolujVyprsanieSimulacnehoCasu();
    }

    @Override
    protected void poVykonaniUdalosti()
    {
        this.kontrola();
        this.skontrolujVyprsanieSimulacnehoCasu();

        if (this.getRychlost() < Konstanty.MAX_RYCHLOST)
        {
            // Aktualizuj iba ak simulacia nebezi v real time
            this.aktualizujGUI(false, true);
        }
    }

    private void kontrola()
    {
        boolean frontOknoPlny = this.obsluhaOkna.getPocetFront() == Konstanty.KAPACITA_FRONT_OKNO;
        if (frontOknoPlny)
        {
            return;
        }

        if (this.automat.getVypnuty() && this.obsluhaOkna.getPocetFront() < (Konstanty.KAPACITA_FRONT_OKNO - 1))
        {
            throw new RuntimeException("Automat je vypnuty hoci front pred oknom nie je plny!");
        }

        boolean obsluhaAutomatPrebieha = this.automat.getObsluhaPrebieha();
        if (obsluhaAutomatPrebieha)
        {
            return;
        }

        boolean frontAutomatPrazdny = (this.automat.getPocetFront() == 0);
        if (frontAutomatPrazdny)
        {
            return;
        }

        for (Udalost udalost : this.getKalendarUdalosti())
        {
            if (udalost instanceof UdalostZaciatokObsluhyAutomat udalostZaciatokObsluhyAutomat)
            {
                if (Double.compare(this.getAktualnySimulacnyCas(), udalostZaciatokObsluhyAutomat.getCasVykonania()) != 0)
                {
                    System.out.println(this.getAktualnySimulacnyCas());
                    throw new RuntimeException("Front pred oknom nie je plny, nikto nepouziva automat, front pred automatom nie je prazdny!");
                }
            }
        }
    }

    private void skontrolujVyprsanieSimulacnehoCasu()
    {
        if (this.getAktualnySimulacnyCas() > this.dlzkaTrvaniaSimulacie
            && !this.prichodyZrusene)
        {
            this.prichodyZrusene = true;

            // Doslo k prekroceniu simulacneho casu, vyprazdni front pred automatom
            this.automat.vyprazdniAutomat(Konstanty.OTVARACIA_DOBA_SEKUND, this.statistikaCasSystem);

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

    public Identifikator getIdentifikator()
    {
        return this.identifikator;
    }
    // Koniec ostatne


    // Automat
    public Automat getAutomat()
    {
        return this.automat;
    }

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
    // Koniec automat


    // Okno
    public ObsluhaOkna getObsluhaOkna()
    {
        return this.obsluhaOkna;
    }

    public GenerovanieTrvaniaPripravy getGeneratorTrvaniePripravy()
    {
        return this.generatorTrvaniePripravy;
    }

    public SpojityRovnomernyGenerator getGeneratorObsluhaObycajni()
    {
        return this.generatorObsluhaObycajni;
    }

    public SpojityTrojuholnikovyGenerator getGeneratorObsluhaOnline()
    {
        return this.generatorObsluhaOnline;
    }
    // Koniec okno


    // Pokladne
    public GenerovanieVelkostiTovaru getGeneratorVelkostTovaru()
    {
        return this.generatorVelkostTovaru;
    }

    public GenerovanieDlzkyPlatenia getGeneratorDlzkaPlatenia()
    {
        return this.generatorDlzkaPlatenia;
    }

    public int getPocetPokladni()
    {
        return this.pocetPokladni;
    }

    public Pokladna[] getPokladne()
    {
        return this.pokladne;
    }

    public Pokladna getPokladna()
    {
        int indexPokladne = this.getIndexPokladne();
        return this.pokladne[indexPokladne];
    }

    private int getIndexPokladne()
    {
        int pocetVyhovujucichFrontov = this.getPocetNajmensieFrontyPokladne();
        int vygenerovanyIndex = this.generatorVyberFrontu.getIndexFrontu(pocetVyhovujucichFrontov);

        int dlzkaNajmensiehoFrontu = this.getNajmensiPocetFrontPokladne();
        int curIndex = 0;
        for (int i = 0; i < this.pokladne.length; i++)
        {
            if (this.pokladne[i].getPocetFront() > dlzkaNajmensiehoFrontu)
            {
                // Dlhsi front, preskoc ho
            }
            else if (this.pokladne[i].getPocetFront() == dlzkaNajmensiehoFrontu)
            {
                // Vyhovujuci front
                if (curIndex < vygenerovanyIndex)
                {
                    curIndex++;
                }
                else
                {
                    // Najdeny spravny front
                    return i;
                }
            }
            else
            {
                throw new RuntimeException("Bol najdeny mensi front pred pokladnami!");
            }
        }

        throw new RuntimeException("Chyba pri generovani indexu frontu pred pokladnami!");
    }

    private int getPocetNajmensieFrontyPokladne()
    {
        int najmensiFront = this.getNajmensiPocetFrontPokladne();

        int pocetNajmensichFrontov = 0;
        for (int i = 0; i < this.pokladne.length; i++)
        {
            if (this.pokladne[i].getPocetFront() < najmensiFront)
            {
                throw new RuntimeException("Bol najdeny mensi front pred pokladnami!");
            }
            else if (this.pokladne[i].getPocetFront() == najmensiFront)
            {
                pocetNajmensichFrontov++;
            }
        }

        if (pocetNajmensichFrontov == 0)
        {
            throw new RuntimeException("Nebol najdeny front najmensej dlzky pred pokladnami!");
        }

        return pocetNajmensichFrontov;
    }

    private int getNajmensiPocetFrontPokladne()
    {
        int najmensiFront = Integer.MAX_VALUE;
        for (int i = 0; i < this.pokladne.length; i++)
        {
            if (this.pokladne[i].getPocetFront() < najmensiFront)
            {
                najmensiFront = this.pokladne[i].getPocetFront();
            }
        }

        return najmensiFront;
    }
    // Koniec pokladne


    // Vyzdvihnutie tovaru
    public SpojityRovnomernyGenerator getGeneratorVyzdvihnutieTovaru()
    {
        return this.generatorVyzdvihnutieTovaru;
    }
    // Koniec vyzdvihnutie tovaru


    // Statistiky 1 replikacie
    public DiskretnaStatistika getStatistikaCasSystem()
    {
        return this.statistikaCasSystem;
    }
    // Koniec statistiky 1 replikacie


    // Celkove statistiky
    public DiskretnaStatistika getCelkovaStatistikaCasSystem()
    {
        return this.celkovaStatistikaCasSystem;
    }

    public DiskretnaStatistika getCelkovaStatistikaDlzkaFrontAutomat()
    {
        return this.celkovaStatistikaDlzkaFrontAutomat;
    }

    public DiskretnaStatistika getCelkovaStatistikaCasFrontAutomat()
    {
        return this.celkovaStatistikaCasFrontAutomat;
    }

    public DiskretnaStatistika getCelkovaStatistikaVytazenieAutomat()
    {
        return this.celkovaStatistikaVytazenieAutomat;
    }

    public DiskretnaStatistika getCelkovaStatistikaCasPoslednyOdchod()
    {
        return this.celkovaStatistikaCasPoslednyOdchod;
    }

    public DiskretnaStatistika getCelkovaStatistikaPocetObsluzenychAgentov()
    {
        return this.celkovaStatistikaPocetObsluzenychAgentov;
    }

    public DiskretnaStatistika getCelkovaStatistikaDlzkaFrontOkno()
    {
        return this.celkovaStatistikaDlzkaFrontOkno;
    }

    public DiskretnaStatistika getCelkovaStatistikaCasFrontOkno()
    {
        return this.celkovaStatistikaCasFrontOkno;
    }

    public DiskretnaStatistika[] getCelkovaStatistikaVytazenieObycajneOkna()
    {
        return this.celkovaStatistikaVytazenieObycajneOkna;
    }

    public DiskretnaStatistika[] getCelkovaStatistikaVytazenieOnlineOkna()
    {
        return this.celkovaStatistikaVytazenieOnlineOkna;
    }

    public DiskretnaStatistika[] getCelkovaStatistikaVytazeniePokladne()
    {
        return this.celkovaStatistikaVytazeniePokladne;
    }

    public DiskretnaStatistika[] getCelkovaStatistikaDlzkaFrontPokladne()
    {
        return this.celkovaStatistikaDlzkaFrontPokladne;
    }

    public DiskretnaStatistika[] getCelkovaStatistikaCakanieFrontPokladne()
    {
        return this.celkovaStatistikaCakanieFrontPokladne;
    }
    // Koniec celkove statistiky
}
