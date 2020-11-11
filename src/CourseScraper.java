import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class CourseScraper {
	
	public ArrayList<Course> getCourses(File file) throws IOException {
		String content = read(file);
		String[] rows = content.split("\\r?\\n"); // split at newline
		ArrayList<Course> courses = new ArrayList<>();
		
		for (String row : rows) {
			Course course = getCourse(row);
			if (!course.getCode().equals("not a course")) {
				courses.add(course);
			}
		}
		
		return courses;
	}
	
	private String read(File file) throws IOException {
		PDDocument doc = PDDocument.load(file);
		PDFTextStripper stripper = new PDFTextStripper();
		
		String content = stripper.getText(doc);
		doc.close();
		
		return content;
	}

	private Course getCourse(String row) {
		Pattern coursePattern = Pattern.compile("^([A-Z]{4}[0-9]{2})");
		Matcher matcher = coursePattern.matcher(row);
		
		if (matcher.lookingAt()) { // found valid course
			String[] components = row.split("\\s+"); // split at spaces
			String code = components[0];
			String name = "";
			int grade = 0;
			double credits = 0.0;
			
			if (components.length < 6) { // does not contain all data
				return new Course("not a course", "", -1, -1.0); 
			}
			
			// loop over course name
			int i = 1;
			// this checks for "x,x hp", but not goodly
			while (!(components[i].contains(",") && components[i].contains("hp"))) {
				name += (components[i] + " ");
				i += 1;
			}
			name = name.trim(); // trim name
			
			// continue loop in search of other data
			for (; i < components.length; i++) {
				if (components[i].matches("^[3-5]")) {
					grade = Integer.parseInt(components[i]);
				}
				else if (components[i].matches("^[G]")) {
					grade = 0;
				}
				else if (components[i].contains(",") && components[i].contains("hp")) {
					credits = parseCredits(components[i]);
				}
			}
			
			return new Course(code, name, grade, credits);
		} else { // row was not a valid course
			return new Course("not a course", "", -1, -1.0);
		}
	}
	
	private double parseCredits(String creditsString) {
		creditsString = creditsString.substring(1); // trim string
		
		String[] leftAndRight = creditsString.split(",");
		leftAndRight[1] = leftAndRight[1].substring(0, 1); // only interested in first char
		double credits = 0.0;
		
		credits += Double.parseDouble(leftAndRight[0]);
		String right = "0." + leftAndRight[1];
		credits += Double.parseDouble(right);
		
		return credits;
	}
}
