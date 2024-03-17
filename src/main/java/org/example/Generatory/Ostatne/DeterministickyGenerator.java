package org.example.Generatory.Ostatne;

import org.example.Generatory.Rozhrania.IGenerator;

public class DeterministickyGenerator<T> implements IGenerator
{
    private final T hodnota;

    public DeterministickyGenerator(T hodnota)
    {
        this.hodnota = hodnota;
    }

    public T sample()
    {
        return this.hodnota;
    }
}
