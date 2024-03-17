package org.example;

import org.example.GUI.HlavneOkno;
import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Testovanie.TestSpojityEmpirickyGenerator;

public class Main
{
    private static final int TESTOVACIA_NASADA = -1;
    private static final boolean POUZI_TESTOVACIU_NASADU = false;
    private static char REZIM = 'T';

    public static void main(String[] args)
    {
        if (Main.REZIM == 'G')
        {
            new HlavneOkno();
        }
        else if (Main.REZIM == 'T')
        {
            GeneratorNasad.inicializujGeneratorNasad(Main.TESTOVACIA_NASADA, Main.POUZI_TESTOVACIU_NASADU);

            TestSpojityEmpirickyGenerator test = new TestSpojityEmpirickyGenerator();
            test.test(1000000000);
        }
    }
}