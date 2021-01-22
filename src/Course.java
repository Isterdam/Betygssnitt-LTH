import java.time.LocalDate;

public class Course {
	private LocalDate date;
	private String code;
	private String name;
	private int grade;
	private double credits;
	
	public Course(LocalDate date, String code, String name, int grade, double credits) {
		this.date = date;
		this.code = code;
		this.name = name;
		this.grade = grade;
		this.credits = credits;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public double getGrade() {
		return (double) grade;
	}
	
	public double getCredits() {
		return credits;
	}
	
	public String toString() {
		return code + " " + name + ", " + credits + " hp. Betyg: " + ((grade == 0) ? "G" : grade);
	}
}
