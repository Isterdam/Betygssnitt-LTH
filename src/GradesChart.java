import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;


public class GradesChart extends JPanel {
	private ArrayList<Double> grades;
	
	public GradesChart(ArrayList<Double> grades) {
		this.grades = grades;
		initUI();
	}
	
	private void initUI() {
		XYDataset data = createDataset();
		JFreeChart chart = createChart(data);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		add(chartPanel);
	}
	
	private JFreeChart createChart(XYDataset data) {
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Betygsutveckling över tid",
				"Tentatillfälle",
				"Betygssnitt",
				data,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);
		
		XYPlot plot = chart.getXYPlot();
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Betygssnitt över tid",
        		new Font("Serif", java.awt.Font.BOLD, 18)
        		)
        );
        
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setRange(2.9, 5.1);

        return chart;
	}

	private XYDataset createDataset() {
		XYSeries series = new XYSeries("Betyg");
		
		int i = 1;
		for (Double grade : grades) {
			if (grade >= 3) {
				series.add(i, grade);
				i++;
			}
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		
		return dataset;
	}
}
