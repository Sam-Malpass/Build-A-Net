/**
 * IntegerWrapper
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.wrappers;

/*
    This is a helper class, for performing the commands that involve integer primitives. Since Java does not use pointers for primitives
    a wrapper is required. This means that when the command operates on an integer it is actually operating on this object. This is because
    the command will have been given a point to the object
 */
public class IntegerWrapper {

    /**
     * value is the actual integer value
     */
    public Integer value;

    /**
     * Constructor with arguments
     * <p>
     *     Sets the value to the passed integer
     * </p>
     * @param value is the integer to use
     */
    public IntegerWrapper(Integer value) {
        // Set the value
        this.value = value;
    }

    /**
     * Function toString()
     * <p>
     *     Returns the string of the value
     * </p>
     * @return
     */
    @Override
    public String toString() {
        // Return the String of the value
        return String.valueOf(value);
    }
}
