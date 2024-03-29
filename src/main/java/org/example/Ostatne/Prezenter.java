package org.example.Ostatne;

import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.SimulaciaSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.SortedSet;

public class Prezenter
{
    public static DecimalFormat FORMATOVAC = new DecimalFormat("#.##");

    public static void celkovyPriemernyCasSystem(SimulaciaSystem simulacia, JLabel label)
    {
        DiskretnaStatistika statistika = simulacia.getCelkovaStatistikaCasSystem();

        statistika.skusPrepocitatStatistiky();
        if (!statistika.getStatistikyVypocitane())
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.zaokruhli(statistika.getPriemer()) + " [" +
                Prezenter.zaokruhli(statistika.getDolnaHranicaIS()) + ", " +
                Prezenter.zaokruhli(statistika.getHornaHranicaIS()) + "]");
        }
    }

    public static void aktualnaReplikacia(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(simulacia.getAktualnaReplikacia()));
    }

    public static void simulacnyCas(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(simulacia.getAktualnaReplikacia()));
    }

    public static void tabulkaAgentov(SimulaciaSystem simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeAndWait(() -> {
                SortedSet<Agent> agenti = simulacia.getAgenti();
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);
                for (Agent agent : agenti)
                {
                    model.addRow(new Object[]{
                        agent.getID(),
                        Prezenter.zaokruhli(agent.getCasPrichodSystem()),
                        Prezenter.zaokruhli(agent.getCasZaciatokObsluhyAutomat()),
                        Prezenter.zaokruhli(agent.getCasKoniecObsluhyAutomat()),
                        Prezenter.zaokruhli(agent.getCasZaciatokObsluhyOkno()),
                        Prezenter.zaokruhli(agent.getCasKoniecObsluhyOkno())
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii tabulky agentov!");
        }
    }

    private static String zaokruhli(double cislo)
    {
        return Prezenter.FORMATOVAC.format(cislo);
    }
}
