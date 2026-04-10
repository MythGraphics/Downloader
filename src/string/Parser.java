/*
 *
 */

package string;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 3.1.0
 *
 */

import java.util.StringTokenizer;
import static util.NumberFormat.getNumber;

/*
 * Single Parser<br />
 * löst exakt ein Klammerpaar ([…]) auf
 */
public class Parser implements ParserInterface {

    public final static char SEPARATOR_OPEN  = '[';
    public final static char SEPARATOR_CLOSE = ']';
    public final static String SEPARATORS    = String.valueOf(SEPARATOR_OPEN) +
                                               String.valueOf(SEPARATOR_CLOSE);

    private String part1;
    private String part2 = "";
    /*
     * first  --> Startnummer
     * last   --> Endnummer
     * counts --> Anzahl der nummerischen Stellen
     * pos    --> Position --> aktueller Wert des Zählers --> Counter
     */
    private int first = 0, last = 0, counts, pos = 0;

    public Parser(String source) {
        /*
         *  String[] array = source.split(SEPARATORS);
         *  int length = array.length;
         */

        if ( !isParsable(source) ) {                                                                // EinzelDatei
            part1   = source;
            return;
        }

        StringTokenizer t = new StringTokenizer( source, SEPARATORS, true );
        part1 = t.nextToken();
        t.nextToken();                                                                              // '[' übergehen
        StringTokenizer t2 = new StringTokenizer( t.nextToken(), "-:", false );
        first = Integer.parseInt( t2.nextToken() );
        last  = Integer.parseInt( t2.nextToken() );
        if ( t2.hasMoreTokens() ) {                                                                 // initiallisiert "counts"
            counts = Integer.parseInt( t2.nextToken() );
        } else {
            counts = 1;
        }
        t.nextToken();                                                                              // ']' übergehen
        while ( t.hasMoreTokens() ) {
            part2 += t.nextToken();
        }
        pos = first;                                                                                // initiallisiert "pos"
    }

    @Override
    public boolean hasMoreStrings() {
        return ( pos <= last );
    }

    @Override
    public int getCounter() {
        return pos;
    }

    public int getCounts() {
        return counts;
    }

    public int getFirst() {
        return first;
    }

    @Override
    public int length() {
        return last + 1;
    }

    public static boolean isParsable(String s) {
        return ((( s.indexOf(SEPARATOR_OPEN) +1 ) * ( s.indexOf(SEPARATOR_CLOSE) +1 )) != 0 );      // also beide vorhanden, denn (-1+1)*(-1+1)=0
    }

    @Override
    public String nextString() {
        String s = part1 + getNumber(pos, counts) + part2;
        if ( s.endsWith( "0" )) {                                                                   // EinzelDatei --> "0" entfernen
            s = s.substring( 0, s.length()-1 );
        }
        ++pos;
        return s;
    }

    public static MultiParser resolve(String s) {
        MultiParser multi = new MultiParser();
        resolve( s, multi );
        return multi;
    }

    private static void resolve(String s, MultiParser multi) {
        Parser parser = new Parser( s );
        String str = null;
        if ( s.indexOf( Parser.SEPARATOR_OPEN ) != s.lastIndexOf( Parser.SEPARATOR_OPEN )) {
            while ( parser.hasMoreStrings() ) {
                str = parser.nextString();
                resolve( str, multi );                                                              // Rekursion
            }
        }
        if (( str != null ) && ( s.indexOf( Parser.SEPARATOR_OPEN ) == -1 )) {
            multi.add( str );
        }
        while ( parser.hasMoreStrings() ) {
            multi.add( parser.nextString() );
        }
    }

    /*
     * DEBUG
     */
    public static void resolveTest(String s) {
        Parser parser = new Parser( s );
        String str = null;
        if ( s.indexOf( Parser.SEPARATOR_OPEN ) != s.lastIndexOf( Parser.SEPARATOR_OPEN )) {
            while ( parser.hasMoreStrings() ) {
                str = parser.nextString();
                resolveTest( str );                                                                 // Rekursion
            }
        }
        if (( str != null ) && ( s.indexOf( Parser.SEPARATOR_OPEN ) == -1 )) {
            System.out.println( str );
        }
        while ( parser.hasMoreStrings() ) {
            System.out.println( parser.nextString() );
        }
    }

}
