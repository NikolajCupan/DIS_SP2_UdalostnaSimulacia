package org.example.Generatory;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.Rozhrania.ISpojityGenerator;
import org.example.Generatory.Ostatne.SkupinaEmpirickyGenerator;
import org.example.Ostatne.Konstanty;

public class SpojityEmpirickyGenerator implements ISpojityGenerator
{
    private final SpojityRovnomernyGenerator generatorSkupiny;

    private final SpojityRovnomernyGenerator[] randoms;
    private final double[] pravdepodobnosti;

    public SpojityEmpirickyGenerator(SkupinaEmpirickyGenerator[] skupiny, GeneratorNasad generatorNasad)
    {
        this.validujVstupy(skupiny);

        this.generatorSkupiny = new SpojityRovnomernyGenerator(0.0, 1.0, generatorNasad);
        this.randoms = new SpojityRovnomernyGenerator[skupiny.length];
        this.pravdepodobnosti = new double[skupiny.length];

        for (int i = 0; i < skupiny.length; i++)
        {
            SkupinaEmpirickyGenerator curSkupina = skupiny[i];
            this.randoms[i] = new SpojityRovnomernyGenerator(curSkupina.minHodnota, curSkupina.maxHodnota, generatorNasad);
            this.pravdepodobnosti[i] = curSkupina.pravdepodobnost;
        }
    }

    private void validujVstupy(SkupinaEmpirickyGenerator[] skupiny)
    {
        if (skupiny.length < 1)
        {
            throw new RuntimeException("Pocet skupin empirickeho generatora nemoze byt mensi ako 1!");
        }

        double sucetPravdepodobnosti = 0.0;
        for (SkupinaEmpirickyGenerator curSkupina : skupiny)
        {
            sucetPravdepodobnosti += curSkupina.pravdepodobnost;

            if (curSkupina.minHodnota > curSkupina.maxHodnota)
            {
                throw new RuntimeException("Maximalna hodnota nemoze byt mensia ako minimalna hodnota!");
            }
            else if (curSkupina.minHodnota == curSkupina.maxHodnota)
            {
                throw new RuntimeException("Maximalna hodnota nemoze byt rovnaka ako minimalna hodnota!");
            }
        }

        if (Math.abs(sucetPravdepodobnosti - 1.0) > Konstanty.EPSILON)
        {
            throw new RuntimeException("Sucet pravdepodobnosti sa musi rovnat 1 (epsilon: " + Konstanty.EPSILON + ")!");
        }
    }

    @Override
    public double sample()
    {
        double skupina = this.generatorSkupiny.sample();
        double kumulovanePravdepodobnosti = 0.0;
        int indexSkupiny = -1;

        for (int i = 0; i < this.pravdepodobnosti.length; i++)
        {
            kumulovanePravdepodobnosti += this.pravdepodobnosti[i];
            if (skupina < kumulovanePravdepodobnosti)
            {
                indexSkupiny = i;
                break;
            }
        }

        return this.randoms[indexSkupiny].sample();
    }
}
