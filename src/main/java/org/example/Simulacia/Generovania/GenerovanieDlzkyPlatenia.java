package org.example.Simulacia.Generovania;

import org.example.Generatory.DiskretnyRovnomernyGenerator;
import org.example.Generatory.Ostatne.GeneratorNasad;

public class GenerovanieDlzkyPlatenia
{
    private static final int PRAVDEPODOBNOST_HOTOVOST = 40;
    private static final int PRAVDEPODOBNOST_KARTA = 60;

    private final DiskretnyRovnomernyGenerator generatorTypuPlatenia;
    private final DiskretnyRovnomernyGenerator generatorDlzkyPlateniaHotovost;
    private final DiskretnyRovnomernyGenerator generatorDlzkyPlateniaKarta;

    public GenerovanieDlzkyPlatenia(GeneratorNasad generatorNasad)
    {
        this.validujStav();

        this.generatorTypuPlatenia = new DiskretnyRovnomernyGenerator(1, 100, generatorNasad);
        this.generatorDlzkyPlateniaHotovost = new DiskretnyRovnomernyGenerator(180, 480, generatorNasad);
        this.generatorDlzkyPlateniaKarta = new DiskretnyRovnomernyGenerator(180, 360, generatorNasad);
    }

    private void validujStav()
    {
        if (GenerovanieDlzkyPlatenia.PRAVDEPODOBNOST_HOTOVOST + GenerovanieDlzkyPlatenia.PRAVDEPODOBNOST_KARTA != 100)
        {
            throw new RuntimeException("Sucet pravdepodobnosti nie je rovny 1!");
        }
    }

    public int getDlzkaPlatenia()
    {
        int typPlatenia = this.generatorTypuPlatenia.sample();

        if (typPlatenia <= GenerovanieDlzkyPlatenia.PRAVDEPODOBNOST_HOTOVOST)
        {
            // Platba hotovostou
            return this.generatorDlzkyPlateniaHotovost.sample();
        }
        else if (typPlatenia <= GenerovanieDlzkyPlatenia.PRAVDEPODOBNOST_HOTOVOST + GenerovanieDlzkyPlatenia.PRAVDEPODOBNOST_KARTA)
        {
            // Platba kartou
            return this.generatorDlzkyPlateniaKarta.sample();
        }
        else
        {
            throw new RuntimeException("Chyba pri generovani typu platenia!");
        }
    }
}
