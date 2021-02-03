import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class CourseScraper {
	private String decimal;
	private String splitter;
	
	public ArrayList<Course> getCourses(File file) throws IOException {
		String content = read(file).replace("\n", "").replace("\r", "");
		String[] rows = content.split("(?<=[\\s])(?=[12][\\D])"); // split at note
		
		ArrayList<Course> courses = new ArrayList<>();
		
		if (rows[0].contains("Official")) { // transcript in English
			splitter = " Note";
			decimal = ".";
		} else { // transcript in Swedish
			splitter = " Not";
			decimal = ",";
		}
		
		for (String row : rows) {
			try {
				if (row.contains(splitter)) {
					row = row.split(splitter)[1]; // speciell kommandor�relse ;)
				}
				Course course = getCourse(row);
				courses.add(course);
			} catch (IllegalArgumentException e) {
				// not a valid course
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

	private Course getCourse(String row) throws IllegalArgumentException {
		if (Character.isDigit(row.charAt(0))) {
			row = row.substring(1); // remove leading digit
		}
		
		Pattern coursePattern = Pattern.compile("^([A-Z]{4}[0-9]{2})");
		Matcher courseMatch = coursePattern.matcher(row);
		
		if (courseMatch.lookingAt()) { // found valid course
			row = row.split("[0-9]{5}")[0]; // split at random code
			String[] components = row.split("\\s+"); // split at spaces
			
			if (components.length < 4 || components.length > 5) {
				throw new IllegalArgumentException("Row was not a valid course!");
			}
			
			String code = components[0];
			
			// have to take care of special case when course name is too long here
			String name = "";
			int i = 0;
			// this looks for "x.x hp", but not goodly
			while (i < components[1].length() - 1 && !(Character.isDigit(components[1].charAt(i)) && components[1].charAt(i + 1) == decimal.charAt(0))) {
				name += components[1].charAt(i);
				if (Character.isLowerCase(components[1].charAt(i)) && Character.isUpperCase(components[1].charAt(i + 1))) {
					name += " ";
				}
				i += 1;
			}
			
			if (name.length() == i) {
				name += components[1].charAt(i);
			}
			
			double credits;
			int grade;
			
			if (components.length == 4) {
				String creditsString = components[1].substring(i - 1);
				credits = parseCredits(creditsString);
				grade = (components[2].equals("G")) ? 0 : Integer.valueOf(components[2]);
			} else {
				credits = parseCredits(components[2]);
				grade = (components[3].equals("G")) ? 0 : Integer.valueOf(components[3]);
			}
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy�MM�dd");
			LocalDate date = LocalDate.parse(components[components.length - 1], formatter);
			
			return new Course(date, code, name, grade, credits);
		} else { 
			throw new IllegalArgumentException("Row was not a valid course!");
		}
	}
	
	private double parseCredits(String creditsString) {
		creditsString = creditsString.substring(1); // trim prefix
		
		String[] leftAndRight = creditsString.split("\\" + decimal);
		leftAndRight[1] = leftAndRight[1].substring(0, 1); // only interested in first char
		double credits = 0.0;
		
		credits += Double.parseDouble(leftAndRight[0]);
		String right = "0." + leftAndRight[1];
		credits += Double.parseDouble(right);
		
		return credits;
	}
}
