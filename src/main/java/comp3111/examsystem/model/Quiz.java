package comp3111.examsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    public long id;
    public String courseId;
    public String quizName;
    public String quizTime;
    public String numQuestions;
    public String isPublished;
    public String questions;    // storing Question ids instead of Question objects

    // default constructor
    public Quiz (){
        this.id = 0;
        this.courseId = "courseId";
        this.quizName = "quizName";
        this.quizTime = "quizTime";
        this.numQuestions = "numQuestions";
        this.isPublished = "false";
        this.questions = "hi";
    }

    public Quiz(String quizName, String quizTime, String numQuestions, long id, String courseId, String questions) {
        this.id = id;
        this.courseId = courseId;
        this.quizName = quizName;
        this.quizTime = quizTime;
        this.numQuestions = numQuestions;
        this.isPublished = "false";
        this.questions = questions;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public long getId() {
        return id;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getQuizTime() {
        return quizTime;
    }

    public String getNumQuestions() {
        return numQuestions;
    }

    public String isPublished() {
        return isPublished;
    }

    public void publish() {
        this.isPublished = "true";
    }

    public String getQuestions() {
        return questions;
    }

//    public void addQuestion(Question question) {
//        questions.add(question.getId());
//    }
}