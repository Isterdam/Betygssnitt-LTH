import java.util.ArrayList;

public class Student {
	private ArrayList<Course> courses;
	
	public Student(ArrayList<Course> courses) {
		this.courses = courses;
	}
	
	public double getAverageGrade() {
		double total = 0.0;
		double validCredits = 0.0;
		
		for (Course course : courses) {
			if (course.getGrade() > 0) {
				total += (course.getGrade() * course.getCredits());
				validCredits += course.getCredits();
			}
		}
		
		return total / validCredits;
	}
}
