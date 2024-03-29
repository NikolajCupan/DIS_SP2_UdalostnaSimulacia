package org.example.Simulacia.Generovania;

import org.example.Generatory.DiskretnyRovnomernyGenerator;
import org.example.Generatory.Ostatne.GeneratorNasad;

public class GenerovanieVelkostiTovaru
{
    private static final int PRAVDEPODOBNOST_VELKY = 60;
    private static final int PRAVDEPODOBNOST_MALY = 40;

    private final DiskretnyRovnomernyGenerator generatorVelkostiTovaru;

    public GenerovanieVelkostiTovaru(GeneratorNasad generatorNasad)
    {
        this.validujStav();

        this.generatorVelkostiTovaru = new DiskretnyRovnomernyGenerator(1, 100, generatorNasad);
    }

    private void validujStav()
    {
        if ((GenerovanieVelkostiTovaru.PRAVDEPODOBNOST_VELKY + GenerovanieVelkostiTovaru.PRAVDEPODOBNOST_MALY) != 100)
        {
            throw new RuntimeException("Sucet pravdepodobnosti nie je rovny 1!");
        }
    }

    public VelkostTovaru getVelkostTovaru()
    {
        int sample = this.generatorVelkostiTovaru.sample();

        if (sample <= GenerovanieVelkostiTovaru.PRAVDEPODOBNOST_VELKY)
        {
            return VelkostTovaru.VELKY;
        }
        else if (sample <= (GenerovanieVelkostiTovaru.PRAVDEPODOBNOST_VELKY + GenerovanieVelkostiTovaru.PRAVDEPODOBNOST_MALY))
        {
            return VelkostTovaru.MALY;
        }
        else
        {
            throw new RuntimeException("Chyba pri generovani velkosti tovaru!");
        }
    }
}
