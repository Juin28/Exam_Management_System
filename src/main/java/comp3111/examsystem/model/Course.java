package comp3111.examsystem.model;

public class Course {
    public long id;
    public String courseName;
    public String courseID;
    public String department;

    // Default constructor
    public Course() {
        this.courseName = "courseName";
        this.courseID = "courseID";
        this.department = "department";
        this.id = 0;
    }

    public Course(String courseName, String courseID, String department, long id) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.department = department;
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getDepartment() {
        return department;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
