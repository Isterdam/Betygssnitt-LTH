import java.util.ArrayList;

public class Student {
	private ArrayList<Course> courses;
	
	public Student(ArrayList<Course> courses) {
		this.courses = courses;
	}
	
	public double getAverageGrade(int courseNumber) {
		double total = 0.0;
		double validCredits = 0.0;
		
		for (int i = 0; i < courseNumber; i++) {
			Course course = courses.get(i);
			if (course.getGrade() > 0) {
				total += (course.getGrade() * course.getCredits());
				validCredits += course.getCredits();
			}
		}
		
		return round(total / validCredits, 2); // round to 2 decimals
	}
	
	private double round(double value, int places) {
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long temp = Math.round(value);
		return (double) temp / factor;
	}
}
