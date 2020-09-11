/**
 * DoubleWrapper
 * @author Sam Malpass
 * @verion 0.0.1
 * @since 0.0.1
 */
package application.wrappers;

/*
    This is a helper class, for performing the commands that involve double primitives. Since Java does not use pointers for primitives
    a wrapper is required. This means that when the command operates on an double it is actually operating on this object. This is because
    the command will have been given a point to the object
 */
public class DoubleWrapper {

    /**
     * value holds the Double in the wrapper
     */
    public Double value;

    /**
     * Constructor with arguments
     * <p>
     *     Sets the value to the passed Double
     * </p>
     * @param value is the double to use
     */
    public DoubleWrapper(Double value) {
        // Set the value
        this.value = value;
    }

    /**
     * Function toString()
     * <p>
     *     Returns the String of the value
     * </p>
     * @return the string of the value
     */
    @Override
    public String toString() {
        // Return String of the value
        return String.valueOf(value);
    }
}
