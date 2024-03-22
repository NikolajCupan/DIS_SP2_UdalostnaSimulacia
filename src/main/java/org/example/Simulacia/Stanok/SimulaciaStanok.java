package org.example.Simulacia.Stanok;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.SpojityExponencialnyGenerator;
import org.example.Generatory.SpojityRovnomernyGenerator;
import org.example.Ostatne.Identifikator;
import org.example.Simulacia.Generovania.GenerovanieTypuZakaznika;
import org.example.Simulacia.Jadro.SimulacneJadro;
import org.example.Simulacia.Stanok.Agenti.Agent;
import org.example.Simulacia.Stanok.Udalosti.UdalostKomparator;
import org.example.Simulacia.Stanok.Udalosti.UdalostPrichodZakaznika;
import org.example.Simulacia.Jadro.Udalost;

import java.util.Comparator;

public class SimulaciaStanok extends SimulacneJadro
{
    private final GeneratorNasad generatorNasad;

    // Generatory
    private SpojityExponencialnyGenerator generatorDalsiehoVstupu;
    private GenerovanieTypuZakaznika generatorTypuZakaznika;
    private SpojityRovnomernyGenerator generatorVydanieListka;

    public SimulaciaStanok(int pocetReplikacii, double dlzkaTrvaniaSimulacie, int nasada, boolean pouziNasadu)
    {
        super(pocetReplikacii, dlzkaTrvaniaSimulacie);

        GeneratorNasad.inicializujGeneratorNasad(nasada, pouziNasadu);
        this.generatorNasad = new GeneratorNasad();
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

    @Override
    protected void predReplikaciami()
    {
        Comparator<Udalost> komparator = new UdalostKomparator();
        this.nastavKomparator(komparator);

        // Generatory
        this.generatorDalsiehoVstupu = new SpojityExponencialnyGenerator(1.0 / 120.0, this.generatorNasad);
        this.generatorTypuZakaznika = new GenerovanieTypuZakaznika(this.generatorNasad);
        this.generatorVydanieListka = new SpojityRovnomernyGenerator(30.0, 180.0, this.generatorNasad);
    }

    @Override
    protected void poReplikaciach()
    {
    }

    @Override
    protected void predReplikaciou()
    {
        // Naplanovanie prichodu 1. zakaznika v case 0.0
        Agent vykonavajuciAgent = new Agent(Identifikator.getID(), this.generatorTypuZakaznika.getTypAgenta());
        UdalostPrichodZakaznika prichod = new UdalostPrichodZakaznika(this, 0.0, vykonavajuciAgent);
        this.naplanujUdalost(prichod);
    }

    @Override
    protected void poReplikacii()
    {
    }
}
