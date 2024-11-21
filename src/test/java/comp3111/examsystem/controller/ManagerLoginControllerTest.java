package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.controller.ManagerLoginController;
import comp3111.examsystem.model.Manager;
import comp3111.examsystem.service.Database;
import comp3111.examsystem.service.MsgSender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.assertions.api.Assertions.assertThat;

public class ManagerLoginControllerTest extends ApplicationTest {

    @InjectMocks
    private ManagerLoginController controller;

    @Mock
    private Database<Manager> mockDatabase;

    @Mock
    private MsgSender mockMsgSender;

    @Mock
    private FXMLLoader mockLoader;

    @Mock
    private Stage mockStage; // Mocked Stage for showing

    private static MockedStatic<MsgSender> mockedStatic;


    @BeforeAll
    public static void initJavaFx() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> { // Initializes JavaFX environment
            latch.countDown();
        });
        latch.await(); // Ensures JavaFX is up before proceeding
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller.usernameTxt = new TextField();
        controller.passwordTxt = new PasswordField();
        controller.fxmlLoader = mockLoader;
        controller.stage = mockStage;
    }

    @AfterAll
    public static void tearDownJavaFx() {

        Platform.exit(); // Cleans up the JavaFX platform after all tests are run.
    }


    @Test
    void testShowManagerUI() throws IOException, IOException, InterruptedException {
        // Arrange

        Platform.runLater(() -> {
            try {
                FXMLLoader mockLoader = mock(FXMLLoader.class);
                when(mockLoader.load()).thenReturn(new Pane());
                controller.fxmlLoader = mockLoader;
                Button event = mock(Button.class);
                Scene mockScene = mock(Scene.class);
                Stage mockStage = mock(Stage.class);
                controller.stage = mockStage;

                when(event.getScene()).thenReturn(mockScene); // Simulate a button event
                when(mockScene.getWindow()).thenReturn(mockStage); // Mock the window
                when(mockLoader.load()).thenReturn(new Pane()); // Simulate FXML loading
                controller.usernameTxt = new TextField("user");

                // Act
                controller.showManagerUI(event);
                verify(mockStage).setTitle(anyString());
                verify(mockStage).show();
                verify(mockStage).setScene(any(Scene.class));
                verify(mockStage, times(1)).close(); // Ensure original stage is closed


//                Platform.exit();
            }
            catch (IOException e) {
                fail("Should not throw an exception: " + e.getMessage());
            }
        });
        // Assert
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testLoginSuccess()
    {
        // first, initialise username and password
        controller.usernameTxt.setText("admin");
        controller.passwordTxt.setText("admin");

        // then, initialise the mock database
        when(mockDatabase.getAll()).thenReturn(List.of(new Manager()));

        // initialise e.getSource()
        Button button = mock(Button.class);

        Platform.runLater(()-> {
            try{
                mockMsgSender = mock(MsgSender.class);
                FXMLLoader mockLoader = mock(FXMLLoader.class);
                when(mockLoader.load()).thenReturn(new Pane());
                controller.fxmlLoader = mockLoader;
                Scene mockScene = mock(Scene.class);
                Stage mockStage = mock(Stage.class);
                controller.stage = mockStage;

                when(button.getScene()).thenReturn(mockScene); // Simulate a button event
                when(mockScene.getWindow()).thenReturn(mockStage); // Mock the window
                when(mockLoader.load()).thenReturn(new Pane()); // Simulate FXML loading

                ActionEvent event = mock(ActionEvent.class);
                when(event.getSource()).thenReturn(button);
                boolean result = controller.login(event);
                assertTrue(result);

                verify(mockStage).setTitle(anyString());
                verify(mockStage).show();
                verify(mockStage).setScene(any(Scene.class));
                verify(mockStage, times(1)).close();



            }catch(Exception e)
            {
                fail("Should not throw an exception: " + e.getMessage());
            }
    });
        // Wait for the Platform.runLater() to complete
        WaitForAsyncUtils.waitForFxEvents();


    }

    @Test
    public void testAdminCreation() {
        // Simulate an empty database
        when(mockDatabase.queryByField("username", "admin")).thenReturn(List.of());

        // Initialize controller
        controller.initialize(null, null);

        // Verify that an admin manager was added to the database
        verify(mockDatabase).add(any(Manager.class));
    }

    @Test
    public void testExistingAdmin()
    {
        // Simulate an existing admin in the database
        when(mockDatabase.queryByField("username", "admin")).thenReturn(List.of(new Manager()));

        Platform.runLater(() -> {
            // Initialize controller
            controller.initialize(null, null);

            // Verify that no new admin manager was added to the database
            verify(mockDatabase, never()).add(any(Manager.class));
        });

        // Wait for the Platform.runLater() to complete
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testCorrectHandleLogin()
    {
        // initialise mock database
        when(mockDatabase.getAll()).thenReturn(List.of(new Manager()));
        // set the username and password fields
        String username = "admin";
        String password = "admin";
        // call handleManagerLogin to validate the credentials
        boolean actual = controller.handleManagerLogin(username,password);
        boolean expected = true;

        assertEquals(actual,expected);

    }

    @Test
    public void testWrongHandleLogin()
    {
        // initialise mock database
        when(mockDatabase.getAll()).thenReturn(List.of(new Manager()));
        // set the username and password fields
        String username = "wrongadmin";
        String password = "wrongadmin";
        // call handleManagerLogin to validate the credentials
        boolean actual = controller.handleManagerLogin(username,password);
        boolean expected = false;

        assertEquals(actual,expected);

    }




}
