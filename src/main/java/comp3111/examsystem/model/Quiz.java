package comp3111.examsystem.model;

public class Quiz {
    public long id;
    public String quizName;
    public String quizTime;
    public String courseID;
    public String publishStatus;
    public String questionIDs;     // storing Question ids by concatenating them with "|"

    // Default constructor
    public Quiz() {
        this.id = 0;
        this.quizName = "quizName";
        this.quizTime = "60";
        this.courseID = "courseID";
        this.publishStatus = "no";
        this.questionIDs = "";
    }

    public Quiz(String quizName, String quizTime, String courseID, String publishStatus, long id, String questionIDs) {
        this.id = id;
        this.quizName = quizName;
        this.quizTime = quizTime;
        this.courseID = courseID;
        this.publishStatus = publishStatus;
        this.questionIDs = questionIDs;
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

    public String getCourseID() {
        return courseID;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void publish() {
        this.publishStatus = "yes";
    }

    public String getQuestionIDs() {
        return questionIDs;
    }

    public void addQuestion(String questionID) {
        questionIDs += questionID + "|";
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public void setQuizTime(String quizTime) {
        this.quizTime = quizTime;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public void setQuestionIDs(String questionIDs) {
        this.questionIDs = questionIDs;
    }
}