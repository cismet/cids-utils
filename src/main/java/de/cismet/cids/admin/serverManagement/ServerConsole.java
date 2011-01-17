/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ServerConsole.java
 * $Revision: 1.1.1.1 $
 * Created on 26. Februar 2004, 14:42
 */
package de.cismet.cids.admin.serverManagement;

import Sirius.server.*;
import Sirius.server.newuser.User;
import Sirius.server.newuser.UserGroup;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBluer;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import java.io.*;

import java.lang.reflect.*;

import java.util.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;

import de.cismet.cids.admin.serverManagement.servlet.ServerLogFile;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/* MiniatureServer Code an neue MiniServerVersion angepasst.
 * Variable miniatureServerInstance wurde entfernt.
 *
 */

/**
 * The class ServerConsole starts a cids server specified by a class parameter and creates a console to manage the
 * server. The console shows logging and runtime information, if needed a help file and provides functions to shutdown
 * and reset the server. There is also the possibility to call the class FileEditor to edit the cids server's
 * configuration file.
 *
 * @author   hell, oaltpeter, jfischer
 * @version  $Revision$, $Date$
 */
public class ServerConsole extends javax.swing.JFrame { // implements
                                                        // SysTrayMenuListener{

    //~ Static fields/initializers ---------------------------------------------

    /** Number of milliseconds for 1 second. */
    public static final long MILLISECONDS_SECOND = 1000;
    /** Number of milliseconds for 1 minute. */
    public static final long MILLISECONDS_MINUTE = 60000;
    /** Number of milliseconds for 1 hour. */
    public static final long MILLISECONDS_HOUR = 3600000;
    /** Number of milliseconds for 1 day. */
    public static final long MILLISECONDS_DAY = 86400000;
    /** Number of milliseconds for 1 week. */
    public static final long MILLISECONDS_WEEK = 604800000;

    private static final int START_HELP_FILE = 1;
    private static final int MAIN_HELP_FILE = 2;

    private static final String DEFAULT_MINIATURE_SERVER_PORT = "82";

    private static ServerConsole instance;

    private static String serverManagementRoot = null;
    private static File cidsServerConfigFile = null;

    private static boolean startMiniatureServer = true;

    static Logger logger;

    //~ Instance fields --------------------------------------------------------

    SimpleAttributeSet STANDARD = new SimpleAttributeSet();
    SimpleAttributeSet INFO = new SimpleAttributeSet();
    SimpleAttributeSet ERROR = new SimpleAttributeSet();

    FileEditor fileEditor = null;

    /** Creates new form ServerConsole. */
    private boolean serverRunnin = false;
    private String[] serverArgs = null;
    private String serverClassName = null;
    private Object serverInstance = null;
    private Method getServerInstance = null;
    private Method mainMethod = null;
    private Class serverClass = null;

    private ImageIcon red = new javax.swing.ImageIcon(getClass().getResource(
                "/de/cismet/cids/admin/serverConsole/buttons/redled.png"));
    private ImageIcon yellow = new javax.swing.ImageIcon(getClass().getResource(
                "/de/cismet/cids/admin/serverConsole/buttons/yellowled.png"));
    private ImageIcon green = new javax.swing.ImageIcon(getClass().getResource(
                "/de/cismet/cids/admin/serverConsole/buttons/greenled.png"));

    private GuiStream gsOut = null;
    private GuiStream gsError = null;

    private long serverStartTime = -1;
    private long lastErrorMessage = -1;
//    private SysTrayMenuIcon stIcon=null;
//    private SysTrayMenu stMenu=null;

    // TODO Entfernen
    // private String[] miniatureServerArgs = null;

    private HashMap miniatureServerArgs = null;

    private String serverType = null;
    private String log4jConfig = "default";
    private String serverLogFileName;
    private String miniatureServerPort = null;
    private String miniatureServerConfig = "default";
//    private Serve minuatureServerInstance = null;

    private SimpleWebServer minuatureServerInstance = null;

