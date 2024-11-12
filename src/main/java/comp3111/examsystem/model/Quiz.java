package comp3111.examsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String quizName;
    private int quizTime;
    private int numQuestions;
    private boolean isPublished;
    private List<Integer> questions;    // storing Question ids instead of Question objects

    public Quiz(String quizName, int quizTime, int numQuestions) {
        this.quizName = quizName;
        this.quizTime = quizTime;
        this.numQuestions = numQuestions;
        this.isPublished = false;
        this.questions = new ArrayList<>();
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

    public List<Integer> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question.getQuestionId());
    }
}