/**
 * Generator
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.generator;

import application.wrappers.IntegerWrapper;

import java.util.Random;
import java.util.UUID;

public class Generator {

    /**
     * randomiser is the random object that the generator will use
     */
     private static Random randomiser = new Random(0);
     private static IntegerWrapper seed = new IntegerWrapper(0);

     public static int genInt() {
         return randomiser.nextInt();
     }

     public static int genInt(int bound) {
         return randomiser.nextInt(bound);
     }
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

    /**
     * Function genUUID()
     * <p>
     *     Generates a random UUID
     * </p>
     * @return a random UUID
     */
    public static String genUUID() {
        return UUID.randomUUID().toString();
    }

    public static void setSeed(int newSeed) {
        seed.value = newSeed;
    }

    public static int getSeed() {
        return seed.value;
    }

    public static IntegerWrapper getWrapper() {
        return seed;
    }
}
