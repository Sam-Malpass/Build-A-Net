package application.fileHandler;

import application.Main;
import neuralNetwork.Network;

import java.io.*;

public class FileHandler {

    public FileHandler() { }

    public void saveNetwork(Network object) {
        try {
            // Saving of object in a file
            FileOutputStream file = new FileOutputStream(object.getName());
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(object);

            out.close();
            file.close();
        }

        catch (IOException e) {
            Main.passMessage("Problem saving network", "-e");
        }
    }

    public Network loadNetwork(String filePath) {
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            Network net = (Network) in.readObject();

            in.close();
            file.close();

            return net;
        }
        catch (Exception e) {
            Main.passMessage("Problem loading network", "-e");
        }
        return null;
    }
}
