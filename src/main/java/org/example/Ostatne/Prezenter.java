package org.example.Ostatne;

import org.example.Simulacia.Statistiky.DiskretnaStatistika;
import org.example.Simulacia.System.Agenti.Objekty.Automat;
import org.example.Simulacia.System.Agenti.Zakaznik.Agent;
import org.example.Simulacia.System.Agenti.Objekty.Okno;
import org.example.Simulacia.System.Agenti.Objekty.Pokladna;
import org.example.Simulacia.System.Agenti.Zakaznik.TypAgenta;
import org.example.Simulacia.System.SimulaciaSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Queue;

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
            label.setText(Prezenter.zaokruhli(statistika.getPriemer() / 60.0) + " min [" +
                Prezenter.zaokruhli(statistika.getDolnaHranicaIS() / 60.0) + ", " +
                Prezenter.zaokruhli(statistika.getHornaHranicaIS() / 60.0) + "]");
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
            label.setText(Prezenter.zaokruhli(statistika.getPriemer()) + " sec [" +
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
            label.setText(Prezenter.zaokruhli(statistika.getPriemer())+ " [" +
                Prezenter.zaokruhli(statistika.getDolnaHranicaIS()) + ", " +
                Prezenter.zaokruhli(statistika.getHornaHranicaIS()) + "]");
        }
    }

    public static void celkoveVytazenieAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        DiskretnaStatistika statistika = simulacia.getCelkovaStatistikaVytazenieAutomat();

        statistika.skusPrepocitatStatistiky();
        if (!statistika.getStatistikyVypocitane())
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.zaokruhli(statistika.getPriemer() * 100) + " % [" +
                Prezenter.zaokruhli(statistika.getDolnaHranicaIS() * 100) + ", " +
                Prezenter.zaokruhli(statistika.getHornaHranicaIS() * 100) + "]");
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
            label.setText(Prezenter.zaokruhli(statistika.getPriemer()) + " sec [" +
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

    public static void tabulkaCelkoveOkna(SimulaciaSystem simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeLater(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);
                DiskretnaStatistika[] obycajneOknaStatistiky = simulacia.getCelkovaStatistikaVytazenieObycajneOkna();
                for (DiskretnaStatistika statistika : obycajneOknaStatistiky)
                {
                    model.addRow(new Object[]{
                        "Obycajny",
                        Prezenter.zaokruhli(statistika.forceGetPriemer() * 100) + " %"
                    });
                }

                DiskretnaStatistika[] onlineOknaStatistiky = simulacia.getCelkovaStatistikaVytazenieOnlineOkna();
                for (DiskretnaStatistika statistika : onlineOknaStatistiky)
                {
                    model.addRow(new Object[]{
                        "Online",
                        Prezenter.zaokruhli(statistika.forceGetPriemer() * 100) + " %"
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii celkovej tabulky okien!");
        }
    }

    public static void tabulkaCelkovePokladne(SimulaciaSystem simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeLater(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);

                DiskretnaStatistika[] vytazenie = simulacia.getCelkovaStatistikaVytazeniePokladne();
                DiskretnaStatistika[] cakanie = simulacia.getCelkovaStatistikaCakanieFrontPokladne();
                DiskretnaStatistika[] dlzkaFront = simulacia.getCelkovaStatistikaDlzkaFrontPokladne();

                for (int i = 0; i < vytazenie.length; i++)
                {
                    model.addRow(new Object[]{
                        i,
                        Prezenter.zaokruhli(vytazenie[i].forceGetPriemer() * 100) + " %",
                        Prezenter.zaokruhli(dlzkaFront[i].forceGetPriemer()),
                        Prezenter.zaokruhli(cakanie[i].forceGetPriemer()) + " sec"
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
        label.setText(Prezenter.naformatujCas(simulacia.getAktualnySimulacnyCas()));
    }

    public static void dlzkaFrontAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(Prezenter.zaokruhli(simulacia.getAutomat().getPriemernaDlzkaFrontu())));
    }

    public static void casFrontAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(Prezenter.zaokruhli(simulacia.getAutomat().getPriemerneCakenieFront()) + " sec");
    }

    public static void aktualnaDlzkaFrontAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(simulacia.getAutomat().getPocetFront()));
    }

    public static void vytazenieAutomat(SimulaciaSystem simulacia, JLabel label)
    {
        Automat automat = simulacia.getAutomat();
        label.setText(Prezenter.zaokruhli(automat.getVytazenie() * 100) + " %");
    }

    public static void casFrontOkno(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(Prezenter.zaokruhli(simulacia.getObsluhaOkna().getPriemerneCakenieFront()) + " sec");
    }

    public static void dlzkaFrontOkno(SimulaciaSystem simulacia, JLabel label)
    {
        label.setText(String.valueOf(Prezenter.zaokruhli(simulacia.getObsluhaOkna().getPriemernaDlzkaFrontu())));
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
                ArrayList<Agent> agenti = new ArrayList<>(simulacia.getAgenti());

                int index = 0;
                while (index < model.getRowCount())
                {
                    int stlpec = 0;
                    model.setValueAt(agenti.get(index).getID(), index, stlpec++);
                    model.setValueAt(agenti.get(index).getTypAgenta(), index, stlpec++);
                    model.setValueAt(Prezenter.naformatujCas(agenti.get(index).getCasPrichodSystem()), index, stlpec++);
                    model.setValueAt(Prezenter.naformatujCas(agenti.get(index).getCasZaciatokObsluhyAutomat()), index, stlpec++);
                    model.setValueAt(Prezenter.naformatujCas(agenti.get(index).getCasKoniecObsluhyAutomat()), index, stlpec++);
                    model.setValueAt(Prezenter.naformatujCas(agenti.get(index).getCasZaciatokObsluhyOkno()), index, stlpec++);
                    model.setValueAt(Prezenter.naformatujCas(agenti.get(index).getCasKoniecObsluhyOkno()), index, stlpec++);
                    model.setValueAt(Prezenter.naformatujCas(agenti.get(index).getCasZaciatokObsluhyPokladna()), index, stlpec++);
                    model.setValueAt(Prezenter.naformatujCas(agenti.get(index).getCasKoniecObsluhyPokladna()), index, stlpec++);
                    model.setValueAt(Prezenter.naformatujCas(agenti.get(index).getCasZaciatokVyzdvihnutie()), index, stlpec++);
                    model.setValueAt(Prezenter.naformatujCas(agenti.get(index).getCasKoniecVyzdvihnutie()), index, stlpec++);

                    index++;
                }

                while (index < agenti.size())
                {
                    model.addRow(new Object[]{
                        agenti.get(index).getID(),
                        agenti.get(index).getTypAgenta(),
                        Prezenter.naformatujCas(agenti.get(index).getCasPrichodSystem()),
                        Prezenter.naformatujCas(agenti.get(index).getCasZaciatokObsluhyAutomat()),
                        Prezenter.naformatujCas(agenti.get(index).getCasKoniecObsluhyAutomat()),
                        Prezenter.naformatujCas(agenti.get(index).getCasZaciatokObsluhyOkno()),
                        Prezenter.naformatujCas(agenti.get(index).getCasKoniecObsluhyOkno()),
                        Prezenter.naformatujCas(agenti.get(index).getCasZaciatokObsluhyPokladna()),
                        Prezenter.naformatujCas(agenti.get(index).getCasKoniecObsluhyPokladna()),
                        Prezenter.naformatujCas(agenti.get(index).getCasZaciatokVyzdvihnutie()),
                        Prezenter.naformatujCas(agenti.get(index).getCasKoniecVyzdvihnutie())
                    });

                    index++;
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
                        "Obycajny",
                        okno.getObsadene(),
                        Prezenter.zaokruhli(okno.getVytazenie() * 100) + " %"
                    });
                }

                Okno[] oknaOnline = simulacia.getObsluhaOkna().getOknaOnline();
                for (Okno okno : oknaOnline)
                {
                    model.addRow(new Object[]{
                        "Online",
                        okno.getObsadene(),
                        Prezenter.zaokruhli(okno.getVytazenie() * 100) + " %"
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
                        Prezenter.zaokruhli(pokladna.getVytazenie() * 100) + " %",
                        pokladna.getPocetFront(),
                        Prezenter.zaokruhli(pokladna.getPriemernaDlzkaFrontu()),
                        Prezenter.zaokruhli(pokladna.getPriemerneCakenieFront()) + " sec"
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
        if (casOdZaciatku < 0)
        {
            return "n/a";
        }

        int pocetHodin = (int)Math.floor(casOdZaciatku / 3600);
        int pocetMinut = (int)Math.floor((casOdZaciatku - pocetHodin * 3600) / 60);
        int pocetSekund = (int)Math.round(casOdZaciatku - pocetHodin * 3600 - pocetMinut * 60);

        final int hodinaOtvorenia = 9;
        pocetHodin += hodinaOtvorenia;

        return Prezenter.casNaString(pocetHodin) + ":" + Prezenter.casNaString(pocetMinut) + ":" + Prezenter.casNaString(pocetSekund);
    }

    private static String casNaString(int cas)
    {
        String casString = String.valueOf(cas);

        if (casString.length() == 1)
        {
            return '0' + casString;
        }
        else
        {
            return casString;
        }
    }

    private static String zaokruhli(double cislo)
    {
        if (cislo < 0)
        {
            return "n/a";
        }

        return Prezenter.FORMATOVAC.format(cislo);
    }
}
