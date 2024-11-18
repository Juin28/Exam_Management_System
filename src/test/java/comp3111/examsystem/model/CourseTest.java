package comp3111.examsystem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void testDefaultConstructor() {
        Course course = new Course();
        assertEquals("courseName", course.getCourseName());
        assertEquals("courseID", course.getCourseID());
        assertEquals("department", course.getDepartment());
        assertEquals(0, course.id);
    }

    @Test
    void testParameterizedConstructor() {
        Course course = new Course("Software Engineering", "COMP3111", "CSE", 123);
        assertEquals("Software Engineering", course.getCourseName());
        assertEquals("COMP3111", course.getCourseID());
        assertEquals("CSE", course.getDepartment());
        assertEquals(123, course.id);
    }

    @Test
    void testSettersAndGetters() {
        Course course = new Course();
        course.setCourseName("Software Engineering");
        course.setCourseID("COMP3111");
        course.setDepartment("CSE");
        course.id = 123;

        assertEquals("Software Engineering", course.getCourseName());
        assertEquals("COMP3111", course.getCourseID());
        assertEquals("CSE", course.getDepartment());
        assertEquals(123, course.id);
    }
}