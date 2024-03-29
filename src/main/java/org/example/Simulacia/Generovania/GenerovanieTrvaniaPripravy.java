package org.example.Simulacia.Generovania;

import org.example.Generatory.DiskretnyRovnomernyGenerator;
import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.Ostatne.SkupinaEmpirickyGenerator;
import org.example.Generatory.SpojityEmpirickyGenerator;
import org.example.Generatory.SpojityRovnomernyGenerator;

public class GenerovanieTrvaniaPripravy
{
    private static final int PRAVDEPODOBNOST_JEDNODUCHA_OBJEDNAVKA = 30;
    private static final int PRAVDEPODOBNOST_MIERNE_ZLOZITA_OBJEDNAVKA = 40;
    private static final int PRAVDEPODOBNOST_ZLOZITA_OBJEDNAVKA = 30;

    private final DiskretnyRovnomernyGenerator generatorZlozitostiObjednavky;

    private final SpojityEmpirickyGenerator generatorJednoduchaObjednavka;
    private final SpojityRovnomernyGenerator generatorMierneZlozitaObjednavka;
    private final SpojityEmpirickyGenerator generatorZlozitaObjednavka;

    public GenerovanieTrvaniaPripravy(GeneratorNasad generatorNasad)
    {
        this.validujStav();

        this.generatorZlozitostiObjednavky = new DiskretnyRovnomernyGenerator(1, 100, generatorNasad);
        this.generatorMierneZlozitaObjednavka = new SpojityRovnomernyGenerator(540.0, 660.0, generatorNasad);

        SkupinaEmpirickyGenerator[] skupinyJednoducha = new SkupinaEmpirickyGenerator[]{
                new SkupinaEmpirickyGenerator(120.00, 300.00, 0.60),
                new SkupinaEmpirickyGenerator(300.00, 540.00, 0.40)
        };
        this.generatorJednoduchaObjednavka = new SpojityEmpirickyGenerator(skupinyJednoducha, generatorNasad);

        SkupinaEmpirickyGenerator[] skupinyZlozita = new SkupinaEmpirickyGenerator[]{
                new SkupinaEmpirickyGenerator(660.00, 720.00, 0.10),
                new SkupinaEmpirickyGenerator(720.00, 1200.00, 0.60),
                new SkupinaEmpirickyGenerator(1200.00, 1500.00, 0.30)
        };
        this.generatorZlozitaObjednavka = new SpojityEmpirickyGenerator(skupinyZlozita, generatorNasad);
    }

    private void validujStav()
    {
        if ((GenerovanieTrvaniaPripravy.PRAVDEPODOBNOST_JEDNODUCHA_OBJEDNAVKA
            + GenerovanieTrvaniaPripravy.PRAVDEPODOBNOST_MIERNE_ZLOZITA_OBJEDNAVKA
            + GenerovanieTrvaniaPripravy.PRAVDEPODOBNOST_ZLOZITA_OBJEDNAVKA) != 100)
        {
            throw new RuntimeException("Sucet pravdepodobnosti nie je rovny 1!");
        }
    }

    public double getDlzkaPripravy()
    {
        int zlozitost = this.generatorZlozitostiObjednavky.sample();

        if (zlozitost <= (GenerovanieTrvaniaPripravy.PRAVDEPODOBNOST_JEDNODUCHA_OBJEDNAVKA))
        {
            // Jednoducha objednavka
            return this.generatorJednoduchaObjednavka.sample();
        }
        else if (zlozitost <= (GenerovanieTrvaniaPripravy.PRAVDEPODOBNOST_JEDNODUCHA_OBJEDNAVKA
                               + GenerovanieTrvaniaPripravy.PRAVDEPODOBNOST_MIERNE_ZLOZITA_OBJEDNAVKA))
        {
            // Mierne zlozita objednavka
            return this.generatorMierneZlozitaObjednavka.sample();
        }
        else if (zlozitost <= (GenerovanieTrvaniaPripravy.PRAVDEPODOBNOST_JEDNODUCHA_OBJEDNAVKA
                               + GenerovanieTrvaniaPripravy.PRAVDEPODOBNOST_MIERNE_ZLOZITA_OBJEDNAVKA
                               + GenerovanieTrvaniaPripravy.PRAVDEPODOBNOST_ZLOZITA_OBJEDNAVKA))
        {
            // Zlozita objednavka
            return this.generatorZlozitaObjednavka.sample();
        }
        else
        {
            throw new RuntimeException("Chyba pri generovani trvania priravy objednavky!");
        }
    }
}
