import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
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
			frame.setSize(750, 500);
			frame.setLocationRelativeTo(null);
			frame.getContentPane().setLayout(new BorderLayout());
			
			JFileChooser fc = new JFileChooser(System.getProperty("user.home") + "/Desktop");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("pdf", "pdf");
			fc.setFileFilter(filter);
			fc.setDialogTitle("Välj resultatintyg");
			int result = fc.showOpenDialog(null);
			
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					selectedFile = fc.getSelectedFile();
					courses = new CourseScraper().getCourses(selectedFile);
					if (courses.size() == 0) {
						JOptionPane.showMessageDialog(null, "Kunde inte läsa filen... Valde du verkligen ditt resultatintyg?");
						throw new Error("Wrong file!");
					}
					student = new Student(courses);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Kunde inte läsa filen... Valde du verkligen ditt resultatintyg?");
					throw new Error("Could not read file!", e);
				}
			} else {
				return;
			}
			
			JLabel label1 = new JLabel();
			label1.setText("Ditt betygssnitt:");
			label1.setHorizontalAlignment(JLabel.CENTER);
			label1.setVerticalAlignment(JLabel.BOTTOM);
			JLabel label2 = new JLabel();
			label2.setText("<html><h2>" + student.getAverageGrade() + " / 5</h2></html>");
			label2.setHorizontalAlignment(JLabel.CENTER);
			label2.setVerticalAlignment(JLabel.TOP);
			
			JSplitPane grade = new JSplitPane();
			grade.setDividerSize(0);
			grade.setDividerLocation(200);
			grade.setOrientation(JSplitPane.VERTICAL_SPLIT);
			grade.setLeftComponent(label1);
			grade.setRightComponent(label2);
			
			JTextArea textArea = new JTextArea();
			for (Course course : courses) {
				textArea.append(course.toString() + " \n");
			}
			JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JSplitPane splitPane = new JSplitPane();
			splitPane.setDividerSize(2);
			splitPane.setDividerLocation(200);
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setLeftComponent(grade);
			splitPane.setRightComponent(scroll);
			
			frame.add(splitPane);
			frame.setVisible(true);
		});
	}
}
