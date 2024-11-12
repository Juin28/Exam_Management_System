package comp3111.examsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseName;
    private String courseId;
    private int numOfQuiz;
    private List<Integer> enrolledStudents;  // storing Student ids instead of Student objects
    private List<Integer> assignedTeachers;  // storing Teacher ids instead of Teacher objects

    public Course(String courseName, String courseId, int numOfQuiz) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.numOfQuiz = numOfQuiz;
        this.enrolledStudents = new ArrayList<Integer>();
        this.assignedTeachers = new ArrayList<Integer>();
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getNumOfQuiz() {
        return numOfQuiz;
    }

    public List<Integer> getEnrolledStudents() {
        return enrolledStudents;
    }

    public List<Integer> getAssignedTeachers() {
        return assignedTeachers;
    }

    public void enrollStudent(Student student) {
        enrolledStudents.add(student.getStudentId());
    }

    public void assignTeacher(Teacher teacher) {
        assignedTeachers.add(teacher.getTeacherId());
    }
}
