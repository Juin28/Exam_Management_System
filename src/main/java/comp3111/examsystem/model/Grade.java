package comp3111.examsystem.model;

public class Grade {
    public long id;
    public String studentId;
    public String questionId;
    public String studentScore;
    // testing
    public String timeSpent;

    public Grade(){
        this.studentId = "studentId";
        this.questionId = "questionId";
        this.studentScore = "studentScore";
        this.timeSpent = "timeSpent";
        this.id = 0;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Grade(String studentId, String questionId, String studentScore, String timeSpent){
        this.studentId = studentId;
        this.questionId = questionId;
        this.studentScore = studentScore;
        this.timeSpent = timeSpent;
        this.id = 0;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(String studentScore) {
        this.studentScore = studentScore;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public long getID(){
        return id;
    }

}
