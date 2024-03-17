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
        if (!GeneratorNasad.generatorInicializovany)
        {
            GeneratorNasad.inicializacia(nasada, pouziNasadu);
        }
        else
        {
            StringBuilder chybovaSprava = new StringBuilder();
            chybovaSprava.append("Generator nasad uz bol inicializovany, ");

            if (GeneratorNasad.pevneStanovenaNasada)
            {
                chybovaSprava.append("bola pouzita pevna stanovena nasada: ").append(GeneratorNasad.nasada).append("!");
            }
            else
            {
                chybovaSprava.append("bola pouzita nahodna nasada!");
            }

            throw new RuntimeException(chybovaSprava.toString());
        }
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
