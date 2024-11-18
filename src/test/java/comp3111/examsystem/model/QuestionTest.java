package comp3111.examsystem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testDefaultConstructor() {
        Question question = new Question();
        assertEquals("questionDescription", question.getQuestionDescription());
        assertEquals("optionA", question.getOptionA());
        assertEquals("optionB", question.getOptionB());
        assertEquals("optionC", question.getOptionC());
        assertEquals("optionD", question.getOptionD());
        assertEquals("A", question.getAnswer());
        assertEquals("10", question.getQuestionScore());
        assertEquals("Single", question.getQuestionType());
        assertEquals(0, question.getId());
    }

    @Test
    void testParameterizedConstructor() {
        Question question = new Question("What is Java?", "A programming language", "A coffee", "An island", "A car", "A", "5", "Multiple", 1);
        assertEquals("What is Java?", question.getQuestionDescription());
        assertEquals("A programming language", question.getOptionA());
        assertEquals("A coffee", question.getOptionB());
        assertEquals("An island", question.getOptionC());
        assertEquals("A car", question.getOptionD());
        assertEquals("A", question.getAnswer());
        assertEquals("5", question.getQuestionScore());
        assertEquals("Multiple", question.getQuestionType());
        assertEquals(1, question.getId());
    }

    @Test
    void testSettersAndGetters() {
        Question question = new Question();
        question.setQuestionDescription("What is Java?");
        question.setOptionA("A programming language");
        question.setOptionB("A coffee");
        question.setOptionC("An island");
        question.setOptionD("A car");
        question.setAnswer("A");
        question.setQuestionScore("5");
        question.setQuestionType("Multiple");
        question.id = 1;

        assertEquals("What is Java?", question.getQuestionDescription());
        assertEquals("A programming language", question.getOptionA());
        assertEquals("A coffee", question.getOptionB());
        assertEquals("An island", question.getOptionC());
        assertEquals("A car", question.getOptionD());
        assertEquals("A", question.getAnswer());
        assertEquals("5", question.getQuestionScore());
        assertEquals("Multiple", question.getQuestionType());
        assertEquals(1, question.getId());
    }
}