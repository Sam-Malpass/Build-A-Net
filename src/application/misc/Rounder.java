/**
 * Rounder
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.misc;

import application.Main;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Rounder {
    public static double round(double val, int places) {

        if (places < 0) {
            Main.passMessage("Error rounding number", "-e");
            throw new IllegalArgumentException();
        }

        BigDecimal tmp = new BigDecimal(Double.toString(val));
        tmp = tmp.setScale(places, RoundingMode.HALF_UP);
        return tmp.doubleValue();
    }
}
