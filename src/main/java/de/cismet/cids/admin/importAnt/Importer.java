/*
 * Importer.java
 *
 * Created on 15. September 2003, 17:23
 */
package de.cismet.cids.admin.importAnt;

import de.cismet.cids.admin.importAnt.castorGenerated.*;

import java.lang.reflect.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;

/** Hauptklasse des ganzen Paketes. Hier laufen alle F\u00E4den zusammen ;-)
 *
 * <br>
 * @author hell
 * @toDo Exceptions bereinigen
 */
public class Importer {

    /** Logger */
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private static final org.apache.log4j.Logger logS = org.apache.log4j.Logger.getLogger("de.cismet.cids.admin.tools.importAnt.Importer");
    /** CastorObjekt der ImportKonfigurationsDatei (XML) */
    private ImportRules impRules;
    /** Quellen Recordset */
    private ResultSet sourceRS;
    /** Quell-Datenbankverbindung */
    private Connection sourceConn;
    /** Ziel-Datenbankverbindung */
    private Connection targetConn;
    /** Metainformation die aus impRules erzeugt wurden */
    private ImportMetaInfo rulesMetaInfo;
    /** Datenstruktur in die die Zieltabellen gespeichert werden */
    private IntermedTablesContainer intermedTables;
    /** Datenstruktur in der die Einstellungen gespeichert sind die notwendig sind um die Anwendung zu starten*/
    private RuntimeProperties runtimeProps;
    /** Klassenname der dynamisch erzeugten Java-Klasse */
    private String assigningClass;
    /** String in dem der Initialisierungslog gespeichert wird*/
    private String initializeLog = "";
    private DynClass dynAssigner;

    /** Creates a new instance of Importer
     * in dieser Klasse wird das Import XML-File gelesen und danach der Import
     * durchgef\u00FChrt.
     * @param impRules CastorObjekt der ImportKonfigurationsDatei (XML)
     * @throws Exception wird geworfen
     */
    public Importer(RuntimeProperties runtimeProps, ImportRules impRules, javax.swing.JFrame parent) throws InitializingException {
        //super(parent,true);
        //initialisieren mit Properties und ImportRules...
        init(runtimeProps, impRules);
    }

    public Importer(RuntimeProperties runtimeProps, ImportRules impRules) throws InitializingException {
        init(runtimeProps, impRules);
    }

    private void init(RuntimeProperties runtimeProps, ImportRules impRules) throws InitializingException {

        initializeLog += "Initialisierung des Importer wird gestartet.\n";
        // Speichere die Runtime Properties
        this.runtimeProps = runtimeProps;

        // Erzeuge einen Filereader aus dem Namen des XML-Files
        try {

//            FileReader r=new FileReader(runtimeProps.getImportFileName());
//            initializeLog+="Import-Beschreibung wird ge\u00F6ffnet\n";
//
//            // aus dem XML File die entsprechende Datenstruktur machen (CASTOR)
            this.impRules = impRules; //ImportRules.unmarshal(r);
            initializeLog += "Import-Beschreibung ist syntaktisch korrekt\n";

            // connect to source db and get resultset
            log.info("Lese Importdatenquelle aus.");
            sourceRS = getSourceResultSet();
            initializeLog += "Verbindung zur Import-Datenquelle konnte aufgebaut werden\n";

            //create connection to target db system
            targetConn = getTargetDbConnection();
            initializeLog += "Verbindung zum Zielsystem konnte aufgebaut werden.\n";

            //analyse the import rules
            rulesMetaInfo = new ImportMetaInfo(impRules);
            initializeLog += "Import-MetaInformationen wurden angelegt\n";
            log.debug("Import-MetaInformationen wurden angelegt\n" + rulesMetaInfo.toString());
            //setzt die SourceFields in den MetaInformationen
            rulesMetaInfo.setSourceFields(sourceRS);

            //check target db system
            checkTargetDbSystem();
            initializeLog += "Zielsystem entspricht den Import-Anforderungen\n";


            //create intermed structure
            intermedTables = new IntermedTablesContainer(rulesMetaInfo, targetConn);
            initializeLog += "Interne Struktur aufgebaut \n";

            //erzeuge den Assigner
            compileAssigner();
            initializeLog += "Initialisierung abgeschlossen\n";


        } catch (ImportMetaInfoException miEx) {
            throw new InitializingException("Fehler beim Anlegen der Metainformationen! (" + miEx.getMessage() + ")", initializeLog, miEx);
        } catch (TargetTableProblemException ttEx) {
            throw new InitializingException("Zielsystem hat nicht die entsprechenden Strukturen um das Mapping durchzuf\u00FChren!", initializeLog, ttEx);
        } catch (WrongNameException wnEx) {
            throw new InitializingException("Fehler beim Aufbau einer internen Datenstruktur", initializeLog, wnEx);
        } catch (DynamicCompilingException dcEx) {
            throw new InitializingException("Fehler beim Erstellen der Mapping-Klasse.", initializeLog, dcEx);
        }
    }

