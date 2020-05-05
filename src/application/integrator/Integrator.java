/**
 * Integrator
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.integrator;

import application.Main;
import data.preprocessors.Preprocessor;
import neuralNetwork.activationFunctions.ActivationFunction;
import neuralNetwork.components.layers.Layer;
import neuralNetwork.learningAlgorithms.LearningAlgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

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
    public static ArrayList<File> getInternalClasses(String packagePath) {
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
     * Function createPreprocessor()
     * <p>
     *     Takes the internal path and a class name, then generates and returns an object of that class
     * </p>
     * @param path is the package path for the class
     * @param name is the name of the class
     * @return the instantiated object of the given class
     */
    public static Preprocessor createPreprocessor(String path, String name) {
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
        return (Preprocessor) instance;
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

    /**
     * Function loadFunctions()
     * <p>
     *     Iterates over all files in the functions folder and then creates objects of each class in those JARs.
     *     If the object is then an instance of the ActivationFunction then it is added to a list. This list it then
     *     returned to the prior function
     * </p>
     * @return a list of ActivationFunctions
     * @throws IOException is and input/output error
     * @throws ClassNotFoundException is an error where the class is not found when loading
     * @throws IllegalAccessException is an issue with accessing the file(s)
     * @throws InstantiationException is where the class could not be instantiated
     */
    public static ArrayList<Object> loadFunctions() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // Create a list for the functions
        ArrayList<Object> functions = new ArrayList<>();
        // Generate an array of Files
        File[] fileList = new File("plugins/functions").listFiles();
        // Convert the array to an ArrayList because they are just better
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(fileList));
        // For all files in the folder
        for(File f : files) {
            // Check the file is a JAR
            if(f.getName().endsWith(".jar")) {
                // Get the names of the JAR
                String jarName = f.getAbsolutePath();
                // Setup a stream
                JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
                // Declare the entry
                JarEntry jarEntry;
                // Repeat until broken
                while (true) {
                    // Get the next entry in the JAR
                    jarEntry = jarFile.getNextJarEntry();
                    // If it is null
                    if (jarEntry == null) {
                        // Break
                        break;
                    }
                    // If the name ends with .class
                    if (jarEntry.getName().endsWith(".class")) {
                        // Setup the URL
                        URL jarPath = f.toURI().toURL();
                        // Get the jarURL
                        String jarURL = "jar:" + jarPath + "!/";
                        // Add it to a list of URLs
                        URL[] urls = {new URL(jarURL)};
                        // Get the URLClassLoader setup
                        URLClassLoader child = new URLClassLoader(urls);
                        // Get the class
                        Class load = Class.forName(jarEntry.getName().replace(".class", ""), true, child);
                        // Create an object
                        Object instance = load.newInstance();
                        // If it is an instance of ActivationFunction
                        if (instance instanceof ActivationFunction) {
                            // Add it to the list of functions
                            functions.add(instance);
                        }
                    }
                }
            }
        }
        // Return the list of functions
        return functions;
    }

    /**
     * Function loadAlgorithms()
     * <p>
     *     Iterates over all files in the algorithms folder and then creates objects of each class in those JARs.
     *     If the object is then an instance of the Algorithm then it is added to a list. This list it then
     *     returned to the prior function
     * </p>
     * @return a list of Algorithms
     * @throws IOException is and input/output error
     * @throws ClassNotFoundException is an error where the class is not found when loading
     * @throws IllegalAccessException is an issue with accessing the file(s)
     * @throws InstantiationException is where the class could not be instantiated
     */
    public static ArrayList<Object> loadAlgorithms() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // Create a list for the functions
        ArrayList<Object> functions = new ArrayList<>();
        // Generate an array of Files
        File[] fileList = new File("plugins/algorithms").listFiles();
        // Convert the array to an ArrayList because they are just better
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(fileList));
        // For all files in the folder
        for(File f : files) {
            // Check the file is a JAR
            if(f.getName().endsWith(".jar")) {
                // Get the names of the JAR
                String jarName = f.getAbsolutePath();
                // Setup a stream
                JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
                // Declare the entry
                JarEntry jarEntry;
                // Repeat until broken
                while (true) {
                    // Get the next entry in the JAR
                    jarEntry = jarFile.getNextJarEntry();
                    // If it is null
                    if (jarEntry == null) {
                        // Break
                        break;
                    }
                    // If the name ends with .class
                    if (jarEntry.getName().endsWith(".class")) {
                        // Setup the URL
                        URL jarPath = f.toURI().toURL();
                        // Get the jarURL
                        String jarURL = "jar:" + jarPath + "!/";
                        // Add it to a list of URLs
                        URL[] urls = {new URL(jarURL)};
                        // Get the URLClassLoader setup
                        URLClassLoader child = new URLClassLoader(urls);
                        // Get the class
                        Class load = Class.forName(jarEntry.getName().replace(".class", ""), true, child);
                        // Create an object
                        Object instance = load.newInstance();
                        // If it is an instance of ActivationFunction
                        if (instance instanceof LearningAlgorithm) {
                            // Add it to the list of functions
                            functions.add(instance);
                        }
                    }
                }
            }
        }
        // Return the list of functions
        return functions;
    }

    /**
     * Function loadPreprocessors()
     * <p>
     *     Iterates over all files in the preprocessors folder and then creates objects of each class in those JARs.
     *     If the object is then an instance of the Preprocessor then it is added to a list. This list is then returned
     *     to the prior function.
     * </p>
     * @return a list of Preprocessors
     * @throws IOException is an input/output error
     * @throws ClassNotFoundException is an error where the class is not fount when loading
     * @throws IllegalAccessException is an issue with accessing the file(s)
     * @throws InstantiationException is where the class could not be instantiated
     */
    public static ArrayList<Object> loadPreprocessors() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // Create a list for the functions
        ArrayList<Object> functions = new ArrayList<>();
        // Generate an array of Files
        File[] fileList = new File("plugins/preprocessors").listFiles();
        // Convert the array to an ArrayList because they are just better
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(fileList));
        // For all files in the folder
        for(File f : files) {
            // Check the file is a JAR
            if(f.getName().endsWith(".jar")) {
                // Get the names of the JAR
                String jarName = f.getAbsolutePath();
                // Setup a stream
                JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
                // Declare the entry
                JarEntry jarEntry;
                // Repeat until broken
                while (true) {
                    // Get the next entry in the JAR
                    jarEntry = jarFile.getNextJarEntry();
                    // If it is null
                    if (jarEntry == null) {
                        // Break
                        break;
                    }
                    // If the name ends with .class
                    if (jarEntry.getName().endsWith(".class")) {
                        // Setup the URL
                        URL jarPath = f.toURI().toURL();
                        // Get the jarURL
                        String jarURL = "jar:" + jarPath + "!/";
                        // Add it to a list of URLs
                        URL[] urls = {new URL(jarURL)};
                        // Get the URLClassLoader setup
                        URLClassLoader child = new URLClassLoader(urls);
                        // Get the class
                        Class load = Class.forName(jarEntry.getName().replace(".class", ""), true, child);
                        // Create an object
                        Object instance = load.newInstance();
                        // If it is an instance of ActivationFunction
                        if (instance instanceof Preprocessor) {
                            // Add it to the list of functions
                            functions.add(instance);
                        }
                    }
                }
            }
        }
        // Return the list of functions
        return functions;
    }
}

