package org.example;

import org.example.GUI.HlavneOkno;
import org.example.Testovanie.TestSpojityEmpirickyGenerator;

public class Main
{
    private static char REZIM = 'G';

    public static void main(String[] args)
    {
        if (Main.REZIM == 'G')
        {
            new HlavneOkno();
        }
        else if (Main.REZIM == 'T')
        {
            TestSpojityEmpirickyGenerator test = new TestSpojityEmpirickyGenerator();
            test.test(0, false);
        }
    }
}