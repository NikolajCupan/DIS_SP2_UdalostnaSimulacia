package org.example.Simulacia.System;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.SpojityExponencialnyGenerator;
import org.example.Generatory.SpojityRovnomernyGenerator;
import org.example.Ostatne.Identifikator;
import org.example.Simulacia.Generovania.GenerovanieTypuZakaznika;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.Agenti.Agent;
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
    private Queue<Agent> frontAutomat;

    // Generatory
    private SpojityExponencialnyGenerator generatorDalsiehoVstupu;
    private GenerovanieTypuZakaznika generatorTypuZakaznika;
    private SpojityRovnomernyGenerator generatorVydanieListka;

    // Statistiky jednej replikacie
    private DiskretnaStatistika statistikaCasSystem;

    // Celkove statistiky
    private DiskretnaStatistika celkovaStatistikaCasSystem;

    public SimulaciaSystem(int pocetReplikacii, double dlzkaTrvaniaSimulacie, int nasada, boolean pouziNasadu)
    {
        super(pocetReplikacii, dlzkaTrvaniaSimulacie);

        GeneratorNasad.inicializujGeneratorNasad(nasada, pouziNasadu);
        this.generatorNasad = new GeneratorNasad();
    }

    public void pridajAgentaDoFrontuAutomat(Agent agent)
    {
        this.frontAutomat.add(agent);
    }

    public Agent odoberAgentaZFrontuAutomat()
    {
        if (this.frontAutomat.isEmpty())
        {
            throw new RuntimeException("Pokus o vybratie agenta z frontu pred automatom, ktory je prazdny!");
        }

        return this.frontAutomat.poll();
    }

    public int getPocetAgentovVoFronteAutomat()
    {
        return this.frontAutomat.size();
    }

    public boolean getObsluhaAutomatPrebieha()
    {
        return this.obsluhaAutomatPrebieha;
    }

    public SpojityExponencialnyGenerator getGeneratorDalsiehoVstupu()
    {
        return this.generatorDalsiehoVstupu;
    }

    public GenerovanieTypuZakaznika getGeneratorTypuZakaznika()
    {
        return this.generatorTypuZakaznika;
    }

    public SpojityRovnomernyGenerator getGeneratorVydanieListka()
    {
        return this.generatorVydanieListka;
    }

    public DiskretnaStatistika getStatistikaCasSystem()
    {
        return this.statistikaCasSystem;
    }

    public void setObsluhaAutomatPrebieha(boolean obsluhaAutomatPrebieha)
    {
        this.obsluhaAutomatPrebieha = obsluhaAutomatPrebieha;
    }

    @Override
    protected void predReplikaciami()
    {
        Comparator<Udalost> komparator = new UdalostKomparator();
        this.nastavKomparator(komparator);

        // Generatory
        this.generatorDalsiehoVstupu = new SpojityExponencialnyGenerator(1.0 / 120.0, this.generatorNasad);
        this.generatorTypuZakaznika = new GenerovanieTypuZakaznika(this.generatorNasad);
        this.generatorVydanieListka = new SpojityRovnomernyGenerator(30.0, 180.0, this.generatorNasad);

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
        this.frontAutomat = new LinkedList<>();

        // Statistiky
        this.statistikaCasSystem = new DiskretnaStatistika();

        // Naplanovanie prichodu 1. zakaznika v case 0.0
        Agent vykonavajuciAgent = new Agent(Identifikator.getID(), this.generatorTypuZakaznika.getTypAgenta());
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
}
