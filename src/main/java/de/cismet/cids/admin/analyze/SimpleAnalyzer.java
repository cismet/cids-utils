/*
 * SimpleAnalyzer.java
 *
 * Created on 5. Februar 2004, 14:27
 */

package de.cismet.cids.admin.analyze;
import java.io.*;
import de.cismet.cids.admin.analyze.castorGenerated.*;
import de.cismet.cids.admin.analyze.analyzerUnit.*;
import java.util.*;
import java.sql.*;
/**
 *
 * @author  hell
 */
public class SimpleAnalyzer {
    AnalyzerInformation anInfo;
    /** Creates a new instance of SimpleAnalyzer */
    public SimpleAnalyzer() {
        try {
            InputStreamReader r=new InputStreamReader(getClass().getResourceAsStream("/de/cismet/cids/admin/analyze/analyzeInfo.xml"));
            anInfo=new AnalyzerInformation().unmarshal(r);
        }
        catch (Exception e) {
            
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args==null||args.length!=2) {
            System.out.println("Aufruf mit den Parametern Config-File und Ausgabeverzeichniss!!!");
            System.exit(-1);
        }
        
        try {
            Properties props=new Properties();
            String filename=args[0];
            String dir=args[1];
            FileInputStream fStream = null;
            fStream = new FileInputStream(filename);
            props.load(fStream); 
            fStream.close();
            
            Class.forName(props.getProperty("Driverclass"));
            String url = props.getProperty("Url");
            Connection conn = DriverManager.getConnection( url, props.getProperty("User"), props.getProperty("Pass"));

            SimpleAnalyzer sa=new SimpleAnalyzer();
            MasterAnalyzer master=new MasterAnalyzer(conn, sa.anInfo);
            AnalyseResult r=master.analyse();
            FileWriter fw = new FileWriter( dir+"/analyzerOutput.html" );
            fw.write(r.toHTMLString());
            fw.close();
            System.exit(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
