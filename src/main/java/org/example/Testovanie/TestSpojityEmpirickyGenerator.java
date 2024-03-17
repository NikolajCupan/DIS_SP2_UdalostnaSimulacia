package org.example.Testovanie;

import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.Ostatne.SkupinaEmpirickyGenerator;
import org.example.Generatory.SpojityEmpirickyGenerator;

public class TestSpojityEmpirickyGenerator
{
    public void test(int nasada, boolean pouziNasadu)
    {
        final int pocetGenerovani = 100000000;

        GeneratorNasad generatorNasad = (pouziNasadu ? new GeneratorNasad(nasada) : new GeneratorNasad());

        SkupinaEmpirickyGenerator[] skupiny = new SkupinaEmpirickyGenerator[]{
                new SkupinaEmpirickyGenerator(0.10, 0.30, 0.10),
                new SkupinaEmpirickyGenerator(0.30, 0.80, 0.35),
                new SkupinaEmpirickyGenerator(0.80, 1.20, 0.20),
                new SkupinaEmpirickyGenerator(1.20, 2.50, 0.15),
                new SkupinaEmpirickyGenerator(2.50, 3.80, 0.15),
                new SkupinaEmpirickyGenerator(3.80, 4.80, 0.05)
        };
        SpojityEmpirickyGenerator generator = new SpojityEmpirickyGenerator(skupiny, generatorNasad);

        double[] pocetVygenerovanychHodnot = new double[6];
        for (int i = 0; i < pocetGenerovani; i++)
        {
            double sample = generator.sample();
            if (sample >= 0.10 && sample < 0.30)
            {
                pocetVygenerovanychHodnot[0]++;
            }
            else if (sample >= 0.30 && sample < 0.80)
            {
                pocetVygenerovanychHodnot[1]++;
            }
            else if (sample >= 0.80 && sample < 1.20)
            {
                pocetVygenerovanychHodnot[2]++;
            }
            else if (sample >= 1.20 && sample < 2.50)
            {
                pocetVygenerovanychHodnot[3]++;
            }
            else if (sample >= 2.50 && sample < 3.80)
            {
                pocetVygenerovanychHodnot[4]++;
            }
            else if (sample >= 3.80 && sample < 4.80)
            {
                pocetVygenerovanychHodnot[5]++;
            }
            else
            {
                throw new RuntimeException("Vygenerovana hodnota je mimo definovany rozsah!");
            }
        }

        double[] pomerVygenerovanychHodnot = new double[6];
        double sucetPocet = 0.0;
        double sucetPomer = 0.0;
        for (int i = 0; i < 6; i++)
        {
            pomerVygenerovanychHodnot[i] = pocetVygenerovanychHodnot[i] / pocetGenerovani;

            sucetPocet += pocetVygenerovanychHodnot[i];
            sucetPomer += pomerVygenerovanychHodnot[i];
        }

        for (int i = 0; i < 6; i++)
        {
            System.out.println(i + ". " + pomerVygenerovanychHodnot[i]);
        }
        System.out.println("Pocet: " + sucetPocet);
        System.out.println("Pomer: " + sucetPomer);
    }
}
