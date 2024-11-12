//package comp3111.examsystem.controller;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class StudentLoginControllerTest {
//    @Test
//    void testStudentRegister() {
//        StudentLoginController loginController = new StudentLoginController();
//        String studentUsername = "jt123";
//        String studentPassword = "abc123";
//        boolean expected = true;
//        boolean actual = loginController.register(studentUsername, studentPassword);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void testCheckValidUsername() {
//        StudentLoginController loginController = new StudentLoginController();
//        String studentUsername = "jt123";
//        boolean expected = true;
//        boolean actual = loginController.isValidUsername(studentUsername);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void testStudentRegisterEmptyUsername() {
//        StudentLoginController loginController = new StudentLoginController();
//        String studentUsername = "";
//        String studentPassword = "abc123";
//        boolean expected = false;
//        boolean actual = loginController.register(studentUsername, studentPassword);
//        assertEquals(expected, actual);
//    }
//
//    // Username length longer than 10 is invalid
//    @Test
//    void testStudentRegisterLongUsername() {
//        StudentLoginController loginController = new StudentLoginController();
//        String studentUsername = "jt123456789";
//        String studentPassword = "abc123";
//        boolean expected = false;
//        boolean actual = loginController.register(studentUsername, studentPassword);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void testCheckValidPassword() {
//        StudentLoginController loginController = new StudentLoginController();
//        String studentPassword = "abc123";
//        boolean expected = true;
//        boolean actual = loginController.isValidPassword(studentPassword);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void testStudentRegisterEmptyPassword() {
//        StudentLoginController loginController = new StudentLoginController();
//        String studentUsername = "jt123";
//        String studentPassword = "";
//        boolean expected = false;
//        boolean actual = loginController.register(studentUsername, studentPassword);
//        assertEquals(expected, actual);
//    }
//
//    // Password length shorter than 6 is invalid
//    @Test
//    void testStudentRegisterShortPassword() {
//        StudentLoginController loginController = new StudentLoginController();
//        String studentUsername = "jt123";
//        String studentPassword = "abc";
//        boolean expected = false;
//        boolean actual = loginController.register(studentUsername, studentPassword);
//        assertEquals(expected, actual);
//    }
//}
//
