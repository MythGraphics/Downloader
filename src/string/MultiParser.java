/*
 *
 */

package string;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.util.ArrayList;

public class MultiParser implements ParserInterface {

    private final ArrayList<String> list;
    private int counter = 0;

    public MultiParser() {
        list = new ArrayList<>(100);
    }

    public void add(String s) {
        list.add( s );
    }

    @Override
    public boolean hasMoreStrings() {
        return ( counter < length() );
    }

    @Override
    public String nextString() {
        String s = list.get( counter );
        ++counter;
        return s;
    }

    @Override
    public int getCounter() {
        return counter;
    }

    @Override
    public int length() {
        return list.size();
    }

    public static MultiParser resolve(String s) {
        return Parser.resolve( s );
    }

}
