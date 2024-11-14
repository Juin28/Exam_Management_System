package comp3111.examsystem.controller;

import static org.junit.jupiter.api.Assertions.*;
import comp3111.examsystem.controller.ManagerLoginController;
import org.junit.jupiter.api.Test;

class ManagerLoginControllerTest {
    @Test
    void testWrongLoginCredentials()
    {
        // this function tests if invalid credentials will be accepted (i.e. any input apart from "admin" and "admin")
        ManagerLoginController managerLoginController = new ManagerLoginController();
        String wrongUsername = "notAdmin";
        String wrongPassword = "notAdmin";

        // call the handleManagerLogin function, check if "False" is returned
        boolean result = managerLoginController.handleManagerLogin(wrongUsername, wrongPassword);
        assertFalse(result);
    }

    @Test
    void testInputTypeLoginCredentials()
    {
        // this function tests if the function can handle wrong input types (i.e. numbers)
        ManagerLoginController managerLoginController = new ManagerLoginController();
        String numUsername = "1";
        String numPassword = "*";

        // call the handleManagerLogin function, check if "False" is returned
        boolean result = managerLoginController.handleManagerLogin(numUsername, numPassword);
        assertFalse(result);
    }

    @Test
    void testEmptyInputLoginCredentials()
    {
        // this function tests if the function can handle empty inputs
        ManagerLoginController managerLoginController = new ManagerLoginController();
        String emptyUsername = "";
        String emptyPassword = "";

        // call the handleManagerLogin function, check if "False" is returned
        boolean result = managerLoginController.handleManagerLogin(emptyUsername, emptyPassword);
        assertFalse(result);
    }

    @Test
    void testCorrectLoginCredentials()
    {
        // this function tests if the function can handle correct inputs
        ManagerLoginController managerLoginController = new ManagerLoginController();
        String correctUsername = "admin";
        String correctPassword = "admin";

        // call the handleManagerLogin function, check if "True" is returned
        boolean result = managerLoginController.handleManagerLogin(correctUsername, correctPassword);
        assertTrue(result);
    }
}