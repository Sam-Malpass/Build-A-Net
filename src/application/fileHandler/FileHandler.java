/**
 * FileHandler
 * @version 0.0.1
 * @since 0.0.1
 */
package application.fileHandler;

import application.Main;
import javafx.scene.image.Image;
import neuralNetwork.Network;
import java.io.*;

public class FileHandler {

    /**
     * Constructor with no arguments
     * <p>
     *     Sets up the object
     * </p>
     */
    public FileHandler() { }

    /**
     * Function saveNetwork()
     * <p>
     *     Takes a network object and saves it
     * </p>
     * @param object is the network to save
     */
    public void saveNetwork(Network object) {
        // Try
        try {
            // Create FileOutputStream
            FileOutputStream file = new FileOutputStream(object.getName());
            // Create ObjectOutputStream, the underlying stream is above
            ObjectOutputStream out = new ObjectOutputStream(file);
            // Write the object
            out.writeObject(object);
            // Close the streams
            out.close();
            file.close();
        }
        // If error occurs
        catch (IOException e) {
            // Output error message
            Main.passMessage("Problem saving network", "-e");
        }
    }

    /**
     * Function loadNetwork()
     * <p>
     *     Takes a filePath and loads a network object from that location
     * </p>
     * @param filePath is the path to the file
     * @return the network object
     */
    public Network loadNetwork(String filePath) {
        // Try
        try {
            // Create the underlying input stream
            FileInputStream file = new FileInputStream(filePath);
            // Create the ObjectInputStream
            ObjectInputStream in = new ObjectInputStream(file);
            // Read in the object
            Network net = (Network) in.readObject();
            // Close the streams
            in.close();
            file.close();
            // Return the network
            return net;
        }
        // If error occurs
        catch (Exception e) {
            // Output error message
            Main.passMessage("Problem loading network", "-e");
        }
        // If error does occur, function will return null
        return null;
    }

    public Image loadIcon() {
        Image icon = null;
        try {
            /*Open a file*/
            File test = new File("src/resources/icon.png");
            /*Make a file input stream*/
            FileInputStream input = new FileInputStream(test);
            icon = new Image(input);
        }
        catch (Exception e) {
            Main.passMessage("Issue loading the application icon", "-e");

        }
        return icon;
    }
}
