import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			File selectedFile;
			ArrayList<Course> courses;
			Student student;
			
			JFrame frame = new JFrame("Betygssnitt LTH");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1500, 900);
			frame.setLocationRelativeTo(null);
			
			JFileChooser fc = new JFileChooser(System.getProperty("user.home") + "/Downloads");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("pdf", "pdf");
			fc.setFileFilter(filter);
			fc.setDialogTitle("Välj resultatintyg");
			int result = fc.showOpenDialog(null);
			
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					selectedFile = fc.getSelectedFile();
					courses = new CourseScraper().getCourses(selectedFile);
					if (courses.size() == 0) {
						JOptionPane.showMessageDialog(null, "Kunde inte läsa filen :( Valde du verkligen rätt resultatintyg?");
						return;
					}
					student = new Student(courses);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Kunde inte läsa filen :( Valde du verkligen rätt resultatintyg?");
					return;
				}
			} else {
				return;
			}
			
			JSplitPane charts = new JSplitPane();
			charts.setDividerSize(1);
			charts.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			GradesChart chart1 = new GradesChart(getGradeProgression(courses, student));
			GradeDistribution chart2 = new GradeDistribution(courses);
			charts.setRightComponent(chart2);
			charts.setLeftComponent(chart1);
			
			JSplitPane window = new JSplitPane();
			window.setDividerSize(2);
			window.setOrientation(JSplitPane.VERTICAL_SPLIT);
			window.setTopComponent(getWindow(courses, student));
			window.setBottomComponent(charts);
			
			frame.add(window);
			frame.setVisible(true);
		});
	}
	
	private static JSplitPane getWindow(ArrayList<Course> courses, Student student) {
		JLabel label1 = new JLabel();
		label1.setText("<html><h2>Ditt betygssnitt:</h2></html>");
		label1.setHorizontalAlignment(JLabel.CENTER);
		label1.setVerticalAlignment(JLabel.BOTTOM);
		JLabel label2 = new JLabel();
		int courseNumber = courses.size();
		label2.setText("<html><h1>" + student.getAverageGrade(courseNumber) + " / 5</h1></html>");
		label2.setHorizontalAlignment(JLabel.CENTER);
		label2.setVerticalAlignment(JLabel.TOP);
		
		JSplitPane grade = new JSplitPane();
		grade.setDividerSize(0);
		grade.setDividerLocation(200);
		grade.setOrientation(JSplitPane.VERTICAL_SPLIT);
		grade.setLeftComponent(label1);
		grade.setRightComponent(label2);
		
		String[][] courseData = new String[courses.size() + 2][5];
		String[] columns = {"Kurskod", "Kursnamn", "Högskolepoäng", "Betyg", "Datum"};
		double totalCredits = 0.0;
		for (int i = 0; i < courses.size(); i++) {
			Course course = courses.get(i);
			totalCredits += course.getCredits();
			String creditsString = String.valueOf(course.getCredits());
			String gradeString = course.getGrade() == 0 ? "G" : String.valueOf(course.getGrade());
			courseData[i] = new String[]{course.getCode(), course.getName(), creditsString + " hp", gradeString, course.getDate().toString()};
		}
		courseData[courses.size()] = new String[]{"", "", "", "", ""};
		courseData[courses.size() + 1] = new String[]{"TOTALT", "", String.valueOf(totalCredits) + " hp", "", ""};
		JTable jt = new JTable(courseData, columns);
		
		JScrollPane scroll = new JScrollPane(jt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(2);
		splitPane.setDividerLocation(200);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(grade);
		splitPane.setRightComponent(scroll);
		
		return splitPane;
	}
	
	private static ArrayList<Double> getGradeProgression(ArrayList<Course> courses, Student student) {
		ArrayList<Double> grades = new ArrayList<>();
		
		for (int i = 0; i < courses.size() + 1; i++) {
			grades.add(student.getAverageGrade(i));
		}
		
		return grades;
	}
}
