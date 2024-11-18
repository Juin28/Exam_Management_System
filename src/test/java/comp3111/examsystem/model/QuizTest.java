package comp3111.examsystem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuizTest {

    @Test
    void testDefaultConstructor() {
        Quiz quiz = new Quiz();
        assertEquals(0, quiz.getId());
        assertEquals("quizName", quiz.getQuizName());
        assertEquals("60", quiz.getQuizTime());
        assertEquals("courseID", quiz.getCourseID());
        assertEquals("no", quiz.getPublishStatus());
        assertEquals("", quiz.getQuestionIDs());
    }

    @Test
    void testParameterizedConstructor() {
        Quiz quiz = new Quiz("Final Exam", "120", "COMP3111", "no", 1, "Q1|Q2|");
        assertEquals(1, quiz.getId());
        assertEquals("Final Exam", quiz.getQuizName());
        assertEquals("120", quiz.getQuizTime());
        assertEquals("COMP3111", quiz.getCourseID());
        assertEquals("no", quiz.getPublishStatus());
        assertEquals("Q1|Q2|", quiz.getQuestionIDs());
    }

    @Test
    void testSettersAndGetters() {
        Quiz quiz = new Quiz();
        quiz.setQuizName("Midterm");
        quiz.setQuizTime("90");
        quiz.setCourseID("COMP3111");
        quiz.setPublishStatus("yes");
        quiz.setQuestionIDs("Q1|Q2|Q3|");
        quiz.publish();
        quiz.id = 2;

        assertEquals(2, quiz.getId());
        assertEquals("Midterm", quiz.getQuizName());
        assertEquals("90", quiz.getQuizTime());
        assertEquals("COMP3111", quiz.getCourseID());
        assertEquals("yes", quiz.getPublishStatus());
        assertEquals("Q1|Q2|Q3|", quiz.getQuestionIDs());
    }

    @Test
    void testAddQuestion() {
        Quiz quiz = new Quiz();
        quiz.addQuestion("Q1");
        quiz.addQuestion("Q2");

        assertEquals("Q1|Q2|", quiz.getQuestionIDs());
    }

    @Test
    void testPublish() {
        Quiz quiz = new Quiz();
        quiz.publish();

        assertEquals("yes", quiz.getPublishStatus());
    }
}