package comp3111.examsystem.model;

public class Grade {
    public long id;
    public String studentId;
    public String questionId;
    public String studentScore;

    public Grade(){
        this.studentId = "studentId";
        this.questionId = "questionId";
        this.studentScore = "studentScore";
        this.id = 0;
    }

    public Grade(String studentId, String questionId, String studentScore){
        this.studentId = studentId;
        this.questionId = questionId;
        this.studentScore = studentScore;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
