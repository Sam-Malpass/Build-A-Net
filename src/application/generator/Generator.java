/**
 * Generator
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.generator;

import java.util.Random;

public class Generator {

    /**
     * randomiser is the random object that the generator will use
     */
     private static Random randomiser = new Random();

    /**
     * Function genLong()
     * <p>
     *     Returns a random long
     * </p>
     * @return random long
     */
    public static long genLong() {
        return randomiser.nextLong();
    }

    /**
     * Function genDouble()
     * <p>
     *     Returns a random double
     * </p>
     * @return random double
     */
    public static double genDouble() {
        return randomiser.nextDouble();
    }
}
