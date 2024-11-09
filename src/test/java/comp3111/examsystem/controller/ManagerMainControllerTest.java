package comp3111.examsystem.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerMainControllerTest {

    @Test
    void testEvenNumber() {
        ManagerMainController managerMainController = new ManagerMainController();
        // test for even number
        boolean expectedResult = true;
        boolean actual = managerMainController.testEvenNumber(2);
        assertEquals(expectedResult, actual);


    }

    @Test
    void testOddNumber()
    {
        ManagerMainController managerMainController = new ManagerMainController();
        // test for odd number
        boolean expectedResult2 = false;
        boolean actual2 = managerMainController.testEvenNumber(3);
        assertEquals(expectedResult2, actual2);
    }
}