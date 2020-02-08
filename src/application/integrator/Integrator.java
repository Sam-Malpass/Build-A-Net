/**
 * Integrator
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.integrator;

import application.Main;
import neuralNetwork.activationFunctions.ActivationFunction;
import neuralNetwork.components.layers.Layer;
import neuralNetwork.learningAlgorithms.LearningAlgorithm;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Integrator {

    /**
     * Function getInternalClasses()
     * <p>
     *     After being passed the internal package path, the function finds all the files in that package and lists them,
     *     returning the list of files
     * </p>
     * @param packagePath is the package path in the application
     * @return the list of files in that package
     */
    public static ArrayList<File> getInternalClasses(String packagePath)
    {
        // Declare the list
        ArrayList<File> files;
        // Create the ClassLoader object
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // Set the URL for the ClassLoader
        URL url = loader.getResource(packagePath);
        // Get the URL path
        String path = url.getPath();
        // Generate an array of Files
        File[] fileList = new File(path).listFiles();
        // Convert the array to an ArrayList because they are just better
        files = new ArrayList<>(Arrays.asList(fileList));
        // Return those files
        return files;
    }

    /**
     * Function createFunction()
     * <p>
     *     This function takes an internal path and a class name, then generates and returns an object of that class
     * </p>
     * @param path is the package path for the class
     * @param name is the name of the class
     * @return the instantiated object of the given class
     */
    public static ActivationFunction createFunction(String path, String name) {
        // Create a null object
        Object instance = null;
        // Attempt
        try {
            // Fix the path to a package name structure
            path = path.replace("/",".");
            // Remove .class from the name
            name = name.replace(".class", "");
            // Concatenate path with class name
            path = path + "." + name;
            // Generate a ClassLoader
            ClassLoader loader = Integrator.class.getClassLoader();
            // Create a Class object
            Class aClass = Class.forName(path);
            // Make an instantiation of that class
            instance = aClass.newInstance();
        }
        catch (Exception e) {
            Main.passMessage("There was an issue creating an object for <" + name + ">", "-e");
        }
        // Return object
        return (ActivationFunction) instance;
    }

    /**
     * Function createAlgorithm()
     * <p>
     *     This function takes an internal path and a class name, then generates and returns an object of that class
     * </p>
     * @param path is the package path for the class
     * @param name is the name of the class
     * @return the instantiated object of the given class
     */
    public static LearningAlgorithm createAlgorithm(String path, String name) {
        // Create a null object
        Object instance = null;
        // Attempt
        try {
            // Fix the path to a package name structure
            path = path.replace("/",".");
            // Remove .class from the name
            name = name.replace(".class", "");
            // Concatenate path with class name
            path = path + "." + name;
            // Generate a ClassLoader
            ClassLoader loader = Integrator.class.getClassLoader();
            // Create a Class object
            Class aClass = Class.forName(path);
            // Make an instantiation of that class
            instance = aClass.newInstance();
        }
        catch (Exception e) {
            Main.passMessage("There was an issue creating an object for <" + name + ">", "-e");
        }
        // Return object
        return (LearningAlgorithm) instance;
    }

    /**
     * Function createLayer()
     * <p>
     *     This function takes an internal path and a class name, then generates and returns an object of that class
     * </p>
     * @param path is the package path for the class
     * @param name is the name of the class
     * @return the instantiated object of the given class
     */
    public static Layer createLayer(String path, String name) {
        // Create a null object
        Object instance = null;
        // Attempt
        try {
            // Fix the path to a package name structure
            path = path.replace("/",".");
            // Remove .class from the name
            name = name.replace(".class", "");
            // Concatenate path with class name
            path = path + "." + name;
            // Generate a ClassLoader
            ClassLoader loader = Integrator.class.getClassLoader();
            // Create a Class object
            Class aClass = Class.forName(path);
            // Make an instantiation of that class
            instance = aClass.newInstance();
        }
        catch (Exception e) {
            Main.passMessage("There was an issue creating an object for <" + name + ">", "-e");
        }
        // Return object
        return (Layer) instance;
    }
}

