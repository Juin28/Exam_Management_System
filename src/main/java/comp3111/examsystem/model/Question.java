package comp3111.examsystem.model;

import java.util.Arrays;

public class Question {

    public String questionDescription;
    public String optionA;
    public String optionB;
    public String optionC;
    public String optionD;
    public String answer;
    public String questionScore;
    public String questionType;
    public long id;

    // Default constructor
    public Question() {
        this.questionDescription = "questionDescription";
        this.optionA = "optionA";
        this.optionB = "optionB";
        this.optionC = "optionC";
        this.optionD = "optionD";
        this.answer = "A";
        this.questionScore = "10";
        this.questionType = "Single";
        this.id = 0;
    }

    public Question(String questionDescription, String optionA, String optionB, String optionC, String optionD, String answer, String questionScore, String questionType, long id) {
        this.questionDescription = questionDescription;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.questionScore = questionScore;
        this.questionType = questionType;
        this.id = id;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestionScore() {
        return questionScore;
    }

    public String getQuestionType() {
        return questionType;
    }

    public long getId() {
        return id;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestionScore(String questionScore) {
        this.questionScore = questionScore;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}

