package org.example.Generatory;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.Rozhrania.IDiskretnyGenerator;

import java.util.Random;

public class DiskretnyRovnomernyGenerator implements IDiskretnyGenerator
{
    private final Random random;
    private final int minHodnota;
    private final int maxHodnota;

    public DiskretnyRovnomernyGenerator(int minHodnota, int maxHodnota, GeneratorNasad generatorNasad)
    {
        this.validujVstupy(minHodnota, maxHodnota);

        this.minHodnota = minHodnota;
        this.maxHodnota = maxHodnota;

        this.random = new Random(generatorNasad.nasada());
    }

    private void validujVstupy(int minHodnota, int maxHodnota)
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
    public int sample()
    {
        return this.random.nextInt((this.maxHodnota - this.minHodnota) + 1) + this.minHodnota;
    }
}
