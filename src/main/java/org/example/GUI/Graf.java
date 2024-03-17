package org.example.GUI;

import org.example.Ostatne.Konstanty;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graf
{
    private JFreeChart graf;
    private XYSeries dataset;

    private double curMaxHodnota;
    private double curMinHodnota;

    public Graf(String nadpisGrafu)
    {
        this.dataset = new XYSeries("Data");
        XYSeriesCollection seriesCollection = new XYSeriesCollection(this.dataset);

        this.graf = ChartFactory.createXYLineChart(
            nadpisGrafu,
            "Replikacia",
            "Hodnota",
            seriesCollection,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );

        this.curMaxHodnota = Integer.MIN_VALUE;
        this.curMinHodnota = Integer.MAX_VALUE;
    }

    public void pridajHodnotu(int cisloReplikacie, double vysledok)
    {
        this.curMaxHodnota = Math.max(this.curMaxHodnota, vysledok);
        this.curMinHodnota = Math.min(this.curMinHodnota, vysledok);

        this.dataset.add(cisloReplikacie, vysledok);
        XYPlot xyPlot = (XYPlot)this.graf.getPlot();
        NumberAxis range = (NumberAxis)xyPlot.getRangeAxis();
        range.setRange(this.curMinHodnota - Konstanty.ODSADENIE_HRANICNE_HODNOTY,
                       this.curMaxHodnota + Konstanty.ODSADENIE_HRANICNE_HODNOTY);
        this.graf.fireChartChanged();
    }

    public JFreeChart getGraf()
    {
        return this.graf;
    }

    public void resetuj()
    {
        this.dataset.clear();
        this.curMaxHodnota = Integer.MIN_VALUE;
        this.curMinHodnota = Integer.MAX_VALUE;
    }
}
