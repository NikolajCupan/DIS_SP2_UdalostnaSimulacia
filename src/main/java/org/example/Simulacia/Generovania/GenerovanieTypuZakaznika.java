package org.example.Simulacia.Generovania;

import org.example.Generatory.DiskretnyRovnomernyGenerator;
import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Simulacia.Stanok.Agenti.TypAgenta;

public class GenerovanieTypuZakaznika
{
    private static final int PRAVDEPODOBNOST_BEZNY = 50;
    private static final int PRAVDEPODOBNOST_ZMLUVNY = 15;
    private static final int PRAVDEPODOBNOST_ONLINE = 35;

    private final DiskretnyRovnomernyGenerator generatorTypuZakaznika;

    public GenerovanieTypuZakaznika(GeneratorNasad generatorNasad)
    {
        this.validujStav();
        this.generatorTypuZakaznika = new DiskretnyRovnomernyGenerator(1, 100, generatorNasad);
    }

    private void validujStav()
    {
        if (GenerovanieTypuZakaznika.PRAVDEPODOBNOST_BEZNY + GenerovanieTypuZakaznika.PRAVDEPODOBNOST_ZMLUVNY
            + GenerovanieTypuZakaznika.PRAVDEPODOBNOST_ONLINE != 100)
        {
            throw new RuntimeException("Sucet pravdepodobnosti nie je rovny 1!");
        }
    }

    public TypAgenta getTypAgenta()
    {
        int sample = this.generatorTypuZakaznika.sample();

        if (sample <= GenerovanieTypuZakaznika.PRAVDEPODOBNOST_BEZNY)
        {
            return TypAgenta.BEZNY;
        }
        else if (sample <= GenerovanieTypuZakaznika.PRAVDEPODOBNOST_BEZNY + GenerovanieTypuZakaznika.PRAVDEPODOBNOST_ZMLUVNY)
        {
            return TypAgenta.ZMLUVNY;
        }
        else if (sample <= GenerovanieTypuZakaznika.PRAVDEPODOBNOST_BEZNY + GenerovanieTypuZakaznika.PRAVDEPODOBNOST_ZMLUVNY
            + GenerovanieTypuZakaznika.PRAVDEPODOBNOST_ONLINE)
        {
            return TypAgenta.ONLINE;
        }
        else
        {
            throw new RuntimeException("Chyba pri generovani typu zakaznika!");
        }
    }
}
