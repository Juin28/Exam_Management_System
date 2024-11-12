package comp3111.examsystem.model;

public class Question {

    public enum QuestionType {
        ONE_CORRECT_ANSWER,
        MULTIPLE_CORRECT_ANSWERS
    }

    private String questionDescription;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private char[] correctOption;
    private int questionScore;
    private QuestionType questionType;
    private int questionId;

    public Question(String questionDescription, String optionA, String optionB, String optionC, String optionD, char[] correctOption, int questionScore, QuestionType questionType, int questionId) {
        this.questionDescription = questionDescription;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.questionScore = questionScore;
        this.questionType = questionType;
        this.questionId = questionId;
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

    public char[] getCorrectOption() {
        return correctOption;
    }

    public int getQuestionScore() {
        return questionScore;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public int getQuestionId() {
        return questionId;
    }
}

