/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SimpleAnalyzer.java
 *
 * Created on 5. Februar 2004, 14:27
 */
package de.cismet.cids.admin.analyze;
import java.io.*;

import java.sql.*;

import java.util.*;

import de.cismet.cids.admin.analyze.analyzerUnit.*;
import de.cismet.cids.admin.analyze.castorGenerated.*;
/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class SimpleAnalyzer {

    //~ Instance fields --------------------------------------------------------

    AnalyzerInformation anInfo;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of SimpleAnalyzer.
     */
    public SimpleAnalyzer() {
        try {
            final InputStreamReader r = new InputStreamReader(getClass().getResourceAsStream(
                        "/de/cismet/cids/admin/analyze/analyzeInfo.xml"));
            anInfo = new AnalyzerInformation().unmarshal(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        if ((args == null) || (args.length != 2)) {
            System.out.println("Aufruf mit den Parametern Config-File und Ausgabeverzeichniss!!!");
            System.exit(-1);
        }

        try {
            final Properties props = new Properties();
            final String filename = args[0];
            final String dir = args[1];
            FileInputStream fStream = null;
            fStream = new FileInputStream(filename);
            props.load(fStream);
            fStream.close();

            Class.forName(props.getProperty("Driverclass"));
            final String url = props.getProperty("Url");
            final Connection conn = DriverManager.getConnection(
                    url,
                    props.getProperty("User"),
                    props.getProperty("Pass"));

            final SimpleAnalyzer sa = new SimpleAnalyzer();
            final MasterAnalyzer master = new MasterAnalyzer(conn, sa.anInfo);
            final AnalyseResult r = master.analyse();
            final FileWriter fw = new FileWriter(dir + "/analyzerOutput.html");
            fw.write(r.toHTMLString());
            fw.close();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
