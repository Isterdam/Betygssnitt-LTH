import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class GradeDistribution extends JPanel {
	ArrayList<Course> courses;
	
	public GradeDistribution(ArrayList<Course> courses) {
		this.courses = courses;
		initUI();
	}
	
	private void initUI() {
		CategoryDataset dataset = createDataset();
		
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(this.getBackground());
		add(chartPanel);
	}
	
	private CategoryDataset createDataset() {
		int[] grades = new int[3];
		
		for (Course course : courses) {
			if (course.getGrade() > 0) {
				grades[(int) (course.getGrade() - 3)] += 1;
			}
		}
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(grades[0], "Betyg", "3");
		dataset.setValue(grades[1], "Betyg", "4");
		dataset.setValue(grades[2], "Betyg", "5");
		
		return dataset;
	}
	
	private JFreeChart createChart(CategoryDataset dataset) {
		JFreeChart barChart = ChartFactory.createBarChart("Antal betyg", 
				"Betyg", 
				"Förekomst", 
				dataset,
				PlotOrientation.VERTICAL, 
				false, 
				true, 
				false
		);
		
		barChart.setTitle(new TextTitle("Betygsfördelning",
				new Font("Serif", java.awt.Font.PLAIN, 18)
			)
		);
		
		return barChart;
	}
}
