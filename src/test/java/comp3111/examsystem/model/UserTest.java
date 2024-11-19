package comp3111.examsystem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructor() {
        User user = new User("john_doe", "John Doe", "Male", "25", "CSE", "password123");
        assertEquals("john_doe", user.getUsername());
        assertEquals("John Doe", user.getName());
        assertEquals("Male", user.getGender());
        assertEquals("25", user.getAge());
        assertEquals("CSE", user.getDepartment());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User("john_doe", "John Doe", "Male", "25", "CSE", "password123");
        user.setUsername("jane_doe");
        user.setName("Jane Doe");
        user.setGender("Female");
        user.setAge("30");
        user.setDepartment("Math");
        user.setPassword("newpassword");

        assertEquals("jane_doe", user.getUsername());
        assertEquals("Jane Doe", user.getName());
        assertEquals("Female", user.getGender());
        assertEquals("30", user.getAge());
        assertEquals("Math", user.getDepartment());
        assertEquals("newpassword", user.getPassword());
    }
}