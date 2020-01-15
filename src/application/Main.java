package application; /**
 * application.Main
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */

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

    /**
     * messageInterface holds the MessageBus that allows strings to be passed to the console on the application thread
     */
    private static MessageBus messageInterface;

    /**
     * Function setup()
     * <p>
     * Sets up the actual window
     * </p>
     */
    private void setup() {
        // Set the title of the window
        mainStage.setTitle("Build-A-Net");
        // Set the scene of the window
        mainStage.setScene(this.mainScene);
    }

    /**
     * Function start()
     * <p>
     *     Sets the mainStage and then loads the given scene. Also handles the setup of the MessageBus
     * </p>
     * @param stage is the stage for the application
     * @throws Exception can arise when fxml file is missing
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Set the mainStage
        mainStage = stage;
        // Attempt
        try {
            // Load the FXML
            Parent root = FXMLLoader.load(getClass().getResource("/graphicalUserInterface/fxml/ApplicationWindow.fxml"));
            // Set the scene to the loaded FXML
            mainScene = new Scene(root, windowWidth, windowHeight);
            // Setup the MessageBus
            messageInterface = ApplicationWindowController.getMessageBus();
            // Output the message - also tests the MessageBus
            messageInterface.write("Application loaded successfully!");
        // Catch
        } catch (Exception e) {
            // Error handling occurs here
        }
        // Run the setup function
        setup();
        // Disables resizing the window
        mainStage.setResizable(false);
        // Show the window
        mainStage.show();
    }

    /**
     * Function passMessage()
     * <p>
     *     Allows messages to be output to the console through the main application thread
     * </p>
     * @param line is the message to output
     */
    public static void passMessage(String line) {
        // Send a message through the MessageBus
        messageInterface.write(line);
    }

    /**
     * Function passMessage()
     * <p>
     *     Overloaded variant of the default passMessage function. Allows for specific label to be attached to message
     * </p>
     * @param line is the message to send
     * @param mode is the label to attach
     */
    public static void passMessage(String line, String mode) {
        // Send a message through the MessageBus
        messageInterface.write(line, mode);
    }
}
