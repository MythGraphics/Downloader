/*
 * Net.java
 *
 * Created on 28. Februar 2010, 03:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import java.net.URL;

public class Net {
    
    private Net() {}
    
    public static String getFileName(URL url) {
        String s = url.toString();
        int i = s.lastIndexOf('/');
        return s.substring(i+1);
    }
    
}
