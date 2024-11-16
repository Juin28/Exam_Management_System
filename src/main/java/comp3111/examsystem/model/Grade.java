package comp3111.examsystem.model;

public class Grade {
    public long id;
    public long studentId;
    public long questionId;
    public String studentScore;

    //    public Grade(int studentScore) {
//        this.studentScore = studentScore;
//    }
    public Grade(){
        this.studentId = 0;
        this.questionId = 0;
        this.studentScore = "studentScore";
        this.id = 0;
    }

    public Grade(long studentId, long questionId, String studentScore){
        this.studentId = studentId;
        this.questionId = questionId;
        this.studentScore = studentScore;
        this.id = 0;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getStudentScore() {
        return studentScore;
    }

    public void setStudentScore(String studentScore) {
        this.studentScore = studentScore;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getID(){
        return id;
    }

//    public void setStudentScore(int studentId, int questionId, int studentScore) {
//        this.studentId = studentId;
//        this.questionId = questionId;
//        this.studentScore = studentScore;
}