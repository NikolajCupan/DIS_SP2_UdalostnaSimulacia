package org.example.Ostatne;

import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.SimulaciaSystem;

import java.text.DecimalFormat;

public class Prezenter
{
    public static DecimalFormat FORMATOVAC = new DecimalFormat("#.##");

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

    public static String simulacnyCas(SimulaciaSystem simulacia)
    {
        return Prezenter.zaokruhli(simulacia.getAktualnySimulacnyCas());
    }

    private static String zaokruhli(double cislo)
    {
        return Prezenter.FORMATOVAC.format(cislo);
    }
}
