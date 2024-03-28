package org.example.Generatory.Ostatne;

import java.util.Random;

public final class GeneratorNasad
{
    private static int nasada;
    private static boolean pevneStanovenaNasada;
    private static Random random;

    private static boolean generatorInicializovany = false;

    public GeneratorNasad()
    {
        if (!GeneratorNasad.generatorInicializovany)
        {
            throw new RuntimeException("Generator nasad nie je inicializovany!");
        }

        if (GeneratorNasad.pevneStanovenaNasada)
        {
            System.out.println("Bola vytvorena instancia generatora nasad s pevne stanovenou nasadou: "
                + GeneratorNasad.nasada + "!");
        }
        else
        {
            System.out.println("Bola vytvorena instancia generatora nasad s nahodnou nasadou!");
        }
    }

    public int nasada()
    {
        return GeneratorNasad.random.nextInt();
    }

    public static void inicializujGeneratorNasad(int nasada, boolean pouziNasadu)
    {
        GeneratorNasad.vypisStatus();
        GeneratorNasad.inicializacia(nasada, pouziNasadu);
    }

    private static void vypisStatus()
    {
        StringBuilder sprava = new StringBuilder();
        sprava.append("Generator nasad uz bol inicializovany, ");

        if (GeneratorNasad.pevneStanovenaNasada)
        {
            sprava.append("bola pouzita pevna stanovena nasada: ").append(GeneratorNasad.nasada).append("!");
        }
        else
        {
            sprava.append("bola pouzita nahodna nasada!");
        }

        System.out.println(sprava);
    }

    private static void inicializacia(int nasada, boolean pouziNasadu)
    {
        if (pouziNasadu)
        {
            System.out.println("Bol inicializovany generator nasad s pevne stanovenou nasadou: " + nasada + "!");

            GeneratorNasad.nasada = nasada;
            GeneratorNasad.pevneStanovenaNasada = true;
            GeneratorNasad.random = new Random(nasada);
        }
        else
        {
            System.out.println("Bol inicializovany generator nasad s nahodnou nasadou!");

            GeneratorNasad.nasada = -1;
            GeneratorNasad.pevneStanovenaNasada = false;
            GeneratorNasad.random = new Random();
        }

        GeneratorNasad.generatorInicializovany = true;
    }
}
