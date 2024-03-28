package org.example.Ostatne;

import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.SimulaciaSystem;

import java.text.DecimalFormat;

public class Prezenter
{
    public static DecimalFormat formatovac = new DecimalFormat("#.##");

    public static String celkovaStatistikaSystem(SimulaciaSystem simulacia)
    {
        DiskretnaStatistika statistika = simulacia.getCelkovaStatistikaCasSystem();

        statistika.skusPrepocitatStatistiky();
        if (!statistika.getStatistikyVypocitane())
        {
            return "n/a";
        }

        return Prezenter.zaokruhli(statistika.getPriemer()) + " [" +
               Prezenter.zaokruhli(statistika.getDolnaHranicaIS()) + ", " +
               Prezenter.zaokruhli(statistika.getHornaHranicaIS()) + "]";
    }

    private static double zaokruhli(double cislo)
    {
        return Double.parseDouble(Prezenter.formatovac.format(cislo));
    }
}
