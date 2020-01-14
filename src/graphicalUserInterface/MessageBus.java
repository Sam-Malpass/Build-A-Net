/**
 * MessageBus
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface;

import graphicalUserInterface.controllers.ApplicationWindowController;

public class MessageBus {

    /**
     * window holds the instantiated ApplicationWindowController object
     */
    static ApplicationWindowController window;

    /**
     * Constructor with argument(s)
     * <p>
     *     Takes an ApplicationWindowController to create the object
     * </p>
     * @param a is an ApplicationWindowController object
     */
    public MessageBus(ApplicationWindowController a) {
        // Set the window
        window = a;
    }

    /**
     * Function write()
     * <p>
     *     Takes a string and passes it to the console in the ApplicationWindowController
     * </p>
     * @param line is the message to pass
     */
    public void write(String line) {
        // Pass the message
        window.write(line);
    }

    /**
     * Function write()
     * <p>
     *     Overloaded function to take and pass the mode as well as the message to the console in ApplicationWindowController
     * </p>
     * @param line is the message to send
     * @param mode is the label to append
     */
    public void write(String line, String mode) {
        // Pass the message and mode
        window.write(line, mode);
    }
}
