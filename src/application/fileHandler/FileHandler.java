/**
 * FileHandler
 * @version 0.0.1
 * @since 0.0.1
 */
package application.fileHandler;

import application.Main;
import data.UserSpecified;
import javafx.scene.image.Image;
import neuralNetwork.Network;
import java.io.*;
import java.util.ArrayList;

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

    /**
     * Function loadIcon()
     * <p>
     *     Loads and returns the icon of the application from the resource directory
     *     in the JAR file itself
     * </p>
     * @return the loaded icon as an image
     */
    public Image loadIcon() {
        // Declare the icon
        Image icon = null;
        try {
            /*Open a file*/
            File test = new File("src/resources/icon.png");
            /*Make a file input stream*/
            FileInputStream input = new FileInputStream(test);
            icon = new Image(input);
        }
        // If an error occurs
        catch (Exception e) {
            // Output an error message
            Main.passMessage("Issue loading the application icon", "-e");

        }
        // Return the icon
        return icon;
    }

    /**
     * Function loadData()
     * <P>
     *     Takes a file path and loads the data from the given file into an ArrayList of strings.
     *     Works line by line.
     * </P>
     * @param filePath is the file location/path
     * @return the list of lines within the file
     */
    public ArrayList<String> loadData(String filePath) {
        // Declare the ArrayList
        ArrayList<String> data = new ArrayList<>();
        // Attempt
        try {
            // Create the file object
            File file = new File(filePath);
            // Create the file reader object
            FileReader fileReader = new FileReader(file);
            // Create the buffered reader object
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            // Read a line
            String line = bufferedReader.readLine();
            // While not EOF
            while(line != null) {
                // Add the line to the list
                data.add(line);
                // Read the next line
                line = bufferedReader.readLine();
            }
        }
        // Catch errors
        catch (Exception e) {
            Main.passMessage("Problem reading data file", "-e");
        }
        // Return the data list
        return data;
    }
}