    private File logOutputDirectory;
    private File workpath = null;

//    public void iconLeftClicked(snoozesoft.systray4j.SysTrayMenuEvent sysTrayMenuEvent) {
//    }
//
//
//
//    public void iconLeftDoubleClicked(snoozesoft.systray4j.SysTrayMenuEvent sysTrayMenuEvent) {
//
//        this.show();
//
//    }
//
//
//
//    public void menuItemSelected(snoozesoft.systray4j.SysTrayMenuEvent sysTrayMenuEvent) {
//    }
//

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdDeleteLogs;
    private javax.swing.JButton cmdHelp;
    private javax.swing.JButton cmdInfo;
    private javax.swing.JButton cmdQueue;
    private javax.swing.JButton cmdRestart;
    private javax.swing.JButton cmdSaveLogs;
    private javax.swing.JButton cmdShutdown;
    private javax.swing.JButton cmdTray;
    private javax.swing.JButton cmdedit;
    private de.cismet.cids.tools.gui.farnsworth.GradientJPanel gplTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAmpel;
    private javax.swing.JLabel lblExportTitle;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitleControl;
    private de.cismet.cids.tools.gui.farnsworth.GradientJPanel pnlControl;
    private javax.swing.JPanel pnlControlAndInfo;
    private javax.swing.JPanel pnlInformation;
    private javax.swing.JPanel pnlLogging;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlStatus;
    private javax.swing.JPanel pnlTable;
    private de.cismet.cids.tools.gui.farnsworth.GradientJPanel pnlTitleExport;
    private javax.swing.JProgressBar prbStatus;
    private javax.swing.JScrollPane spnTable;
    private javax.swing.JTable tblInfo;
    private javax.swing.JTextPane txtLog;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ServerConsole.
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    private ServerConsole(final String[] args) {
        if (instance != null) {
            throw new RuntimeException("Es existiert bereits eine ServerConsole-Instanz!");
        }

        instance = this;

        initComponents();

        StyleConstants.setForeground(
            INFO,
            (java.awt.Color)javax.swing.UIManager.getDefaults().get("CheckBoxMenuItem.selectionBackground"));
        StyleConstants.setForeground(ERROR, Color.red);
        StyleConstants.setForeground(STANDARD, Color.black);

        /*try {
         *  lblIcon.setIcon(new
         * javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/32/"+args[3]+".png")));
         * this.setIconImage(new
         * javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/16/"+args[3]+".png")).getImage());
         * } catch (Throwable e) { lblIcon.setIcon(new
         * javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/32/default.png")));
         * this.setIconImage(new
         * javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/32/default.png")).getImage());}*/

        // new streams to redirect the standard and error output:
        gsOut = new GuiStream(System.out, txtLog, INFO, false);
        gsError = new GuiStream(System.err, txtLog, ERROR, true);
        // redirect output to the new streams:
        System.setErr(gsError);
        System.setOut(gsOut);

        // -t "cids Registry" -n Sirius.server.registry.Registry -l
        // D:\cvs_work\cids\src\de\cismet\cids\admin\serverManagement\log4j.properties -p 80 -s
        // D:\cvs_work\cids\dist\server -c D:\cvs_work\cids\dist\server\config\miniServer.cfg -a
        // D:\cvs_work\cids\dist\server\TestConfigDatei.cfg -t "cids Broker" -n
        // Sirius.server.middleware.impls.proxy.StartProxy -l
        // D:\cvs_work\cids\src\de\cismet\cids\admin\serverManagement\log4j.properties -p 81 -s
        // D:\cvs_work\cids\dist\server -c D:\cvs_work\cids\dist\server\config\miniServer.cfg -a
        // D:\cvs_work\cids\dist\server\config\broker.cfg

        /*
         * Read in the command line parameters into a Hashmap (switch, parameter)
         *
         * command line parameters: parameter    :  -t serverType   -n serverClassName   -l log4jConfig   -p
         * miniatureServerPort   -s serverManagementRoot  -c miniatureServerConfig   -a cidsServerArgs1 cidsServerArgs2
         * ... referred to  :  cidsServ        cidsServ             ServerCon        MiniServ                 MiniServ
         * and ServerCon   MiniServ                   cidsServ needed by    :  ServerCon       ServerCon ServerCon
         *  MiniServ                 MiniServ and ServerCon   MiniServ                   cidsServ
         *                      optional         optional                 optional  optional                   optional
         *
         * conditions: - parameter with switch -t must be the first, parameter with switch -n must be the second -
         * parameter with switch -a must be the last - if a configuration file of the cids server is specified, it must
         * be the first parameter behind the switch -a
         *
         * hints: - if the switch -p is not specified, the Miniature Server won't be started - the path specifications
         * log4jConfig and miniatureServerConfig can be absolute or relative
         */

        final int argl = args.length;
        int argn;
        int control = 1;

        final Map parameter = new HashMap();

        final Properties runtimeProperties = new Properties();
        try {
            runtimeProperties.load(new FileInputStream("runtime.properties"));
            System.out.println("runtime.properties gefunden");
            parameter.put("serverType", runtimeProperties.getProperty("serverTitle"));
            parameter.put("serverClassName", runtimeProperties.getProperty("serverClass"));
            parameter.put("log4jConfig", runtimeProperties.getProperty("log4jConfig"));
            parameter.put("miniatureServerPort", runtimeProperties.getProperty("webserverPort"));
            parameter.put("serverManagementRoot", runtimeProperties.getProperty("managementRoot"));
            parameter.put("miniatureServerConfig", runtimeProperties.getProperty("webserverInterfaceConfig"));
            serverArgs = runtimeProperties.getProperty("runtimeArgs").split(" ");
            serverLogFileName = runtimeProperties.getProperty("log4j.appender.ErrorHtml.file");
            parameter.put("cidsServerArgs", serverArgs);
        } catch (IOException skip) {
            skip.printStackTrace();
        }

        if (args.length != 0) {
            for (argn = 0; (argn < argl) && (args[argn].charAt(0) == '-');) {
                if (args[argn].equals("-t") && ((argn + 1) < argl)) {
                    ++argn;
                    parameter.put("serverType", args[argn]);
                    control += 2;
                } else if (args[argn].equals("-n") && ((argn + 1) < argl)) {
                    ++argn;
                    parameter.put("serverClassName", args[argn]);
                    control += 2;
                } else if (args[argn].equals("-l") && ((argn + 1) < argl)) {
                    ++argn;
                    parameter.put("log4jConfig", args[argn]);
                    control += 2;
                } else if (args[argn].equals("-p") && ((argn + 1) < argl)) {
                    ++argn;
                    parameter.put("miniatureServerPort", args[argn]);
                    control += 2;
                } else if (args[argn].equals("-s") && ((argn + 1) < argl)) {
                    ++argn;
                    parameter.put("serverManagementRoot", args[argn]);
                    control += 2;
                } else if (args[argn].equals("-c") && ((argn + 1) < argl)) {
                    ++argn;
                    parameter.put("miniatureServerConfig", args[argn]);
                    control += 2;
                } else if (args[argn].equals("-a") && ((argn + 1) < argl)) {
                    serverArgs = new String[argl - control];
                    for (int i = 0; i < (argl - control);) {
                        ++argn;
                        serverArgs[i] = args[argn];
                        ++i;
                    }
                    parameter.put("cidsServerArgs", serverArgs);
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources").getString(
                            "ungueltiger_schalter_angegeben"),
                        "cids ServerConsole",
                        JOptionPane.ERROR_MESSAGE);
                    showHelp(START_HELP_FILE);
                    System.exit(0);
                }
                ++argn;
            }
        }
