package org.example.Testovanie;

import org.example.Generatory.DiskretnyRovnomernyGenerator;
import org.example.Generatory.Ostatne.DeterministickyGenerator;
import org.example.Generatory.Ostatne.GeneratorNasad;
import org.example.Generatory.Ostatne.SkupinaEmpirickyGenerator;
import org.example.Generatory.SpojityEmpirickyGenerator;
import org.example.Generatory.SpojityExponencialnyGenerator;
import org.example.Generatory.SpojityRovnomernyGenerator;
import org.example.Ostatne.Konstanty;

import java.io.PrintWriter;

public class TestGeneratory
{
    private final GeneratorNasad generatorNasad;

    public TestGeneratory(int nasada, boolean pouziNasadu)
    {
        GeneratorNasad.inicializujGeneratorNasad(nasada, pouziNasadu);
        this.generatorNasad = new GeneratorNasad();
    }

    public void testDeterministickyGenerator() throws Exception
    {
        PrintWriter w = new PrintWriter("test_deterministicky_generator.txt");

        final int pocetGenerovani = 1000000;
        final double generovaneCislo = 1.2345;

        DeterministickyGenerator<Double> dg = new DeterministickyGenerator<>(generovaneCislo);

        for (int i = 0; i < pocetGenerovani; i++)
        {
            double sample = dg.sample();
            w.println(sample);

            if (Math.abs(sample - generovaneCislo) > Konstanty.EPSILON)
            {
                throw new RuntimeException("Deterministicky generator vygeneroval nespravne cislo!");
            }
        }

        System.out.println("OK: Test deterministicky generator\n");

        w.flush();
        w.close();
    }

    public void testDiskretnyRovnomernyGenerator() throws Exception
    {
        PrintWriter w = new PrintWriter("test_diskretny_rovnomerny_generator.txt");

        final int pocetGenerovani = 1000000;
        final int minHodnota = -5;
        final int maxHodnota = 4;
        final int rozsahHodnot = maxHodnota - minHodnota + 1;

        DiskretnyRovnomernyGenerator drg = new DiskretnyRovnomernyGenerator(minHodnota, maxHodnota, this.generatorNasad);
        int[] pocetVygenerovanychHodnot = new int[rozsahHodnot];

        for (int i = 0; i < pocetGenerovani; i++)
        {
            int sample = drg.sample();
            w.println(sample);

            pocetVygenerovanychHodnot[sample - minHodnota]++;
        }

        int curHodnota = minHodnota;
        for (int i = 0; i < rozsahHodnot; i++)
        {
            System.out.format("%3d", curHodnota);
            System.out.print(": pocet: ");
            System.out.format("%9d", pocetVygenerovanychHodnot[curHodnota - minHodnota]);
            System.out.print(", pomer: ");
            System.out.format("%1.10f%n", (double)pocetVygenerovanychHodnot[curHodnota - minHodnota] / pocetGenerovani);

            curHodnota++;
        }

        System.out.println("OK: Test diskretny rovnomerny generator\n");

        w.flush();
        w.close();
    }

    public void testSpojityRovnomernyGenerator() throws Exception
    {
        PrintWriter w = new PrintWriter("test_spojity_rovnomerny_generator.txt");

        final int pocetGenerovani = 1000000;
        final double minHodnota = -2.5;
        final double maxHodnota = 5;

        SpojityRovnomernyGenerator srg = new SpojityRovnomernyGenerator(minHodnota, maxHodnota, this.generatorNasad);
        for (int i = 0; i < pocetGenerovani; i++)
        {
            double sample = srg.sample();
            w.println(sample);
        }

        System.out.println("OK: Test spojity rovnomerny generator\n");

        w.flush();
        w.close();
    }

    public void testSpojityEmpirickyGenerator() throws Exception
    {
        PrintWriter w = new PrintWriter("test_spojity_empiricky_generator.txt");

        final int pocetGenerovani = 1000000;

        SkupinaEmpirickyGenerator[] skupiny = new SkupinaEmpirickyGenerator[]{
                new SkupinaEmpirickyGenerator(0.10, 0.30, 0.10),
                new SkupinaEmpirickyGenerator(0.30, 0.80, 0.35),
                new SkupinaEmpirickyGenerator(0.80, 1.20, 0.20),
                new SkupinaEmpirickyGenerator(1.20, 2.50, 0.15),
                new SkupinaEmpirickyGenerator(2.50, 3.80, 0.15),
                new SkupinaEmpirickyGenerator(3.80, 4.80, 0.05)
        };
        SpojityEmpirickyGenerator seg = new SpojityEmpirickyGenerator(skupiny, this.generatorNasad);

        int[] pocetVygenerovanychHodnot = new int[6];
        for (int i = 0; i < pocetGenerovani; i++)
        {
            double sample = seg.sample();
            w.println(sample);

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
        int sucetPocet = 0;
        double sucetPomer = 0.0;
        for (int i = 0; i < 6; i++)
        {
            pomerVygenerovanychHodnot[i] = (double)pocetVygenerovanychHodnot[i] / pocetGenerovani;

            sucetPocet += pocetVygenerovanychHodnot[i];
            sucetPomer += pomerVygenerovanychHodnot[i];
        }

        for (int i = 0; i < 6; i++)
        {
            System.out.println(i + ". " + pomerVygenerovanychHodnot[i]);
        }
        System.out.println("Pocet: " + sucetPocet);
        System.out.println("Pomer: " + sucetPomer);

        System.out.println("OK: Test spojity empiricky generator\n");

        w.flush();
        w.close();
    }

    public void testSpojityExponencialnyGenerator() throws Exception
    {
        PrintWriter w = new PrintWriter("test_spojity_exponencialny_generator.txt");

        final int pocetGenerovani = 1000000;
        final double lambda = 20;

        SpojityExponencialnyGenerator seg = new SpojityExponencialnyGenerator(lambda, this.generatorNasad);
        for (int i = 0; i < pocetGenerovani; i++)
        {
            double sample = seg.sample();
            w.println(sample);
        }

        System.out.println("OK: Test spojity exponencialny generator\n");

        w.flush();
        w.close();
    }
}
