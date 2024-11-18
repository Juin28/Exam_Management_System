package comp3111.examsystem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeTest {

    @Test
    void testDefaultConstructor() {
        Grade grade = new Grade();
        assertEquals("studentId", grade.getStudentId());
        assertEquals("questionId", grade.getQuestionId());
        assertEquals("studentScore", grade.getStudentScore());
        assertEquals(0, grade.id);
    }

    @Test
    void testParameterizedConstructor() {
        Grade grade = new Grade("12345", "67890", "95");
        assertEquals("12345", grade.getStudentId());
        assertEquals("67890", grade.getQuestionId());
        assertEquals("95", grade.getStudentScore());
        assertEquals(0, grade.id);
    }

    @Test
    void testSettersAndGetters() {
        Grade grade = new Grade();
        grade.setStudentId("12345");
        grade.setQuestionId("67890");
        grade.setStudentScore("95");
        grade.id = 1;

        assertEquals("12345", grade.getStudentId());
        assertEquals("67890", grade.getQuestionId());
        assertEquals("95", grade.getStudentScore());
        assertEquals(1, grade.id);
    }
}