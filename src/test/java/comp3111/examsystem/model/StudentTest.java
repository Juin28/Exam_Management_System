package comp3111.examsystem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testDefaultConstructor() {
        Student student = new Student();
        assertEquals("username", student.getUsername());
        assertEquals("name", student.getName());
        assertEquals("gender", student.getGender());
        assertEquals("age", student.getAge());
        assertEquals("department", student.getDepartment());
        assertEquals("password", student.getPassword());
        assertEquals(0, student.getId());
    }

    @Test
    void testParameterizedConstructor() {
        Student student = new Student("john_doe", "John Doe", "Male", "21", "CSE", "securepassword", "95", 1);
        assertEquals("john_doe", student.getUsername());
        assertEquals("John Doe", student.getName());
        assertEquals("Male", student.getGender());
        assertEquals("21", student.getAge());
        assertEquals("CSE", student.getDepartment());
        assertEquals("securepassword", student.getPassword());
        assertEquals(1, student.getId());
    }
}