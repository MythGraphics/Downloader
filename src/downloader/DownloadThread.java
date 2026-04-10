/*
 * DownloadThread.java
 *
 */

package downloader;

/*
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.2.1
 *
 */

import io.IO;
import java.io.File;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import string.Parser;
import string.ParserInterface;
import util.NumberFormat;

class DownloadThread extends Thread {

    public final static boolean ON  = true;
    public final static boolean OFF = false;

    public boolean DEBUG = false;

    private final MainFrame mainframe;

    private ParserInterface parser;
    private int counter = 0;
    private boolean state = OFF;

    DownloadThread(MainFrame mainframe) {
        this.mainframe = mainframe;
    }

    @Override
    public void run() {
        // initiallisieren
        String sourcestring = mainframe.source1.getText();
        String folder = mainframe.target1.getText();
        state = ON;

        // Parameter prüfen
        if ( !folder.endsWith( File.separator )) {
            folder += File.separator;
        }
        if ( IO.mkPath( new File(folder) )) {
            mainframe.printMessage( "Verzeichnisstruktur OK." );
        } else {
            mainframe.printMessage( "Verzeichnisstruktur unverwendbar." );
            postProcessing();
            return;
        }

        // Parser initiallisieren
        parser = Parser.resolve( sourcestring );

        // Quelle protokollieren
        mainframe.printMessage( "protokolliere Quelle ..." );
        File sourcelog = new File( folder + "source.txt" );
        if ( !sourcelog.exists() ) {
            try { sourcelog.createNewFile(); }
            catch (IOException e) { mainframe.handleException(e); }
        }
        if ( io.IO.writeToFile( sourcestring, sourcelog, true )) {
            mainframe.printMessage( "... OK." );
        } else {
            mainframe.printMessage( "Schreiben in " + sourcelog.getName() + " fehlgeschlagen." );
        }

        // DEBUG
        if (DEBUG) { while ( parser.hasMoreStrings() ) { mainframe.printMessage( parser.nextString() ); }}

        // Datei-Download
        mainframe.printMessage( "starte Download ..." );
        URL url;                                                                                    // source
        File file = null;                                                                           // target
        while ( parser.hasMoreStrings() ) {
            sourcestring = parser.nextString();
            if ( super.isInterrupted() ) {
                mainframe.printMessage( "Download abgebrochen." );
                postProcessing();
                return;
            }
            try {
                url = new URL(sourcestring);
                file = getFile( folder + util.Net.getFileName(url) );
                if ( file.createNewFile() ) {
                    mainframe.printMessage(
                            "Datei \"" + file.getAbsolutePath() + "\" erstellt."
                    );
                } else if ( file.exists() ) {                                                       // sollte niemals erreicht werden
                    mainframe.printMessage( "Datei existiert; überspringe ..." );
                    continue;
                }
                download(url, file);
            }
            catch (MalformedURLException e) { mainframe.handleException(e); }
            catch (IOException e) { mainframe.handleException(e); }

            // Aufräumen: 0Byte-Dateien löschen, etc.
            if ( (file != null) && (file.length() == 0) && file.isFile() ) {
                if ( file.delete() ) {
                    mainframe.printMessage(
                        "0Byte-Datei \"" +
                        file.getAbsolutePath() +
                        "\" gelöscht."
                    );
                } else {
                    mainframe.printMessage(
                        "0Byte-Datei \"" + file.getAbsolutePath() + "\" nicht löschbar."
                    );
                }
            }
        }
        mainframe.printMessage( "Download abgeschlossen." );
        postProcessing();
    }

    private File getFile(String str) {
        File file = new File(str);
        if ( !file.exists() ) {
            return file;
        }
        String path = file.getParent() + File.separator;
        String name = file.getName();
        do {
            File newFile = new File( path + NumberFormat.getNumber(counter, 3) + "_" + name );
            file = newFile;
            if ( file.exists() ) {
                ++counter;
            }
        } while ( file.exists() );
        return file;
    }

    private void download(URL source, File target) throws IOException {
        int nbytes;
        byte[] buffer = new byte[MainFrame.BUFFERSIZE];
        BufferedInputStream in = new BufferedInputStream( source.openStream() );
        mainframe.printMessage( "lese Daten von " + source.toString() + " ..." );
        BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream(target) );
        while (( nbytes = in.read( buffer )) != - 1) {
            out.write( buffer, 0, nbytes );
        }
        out.flush();
        mainframe.printMessage( "\"" + target.getName() + "\" vollständig geschrieben." );
        in.close();
        out.close();
    }

    private void postProcessing() {
        state = OFF;
        mainframe.printMessage(
            mainframe.getActiveDownloads() + " weitere Downloads verbleibend ..." + "\n" +
            "\n"
        );
    }

    public boolean isActiveState() {
        return state;
    }

}
