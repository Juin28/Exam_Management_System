package comp3111.examsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    public String courseName;
    public String id;
    public int numOfQuiz;
    public List<Long> enrolledStudents;  // storing Student ids instead of Student objects
    public List<Long> assignedTeachers;  // storing Teacher ids instead of Teacher objects

    public Course(String courseName, String id, int numOfQuiz) {
        this.courseName = courseName;
        this.id = id;
        this.numOfQuiz = numOfQuiz;
        this.enrolledStudents = new ArrayList<Long>();
        this.assignedTeachers = new ArrayList<Long>();
    }

    public String getCourseName() {
        return courseName;
    }

    public String getId() {
        return id;
    }

    public int getNumOfQuiz() {
        return numOfQuiz;
    }

    public List<Long> getEnrolledStudents() {
        return enrolledStudents;
    }

    public List<Long> getAssignedTeachers() {
        return assignedTeachers;
    }

    public void enrollStudent(Student student) {
        enrolledStudents.add(student.getId());
    }

    public void assignTeacher(Teacher teacher) {
        assignedTeachers.add(teacher.getId());
    }
}