    /** In dieser Methode wird der dynamiscxh erzeugte Java-Code compiliert.
     * @throws DynamicCompilingException wird geworfen, wenn ein Fehler beimkompilieren aufgetreten ist
     */
    private void compileAssigner() throws DynamicCompilingException {
        try {
            String dir = runtimeProps.getTmpDirectory();
            System.out.println("-------------------------------------------------" + dir);
            File file = File.createTempFile("TmpAssigner", ".java", new File(dir));
            String filename = file.getName();
            String classname = filename.substring(0, filename.length() - 5);

            dynAssigner = new DynClass(rulesMetaInfo.getPreProcessingAndMappingClass(classname), file, dir);
            assigningClass = classname;
            dynAssigner.compile(runtimeProps.isKeepAssigner());

        } catch (DynamicCompilingException ex) {
            throw ex;
        } catch (Exception ex) {
            log.fatal("Fehler", ex);
            throw new DynamicCompilingException("!internal Bug!" + ex.toString());
        }
    }

    public void runImport() throws CidsImportAntException {
        runImport(null);
    }

    /** Der eigentliche Import wird mit Hilfe der dynamischen Klasse durchgef\u00FChrt
     * @throws Exception bei Fehler
     */
    public void runImport(javax.swing.JProgressBar progress) throws CidsImportAntException {
        try {
            ResultSetMetaData md = sourceRS.getMetaData();
            log.debug("RulesMetaInfo\n" + this.rulesMetaInfo.toString());
            long start = System.currentTimeMillis();
            if (progress != null) {

                progress.setIndeterminate(true);
                ResultSet crs = this.getSourceResultSet();
                int counter = 0;
                while (crs.next()) {
                    ++counter;
                }
                progress.setIndeterminate(false);
                //progress.setString("0/"+progress.getMaximum());
                //progress.setStringPainted(true);
                progress.setMaximum(counter);
                progress.setValue(0);
            }

            Class assigner = dynAssigner.getTheClass();
            //log.debug(assigner);

            Method assignM = assigner.getMethod("assign", new Class[]{Connection.class, String[].class, UniversalContainer.class});
            int counter = 0;
            Connection tc = getTargetDbConnection();
            UniversalContainer universalContainer = new UniversalContainer();
            while (sourceRS.next()) {


                String[] init = new String[md.getColumnCount()];
                for (int i = 1; i <= md.getColumnCount(); ++i) {
                    init[i - 1] = sourceRS.getString(i);
                }
                //log.fatal("voher(universalContainer):"+universalContainer);
                Object[] args = new Object[]{tc, init, universalContainer};
                LinkedHashMap<String, String[]> result = (LinkedHashMap<String, String[]>) assignM.invoke(assigner, args);
                //log.fatal("voher(universalContainer):"+universalContainer);
                if (progress != null) {
                    progress.setValue(++counter);
                //progress.setString(counter+"/"+progress.getMaximum());
                }

                //Die aktuelle Zeile wird in die intermed Tables geschrieben
                //log.debug(result);
                intermedTables.addCurrentRows(result);
            }
            tc.close(); //todo: check
            if (sourceRS != null) {
                sourceRS.close();
            }
            if (sourceConn != null) {
                sourceConn.close();
                sourceConn = null;
            }
            long diff = System.currentTimeMillis() - start;
            log.debug(intermedTables);
            log.debug("Interne Datenstruktur fuellen (Zeit ein ms):" + diff);
        } catch (java.sql.SQLException sqlEx) {
            throw new CidsImportAntException("Fehler w\u00E4hrend des Imports!", sqlEx);
        } catch (java.lang.NoSuchMethodException nmEx) {
            throw new CidsImportAntException("Fehler w\u00E4hrend des Imports!", nmEx);
        } catch (java.lang.IllegalAccessException iaEx) {
            throw new CidsImportAntException("Fehler w\u00E4hrend des Imports!", iaEx);
        } catch (java.lang.reflect.InvocationTargetException reflEx) {
            throw new CidsImportAntException("Fehler w\u00E4hrend des Imports!", reflEx);
        }


    }

