package comp3111.examsystem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void testDefaultConstructor() {
        Teacher teacher = new Teacher();
        assertEquals("username", teacher.getUsername());
        assertEquals("name", teacher.getName());
        assertEquals("male", teacher.getGender());
        assertEquals("20", teacher.getAge());
        assertEquals("department", teacher.getDepartment());
        assertEquals("password", teacher.getPassword());
        assertEquals("position", teacher.getPosition());
        assertEquals(0, teacher.getId());
    }

    @Test
    void testParameterizedConstructor() {
        Teacher teacher = new Teacher("jane_doe", "Jane Doe", "Female", "30", "Math", "securepassword", "Professor", 1);
        assertEquals("jane_doe", teacher.getUsername());
        assertEquals("Jane Doe", teacher.getName());
        assertEquals("Female", teacher.getGender());
        assertEquals("30", teacher.getAge());
        assertEquals("Math", teacher.getDepartment());
        assertEquals("securepassword", teacher.getPassword());
        assertEquals("Professor", teacher.getPosition());
        assertEquals(1, teacher.getId());
    }

    @Test
    void testSettersAndGetters() {
        Teacher teacher = new Teacher();
        teacher.setPosition("Associate Professor");
        teacher.id = 2;

        assertEquals("Associate Professor", teacher.getPosition());
        assertEquals(2, teacher.getId());
    }
}