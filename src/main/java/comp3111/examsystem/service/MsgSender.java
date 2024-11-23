package comp3111.examsystem.service;

import comp3111.examsystem.model.Course;
import comp3111.examsystem.model.Quiz;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;

public class MsgSender {
    static public void showMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("Hint");
        alert.headerTextProperty().set(msg);
        alert.showAndWait();
    }

    static public void showConfirm(String title, String msg, Runnable callback) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(msg);
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            callback.run();
        }
    }

    static public void showUpdateConfirm(String title, List<String> changes, Runnable callback, Runnable cancel) {
        StringBuilder msgBuilder = new StringBuilder("Are you sure you want to make the following changes?\n");
        for (String change : changes) {
            msgBuilder.append(change).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(msgBuilder.toString());
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            callback.run();
        }
        else {
            cancel.run();
        }
    }

    static public void showDeleteConfirm(Course course, String title, List<Quiz> changes, Runnable callback) {
        StringBuilder msgBuilder = new StringBuilder("Are you sure you want to delete the course with Course ID: "+ course.getCourseID()+" and these quizzes?\n");
        for (Quiz change : changes) {
        msgBuilder.append(change.getQuizName()).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(msgBuilder.toString());
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            callback.run();
        }
    }
}
