/*
 *
 */

package util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/*
 *
 * @author Martin Pröhl alias MythGraphics
 * @version 2.0.0
 *
 */

public class Math {

    private Math() {}

    public static boolean isEven(int a) {
        return ( ((a/2)*2) == a );
    }

    public static double round(double x, int digits) {
        return BigDecimal.valueOf(x).round(
            new MathContext( digits, RoundingMode.HALF_UP )
        ).doubleValue();
    }

    public static double roundStatistic(double x, int digits) {
        return BigDecimal.valueOf(x).round(
            new MathContext( digits, RoundingMode.HALF_EVEN )
        ).doubleValue();
    }

}
