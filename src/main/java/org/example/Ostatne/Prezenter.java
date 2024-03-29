package org.example.Ostatne;

import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.Agenti.Agent;
import org.example.Simulacia.System.Agenti.Okno;
import org.example.Simulacia.System.Agenti.Pokladna;
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
        label.setText(String.valueOf(Prezenter.zaokruhli(simulacia.getAktualnySimulacnyCas())));
    }

    public static void tabulkaAgenti(SimulaciaSystem simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeAndWait(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);

                SortedSet<Agent> agenti = simulacia.getAgenti();
                for (Agent agent : agenti)
                {
                    model.addRow(new Object[]{
                        agent.getID(),
                        agent.getTypAgenta(),
                        Prezenter.zaokruhli(agent.getCasPrichodSystem()),
                        Prezenter.zaokruhli(agent.getCasZaciatokObsluhyAutomat()),
                        Prezenter.zaokruhli(agent.getCasKoniecObsluhyAutomat()),
                        Prezenter.zaokruhli(agent.getCasZaciatokObsluhyOkno()),
                        Prezenter.zaokruhli(agent.getCasKoniecObsluhyOkno()),
                        Prezenter.zaokruhli(agent.getCasZaciatokObsluhyPokladna()),
                        Prezenter.zaokruhli(agent.getCasKoniecObsluhyPokladna()),
                        Prezenter.zaokruhli(agent.getCasZaciatokVyzdvihnutie()),
                        Prezenter.zaokruhli(agent.getCasKoniecVyzdvihnutie())
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii tabulky agentov!");
        }
    }

    public static void tabulkaOkna(SimulaciaSystem simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeAndWait(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);

                Okno[] oknaObycajni = simulacia.getOknaObycajni();
                for (Okno okno : oknaObycajni)
                {
                    model.addRow(new Object[]{
                        "Obycajne",
                        okno.getObsadene(),
                        Prezenter.zaokruhli(okno.getVytazenie(simulacia.getAktualnySimulacnyCas()))
                    });
                }

                Okno[] oknaOnline = simulacia.getOknaOnline();
                for (Okno okno : oknaOnline)
                {
                    model.addRow(new Object[]{
                        "Online",
                        okno.getObsadene(),
                        Prezenter.zaokruhli(okno.getVytazenie(simulacia.getAktualnySimulacnyCas()))
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii tabulky okien!");
        }
    }

    public static void tabulkaPokladne(SimulaciaSystem simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeAndWait(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);

                int pocitadlo = 0;
                Pokladna[] pokladne = simulacia.getPokladne();
                for (Pokladna pokladna : pokladne)
                {
                    model.addRow(new Object[]{
                        pocitadlo,
                        pokladna.getObsadena(),
                        "TODO"
                    });

                    pocitadlo++;
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii tabulky pokladni!");
        }
    }

    private static String zaokruhli(double cislo)
    {
        return Prezenter.FORMATOVAC.format(cislo);
    }
}
