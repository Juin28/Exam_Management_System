package comp3111.examsystem.service;

import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;

public class JavaFXInitializer {
    private static volatile boolean initialized = false;

    public static void initToolkit() {
        if (!initialized) {
            synchronized (JavaFXInitializer.class) {
                if (!initialized) {
                    final CountDownLatch latch = new CountDownLatch(1);
                    Platform.startup(latch::countDown);
                    try {
                        latch.await(); // Wait for JavaFX to initialize
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("JavaFX initialization interrupted", e);
                    }
                    initialized = true;
                }
            }
        }
    }

    public static void exitJavaFXPlatform() {
        if (initialized) {
            Platform.exit();
        }
    }
}
