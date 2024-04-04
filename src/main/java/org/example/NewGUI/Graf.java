package org.example.NewGUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

public class Graf
{
    private final JFreeChart graf;
    private final DefaultCategoryDataset dataset;
    private int maxHodnota;

    public Graf(int minHodnota, int maxHodnota, double hornaHranica)
    {
        this.validujVstupy(hornaHranica);

        this.dataset = new DefaultCategoryDataset();
        for (int i = minHodnota; i <= maxHodnota; i++)
        {
            this.dataset.addValue(0.0, "Stlpec", String.valueOf(i));
        }

        this.graf = ChartFactory.createBarChart(
            "",
            "Pocet pokladni",
            "Dlzka frontu pred automatom",
            this.dataset,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );

        Color transparent = new Color(0xFF, 0xFF, 0xFF, 0);
        this.graf.setBackgroundPaint(transparent);
        this.graf.getCategoryPlot().getRangeAxis().setLowerBound(0.0);
        this.graf.getCategoryPlot().getRangeAxis().setUpperBound(hornaHranica);

        CategoryItemRenderer renderer = ((CategoryPlot)this.graf.getPlot()).getRenderer();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
            TextAnchor.TOP_CENTER);
        renderer.setDefaultPositiveItemLabelPosition(position);

        this.maxHodnota = Integer.MIN_VALUE;
    }

    private void validujVstupy(double hornaHranica)
    {
        if (hornaHranica < 1)
        {
            throw new RuntimeException("Horna hranica grafu nemoze byt mensia ako 1!");
        }
    }

    public void aktualizujGraf(int stlpec, double hodnota)
    {
        if (hodnota < 0)
        {
            return;
        }

        if (hodnota > this.maxHodnota)
        {
            this.maxHodnota = (int)Math.ceil(hodnota);
            this.graf.getCategoryPlot().getRangeAxis().setUpperBound(this.maxHodnota + 1);
        }

        this.dataset.setValue(hodnota, "Stlpec", String.valueOf(stlpec));

    }

    public JFreeChart getGraf()
    {
        return this.graf;
    }
}