    /** Legt die JDBC Verbindung zur Datenquelle an
     * @throws Exception bei Fehler
     */
    public ResultSet getSourceResultSet() throws InitializingException {
        try {
            SourceJdbcConnectionInfo source = impRules.getConnectionInfo().getSourceJdbcConnectionInfo();
            Class.forName(source.getDriverClass());
            if (source.getPropCount() > 0) {

                Properties prop = getPropertiesFromXmlProps(source.enumerateProp());
                sourceConn = DriverManager.getConnection(source.getUrl(), prop);
            } else {
                sourceConn = DriverManager.getConnection(source.getUrl());
            }
            Statement stmnt = sourceConn.createStatement();

            return stmnt.executeQuery(source.getStatement());
        } catch (Exception e) {
            throw new InitializingException("Fehler beim Verbindungsaufbau zur Import-Datenquelle.", initializeLog, e);
        }
    }

    public Properties getPropertiesFromXmlProps(Enumeration enu) {
        Properties prop = new Properties();
        while (enu.hasMoreElements()) {
            Prop xmlProp = (Prop) enu.nextElement();
            prop.put(xmlProp.getKey(), xmlProp.getContent());
        }
        return prop;
    }

    /** Legt die Verbindung zur Zieldatenbank an
     * @throws Exception bei Fehler
     */
    public Connection getTargetDbConnection() throws InitializingException {
        try {
            TargetJdbcConnectionInfo target = impRules.getConnectionInfo().getTargetJdbcConnectionInfo();
            Class.forName(target.getDriverClass());
            Connection conn;
            if (target.getPropCount() > 0) {
                conn = DriverManager.getConnection(target.getUrl(),
                        getPropertiesFromXmlProps(target.enumerateProp()));
            } else {
                conn = DriverManager.getConnection(target.getUrl());
            }
            return conn;
        } catch (Exception e) {
            throw new InitializingException("Fehler beim Verbindungsaufbau zur Import-Datenquelle.", initializeLog, e);
        }
    }

    /** F\u00FChrt auf der Zieldatenbank Kontroll-Statements aus, um zu \u00FCberpr\u00FCfen ob die
     * Zieltabellen auch vorhanden sind,
     * @throws Exception bei Fehler
     */
    public void checkTargetDbSystem() throws TargetTableProblemException {
        String table = "";
        String xxss = "";
        try {
            Statement stmnt = targetConn.createStatement();
            Iterator ite = rulesMetaInfo.getTargetTableNames();
            while (ite.hasNext()) {
                table = (String) ite.next();
                xxss = rulesMetaInfo.getControlStatement(table);
                log.debug("checkTargetDbSystem:Query:" + xxss);
                stmnt.setMaxRows(1);
                stmnt.executeQuery(xxss);
            }
        } catch (Throwable e) {
            log.debug("Fehler beim Statement:" + xxss, e);

            throw new TargetTableProblemException(table);
        }
    }

    /** Getter for property intermedTables.
     * @return Value of property intermedTables.
     *
     */
    public de.cismet.cids.admin.importAnt.IntermedTablesContainer getIntermedTables() {
        return intermedTables;
    }

    /** Methode zum Starten des ganzen Krams
     * @param args \u00DCbergabeparameter
     */
//    public static void main (String[] args) {
//        try {
//            RuntimeProperties rProp=new RuntimeProperties();
//            rProp.setFinalizerClass("JDBCImportExecutor");
//            rProp.setImportFileName("C:\\importTest.xml");
//            rProp.setTmpDirectory("C:\\temp\\");
//
//
//            Importer imp=new Importer(rProp);
//
//            imp.runImport();
//
////            Properties outputProps=new Properties();
////            outputProps.setProperty("OutputDirectory", "C:\\temp\\");
////            outputProps.setProperty("FilePrefix", "ImportSession4711");
////            outputProps.setProperty("Delimiter", "|");
////            outputProps.setProperty("Seperator",",");
////            ImportFinalizerer finalizer=new ImportFinalizer("SapDBImportFilesGenerator",
////                                                        imp.getIntermedTables(),
////                                                        outputProps);
////            finalizer.finalize();
//
//            Properties outputProps=new Properties();
//            outputProps.setProperty("Rollback", "true");
//            ImportFinalizer finalizer=new ImportFinalizer("JDBCImportExecutor",
//                                                        imp.getIntermedTables(),
//                                                        outputProps);
//            finalizer.finalise();
//      }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    /** Getter for property initializeLog.
     * @return Value of property initializeLog.
     *
     */
    public java.lang.String getInitializeLog() {
        return initializeLog;
    }
}
