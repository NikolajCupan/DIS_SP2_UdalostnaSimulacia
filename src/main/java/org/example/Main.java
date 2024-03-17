package org.example;

import org.example.GUI.HlavneOkno;
import org.example.Testovanie.TestGeneratory;

public class Main
{
    private static final int TESTOVACIA_NASADA = -1;
    private static final boolean POUZI_TESTOVACIU_NASADU = false;
    private static char REZIM = 'T';

    public static void main(String[] args) throws Exception
    {
        if (Main.REZIM == 'G')
        {
            new HlavneOkno();
        }
        else if (Main.REZIM == 'T')
        {
            TestGeneratory test = new TestGeneratory(Main.TESTOVACIA_NASADA, Main.POUZI_TESTOVACIU_NASADU);
            test.testDeterministickyGenerator();
            test.testDiskretnyRovnomernyGenerator();
            test.testSpojityRovnomernyGenerator();
            test.testSpojityEmpirickyGenerator();
            test.testSpojityExponencialnyGenerator();
        }
    }
}