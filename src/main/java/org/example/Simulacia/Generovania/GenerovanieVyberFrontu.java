package org.example.Simulacia.Generovania;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.SpojityRovnomernyGenerator;

public class GenerovanieVyberFrontu
{
    private final SpojityRovnomernyGenerator generatorVyberFrontu;

    public GenerovanieVyberFrontu(GeneratorNasad generatorNasad)
    {
        this.generatorVyberFrontu = new SpojityRovnomernyGenerator(0.0, 1.0, generatorNasad);
    }

    public int getIndexFrontu(int pocetFrontov)
    {
        double sample = this.generatorVyberFrontu.sample();
        double dlzkaBloku = 1.0 / pocetFrontov;

        for (int i = 0; i < pocetFrontov; i++)
        {
            if (sample < dlzkaBloku * (i + 1))
            {
                return i;
            }
        }

        throw new RuntimeException("Chyba pri generovani indexu frontu!");
    }
}
