package comp3111.examsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    public int id;
    public String quizName;
    public int quizTime;
    public int numQuestions;
    public boolean isPublished;
    public List<Long> questions;    // storing Question ids instead of Question objects

    public Quiz(String quizName, int quizTime, int numQuestions, int id) {
        this.id = id;
        this.quizName = quizName;
        this.quizTime = quizTime;
        this.numQuestions = numQuestions;
        this.isPublished = false;
        this.questions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getQuizName() {
        return quizName;
    }

    public int getQuizTime() {
        return quizTime;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void publish() {
        this.isPublished = true;
    }

    public List<Long> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question.getId());
    }
}