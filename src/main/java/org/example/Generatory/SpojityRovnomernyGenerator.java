package org.example.Generatory;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.Rozhrania.ISpojityGenerator;

import java.util.Random;

public class SpojityRovnomernyGenerator implements ISpojityGenerator
{
    private final Random random;
    private final double minHodnota;
    private final double maxHodnota;

    public SpojityRovnomernyGenerator(double minHodnota, double maxHodnota, GeneratorNasad generatorNasad)
    {
        this.validujVstupy(minHodnota, maxHodnota);

        this.minHodnota = minHodnota;
        this.maxHodnota = maxHodnota;

        this.random = new Random(generatorNasad.nasada());
    }

    private void validujVstupy(double minHodnota, double maxHodnota)
    {
        if (minHodnota > maxHodnota)
        {
            throw new RuntimeException("Maximalna hodnota nemoze byt mensia ako minimalna hodnota!");
        }
        else if (minHodnota == maxHodnota)
        {
            throw new RuntimeException("Maximalna hodnota nemoze byt rovnaka ako minimalna hodnota!");
        }
    }

    @Override
    public double sample()
    {
        return this.random.nextDouble() * (this.maxHodnota - this.minHodnota) + this.minHodnota;
    }
}
