/**
 * Command
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.commands;

public abstract class Command {

    /**
     * dataStore is a storage field for data that may need to be kept by the command
     */
    protected Object dataStore;

    /**
     * Function executeCommand()
     * <p>
     *     Will allow the execution of a command
     * </p>
     */
    public abstract void executeCommand();

    /**
     * Function unExecuteCommand()
     * <p>
     *     Will allow the undoing of the command
     * </p>
     */
    public abstract void unExecuteCommand();

}
