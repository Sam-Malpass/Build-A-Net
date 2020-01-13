import graphicalUserInterface.MessageBus;
import graphicalUserInterface.controllers.ApplicationWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * windowHeight holds the height of the window
     */
    private double windowHeight = 720;

    /**
     * windowWidth holds the width of the window
     */
    private double windowWidth = 1280;

    /**
     * mainStage holds the Stage for the application
     */
    private Stage mainStage;

    /**
     * mainScene holds the Scene for the application
     */
    private Scene mainScene;

    private static MessageBus messageInterface;

    /**
     * Function setup()
     * <p>
     * Sets up the actual window
     * </p>
     */
    private void setup() {
        mainStage.setTitle("Build-A-Net");
        mainStage.setScene(this.mainScene);
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/graphicalUserInterface/fxml/ApplicationWindow.fxml"));
            mainScene = new Scene(root, windowWidth, windowHeight);
            messageInterface = ApplicationWindowController.getMessageBus();
            messageInterface.write("Application loaded successfully!");
        } catch (Exception e) {
        }
        setup();
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void passMessage(String line) {
        messageInterface.write(line);
    }
    public static void passMessage(String line, String mode) {
        messageInterface.write(line, mode);
    }
}