//        if ( argn != argl ) {
//            JOptionPane.showMessageDialog(this, argn+","+argl+" "+java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources").getString("ungueltige_kommandozeilenparameterangabe"), "cids ServerConsole", JOptionPane.ERROR_MESSAGE);
//            showHelp(START_HELP_FILE);
//            System.exit(0);
//        }

        /*
         * Process command line parameters.
         *
         */
        if (parameter.get("serverType") != null) {
            serverType = (String)parameter.get("serverType");
            // System.out.println("serverType: " + serverType);
        } else {
            System.err.println("Parameter serverType wurde nicht angegeben!");
        }

        if (parameter.get("serverClassName") != null) {
            serverClassName = (String)parameter.get("serverClassName");
            // System.out.println("serverClassName: " + serverClassName);
        } else {
            JOptionPane.showMessageDialog(
                this,
                "<html>Parameter serverClassName wurde nicht angegeben!<br>Es kann kein cids Server gestartet werden.<br>Die ServerConsole wird beendet!</html>",
                "cids ServerConsole",
                JOptionPane.ERROR_MESSAGE);
            showHelp(START_HELP_FILE);
            System.exit(0);
        }

        if (parameter.get("log4jConfig") != null) {
            log4jConfig = (String)parameter.get("log4jConfig");
            // System.out.println("log4jConfig: " + log4jConfig);
        } else {
            System.out.println("Parameter log4jFile wurde nicht angegeben.");
        }

        /* If there's no valid port information,
         * the default port is used.
         */
        if (parameter.get("miniatureServerPort") != null) {
            if (parameter.get("miniatureServerPort") != null) {
                miniatureServerPort = (String)parameter.get("miniatureServerPort");
                if (miniatureServerPort.trim().equals("")) {
                    miniatureServerPort = DEFAULT_MINIATURE_SERVER_PORT;
                }
            } else {
                miniatureServerPort = DEFAULT_MINIATURE_SERVER_PORT;
            }
            // System.out.println("miniatureServerPort: " + miniatureServerPort);
        } else {
            System.err.println(java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
                        .getString("miniatureServerPort_nicht_angegeben"));
            startMiniatureServer = false;
        }

        if (parameter.get("serverManagementRoot") != null) {
            serverManagementRoot = (String)parameter.get("serverManagementRoot");
            workpath = new File(serverManagementRoot);
            if (!workpath.isDirectory()) {
                workpath = new File(System.getProperty("user.dir"));
                System.err.println("Die angegebene serverManagementRoot \"" + serverManagementRoot
                            + "\" kann nicht gelesen werden!\nDas Arbeitsverzeichnis des cids Servermanagements ist "
                            + workpath + ".");
            }
            // System.out.println("serverManagementRoot: " + serverManagementRoot);
        } else {
            workpath = new File(System.getProperty("user.dir"));
            System.out.println(
                "Parameter serverManagementRoot wurde nicht angegeben.\nDas Arbeitsverzeichnis des cids Servermanagements ist "
                        + workpath
                        + ".");
        }

        if (parameter.get("miniatureServerConfig") != null) {
            miniatureServerConfig = (String)parameter.get("miniatureServerConfig");
            // System.out.println("miniatureServerConfig: " + miniatureServerConfig);
        } else {
            System.out.println("Parameter miniatureServerConfig wurde nicht angegeben.");
        }

        if (parameter.get("cidsServerArgs") == null) {
            serverArgs = new String[0];
            System.out.println(java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
                        .getString("Fuer_den_cids_Server_wurden_keine_Parameter_angegeben"));
        }

        /*System.out.println("serverArgs.length: " + serverArgs.length);
         * for (int i=0; i < serverArgs.length; ) { System.out.println("serverArgs[" + i + "]: " + serverArgs[i]);
         * ++i;}*/

        /*
         * Initialise log4j with the given or with a standard properties file.
         *
         */
        if (log4jConfig.equals("default")) {
            BasicConfigurator.configure();
        } else {
            File log4jFile = new File(log4jConfig);
            if (!log4jFile.isAbsolute()) {
                log4jFile = new File(workpath, log4jFile.getPath());
            }
            if (log4jFile.isDirectory() || !log4jFile.exists() || !log4jFile.canRead()) {
                System.err.println(java.util.ResourceBundle.getBundle(
                        "de/cismet/cids/admin/serverManagement/resources").getString(
                        "Die_Konfigurationsdatei_fuer_log4j") + log4jFile
                            + "\" kann nicht gelesen werden! Es wird der BasicConfigurator benutzt.");
                BasicConfigurator.configure();
            } else {
                PropertyConfigurator.configure(log4jFile.toString());
            }
        }
        logger.getLogger("org.mortbay").setLevel((Level)Level.OFF);
        logger = logger.getLogger(ServerConsole.class);

        // enable System Tray functions only under operating system Windows:
        if ((System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)) {
            cmdTray.setEnabled(true);
//            initSysTray(serverClassName, serverType);
        }

        try {
            lblIcon.setIcon(new javax.swing.ImageIcon(
                    getClass().getResource(
                        "/de/cismet/cids/admin/serverConsole/serverSymbols/32/"
                                + serverClassName
                                + ".png")));
            this.setIconImage(new javax.swing.ImageIcon(
                    getClass().getResource(
                        "/de/cismet/cids/admin/serverConsole/serverSymbols/16/"
                                + serverClassName
                                + ".png")).getImage());
        } catch (Throwable e) {
            lblIcon.setIcon(new javax.swing.ImageIcon(
                    getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/32/default.png")));
            this.setIconImage(new javax.swing.ImageIcon(
                    getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/32/default.png"))
                        .getImage());
        }

        this.setTitle(getTitle() + " - " + serverType);
        this.lblName.setText(serverType);

        if (startMiniatureServer) {
            /*
             * Build the string with the command line parameters for the Miniature Server from the appropriate
             * configuration file.
             *
             */
            PropertyResourceBundle serverProperties = null;

            if (miniatureServerConfig.equals("default")) {
                try {
                    serverProperties = new PropertyResourceBundle(this.getClass().getResourceAsStream(
                                "/de/cismet/cids/admin/serverConsole/miniServer.cfg"));
                } catch (Throwable e) {
                    logger.fatal(java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
                                .getString("Fehler_beim_Einlesen_der_Standard-Konfigurationsdatei_fuer_den_MiniServer"),
                        e);
                    System.err.println(java.util.ResourceBundle.getBundle(
                            "de/cismet/cids/admin/serverManagement/resources").getString(
                            "Fehler_beim_Einlesen_der_Standard-Konfigurationsdatei_fuer_den_MiniServer"));
                }
            } else {
                File miniatureServerConfigFile = new File(miniatureServerConfig);
                if (!miniatureServerConfigFile.isAbsolute()) {
                    miniatureServerConfigFile = new File(workpath, miniatureServerConfigFile.getPath());
                }
                if (miniatureServerConfigFile.isDirectory() || !miniatureServerConfigFile.exists()
                            || !miniatureServerConfigFile.canRead()) {
                    System.err.println(java.util.ResourceBundle.getBundle(
                            "de/cismet/cids/admin/serverManagement/resources").getString(
                            "konfigurationsdatei_fuer_MiniServer") + miniatureServerConfigFile
                                + "\" kann nicht gelesen werden!\nEs wird die Standard-Konfigurationsdatei geladen!");
                    try {
                        serverProperties = new PropertyResourceBundle(this.getClass().getResourceAsStream(
                                    "/de/cismet/cids/admin/serverConsole/miniServer.cfg"));
                    } catch (Throwable e) {
                        logger.fatal(java.util.ResourceBundle.getBundle(
                                "de/cismet/cids/admin/serverManagement/resources").getString(
                                "Fehler_beim_Einlesen_der_Standard-Konfigurationsdatei_fuer_den_MiniServer"),
                            e);
                        System.err.println(java.util.ResourceBundle.getBundle(
                                "de/cismet/cids/admin/serverManagement/resources").getString(
                                "Fehler_beim_Einlesen_der_Standard-Konfigurationsdatei_fuer_den_MiniServer"));
                    }
                } else {
                    try {
                        final FileInputStream miniatureServerConfigFileIn = new FileInputStream(
                                miniatureServerConfigFile);
                        serverProperties = new PropertyResourceBundle(miniatureServerConfigFileIn);
                    } catch (Throwable e) {
                        logger.fatal(java.util.ResourceBundle.getBundle(
                                "de/cismet/cids/admin/serverManagement/resources").getString(
                                "Fehler_beim_Einlesen_der_Konfigurationsparameter_fuer_den_MiniServer"),
                            e);
                        System.err.println(java.util.ResourceBundle.getBundle(
                                "de/cismet/cids/admin/serverManagement/resources").getString(
                                "Fehler_beim_Einlesen_der_Konfigurationsdatei_fuer_den_MiniServer"));
                    }
                }
            }

            try {
                // TODO entfernen
// miniatureServerArgs = new String[] {
// serverProperties.getString("AliasesDefinitionFile_option"),
// serverProperties.getString("AliasesDefinitionFile_value"),
// serverProperties.getString("ServletPropertiesFile_option"),
// serverProperties.getString("ServletPropertiesFile_value"),
// "-p", miniatureServerPort
// //serverProperties.getString("LogOption"),
// //serverProperties.getString("LogOption_value"),
// //"-sr", workpath.toString(),
// //serverProperties.getString("SessionTimeOutInMinutesOption"),
// //serverProperties.getString("SessionTimeOutInMinutesValue")
// };

////            // miniatureServerArgs = new HashMap();
                // TODO entfernen
//                System.out.println("JF: " + workpath.toString());
//                miniatureServerArgs.put(Serve.ARG_ALIASES,  serverProperties.getString("AliasesDefinitionFile_value") );
//                miniatureServerArgs.put(Serve.ARG_SERVLETS, serverProperties.getString("ServletPropertiesFile_value") );
//                miniatureServerArgs.put(Serve.ARG_PORT, new Integer(miniatureServerPort) );

            } catch (Throwable e) {
                logger.fatal(java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
                            .getString("Fehler_beim_Einlesen_der_Konfigurationsparameter_fuer_den_MiniServer"),
                    e);
                System.err.println(java.util.ResourceBundle.getBundle(
                        "de/cismet/cids/admin/serverManagement/resources").getString(
                        "Fehler_beim_Einlesen_der_Konfigurationsdatei_fuer_den_MiniServer"));
            }

            /*
             * Read out and, if necessary, complete the absolute path for the temporary logging files of the servlet
             * from the Miniature Server's configuration file.
             *
             */
            try {
                logOutputDirectory = new File(serverProperties.getString("LogOutputDirectory"));
            } catch (Throwable e) {
                logger.error(
                    "LogOutputDirectory konnte nicht aus der Konfigurationsdatei des Miniature Servers gelesen werden.",
                    e);
                System.err.println(
                    "LogOutputDirectory konnte nicht aus der Konfigurationsdatei des Miniature Servers gelesen werden.");
                logOutputDirectory = workpath;
            }
            if (!logOutputDirectory.isAbsolute()) {
                logOutputDirectory = new File(workpath, logOutputDirectory.getPath());
                // logOutputDirectory.mkdirs();
            } else {
                // logOutputDirectory.mkdirs();
            }
            if (!logOutputDirectory.isDirectory()) {
                logOutputDirectory = workpath;
                System.err.println(java.util.ResourceBundle.getBundle(
                        "de/cismet/cids/admin/serverManagement/resources").getString(
                        "Das_Verzeichnis_f�r_die_Logging_Dateien_konnte_nicht_erstellt_werden") + logOutputDirectory
                            + "\" wird verwendet.");
            }
            logOutputDirectory.mkdirs();
            // System.out.println("logOutputDirectory: " + logOutputDirectory);

            final KeyStroke configLoggerKeyStroke = KeyStroke.getKeyStroke('L', InputEvent.CTRL_MASK);
            final Action configAction = new AbstractAction() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        java.awt.EventQueue.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    Log4JQuickConfig.getSingletonInstance().setVisible(true);
                                }
                            });
                    }
                };
            getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(configLoggerKeyStroke, "CONFIGLOGGING");
            getRootPane().getActionMap().put("CONFIGLOGGING", configAction);
        }

        /*
         * Read out and, if necessary, complete the absolute path of the cids server's configuration file and deliver to
         * the variable cidsServerConfigFile. This variable is needed to be able to edit the configuration file with the
         * FileEditor of the ServerConsole and within the management web interface.
         *
         */
        if (serverArgs.length >= 1) {
            cidsServerConfigFile = new File(serverArgs[0]);

            if (!cidsServerConfigFile.isAbsolute()) {
                cidsServerConfigFile = new File(workpath, cidsServerConfigFile.getPath());
            }
            if (cidsServerConfigFile.isDirectory() || !cidsServerConfigFile.exists()
                        || !cidsServerConfigFile.canRead()) {
                System.err.println(java.util.ResourceBundle.getBundle(
                        "de/cismet/cids/admin/serverManagement/resources").getString(
                        "Die_Konfigurationsdatei_f�r_den_cids_Server") + cidsServerConfigFile
                            + "\" kann nicht gelesen werden!\n"
                            + java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
                            .getString("config_file_muss_als_erster_Parameter_hinter_-a_stehen")
                            + "\nDie ServerConsole muss mit richtiger Kommandozeilenparameter-Reihenfolge neu gestartet werden!");
                cidsServerConfigFile = null;
            }
        }

        // Get a class object and main method object of cids server:
        try {
            serverClass = Class.forName(serverClassName);
            mainMethod = serverClass.getMethod("main", new Class[] { String[].class });
            getServerInstance = serverClass.getMethod("getServerInstance", null);

            tblInfo.setModel(new DefaultTableModel(
                    new Object[][] {
                        { "Online seit:", "-" },
                        { "ohne Fehler seit:", "-" }
                    },
                    new String[] { "", "" }));
            tblInfo.setTableHeader(null);
        } catch (java.lang.NoClassDefFoundError e) {
            logger.fatal("Eine Klasse konnte nicht geladen werden der Server beendet sich", e);
            JOptionPane.showMessageDialog(
                this,
                "Eine Klasse konnte nicht geladen werden "
                        + e.getMessage()
                        + " der Server beendet sich",
                "cids ServerConsole",
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Throwable e) {
            System.err.println(java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
                        .getString("Der_Servername_ist_falsch_oder_unterstuetzt_nicht_das_cids-Servermanagement"));
            logger.fatal(java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
                        .getString("Der_Servername_ist_falsch_oder_unterstuetzt_nicht_das_cids-Servermanagement"),
                e);
            JOptionPane.showMessageDialog(
                this,
                java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources").getString(
                    "Der_Servername_ist_falsch_oder_unterstuetzt_nicht_das_cids-Servermanagement"),
                "cids ServerConsole",
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This Singleton returns an instance of class ServerConsole.
     *
     * @return  an instance of class ServerConsole
     */
    public static ServerConsole getInstance() {
        return instance;
    }

    /**
     * Gets a short description of the cids server type.
     *
     * @return  a short description of the cids server type
     */
    public String getServerType() {
        return serverType;
    }

    /**
     * Gets the HTTP port on which the Mini Webserver can be reached.
     *
     * @return  the HTTP port on which the Mini Webserver can be reached
     */
    public String getMiniatureServerPort() {
        return miniatureServerPort;
    }

    /**
     * Gets the directory for the temporary logging files of the servlet.
     *
     * @return  the directory in which the temporary logging files of the servlet should be written
     */
    public File getlogOutputDirectory() {
        return logOutputDirectory;
    }

    /**
     * Gets the information whether the cids server is running or not.
     *
     * @return  true if the cids server is running, false if the cids server is stopped
     */
    public boolean isServerRunning() {
        return serverRunnin;
    }

    /**
     * Gets the configuration file of the cids server.
     *
     * @return  the configuration file of the Mini Webserver
     */
    public static File getCidsServerConfigFile() {
        return cidsServerConfigFile;
    }

    /**
     * Returns the log messages of the txtLog JTextPane.
     *
     * @return  a String with log messages
     */
    public String getLogMessages() {
        final String logMessages = txtLog.getText();
        return logMessages;
    }

    /**
     * Starts the Miniature Web Server.
     */
    private void startMiniatureServer() {
        try {
            minuatureServerInstance = new SimpleWebServer(Integer.parseInt(miniatureServerPort));
            minuatureServerInstance.start();
        } catch (Throwable e) {
            logger.error("Fehler beim Starten des WebServers", e);
        }
    }

    /**
     * Starts the cids server.
     */
    public void startServer() {
        prbStatus.setIndeterminate(true);
        serverRunnin = true;
        this.lblAmpel.setIcon(yellow);

        new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        // EventQueue.isDispatchThread()
                        mainMethod.invoke(serverClass, new Object[] { serverArgs });
                        SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    serverStartTime = System.currentTimeMillis();
                                    lblAmpel.setIcon(green);
                                    prbStatus.setIndeterminate(false);
                                }
                            });
                    } catch (InvocationTargetException te) {
                        final Throwable t = te.getCause();
                        if (t instanceof ServerExit) {
                            System.out.println(
                                java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
                                            .getString("Der_Server_wurde_ordnungsgemaess_beendet"));
                            if (logger.isDebugEnabled()) {
                                logger.debug(
                                    java.util.ResourceBundle.getBundle(
                                        "de/cismet/cids/admin/serverManagement/resources").getString(
                                        "Der_cids_Server_wurde_ordnungsgemaess_beendet"));
                            }
                            serverStopped();
                        } else if (t instanceof ServerExitError) {
                            t.printStackTrace();
                            System.err.println("\nDer Server hat sich aufgrund eines Fehlers beendet.\n");
                            logger.fatal("Der cids Server hat sich aufgrund eines Fehlers beendet.", t);
                            serverStopped();
                        } else {
                            System.err.println("\nDer Server wurde aufgrund eines Fehlers beendet.\n");
                            logger.fatal("Der cids Server wurde aufgrund eines Fehlers beendet.", t);
                            serverStopped();
                            t.printStackTrace();
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                        serverStopped();
                        System.err.println(
                            "\nBeim Starten des Servers ist ein Fehler aufgetreten\nDer Server befindet sich in einem undefinierten Zustand!!!\nBeenden Sie die ServerConsole!!!\n");
                        logger.fatal("Beim Starten des cids Servers ist ein Fehler aufgetreten.", t);
                        lblAmpel.setIcon(yellow);
                    }
                }
            }).start();
    }

    /**
     * Shuts the cids server down.
     */
    public void shutdownServer() {
        try {
            serverInstance = getServerInstance.invoke(serverClass, null);
            final Method shutdown = serverClass.getMethod("shutdown", null);
            shutdown.invoke(serverInstance, null);
            serverInstance = null;
            serverStopped();
            System.gc();
        } catch (InvocationTargetException te) {
            final Throwable t = te.getCause();
            if (t instanceof ServerExit) {
                System.out.println(java.util.ResourceBundle.getBundle(
                        "de/cismet/cids/admin/serverManagement/resources").getString(
                        "Der_Server_wurde_ordnungsgemaess_beendet"));
                serverStopped();
            } else if (t instanceof ServerExitError) {
                t.printStackTrace();
                System.err.println("\nDer Server hat sich aufgrund eines Fehlers beendet.\n");
                logger.fatal("Der cids Server hat sich aufgrund eines Fehlers beendet.", t);
                serverStopped();
            } else {
                System.err.println("\nDer Server wurde aufgrund eines Fehlers beendet.\n");
                logger.fatal("Der cids Server wurde aufgrund eines Fehlers beendet.", t);
                serverStopped();
                t.printStackTrace();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            serverStopped();
            System.err.println(
                "\nBeim Beenden des Servers ist ein Fehler aufgetreten\nDer Server befindet sich in einem undefinierten Zustand!!!\nBeenden Sie die ServerConsole!!!\n");
            logger.fatal("Beim Beenden des cids Servers ist ein Fehler aufgetreten.", t);
            lblAmpel.setIcon(yellow);
        }
    }

    /**
     * Validates a user.
     *
     * @param   userGroupName  DOCUMENT ME!
     * @param   userName       DOCUMENT ME!
     * @param   password       DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean validateUser(final String userGroupName, final String userName, final String password) {
        boolean ret = false;

        final String adminPassword = "xxx";

        try {
            /* Method only available with the server type "domain server"
             * (Sirius.server.middleware.impls.domainserver.DomainServerImpl)
             *
             * wenn proxy: benutzername=admin, usergroup=proxy, pass=xxx Sirius.server.middleware.impls.proxy.StartProxy
             * wenn registry: benutzername=admin, usergroup=registry, pass=xxx Sirius.server.registry.Registry
             */

            // Get instance of the running server, e.g. proxy, registry, domain server
            serverInstance = getServerInstance.invoke(serverClass, null);

            if (serverInstance instanceof Sirius.server.middleware.impls.domainserver.DomainServerImpl) {
                final UserGroup userGroup = new UserGroup(-1, userGroupName, "");
                final User user = new User(-1, userName, "", userGroup);

                final Method validateUser = serverClass.getMethod(
                        "validateUser",
                        new Class[] { User.class, String.class });
                final Boolean refRet = (Boolean)validateUser.invoke(serverInstance, new Object[] { user, password });
                ret = refRet.booleanValue();
            } else if (serverInstance instanceof Sirius.server.middleware.impls.proxy.StartProxy) {
                if (userGroupName.equals("proxy") && userName.equals("admin") && password.equals(adminPassword)) {
                    ret = true;
                }
            } else if (serverInstance instanceof Sirius.server.registry.Registry) {
                if (userGroupName.equals("registry") && userName.equals("admin") && password.equals(adminPassword)) {
                    ret = true;
                }
            } else {
                throw new Exception("UnknownServerType during authentication occurred: "
                            + serverInstance.getClass().getName() + ".");
            }
        } catch (Exception t) {
            t.printStackTrace();
            System.err.println("\nBeim Validieren des Benutzers " + userName + " ist ein Fehler aufgetreten.!!!\n");
            logger.fatal("Beim Validieren des Benutzers " + userName + " ist ein Fehler aufgetreten.", t);
        }
        return ret;
    }

    /**
     * Shuts the Miniature Web Server down.
     */
    private void shutdownMiniatureServer() {
        try {
            if (minuatureServerInstance != null) {
                minuatureServerInstance.stop();
            }
        } catch (Exception e) {
            logger.error("Fehler beim Stoppen Webservers.", e);
        }
    }

    /**
     * Sets program internal variables for the case that the cids server is stopped.
     */
    private void serverStopped() {
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    serverRunnin = false;
                    serverStartTime = -1;
                    lblAmpel.setIcon(red);
                    prbStatus.setIndeterminate(false);
                }
            });
    }

    /**
     * Refreshes the information of Control & Information panel.
     */
    private void refreshInfo() {
        String online = "-";
        String withoutErrors = "-";
        if (serverRunnin && (serverStartTime != -1)) {
            final long seconds = System.currentTimeMillis() - serverStartTime;
            online = getDuration(seconds);
        }

        if (serverRunnin && (gsError.getLastInputTime() != -1)) {
            final long seconds = System.currentTimeMillis() - gsError.getLastInputTime();
            withoutErrors = getDuration(seconds);
        } else {
            withoutErrors = online;
        }
        final DefaultTableModel model = ((DefaultTableModel)tblInfo.getModel());

        model.setValueAt(online, 0, 1);
        model.setValueAt(withoutErrors, 1, 1);
        model.fireTableDataChanged();
    }

    /**
     * Opens a JOptionPane with a help HTML file.
     *
     * @param  helpfile  DOCUMENT ME!
     */
    public void showHelp(final int helpfile) {
        java.net.URL hilfeHTML = null;
        final JEditorPane hilfeEditorPane = new JEditorPane();
        hilfeEditorPane.setEditable(false);
        JPanel hilfePanel = new JPanel(new BorderLayout());

        if (helpfile == START_HELP_FILE) {
            hilfeHTML = this.getClass()
                        .getResource("/de/cismet/cids/admin/serverManagement/help/serverConsoleHelpStart.html");
        } else if (helpfile == MAIN_HELP_FILE) {
            hilfeHTML = this.getClass()
                        .getResource("/de/cismet/cids/admin/serverManagement/help/serverConsoleHelpMain.html");
        }

        if (hilfeHTML != null) {
            try {
                hilfeEditorPane.setPage(hilfeHTML);
            } catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + hilfeHTML);
            }
        } else {
            System.err.println("Die Hilfedatei konnte nicht gefunden werden.");
        }

        hilfeEditorPane.setBackground(new Color(172, 210, 248));
        hilfeEditorPane.setOpaque(true);

        final JScrollPane hilfeScrollPane = new JScrollPane(hilfeEditorPane);

        hilfePanel = new JPanel(new BorderLayout());
        hilfePanel.setPreferredSize(new Dimension(524, 471));
        hilfePanel.add(hilfeScrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
            this,
            hilfePanel,
            "cids ServerConsole - Kurzanleitung",
            JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Changes a number of miliseconds into the format <i>x weeks, x days, x hours, x minutes, x seconds</i>.
     *
     * @param   milliseconds  the milliseconds since the server's start time
     *
     * @return  a String that contains the formatted information
     */
    public static String getDuration(final long milliseconds) {
        final long weeks = milliseconds / MILLISECONDS_WEEK;
        long rest = milliseconds - (weeks * MILLISECONDS_WEEK);
        final long days = rest / MILLISECONDS_DAY;
        rest = rest - (days * MILLISECONDS_DAY);
        final long hours = rest / MILLISECONDS_HOUR;
        rest = rest - (hours * MILLISECONDS_HOUR);
        final long minutes = rest / MILLISECONDS_MINUTE;
        rest = rest - (minutes * MILLISECONDS_MINUTE);
        final long seconds = rest / MILLISECONDS_SECOND;
        rest = rest - (seconds * MILLISECONDS_SECOND);
        final long millis = rest;

        String ret = "";
        if (weeks > 1) {
            ret += weeks + " Wochen ";
        } else if (weeks > 0) {
            ret += weeks + " Woche ";
        }

        if (days > 1) {
            ret += days + " Tage ";
        } else if (days > 0) {
            ret += days + " Tag ";
        } else if ((days == 0) && (weeks > 0)) {
            ret += " 0 Tage ";
        }

        if (hours > 1) {
            ret += hours + " Stunden ";
        } else if (hours > 0) {
            ret += hours + " Stunde ";
        } else if ((hours == 0) && ((weeks > 0) || (days > 0))) {
            ret += " 0 Minuten ";
        }

        if (minutes > 1) {
            ret += minutes + " Minuten ";
        } else if (minutes > 0) {
            ret += minutes + " Minute ";
        } else if ((minutes == 0) && ((weeks > 0) || (days > 0) || (hours > 0))) {
            ret += " 0 Minuten ";
        }

        if (seconds == 1) {
            ret += "1 Sekunde";
        } else {
            ret += seconds + " Sekunden";
        }
        return ret;
    }

    /**
     * Displays the server's status with a JOptionPane.
     */
    private void displayServerStatus() {
        System.out.println("XXXXXXXXXXXXXXXXXX" + serverInstance);
        try {
            serverInstance = getServerInstance.invoke(serverClass, null);
            final Method getStatus = serverClass.getMethod("getStatus", null);
            final Object ret = getStatus.invoke(serverInstance, null);
            final ServerStatus status = (ServerStatus)ret;
            final HashMap hm = status.getMessages();
            final Iterator i = hm.keySet().iterator();
            String statString = "keine Meldungen vorhanden";
            int j = 0;
            while (i.hasNext()) {
                if (j == 0) {
                    statString = "";
                }
                final String key = (String)i.next();
                final String value = (String)hm.get(key);
                statString += "\n" + key + ":   " + value;
                ++j;
            }

            JOptionPane.showMessageDialog(this, "Serverstatus:\n" + statString);
        } catch (InvocationTargetException te) {
            final Throwable t = te.getCause();

            if (t instanceof ServerExitError) {
                t.printStackTrace();
                System.err.println("\nDer Server hat sich aufgrund eines Fehlers beendet.\n");
                serverStopped();
            } else {
                System.err.println("\nDer Server wurde aufgrund eines Fehlers beendet.\n");
                serverStopped();
                t.printStackTrace();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.err.println(
                "\nLokaler Fehler !!! Der Server befindet sich in einem undefinierten Zustand!!!\nBeenden Sie die ServerConsole!!!\n");
            this.serverStopped();
            this.lblAmpel.setIcon(yellow);
        }
    }

//    /**
//     * Initializes the System Tray menu.
//     *
//     */
//    void initSysTray(String serverClassName, String serverType) {
//        if(serverClassName != null && serverType != null && getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/16/"+serverClassName+".ico") != null) {
//            stIcon=new SysTrayMenuIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/16/"+serverClassName+".ico"));
//            stIcon.addSysTrayMenuListener(this);
//            stMenu = new SysTrayMenu(stIcon,serverType);
//        } else {
//            System.err.println("System tray konnte nicht initialisiert werden! ");
//            logger.debug("System tray konnte nicht initialisiert werden!");
//            cmdTray.setEnabled(false);
//        }
//    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cmdQueue = new javax.swing.JButton();
        cmdSaveLogs = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        pnlStatus = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        lblAmpel = new javax.swing.JLabel();
        prbStatus = new javax.swing.JProgressBar();
        pnlMain = new javax.swing.JPanel();
        pnlControlAndInfo = new javax.swing.JPanel();
        pnlInformation = new javax.swing.JPanel();
        tblInfo = new javax.swing.JTable();
        pnlControl = new de.cismet.cids.tools.gui.farnsworth.GradientJPanel();
        lblTitleControl = new javax.swing.JLabel();
        cmdTray = new javax.swing.JButton();
        cmdShutdown = new javax.swing.JButton();
        cmdRestart = new javax.swing.JButton();
        cmdInfo = new javax.swing.JButton();
        cmdHelp = new javax.swing.JButton();
        cmdedit = new javax.swing.JButton();
        pnlLogging = new javax.swing.JPanel();
        pnlTable = new javax.swing.JPanel();
        spnTable = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        txtLog = new javax.swing.JTextPane();
        pnlTitleExport = new de.cismet.cids.tools.gui.farnsworth.GradientJPanel();
        lblExportTitle = new javax.swing.JLabel();
        cmdDeleteLogs = new javax.swing.JButton();
        gplTitle = new de.cismet.cids.tools.gui.farnsworth.GradientJPanel();
        lblName = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();

        cmdQueue.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/time.png")));
        cmdQueue.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        cmdQueue.setContentAreaFilled(false);
        cmdQueue.setDefaultCapable(false);
        cmdQueue.setFocusPainted(false);
        cmdQueue.setPressedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/time.png")));
        cmdQueue.setSelectedIcon(new javax.swing.ImageIcon(""));
        cmdSaveLogs.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/save.png")));
        cmdSaveLogs.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        cmdSaveLogs.setContentAreaFilled(false);
        cmdSaveLogs.setDefaultCapable(false);
        cmdSaveLogs.setFocusPainted(false);
        cmdSaveLogs.setPressedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/save.png")));
        cmdSaveLogs.setSelectedIcon(new javax.swing.ImageIcon(""));
        cmdSaveLogs.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdSaveLogsActionPerformed(evt);
                }
            });

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("cids ServerConsole");
        addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(final java.awt.event.WindowEvent evt) {
                    exitForm(evt);
                }
                @Override
                public void windowIconified(final java.awt.event.WindowEvent evt) {
                    formWindowIconified(evt);
                }
            });

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlStatus.setLayout(new java.awt.GridBagLayout());

        lblStatus.setText("cismet GmbH");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlStatus.add(lblStatus, gridBagConstraints);

        lblAmpel.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/yellowled.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlStatus.add(lblAmpel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        pnlStatus.add(prbStatus, gridBagConstraints);

        jPanel2.add(pnlStatus, java.awt.BorderLayout.SOUTH);

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlControlAndInfo.setLayout(new java.awt.BorderLayout());

        pnlControlAndInfo.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 0, 0, 0)),
                new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED)));
        pnlInformation.setLayout(new java.awt.GridBagLayout());

        spnTable.setBorder(null);
        tblInfo.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        tblInfo.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                    { null, null }
                },
                new String[] { "Title 1", "Title 2" }) {

                Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };
                boolean[] canEdit = new boolean[] { false, true };

                @Override
                public Class getColumnClass(final int columnIndex) {
                    return types[columnIndex];
                }

                @Override
                public boolean isCellEditable(final int rowIndex, final int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
        tblInfo.setShowHorizontalLines(false);
        tblInfo.setShowVerticalLines(false);
        tblInfo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlInformation.add(tblInfo, gridBagConstraints);

        pnlControlAndInfo.add(pnlInformation, java.awt.BorderLayout.CENTER);

        pnlControl.setLayout(new java.awt.GridBagLayout());

        pnlControl.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        lblTitleControl.setForeground(new java.awt.Color(255, 255, 255));
        lblTitleControl.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/res/pack_empty_co.gif")));
        lblTitleControl.setText("Status & Bedienung");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        pnlControl.add(lblTitleControl, gridBagConstraints);

        cmdTray.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/tray.png")));
        cmdTray.setToolTipText("Minimieren in die System Tray");
        cmdTray.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        cmdTray.setContentAreaFilled(false);
        cmdTray.setDefaultCapable(false);
        cmdTray.setFocusPainted(false);
        cmdTray.setPressedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/tray.png")));
        cmdTray.setSelectedIcon(new javax.swing.ImageIcon(""));
        cmdTray.setEnabled(false);
        cmdTray.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdTrayActionPerformed(evt);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdTray, gridBagConstraints);

        cmdShutdown.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/exit.png")));
        cmdShutdown.setToolTipText("Server beenden");
        cmdShutdown.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        cmdShutdown.setContentAreaFilled(false);
        cmdShutdown.setDefaultCapable(false);
        cmdShutdown.setFocusPainted(false);
        cmdShutdown.setPressedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/exit.png")));
        cmdShutdown.setSelectedIcon(new javax.swing.ImageIcon(""));
        cmdShutdown.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdShutdownActionPerformed(evt);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdShutdown, gridBagConstraints);

        cmdRestart.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/restart.png")));
        cmdRestart.setToolTipText("Server neustarten");
        cmdRestart.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        cmdRestart.setContentAreaFilled(false);
        cmdRestart.setDefaultCapable(false);
        cmdRestart.setFocusPainted(false);
        cmdRestart.setPressedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/restart.png")));
        cmdRestart.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdRestartActionPerformed(evt);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdRestart, gridBagConstraints);

        cmdInfo.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/info.png")));
        cmdInfo.setToolTipText("Informationen \u00fcber den Server-Status");
        cmdInfo.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        cmdInfo.setContentAreaFilled(false);
        cmdInfo.setDefaultCapable(false);
        cmdInfo.setFocusPainted(false);
        cmdInfo.setPressedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/info.png")));
        cmdInfo.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdInfoActionPerformed(evt);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdInfo, gridBagConstraints);

        cmdHelp.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/help.png")));
        cmdHelp.setToolTipText("Hilfedatei anzeigen");
        cmdHelp.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        cmdHelp.setContentAreaFilled(false);
        cmdHelp.setDefaultCapable(false);
        cmdHelp.setFocusPainted(false);
        cmdHelp.setMaximumSize(new java.awt.Dimension(16, 16));
        cmdHelp.setMinimumSize(new java.awt.Dimension(16, 16));
        cmdHelp.setPreferredSize(new java.awt.Dimension(16, 16));
        cmdHelp.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdHelpActionPerformed(evt);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdHelp, gridBagConstraints);

        cmdedit.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/edit.png")));
        cmdedit.setToolTipText("Konfigurationsdatei des Servers editieren");
        cmdedit.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        cmdedit.setContentAreaFilled(false);
        cmdedit.setDefaultCapable(false);
        cmdedit.setFocusPainted(false);
        cmdedit.setPreferredSize(new java.awt.Dimension(16, 16));
        cmdedit.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdEditActionPerformed(evt);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdedit, gridBagConstraints);

        pnlControlAndInfo.add(pnlControl, java.awt.BorderLayout.NORTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlMain.add(pnlControlAndInfo, gridBagConstraints);

        pnlLogging.setLayout(new java.awt.BorderLayout());

        pnlLogging.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 0, 0, 0)),
                new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED)));
        pnlTable.setLayout(new java.awt.GridBagLayout());

        pnlTable.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 2, 2, 2)));
        spnTable.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        spnTable.setForeground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new java.awt.BorderLayout());

        txtLog.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        txtLog.setEditable(false);
        jPanel1.add(txtLog, java.awt.BorderLayout.CENTER);

        spnTable.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlTable.add(spnTable, gridBagConstraints);

        pnlLogging.add(pnlTable, java.awt.BorderLayout.CENTER);

        pnlTitleExport.setLayout(new java.awt.GridBagLayout());

        pnlTitleExport.setForeground(javax.swing.UIManager.getDefaults().getColor(
                "CheckBoxMenuItem.selectionBackground"));
        lblExportTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblExportTitle.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/res/pack_empty_co.gif")));
        lblExportTitle.setText("Ausgabe");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 10);
        pnlTitleExport.add(lblExportTitle, gridBagConstraints);

        cmdDeleteLogs.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/delete.png")));
        cmdDeleteLogs.setToolTipText("Ausgabefenster l\u00f6schen");
        cmdDeleteLogs.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        cmdDeleteLogs.setContentAreaFilled(false);
        cmdDeleteLogs.setDefaultCapable(false);
        cmdDeleteLogs.setFocusPainted(false);
        cmdDeleteLogs.setPressedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/delete.png")));
        cmdDeleteLogs.setSelectedIcon(new javax.swing.ImageIcon(""));
        cmdDeleteLogs.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdDeleteLogsActionPerformed(evt);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlTitleExport.add(cmdDeleteLogs, gridBagConstraints);

        pnlLogging.add(pnlTitleExport, java.awt.BorderLayout.NORTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlMain.add(pnlLogging, gridBagConstraints);

        gplTitle.setLayout(new java.awt.GridBagLayout());

        gplTitle.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        gplTitle.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        lblName.setFont(new java.awt.Font("Courier New", 1, 24));
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblName.setText("export");
        lblName.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 9, 0, 0);
        gplTitle.add(lblName, gridBagConstraints);

        lblIcon.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/32/default.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        gplTitle.add(lblIcon, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlMain.add(gplTitle, gridBagConstraints);

        jPanel2.add(pnlMain, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
    }
    // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdEditActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_cmdEditActionPerformed
    {                                                                         //GEN-HEADEREND:event_cmdEditActionPerformed

        if (fileEditor == null) {
            fileEditor = new FileEditor();
        }
        fileEditor.setFile(cidsServerConfigFile);
        if (!fileEditor.isShowing()) {
            fileEditor.setVisible(true);
        }
    } //GEN-LAST:event_cmdEditActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdHelpActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_cmdHelpActionPerformed
    {                                                                         //GEN-HEADEREND:event_cmdHelpActionPerformed

        showHelp(MAIN_HELP_FILE);
    } //GEN-LAST:event_cmdHelpActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdSaveLogsActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_cmdSaveLogsActionPerformed
    {                                                                             //GEN-HEADEREND:event_cmdSaveLogsActionPerformed

        // TODO add your handling code here:

    } //GEN-LAST:event_cmdSaveLogsActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void formWindowIconified(final java.awt.event.WindowEvent evt) { //GEN-FIRST:event_formWindowIconified
    }                                                                        //GEN-LAST:event_formWindowIconified

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdTrayActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdTrayActionPerformed

        this.hide();
    } //GEN-LAST:event_cmdTrayActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdDeleteLogsActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdDeleteLogsActionPerformed

        this.txtLog.setText("");
    } //GEN-LAST:event_cmdDeleteLogsActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdInfoActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdInfoActionPerformed

        if (serverRunnin) {
            displayServerStatus();
        }
    } //GEN-LAST:event_cmdInfoActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdRestartActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdRestartActionPerformed

        if (serverRunnin) {
            shutdownServer();
        }
        if (!serverRunnin) {
            startServer();
        }
    } //GEN-LAST:event_cmdRestartActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdShutdownActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cmdShutdownActionPerformed

        if (serverRunnin) {
            shutdownServer();
        }
    } //GEN-LAST:event_cmdShutdownActionPerformed

    /**
     * Exit the Application.
     *
     * @param  evt  DOCUMENT ME!
     */
    private void exitForm(final java.awt.event.WindowEvent evt) { //GEN-FIRST:event_exitForm

        if (serverRunnin) {
            final int answer = JOptionPane.showConfirmDialog(
                    this,
                    java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources").getString(
                        "Warnung_cids_Server_laeuft_noch"),
                    "cids ServerConsole",
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                shutdownServer();
                if (startMiniatureServer) {
                    shutdownMiniatureServer();
                }
                System.exit(0);
            } else {
                this.show();
            }
        } else {
            if (startMiniatureServer) {
                shutdownMiniatureServer();
            }
            System.exit(0);
        }
    } //GEN-LAST:event_exitForm

    /**
     * Exits the application. This method can be used by the ServerManager servlet to exit the server management. It
     * shuts down the Miniature Web Server and the cids server if it's still running before exiting the ServerConsole.
     */
    public void exit() {
        if (serverRunnin) {
            shutdownServer();
        }
        if (startMiniatureServer) {
            shutdownMiniatureServer();
        }
        System.exit(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        try {
            // ToDo check with Thorsten
            PlasticLookAndFeel.setCurrentTheme(new SkyBluer());
            javax.swing.UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        final ServerConsole con = new ServerConsole(args);

        con.setSize(550, 550);
        con.setLocationRelativeTo(null);
        con.show();

        try {
            if (startMiniatureServer) {
                con.startMiniatureServer();
            }
        } catch (Throwable e) {
            System.out.println("Miniature Web Server konnte nicht gestartet werden!");
            e.printStackTrace();
        }

        con.startServer();

        final Runnable refreshingGui = new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    con.refreshInfo();
                                }
                            });

                        try {
                            java.lang.Thread.sleep(1000);
                        } catch (java.lang.InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
            };

        new Thread(refreshingGui, "Refresh GUI").start();
    }

    /**
     * Clears the txtLog JTextPane.
     */
    public void clearLogMessages() {
        this.txtLog.setText("");
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getServerLogFile() {
        BufferedReader fileReader = null;
        String logHtml = new String();
        String line;
        final StringBuffer buf = new StringBuffer();
        try {
            final File logfile = new File(serverLogFileName);
            fileReader = new BufferedReader(new FileReader(logfile));
            while ((line = fileReader.readLine()) != null) {
                logHtml += (line + "\n");
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(HeadlessServerConsole.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.error("Could not read ServerLogFile", ex);
        } finally {
            try {
                fileReader.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(HeadlessServerConsole.class.getName())
                        .log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        return logHtml;
    }
}

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class GuiStream extends PrintStream {

    //~ Instance fields --------------------------------------------------------

    protected JTextPane theGuiComponent;
    SimpleAttributeSet set = null;
    boolean logLastInput = false;
    long lastInput = -1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new GuiStream object.
     *
     * @param  out           DOCUMENT ME!
     * @param  component     DOCUMENT ME!
     * @param  set           DOCUMENT ME!
     * @param  logLastInput  DOCUMENT ME!
     */
    public GuiStream(final OutputStream out,
            final JTextPane component,
            final SimpleAttributeSet set,
            final boolean logLastInput) {
        super(out);
        this.logLastInput = logLastInput;
        this.set = set;
        theGuiComponent = component;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     */
    @Override
    public void println(final String s) {
        if (logLastInput) {
            lastInput = System.currentTimeMillis();
        }
        final Runnable update = new Runnable() {

                @Override
                public void run() {
                    try {
                        theGuiComponent.getDocument()
                                .insertString(theGuiComponent.getDocument().getLength(), s + "\n", set);
                    } catch (javax.swing.text.BadLocationException ble) {
                        // no printstacktrace possible else chain reaction
                    }
                    // theGuiComponent.setText(theGuiComponent.getText() + "\n<p>" + s+"<p>");
                }
            };
        javax.swing.SwingUtilities.invokeLater(update);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public long getLastInputTime() {
        return lastInput;
    }

    @Override
    public void println(final Object o) {
        if (o == null) {
            println("null");
        } else {
            println(o.toString()); // theGuiComponent.setText(theGuiComponent.getText() + "\n" + o.toString());
        }
    }
}

//class Serverinformation extends DefaultTableModel {
//    Vector v;
//
//
//    public Class getColumnClass(int columnIndex) {
//        return String.class;
//    }
//
//    public int getColumnCount() {
//        return 2;
//    }
//
//    public String getColumnName(int columnIndex) {
//        return "";
//    }
//
//    public int getRowCount() {
//
//    }
//
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        if (rowIndex==0 && columnIndex==0) {
//            return "Server Online seit:";
//        }
//        else if (rowIndex==1 && columnIndex==0) {
//            return "Ohne Fehler seit:";
//        }
//        if (rowIndex==0 && columnIndex==0) {
//            return "Server Online seit:";
//        }
//        else if (rowIndex==1 && columnIndex==0) {
//            return "Ohne Fehler seit:";
//        }
//
//    }
//
//    public boolean isCellEditable(int rowIndex, int columnIndex) {
//        return false;
//    }
//
//
//    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//
//    }
//
//
//
//}
