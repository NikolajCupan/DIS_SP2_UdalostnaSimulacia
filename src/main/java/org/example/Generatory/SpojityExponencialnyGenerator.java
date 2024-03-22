package org.example.Generatory;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.Rozhrania.ISpojityGenerator;

public class SpojityExponencialnyGenerator implements ISpojityGenerator
{
    private final SpojityRovnomernyGenerator spojityRovnomernyGenerator;
    private final double lambda;

    public SpojityExponencialnyGenerator(double lambda, GeneratorNasad generatorNasad)
    {
        this.validujVstupy(lambda);

        this.lambda = lambda;
        this.spojityRovnomernyGenerator = new SpojityRovnomernyGenerator(0, 1, generatorNasad);
    }

    private void validujVstupy(double lambda)
    {
        if (lambda <= 0)
        {
            throw new RuntimeException("Lambda musi byt vascia ako 0!");
        }
    }

    @Override
    public double sample()
    {
        double sample = this.spojityRovnomernyGenerator.sample();
        if (sample == 0.0)
        {
            throw new RuntimeException("Nemozno vypocitat logaritmus cisla 0!");
        }

        return Math.log(1 - sample) / -this.lambda;
    }
}
