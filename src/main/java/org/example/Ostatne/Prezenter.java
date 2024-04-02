package org.example.Ostatne;

import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Objekty.Okno;
import org.example.Simulacia.System.Agenti.Objekty.Pokladna;
import org.example.Simulacia.System.Agenti.Zakaznik.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Queue;
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

    public static void celkovyCasFrontAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        DiskretnaStatistika statistika = simulacia.getCelkovaStatistikaCasFrontAutomat();

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

    public static void celkovaDlzkaFrontAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        DiskretnaStatistika statistika = simulacia.getCelkovaStatistikaDlzkaFrontAutomat();

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

    public static void poslednyOdchod(SimulaciaSystem simulacia, JLabel label)
    {
        DiskretnaStatistika statistika = simulacia.getCelkovaStatistikaCasPoslednyOdchod();

        statistika.skusPrepocitatStatistiky();
        if (!statistika.getStatistikyVypocitane())
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.naformatujCas(statistika.getPriemer()) + " [" +
                Prezenter.naformatujCas(statistika.getDolnaHranicaIS()) + ", " +
                Prezenter.naformatujCas(statistika.getHornaHranicaIS()) + "]");
        }
    }

    public static void pocetObsluzenych(SimulaciaSystem simulacia, JLabel label)
    {
        DiskretnaStatistika statistika = simulacia.getCelkovaStatistikaPocetObsluzenychAgentov();

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

    public static void celkovyCasFrontOkno(SimulaciaSystem simulacia, JLabel label)
    {
        DiskretnaStatistika statistika = simulacia.getCelkovaStatistikaCasFrontOkno();

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

    public static void celkovaDlzkaFrontOkno(SimulaciaSystem simulacia, JLabel label)
    {
        DiskretnaStatistika statistika = simulacia.getCelkovaStatistikaDlzkaFrontOkno();

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

    public static void textAreaCelkoveOkna(SimulaciaSystem simulacia, JTextArea tabulka)
    {
        try
        {
            EventQueue.invokeAndWait(() -> {
                StringBuilder stringBuilder = new StringBuilder();

                Okno[] oknaOnline = simulacia.getObsluhaOkna().getOknaOnline();
                for (int i = 0; i < oknaOnline.length; i++)
                {
                    stringBuilder.append(oknaOnline[i].getVytazenie(simulacia.getAktualnySimulacnyCas())).append("\n");
                }

                Okno[] oknaObycajni = simulacia.getObsluhaOkna().getOknaObycajni();
                for (int i = 0; i < oknaObycajni.length; i++)
                {
                    stringBuilder.append(oknaObycajni[i].getVytazenie(simulacia.getAktualnySimulacnyCas())).append("\n");
                }

                tabulka.setText(stringBuilder.toString());
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii celkovej statistiky obsluznych okien!");
        }
    }

    public static void tabulkaCelkovePokladne(SimulaciaSystem simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeAndWait(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);

                DiskretnaStatistika[] vytazenie = simulacia.getCelkovaStatistikaVytazeniePokladne();
                DiskretnaStatistika[] cakanie = simulacia.getCelkovaStatistikaCakanieFrontPokladne();
                DiskretnaStatistika[] dlzkaFront = simulacia.getCelkovaStatistikaDlzkaFrontPokladne();

                for (int i = 0; i < vytazenie.length; i++)
                {
                    model.addRow(new Object[]{
                        i,
                        Prezenter.zaokruhli(vytazenie[i].forceGetPriemer()),
                        Prezenter.zaokruhli(dlzkaFront[i].forceGetPriemer()),
                        Prezenter.zaokruhli(cakanie[i].forceGetPriemer())
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii celkovej tabulky okien!");
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

    public static void dlzkaFrontAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(Prezenter.zaokruhli(simulacia.getAutomat().getPriemernaDlzkaFrontu(simulacia.getAktualnySimulacnyCas()))));
    }

    public static void casFrontAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(Prezenter.zaokruhli(simulacia.getAutomat().getPriemerneCakenieFront())));
    }

    public static void aktualnaDlzkaFrontAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(simulacia.getAutomat().getPocetFront()));
    }

    public static void casFrontOkno(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(Prezenter.zaokruhli(simulacia.getObsluhaOkna().getPriemerneCakenieFront())));
    }

    public static void dlzkaFrontOkno(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(Prezenter.zaokruhli(simulacia.getObsluhaOkna().getPriemernaDlzkaFrontu(simulacia.getAktualnySimulacnyCas()))));
    }

    public static void aktualnaDlzkaFrontOkno(SimulaciaSystem simulacia, JLabel label)
    {
        Queue<Agent> front = simulacia.getObsluhaOkna().getFront();
        StringBuilder stavFront = new StringBuilder("]");

        for (Agent agent : front)
        {
            switch (agent.getTypAgenta())
            {
                case TypAgenta.ONLINE:
                    stavFront.append("O ");
                    break;
                case TypAgenta.BEZNY:
                    stavFront.append("B ");
                    break;
                case TypAgenta.ZMLUVNY:
                    stavFront.append("Z ");
                    break;
            }
        }

        for (int i = front.size(); i < Konstanty.KAPACITA_FRONT_OKNO; i++)
        {
            stavFront.append("X ");
        }
        stavFront.setLength(stavFront.length() - 1);
        stavFront.append("[");
        stavFront.reverse();

        stavFront.insert(0, front.size() +  ": -> ");
        stavFront.append(" ->");

        label.setText(stavFront.toString());
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

                Okno[] oknaObycajni = simulacia.getObsluhaOkna().getOknaObycajni();
                for (Okno okno : oknaObycajni)
                {
                    model.addRow(new Object[]{
                        "Obycajne",
                        okno.getObsadene(),
                        Prezenter.zaokruhli(okno.getVytazenie(simulacia.getAktualnySimulacnyCas()))
                    });
                }

                Okno[] oknaOnline = simulacia.getObsluhaOkna().getOknaOnline();
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
                        Prezenter.zaokruhli(pokladna.getVytazenie(simulacia.getAktualnySimulacnyCas())),
                        pokladna.getPocetFront(),
                        Prezenter.zaokruhli(pokladna.getPriemernaDlzkaFrontu(simulacia.getAktualnySimulacnyCas())),
                        Prezenter.zaokruhli(pokladna.getPriemerneCakenieFront())
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

    private static String naformatujCas(double casOdZaciatku)
    {
        int pocetHodin = (int)Math.floor(casOdZaciatku / 3600);
        double pocetMinut = (casOdZaciatku - pocetHodin * 3600) / 60;

        final int hodinaOtvorenia = 9;
        return (pocetHodin + hodinaOtvorenia) + ":" + Prezenter.zaokruhli(pocetMinut);
    }

    private static String zaokruhli(double cislo)
    {
        return Prezenter.FORMATOVAC.format(cislo);
    }
}
