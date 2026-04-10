/*
 * Runtime.java
 *
 * Created on 3. August 2009, 00:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author Martin Pröhl alias MythGraphics
 * @version 1.1.0
 *
 */

public class Runtime {
    
    private Runtime() {}
    
    public static String getCurrentJarName() {
        String[] classPath = System.getProperty("java.class.path").split(java.io.File.pathSeparator);
        String jarName = classPath[0];
        System.out.println("Classpath: " + jarName);
        if ( jarName.toLowerCase().endsWith(".jar") )
            return jarName;
        else
            return null;
    }
    
}
