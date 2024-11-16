package comp3111.examsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    public long id;
    public String courseName;
    public String courseID;
    public String department;
//    public List<Long> enrolledStudents;  // storing Student ids instead of Student objects
//    public List<Long> assignedTeachers;  // storing Teacher ids instead of Teacher objects

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
//        this.enrolledStudents = new ArrayList<Long>();
//        this.assignedTeachers = new ArrayList<Long>();
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

    public long getId() {return id;}

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

//    public List<Long> getEnrolledStudents() {
//        return enrolledStudents;
//    }
//
//    public List<Long> getAssignedTeachers() {
//        return assignedTeachers;
//    }
//
//    public void enrollStudent(Student student) {
//        enrolledStudents.add(student.getId());
//    }
//
//    public void assignTeacher(Teacher teacher) {
//        assignedTeachers.add(teacher.getId());
//    }

}
