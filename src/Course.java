public class Course {
	private String code;
	private String name;
	private int grade;
	private double credits;
	
	public Course(String code, String name, int grade, double credits) {
		this.code = code;
		this.name = name;
		this.grade = grade;
		this.credits = credits;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public int getGrade() {
		return grade;
	}
	
	public double getCredits() {
		return credits;
	}
	
	public String toString() {
		return code + " " + name + ", " + credits + " hp. Betyg: " + ((grade == 0) ? "G" : grade);
	}
}
