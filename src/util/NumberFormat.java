/*
 *
 */

package util;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 * @see java.text.NumberFormat
 *
 */

public class NumberFormat {

    private NumberFormat() {}

    public static java.text.NumberFormat getFormatter() {
        return java.text.NumberFormat.getNumberInstance();
    }

    public static String formatDecimal(String str) {
        return str.replace( ',', '.' );
    }

    public static String format1000(String str, char c) {
        StringBuilder sb = new StringBuilder( str );
        int index = sb.indexOf(".");
        if ( index == -1 )
            index = sb.length()-1;
        while ( index > 3 ) {
            index -= 3;
            sb.insert(index, c);
        }
        return sb.toString();

    }

    public static String getNumber(long number, int digits) {
        return getNumber( new StringBuffer( String.valueOf( number )), digits );
    }

    public static String getNumber(String number, int digits) {
        return getNumber( new StringBuffer( number ), digits );
    }

    public static String getDecimalNumber(String number, int digits) {
        return getDecimalNumber( new StringBuffer( number ), digits );
    }

    public static String getDecimalNumber(double number, int digits) {
        String str = String.valueOf( Math.round( number, digits+1 ));
        return getDecimalNumber( new StringBuffer( str ), digits );
    }

    public static String getNumber(StringBuffer sb, int digits) {
        while ( sb.length() < digits ) {
            sb.insert( 0, 0 );
        }
        return sb.toString();
    }

    public static String getDecimalNumber(StringBuffer sb, int digits) {
        String exp = null;
        int  eindex   = sb.indexOf("E");
        if ( eindex != -1 ) {
            exp = sb.substring( eindex+1, sb.length() );
        }

        int index = sb.indexOf(".");
        if ( index == -1 ) {
            sb.append('.');
            index = sb.indexOf(".");
        }

        if ( index+digits < sb.length() ) {
            sb.delete( index+digits+1, sb.length() );
            return addExp( sb, exp );
        }

        for ( int i = index+1; i <= index+digits; ++i ) {
            if ( i >= sb.length() ) {
                sb.append(0);
            }
        }
        return addExp( sb, exp );
    }

    private static String addExp(StringBuffer number, String exp) {
        if ( exp != null ) {
            number.append("×10^");
            number.append(exp);
        }
        return number.toString();
    }

}
