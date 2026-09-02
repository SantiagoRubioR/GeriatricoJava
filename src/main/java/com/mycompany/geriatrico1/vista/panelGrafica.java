package com.mycompany.geriatrico1.vista;

import com.mycompany.geriatrico1.modelo.Reporte;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class panelGrafica extends JPanel {

    private static final Color COLOR_FONDO = new Color(255, 255, 255);
    private static final Color COLOR_TEAL = new Color(0, 128, 128);
    private static final Color COLOR_TEAL_OSCURO = new Color(24, 76, 74);
    private static final Color COLOR_TEAL_CLARO = new Color(102, 178, 171);
    private static final Color COLOR_TEXTO = new Color(60, 60, 60);
    private static final Color COLOR_GRILLA = new Color(220, 224, 228);
    private static final Font FUENTE_EJES = new Font("Yu Gothic", Font.PLAIN, 12);
    private static final Font FUENTE_LEYENDA = new Font("Yu Gothic", Font.PLAIN, 13);
    private static final Font FUENTE_TITULO = new Font("Arial Rounded MT Bold", Font.BOLD, 15);

    private static final Color[] PALETA = new Color[]{
        COLOR_TEAL, new Color(241, 196, 83), COLOR_TEAL_OSCURO, new Color(230, 126, 110),
        COLOR_TEAL_CLARO, new Color(149, 165, 166), new Color(93, 156, 236),
        new Color(178, 143, 90), new Color(46, 134, 120), new Color(211, 84, 0)
    };

    private ChartPanel chartPanel;

    public panelGrafica() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    public void mostrarBarras(List<Reporte> datos, String titulo) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Reporte r : datos) {
            dataset.addValue(r.getCantidad(), "Cantidad", r.getCategorias());
        }
        JFreeChart chart = ChartFactory.createBarChart(titulo, null, null, dataset,
                PlotOrientation.VERTICAL, false, true, false);
        estilizarBarras(chart);
        renderizar(chart);
    }

    public void mostrarCircular(List<Reporte> datos, String titulo) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Reporte r : datos) {
            dataset.setValue(r.getCategorias(), r.getCantidad());
        }
        JFreeChart chart = ChartFactory.createPieChart(titulo, dataset, true, true, false);
        estilizarCircular(chart, dataset);
        renderizar(chart);
    }

    public void limpiar() {
        removeAll();
        chartPanel = null;
        revalidate();
        repaint();
    }

    private void renderizar(JFreeChart chart) {
        removeAll();
        chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false);
        chartPanel.setBackground(COLOR_FONDO);
        chartPanel.setMouseWheelEnabled(false);
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void estilizarBarras(JFreeChart chart) {
        chart.setBackgroundPaint(COLOR_FONDO);
        chart.getTitle().setFont(FUENTE_TITULO);
        chart.getTitle().setPaint(COLOR_TEAL_OSCURO);
        if (chart.getLegend() != null) {
            chart.removeLegend();
        }
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(COLOR_FONDO);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(COLOR_GRILLA);
        plot.setDomainGridlinePaint(COLOR_GRILLA);

        CategoryAxis ejeCategorias = plot.getDomainAxis();
        ejeCategorias.setLabelPaint(COLOR_TEXTO);
        ejeCategorias.setTickLabelPaint(COLOR_TEXTO);
        ejeCategorias.setTickLabelFont(FUENTE_EJES);
        ejeCategorias.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6));

        NumberAxis ejeValores = (NumberAxis) plot.getRangeAxis();
        ejeValores.setLabelPaint(COLOR_TEXTO);
        ejeValores.setTickLabelPaint(COLOR_TEXTO);
        ejeValores.setTickLabelFont(FUENTE_EJES);
        ejeValores.setAutoRangeIncludesZero(true);
        ejeValores.setNumberFormatOverride(new java.text.DecimalFormat("#,##0"));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, COLOR_TEAL);
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.12);
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelPaint(COLOR_TEXTO);
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
    }

    private void estilizarCircular(JFreeChart chart, DefaultPieDataset<String> dataset) {
        chart.setBackgroundPaint(COLOR_FONDO);
        chart.getTitle().setFont(FUENTE_TITULO);
        chart.getTitle().setPaint(COLOR_TEAL_OSCURO);
        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(FUENTE_LEYENDA);
            chart.getLegend().setBackgroundPaint(COLOR_FONDO);
        }
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(COLOR_FONDO);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setLabelFont(FUENTE_EJES);
        plot.setLabelBackgroundPaint(COLOR_FONDO);
        plot.setLabelOutlinePaint(COLOR_GRILLA);
        plot.setLabelPaint(COLOR_TEXTO);
        plot.setSectionOutlinesVisible(false);
        plot.setInteriorGap(0.04);

        List<String> claves = dataset.getKeys();
        for (int i = 0; i < claves.size(); i++) {
            plot.setSectionPaint(claves.get(i), PALETA[i % PALETA.length]);
        }
    }
}