package comp3111.examsystem.controller;

import comp3111.examsystem.service.JavaFXInitializer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherMainControllerTest {

    @Mock
    private FXMLLoader mockLoader;
    @Mock
    private Stage mockStage;
    @Mock
    private Scene mockScene;
    @Mock
    private VBox mockVBox;

    private TeacherMainController controller;

    @BeforeAll
    static void initToolkit() {
        JavaFXInitializer.initToolkit();

    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new TeacherMainController();
        controller.mainbox = mockVBox;
    }

    @Test
    void testInitialize() {
        controller.initialize(mock(URL.class), mock(ResourceBundle.class));
        // No assertions needed as initialize method does nothing
    }

    @Test
    void testOpenQuestionManageUI() throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                when(mockLoader.load()).thenReturn(mockVBox);
                doAnswer(invocation -> {
                    Stage stage = (Stage) invocation.getArguments()[0];
                    stage.setScene(mockScene);
                    stage.show();
                    return null;
                }).when(mockLoader).setController(any());

                controller.openQuestionManageUI();

                verify(mockLoader, times(1)).load();
                verify(mockStage, times(1)).setTitle("Question Bank Management");
                verify(mockStage, times(1)).setScene(mockScene);
                verify(mockStage, times(1)).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }

    @Test
    void testOpenExamManageUI() throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                when(mockLoader.load()).thenReturn(mockVBox);
                doAnswer(invocation -> {
                    Stage stage = (Stage) invocation.getArguments()[0];
                    stage.setScene(mockScene);
                    stage.show();
                    return null;
                }).when(mockLoader).setController(any());

                controller.openExamManageUI();

                verify(mockLoader, times(1)).load();
                verify(mockStage, times(1)).setTitle("Exam Management");
                verify(mockStage, times(1)).setScene(mockScene);
                verify(mockStage, times(1)).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }

    @Test
    void testOpenGradeStatistic() throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                when(mockLoader.load()).thenReturn(mockVBox);
                doAnswer(invocation -> {
                    Stage stage = (Stage) invocation.getArguments()[0];
                    stage.setScene(mockScene);
                    stage.show();
                    return null;
                }).when(mockLoader).setController(any());

                controller.openGradeStatistic();

                verify(mockLoader, times(1)).load();
                verify(mockStage, times(1)).setTitle("Grade Statistics");
                verify(mockStage, times(1)).setScene(mockScene);
                verify(mockStage, times(1)).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }
}