package org.example.Ostatne;

import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.SimulaciaSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.util.SortedSet;

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

    public static void tabulkaAgentov(SimulaciaSystem simulacia, JTable tabulka)
    {
        SortedSet<Agent> agenti = simulacia.getAgenti();
        DefaultTableModel model = (DefaultTableModel)tabulka.getModel();

        // TODO: BEZ TOHTO RIADKU TO IDE
        model.setRowCount(0);

        for (Agent agent : agenti)
        {
            model.addRow(new Object[] {
                agent.getID(),
                agent.getCasPrichodSystem(),
                agent.getCasZaciatokObsluhyAutomat(),
                agent.getCasKoniecObsluhyAutomat(),
                agent.getCasZaciatokObsluhyOkno(),
                agent.getCasKoniecObsluhyOkno()
            });
        }
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
