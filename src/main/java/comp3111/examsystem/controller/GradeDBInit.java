package comp3111.examsystem.controller;

import comp3111.examsystem.model.Grade;
import comp3111.examsystem.model.Quiz;
import comp3111.examsystem.service.Database;

public class GradeDBInit {
    private Database<Grade> gradeDatabase;
    private Database<Quiz> quizDatabase;

    public GradeDBInit() {
        this.gradeDatabase = new Database<>(Grade.class);
        this.quizDatabase = new Database<>(Quiz.class);
    }

//    public void insertDummyValues() {
//        Grade grade1 = new Grade("1731479984127", "1731752251729", "85");
//        Grade grade2 = new Grade("1731489346821", "1731752687030", "90");
//        Grade grade3 = new Grade("1731479984127", "1731752645727", "75");
//        Grade grade4 = new Grade("1731736707949", "1731752251729", "80");
//
//        gradeDatabase.add(grade1);
//        gradeDatabase.add(grade2);
//        gradeDatabase.add(grade3);
//        gradeDatabase.add(grade4);
//    }
    // insert more dummy values into the quiz database from a variety of courses
    public void insertDummyQuiz()
    {
        Quiz quiz1 = new Quiz("Midterm", "60", "COMP2222", "Yes", 0, "1731747368046|1731748385459");
        Quiz quiz2 = new Quiz("Final", "120", "COMP3333", "Yes", 0, "1731747368046|1731748385459");
        Quiz quiz3 = new Quiz("Quiz 1", "30", "COMP4444", "Yes", 0, "1731747368046|1731748385459");
        Quiz quiz4 = new Quiz("Quiz 2", "30", "COMP5555", "Yes", 0, "1731747368046|1731748385459");

        quizDatabase.add(quiz1);
        quizDatabase.add(quiz2);
        quizDatabase.add(quiz3);
        quizDatabase.add(quiz4);
    }


    // delete all values from the db and insert a variety of new values
    public void insertDummyValues() {
        gradeDatabase.getAll();
        for (Grade grade : gradeDatabase.getAll()) {
            gradeDatabase.delByKey(String.valueOf(grade.getID()));
        }
        Grade grade1 = new Grade("1731479984127", "1731752251729", "40", "10");
        Grade grade2 = new Grade("1731489346821", "1731752687030", "30", "20");
        Grade grade3 = new Grade("1731479984127", "1731752645727", "25", "30");
        Grade grade4 = new Grade("1731736707949", "1731752251729", "15", "40");
        Grade grade5 = new Grade("1731479984127", "1732105054112", "20", "10");
        Grade grade6 = new Grade("1731489346821", "1732105054112", "30", "20");
        Grade grade7 = new Grade("1731479984127", "1732105054123", "40", "30");
        Grade grade8 = new Grade("1731736707949", "1732105054128", "50", "40");


        gradeDatabase.add(grade1);
        gradeDatabase.add(grade2);
        gradeDatabase.add(grade3);
        gradeDatabase.add(grade4);
        gradeDatabase.add(grade5);
        gradeDatabase.add(grade6);
        gradeDatabase.add(grade7);
        gradeDatabase.add(grade8);
    }

    public static void main(String[] args) {
        GradeDBInit initializer = new GradeDBInit();
//        initializer.insertDummyQuiz();
        initializer.insertDummyValues();
        System.out.println("Dummy values inserted into the grade database.");
    }
}