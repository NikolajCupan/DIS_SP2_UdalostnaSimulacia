package org.example.Simulacia.Stanok;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.SpojityExponencialnyGenerator;
import org.example.Generatory.SpojityTrojuholnikovyGenerator;
import org.example.Ostatne.Identifikator;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Stanok.Udalosti.UdalostKomparator;
import org.example.Simulacia.Stanok.Udalosti.UdalostPrichodZakaznika;
import org.example.Simulacia.Jadro.Udalost;
import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.Statistiky.SpojitaStatistika;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class SimulaciaStanok extends SimulacneJadro
{
    private final GeneratorNasad generatorNasad;
    private SpojityExponencialnyGenerator spojityExponencialnyGenerator;
    private SpojityTrojuholnikovyGenerator spojityTrojuholnikovyGenerator;

    private Queue<Agent> front;
    private boolean obsluhaPrebieha;

    // Statistiky 1 replikacie
    private DiskretnaStatistika statistikaCasSystem;
    private DiskretnaStatistika statistikaCasFront;
    private SpojitaStatistika statistikaVelkostFrontu;

    // Celkove statistiky
    private DiskretnaStatistika celkovaStatistikaCasSystem;
    private DiskretnaStatistika celkovaStatistikaCasFront;
    private DiskretnaStatistika celkovaStatistikaVelkostFrontu;

    public SimulaciaStanok(int pocetReplikacii, double dlzkaTrvaniaSimulacie, int nasada, boolean pouziNasadu)
    {
        super(pocetReplikacii, dlzkaTrvaniaSimulacie);

        GeneratorNasad.inicializujGeneratorNasad(nasada, pouziNasadu);
        this.generatorNasad = new GeneratorNasad();
    }

    public void pridajAgentaDoFrontu(Agent agent)
    {
        this.front.add(agent);
    }

    public Agent odoberAgentaZFrontu()
    {
        if (this.front.isEmpty())
        {
            throw new RuntimeException("Pokus o vybratie agenta z frontu, ktory je prazdny!");
        }

        return this.front.poll();
    }

    public int getPocetAgentovVoFronte()
    {
        return this.front.size();
    }

    public boolean getObsluhaPrebieha()
    {
        return this.obsluhaPrebieha;
    }

    public SpojityExponencialnyGenerator getSpojityExponencialnyGenerator()
    {
        return this.spojityExponencialnyGenerator;
    }

    public SpojityTrojuholnikovyGenerator getSpojityTrojuholnikovyGenerator()
    {
        return this.spojityTrojuholnikovyGenerator;
    }

    public DiskretnaStatistika getStatistikaCasSystem()
    {
        return this.statistikaCasSystem;
    }

    public DiskretnaStatistika getStatistikaCasFront()
    {
        return this.statistikaCasFront;
    }

    public SpojitaStatistika getStatistikaVelkostFrontu()
    {
        return this.statistikaVelkostFrontu;
    }

    public void setObsluhaPrebieha(boolean obsluhaPrebieha)
    {
        this.obsluhaPrebieha = obsluhaPrebieha;
    }

    @Override
    protected void predReplikaciami()
    {
        Comparator<Udalost> komparator = new UdalostKomparator();
        this.nastavKomparator(komparator);

        this.spojityExponencialnyGenerator = new SpojityExponencialnyGenerator(1.0 / 240.0, this.generatorNasad);
        this.spojityTrojuholnikovyGenerator = new SpojityTrojuholnikovyGenerator(100.0, 400.0, 120.0, this.generatorNasad);

        // Vynulovanie celkovych statistik
        this.celkovaStatistikaCasSystem = new DiskretnaStatistika();
        this.celkovaStatistikaCasFront = new DiskretnaStatistika();
        this.celkovaStatistikaVelkostFrontu = new DiskretnaStatistika();
    }

    @Override
    protected void poReplikaciach()
    {
        this.celkovaStatistikaCasSystem.skusPrepocitatStatistiky();
        this.celkovaStatistikaCasFront.skusPrepocitatStatistiky();
        this.celkovaStatistikaVelkostFrontu.skusPrepocitatStatistiky();

        System.out.println("[STATISTIKA] Priemerna doba v systeme: "
                + this.celkovaStatistikaCasSystem.getPriemer() + " [" + this.celkovaStatistikaCasSystem.getDolnaHranicaIS()
                + ", " + this.celkovaStatistikaCasSystem.getHornaHranicaIS() + "]");
        System.out.println("[STATISTIKA] Priemerna doba vo fronte: "
                + this.celkovaStatistikaCasFront.getPriemer() + " [" + this.celkovaStatistikaCasFront.getDolnaHranicaIS()
                + ", " + this.celkovaStatistikaCasFront.getHornaHranicaIS() + "]");
        System.out.println("[STATISTIKA] Priemerna velkost frontu: "
                + this.celkovaStatistikaVelkostFrontu.getPriemer() + " [" + this.celkovaStatistikaVelkostFrontu.getDolnaHranicaIS()
                + ", " + this.celkovaStatistikaVelkostFrontu.getHornaHranicaIS() + "]");
    }

    @Override
    protected void predReplikaciou()
    {
        this.front = new LinkedList<>();
        this.obsluhaPrebieha = false;

        // Vynulovanie statistik
        this.statistikaCasSystem = new DiskretnaStatistika();
        this.statistikaCasFront = new DiskretnaStatistika();
        this.statistikaVelkostFrontu = new SpojitaStatistika();

        // Naplanovanie prichodu 1. zakaznika v case 0.0
        UdalostPrichodZakaznika prichod = new UdalostPrichodZakaznika(this, 0.0, new Agent(Identifikator.getID()));
        this.naplanujUdalost(prichod);
    }

    @Override
    protected void poReplikacii()
    {
        this.statistikaVelkostFrontu.pridajHodnotu(this.getAktualnySimulacnyCas(), this.getPocetAgentovVoFronte());

        this.statistikaCasSystem.skusPrepocitatStatistiky();
        this.statistikaCasFront.skusPrepocitatStatistiky();

        if (this.statistikaCasSystem.getStatistikyVypocitane())
        {
            this.celkovaStatistikaCasSystem.pridajHodnotu(this.statistikaCasSystem.getPriemer());
        }

        if (this.statistikaCasFront.getStatistikyVypocitane())
        {
            this.celkovaStatistikaCasFront.pridajHodnotu(this.statistikaCasFront.getPriemer());
        }

        this.celkovaStatistikaVelkostFrontu.pridajHodnotu(this.statistikaVelkostFrontu.getPriemer());

        if (this.getAktualnaReplikacia() % 10000 == 0)
        {
            System.out.println("Aktualna replikacia: " + this.getAktualnaReplikacia());
        }
    }
}
