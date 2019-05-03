/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.admin.serverManagement;

import Sirius.server.ServerExit;
import Sirius.server.ServerExitError;
import Sirius.server.ServerStatus;
import Sirius.server.newuser.User;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import de.cismet.tools.CismetThreadPool;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import java.sql.PreparedStatement;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten.hell@cismet.de
 * @version  $Revision$, $Date$
 */
public class HeadlessServerConsole {

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
    protected static final int START_HELP_FILE = 1;
    protected static final int MAIN_HELP_FILE = 2;
    protected static final String DEFAULT_MINIATURE_SERVER_PORT = "82";
    protected static Boolean startsWithGui = false;
    protected static HeadlessServerConsole instance;
    protected static String serverManagementRoot = null;
    protected static File cidsServerConfigFile;
    protected static boolean startMiniatureServer = true;
    protected static StringBuffer logStringBuffer = new StringBuffer();
    protected static MyPrintStream mySysOut;
    protected static MyPrintStream mySysErr;
    static SimpleAttributeSet STANDARD = new SimpleAttributeSet();
    static SimpleAttributeSet INFO = new SimpleAttributeSet();
    static SimpleAttributeSet ERROR = new SimpleAttributeSet();
    static Logger logger;

    //~ Instance fields --------------------------------------------------------

    protected boolean serverRunnin = false;
    protected String[] serverArgs = null;
    protected String serverClassName = null;
    protected Object serverInstance = null;
    protected Method getServerInstance = null;
    protected Method mainMethod = null;
    protected Class serverClass = null;
    protected JTextPane theGuiComponent = null;
    protected ServerConsoleGui theGuiFrame = null;
    protected String serverType = null;
    protected String log4jConfig = "default";
    protected String miniatureServerPort = null;
    protected String miniatureServerConfig = "default";
    protected String serverLogFileName;
    protected long serverStartTime = -1;
    protected SimpleWebServer minuatureServerInstance = null;
    protected File logOutputDirectory;
    protected File workpath = null;
    FileEditor fileEditor = null;
    private Integer logOutputLimit = 100;
    private Integer logCleanerInterval = 60000;
    private Properties runtimeProperties;
    private String webInfAdminUser = null;
    private String webInfAdminPw = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of HeadlessServerConsole.
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    protected HeadlessServerConsole(final String[] args) {
        if (instance != null) {
            throw new RuntimeException("Es existiert bereits eine ServerConsole-Instanz!");
        }
        instance = this;

        initHeadlessServerConsole(args);
    }

    /**
     * Creates a new HeadlessServerConsole object.
     *
     * @param   args       DOCUMENT ME!
     * @param   hasGui     DOCUMENT ME!
     * @param   component  DOCUMENT ME!
     * @param   gui        DOCUMENT ME!
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    protected HeadlessServerConsole(final String[] args,
            final Boolean hasGui,
            final JTextPane component,
            final ServerConsoleGui gui) {
        if (instance != null) {
            throw new RuntimeException("Es existiert bereits eine ServerConsole-Instanz!");
        }

        instance = this;
        startsWithGui = hasGui;
        theGuiComponent = component;
        theGuiFrame = gui;

        initHeadlessServerConsole(args);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static HeadlessServerConsole getInstance() {
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
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    private void initHeadlessServerConsole(final String[] args) {
        if (startsWithGui) {
            StyleConstants.setForeground(
                INFO,
                (java.awt.Color)javax.swing.UIManager.getDefaults().get("CheckBoxMenuItem.selectionBackground"));
            StyleConstants.setForeground(ERROR, Color.red);
            StyleConstants.setForeground(STANDARD, Color.black);
            setMyOutStream(theGuiComponent, startsWithGui, INFO);
            setMyErrStream(theGuiComponent, startsWithGui, ERROR);
        } else {
            // we set the property, because the toolkit will fail to initialise on headless environments
            System.setProperty("java.awt.headless", "true");
            setMyOutStream(null, startsWithGui, STANDARD);
            setMyErrStream(null, startsWithGui, STANDARD);
        }

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
         * command line parameters: parameter : -t serverType -n serverClassName -l log4jConfig -p miniatureServerPort -s
         * serverManagementRoot -c miniatureServerConfig -a cidsServerArgs1 cidsServerArgs2 ... referred to : cidsServ
         * cidsServ ServerCon MiniServ MiniServ and ServerCon MiniServ cidsServ needed by : ServerCon ServerCon
         * ServerCon MiniServ MiniServ and ServerCon MiniServ cidsServ optional optional optional optional optional
         *
         * conditions: - parameter with switch -t must be the first, parameter with switch -n must be the second -
         * parameter with switch -a must be the last - if a configuration file of the cids server is specified, it must
         * be the first parameter behind the switch -a
         *
         * hints: - if the switch -p is not specified, the Miniature Server won't be started - the path specifications
         * log4jConfig and miniatureServerConfig can be absolute or relative
         */
        final int argl = args.length;
        int argn = 0;
        int control = 1;

        final Map parameter = new HashMap();

        setRuntimeProperties(new Properties());
        try {
            cidsServerConfigFile = new File("runtime.properties"); // NOI18N
            getRuntimeProperties().load(new FileInputStream(cidsServerConfigFile));
            System.out.println("runtime.properties gefunden");
            if (getRuntimeProperties().containsKey("serverConsole.serverTitle")) {
                parameter.put("serverType", getRuntimeProperties().getProperty("serverConsole.serverTitle"));
            } else {
                parameter.put("serverType", getRuntimeProperties().getProperty("serverTitle"));
            }

            if (getRuntimeProperties().containsKey("serverConsole.serverClass")) {
                parameter.put("serverClassName", getRuntimeProperties().getProperty("serverConsole.serverClass"));
            } else {
                parameter.put("serverClassName", getRuntimeProperties().getProperty("serverClass"));
            }

            if (getRuntimeProperties().containsKey("serverConsole.log4jConfig")) {
                parameter.put("log4jConfig", getRuntimeProperties().getProperty("serverConsole.log4jConfig"));
            } else {
                parameter.put("log4jConfig", getRuntimeProperties().getProperty("log4jConfig"));
            }

            if (getRuntimeProperties().containsKey("serverConsole.webinterface.webserverPort")) {
                parameter.put(
                    "miniatureServerPort",
                    getRuntimeProperties().getProperty("serverConsole.webinterface.webserverPort"));
            } else {
                parameter.put("miniatureServerPort", getRuntimeProperties().getProperty("webserverPort"));
            }

            if (getRuntimeProperties().containsKey("serverConsole.managementRoot")) {
                parameter.put(
                    "serverManagementRoot",
                    getRuntimeProperties().getProperty("serverConsole.managementRoot"));
            } else {
                parameter.put("serverManagementRoot", getRuntimeProperties().getProperty("managementRoot"));
            }

            if (getRuntimeProperties().containsKey("serverConsole.webinterface.webserverInterfaceConfig")) {
                parameter.put(
                    "miniatureServerConfig",
                    getRuntimeProperties().getProperty("serverConsole.webinterface.webserverInterfaceConfig"));
            } else {
                parameter.put("miniatureServerConfig", getRuntimeProperties().getProperty("webserverInterfaceConfig"));
            }

            if (getRuntimeProperties().containsKey("serverConsole.cidsServerRuntimeArgs")) {
                serverArgs = getRuntimeProperties().getProperty("serverConsole.cidsServerRuntimeArgs").split(" ");
            } else {
                serverArgs = getRuntimeProperties().getProperty("runtimeArgs").split(" ");
            }

            serverLogFileName = getRuntimeProperties().getProperty("log4j.appender.ErrorHtml.file");
            parameter.put("cidsServerArgs", serverArgs);
        } catch (IOException skip) {
            skip.printStackTrace();
        }
        /*
         * Start a timer that clears the logStringBuffer to avoid memory issues if there are plenty of log messages
         */

        if (getRuntimeProperties().containsKey("serverConsole.logOutputLimit")) {
            try {
                logOutputLimit = Integer.parseInt(getRuntimeProperties().getProperty("serverConsole.logOutputLimit"));
            } catch (NumberFormatException e) {
                logger.error(
                    "Could not parse property serverConsole.logOutputLimit in runtime.properties. Must be a valid Number. Setting limit to "
                            + logOutputLimit,
                    e);
            }
            if (getRuntimeProperties().containsKey("serverConsole.logOutputCleanerInterval")) {
                try {
                    final int intervalInSecs = Integer.parseInt(getRuntimeProperties().getProperty(
                                "serverConsole.logOutputCleanerInterval"));
                    logCleanerInterval = intervalInSecs * 1000;
                } catch (NumberFormatException e) {
                    logger.error(
                        "Could not parse property serverConsole.logOutputCleanerInterval in runtime.properties. Must be a valid Number. Setting interval to "
                                + logCleanerInterval,
                        e);
                }
            }
            final Timer logStringTimer = new Timer();
            logStringTimer.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        final String[] logLines = logStringBuffer.toString().split("\n");
                        if (logLines.length > logOutputLimit) {
                            String result = "";
                            for (int i = logLines.length - 1 - logOutputLimit; i < logLines.length; i++) {
                                result += logLines[i];
                                result += "\n";
                            }
                            clearLogMessages();
                            logStringBuffer.append(result);
                        }
                    }
                }, logCleanerInterval, logCleanerInterval);
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
                    for (int i = 0; i < (argl - control); i++) {
                        ++argn;
                        serverArgs[i] = args[argn];
                    }
                    parameter.put("cidsServerArgs", serverArgs);
                } else {
                    if (startsWithGui) {
                        JOptionPane.showMessageDialog(
                            theGuiFrame,
                            java.util.ResourceBundle.getBundle(
                                "de/cismet/cids/admin/serverManagement/resources").getString(
                                "ungueltiger_schalter_angegeben"),
                            "cids ServerConsole",
                            JOptionPane.ERROR_MESSAGE);
                        theGuiFrame.showHelp(START_HELP_FILE);
                    }
                    System.err.println(
                        java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources").getString(
                            "ungueltiger_schalter_angegeben"));
                    System.exit(0);
                }
                ++argn;
            }
        }
        if (argn != argl) {
            if (startsWithGui) {
                JOptionPane.showMessageDialog(
                    theGuiFrame,
                    argn
                            + ","
                            + argl
                            + " "
                            + java.util.ResourceBundle.getBundle(
                                "de/cismet/cids/admin/serverManagement/resources").getString(
                                "ungueltige_kommandozeilenparameterangabe"),
                    "cids ServerConsole",
                    JOptionPane.ERROR_MESSAGE);
                theGuiFrame.showHelp(START_HELP_FILE);
            }
            System.exit(0);
        }

        /*
         * Process command line parameters.
         *
         */
        if (parameter.get("serverType") != null) {
            serverType = (String)parameter.get("serverType");
        } else {
            System.err.println("Parameter serverType wurde nicht angegeben!");
        }

        if (parameter.get("serverClassName") != null) {
            serverClassName = (String)parameter.get("serverClassName");
        } else {
            if (startsWithGui) {
                JOptionPane.showMessageDialog(
                    theGuiFrame,
                    "<html>Parameter serverClassName wurde nicht angegeben"
                            + "!<br>Es kann kein cids Server gestartet werden.<br>"
                            + ""
                            + "Die ServerConsole wird beendet!</html>",
                    "cids ServerConsole",
                    JOptionPane.ERROR_MESSAGE);
                theGuiFrame.showHelp(START_HELP_FILE);
            }
            System.err.println(
                "<html>Parameter serverClassName wurde nicht angegeben!"
                        + "<br>Es kann kein cids Server gestartet werden.<br>Die "
                        + "ServerConsole wird beendet!</html>");
            System.exit(0);
        }

        if (parameter.get("log4jConfig") != null) {
            log4jConfig = (String)parameter.get("log4jConfig");
        } else {
            System.out.println("Parameter log4jFile wurde nicht angegeben.");
        }

        /*
         * If there's no valid port information, the default port is used.
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
        } else {
            workpath = new File(System.getProperty("user.dir"));
            System.out.println(
                "Parameter serverManagementRoot wurde nicht angegeben.\nDas Arbeitsverzeichnis des cids Servermanagements ist "
                        + workpath
                        + ".");
        }

        if (parameter.get("miniatureServerConfig") != null) {
            miniatureServerConfig = (String)parameter.get("miniatureServerConfig");
        } else {
            System.out.println("Parameter miniatureServerConfig wurde nicht angegeben.");
        }

        if (parameter.get("cidsServerArgs") == null) {
            serverArgs = new String[0];
            System.out.println(java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
                        .getString("Fuer_den_cids_Server_wurden_keine_Parameter_angegeben"));
        }

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
        HeadlessServerConsole.logger.getLogger("org.mortbay").setLevel((Level)Level.OFF);
        HeadlessServerConsole.logger = HeadlessServerConsole.logger.getLogger(HeadlessServerConsole.class);

        if (startsWithGui) {
            try {
                theGuiFrame.getLblIcon()
                        .setIcon(new javax.swing.ImageIcon(
                                getClass().getResource(
                                    "/de/cismet/cids/admin/serverConsole/serverSymbols/32/"
                                    + serverClassName
                                    + ".png")));
                theGuiFrame.setIconImage(new javax.swing.ImageIcon(
                        getClass().getResource(
                            "/de/cismet/cids/admin/serverConsole/serverSymbols/16/"
                                    + serverClassName
                                    + ".png")).getImage());
            } catch (Exception e) {
                theGuiFrame.getLblIcon()
                        .setIcon(new javax.swing.ImageIcon(
                                getClass().getResource(
                                    "/de/cismet/cids/admin/serverConsole/serverSymbols/32/default.png")));
                theGuiFrame.setIconImage(new javax.swing.ImageIcon(
                        getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/32/default.png"))
                            .getImage());
            }

            theGuiFrame.setTitle(theGuiFrame.getTitle() + " - " + serverType);
            theGuiFrame.getLblName().setText(serverType);
        }

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
                    logger.error(java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources")
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
                        logger.error(java.util.ResourceBundle.getBundle(
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

            /*
             * Read out and, if necessary, complete the absolute path for the temporary logging files of the servlet
             * from the Miniature Server's configuration file.
             *
             */
            try {
                if (serverProperties.containsKey("serverConsole.logOutputDirectory")) {
                    logOutputDirectory = new File(serverProperties.getString("serverConsole.logOutputDirectory"));
                } else {
                    logOutputDirectory = new File(serverProperties.getString("LogOutputDirectory"));
                }
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
            } else {
            }
            if (!logOutputDirectory.isDirectory()) {
                logOutputDirectory = workpath;
                System.err.println(java.util.ResourceBundle.getBundle(
                        "de/cismet/cids/admin/serverManagement/resources").getString(
                        "Das_Verzeichnis_f?r_die_Logging_Dateien_konnte_nicht_erstellt_werden") + logOutputDirectory
                            + "\" wird verwendet.");
            }
            logOutputDirectory.mkdirs();

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
            if (startsWithGui) {
                theGuiFrame.getRootPane()
                        .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                        .put(configLoggerKeyStroke, "CONFIGLOGGING");
                theGuiFrame.getRootPane().getActionMap().put("CONFIGLOGGING", configAction);
            }
        }

        // Get a class object and main method object of cids server:
        try {
            serverClass = Class.forName(serverClassName);
            mainMethod = serverClass.getMethod("main", new Class[] { String[].class });
            getServerInstance = serverClass.getMethod("getServerInstance", null);
        } catch (java.lang.NoClassDefFoundError e) {
            logger.fatal("Eine Klasse konnte nicht geladen werden der Server beendet sich", e);
            if (startsWithGui) {
                JOptionPane.showMessageDialog(
                    theGuiFrame,
                    "Eine Klasse konnte nicht geladen werden "
                            + e.getMessage()
                            + " der Server beendet sich",
                    "cids ServerConsole",
                    JOptionPane.ERROR_MESSAGE);
            }
            System.err.println("Eine Klasse konnte nicht geladen werden " + e.getMessage()
                        + " der Server beendet sich");
            System.exit(0);
        } catch (Throwable e) {
            System.err.println(java.util.ResourceBundle.getBundle(
                    "de/cismet/cids/admin/serverManagement/resources").getString(
                    "Der_Servername_ist_falsch_oder_unterstuetzt_nicht_das_cids-Servermanagement"));
            logger.fatal(java.util.ResourceBundle.getBundle(
                    "de/cismet/cids/admin/serverManagement/resources").getString(
                    "Der_Servername_ist_falsch_oder_unterstuetzt_nicht_das_cids-Servermanagement"),
                e);
            if (startsWithGui) {
                JOptionPane.showMessageDialog(
                    theGuiFrame,
                    java.util.ResourceBundle.getBundle(
                        "de/cismet/cids/admin/serverManagement/resources").getString(
                        "Der_Servername_ist_falsch_oder_unterstuetzt_"
                                + "nicht_das_cids-Servermanagement"),
                    "cids ServerConsole",
                    JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void startMiniatureServer() {
        try {
            minuatureServerInstance = new SimpleWebServer(Integer.parseInt(miniatureServerPort));
            minuatureServerInstance.start();
            logger.info("web server port: " + miniatureServerPort);
        } catch (Throwable e) {
            logger.error("Fehler beim Starten des WebServers", e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void startServer() {
        if (startsWithGui) {
            theGuiFrame.getPrbStatus().setIndeterminate(true);
            theGuiFrame.getLblAmpel().setIcon(theGuiFrame.getYellow());
        }
        serverRunnin = true;

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
                                    if (startsWithGui) {
                                        theGuiFrame.getLblAmpel().setIcon(theGuiFrame.getGreen());
                                        theGuiFrame.getPrbStatus().setIndeterminate(
                                            false);
                                    }
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
                        // FIXME: catch Throwable
                    } catch (Throwable t) {
                        t.printStackTrace();
                        serverStopped();
                        System.err.println(
                            "\nBeim Starten des Servers ist ein Fehler aufgetreten\nDer Server befindet sich in einem undefinierten Zustand!!!\nBeenden Sie die ServerConsole!!!\n");
                        logger.fatal("Beim Starten des cids Servers ist ein Fehler aufgetreten.", t);
                        if (startsWithGui) {
                            theGuiFrame.getLblAmpel().setIcon(theGuiFrame.getYellow());
                        }
                    }
                }
            }).start();
    }

    /**
     * DOCUMENT ME!
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
                try {
                    System.out.println(java.util.ResourceBundle.getBundle(
                            "de/cismet/cids/admin/serverManagement/resources").getString(
                            "Der_Server_wurde_ordnungsgemaess_beendet"));
                } catch (Exception ex) {
                    logger.fatal("Could not load ResourceBundle", ex);
                    System.out.println("Der Server wurde ordnungsgem�� beendent");
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
            // FIXME: catch Throwable
        } catch (Throwable t) {
            t.printStackTrace();
            serverStopped();
            System.err.println(
                "\nBeim Beenden des Servers ist ein Fehler aufgetreten\nDer Server befindet sich in einem undefinierten Zustand!!!\nBeenden Sie die ServerConsole!!!\n");
            logger.fatal("Beim Beenden des cids Servers ist ein Fehler aufgetreten.", t);
            if (startsWithGui) {
                theGuiFrame.getLblAmpel().setIcon(theGuiFrame.getYellow());
            }
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

        try {
            /*
             * Method only available with the server type "domain server"
             * (Sirius.server.middleware.impls.domainserver.DomainServerImpl)
             */

            // Get instance of the running server, e.g. proxy, registry, domain server
            serverInstance = getServerInstance.invoke(serverClass, null);

            if (serverInstance instanceof Sirius.server.middleware.impls.domainserver.DomainServerImpl) {
                final User user = new User(-1, userName, "");

                final Method validateUser = serverClass.getMethod(
                        "validateUser",
                        new Class[] { User.class, String.class });
                final Boolean refRet = (Boolean)validateUser.invoke(serverInstance, new Object[] { user, password });
                ret = refRet.booleanValue() && isUserAdmin(userName);
            } else if (serverInstance instanceof Sirius.server.middleware.impls.proxy.StartProxy) {
                ret = validateAdminUser(userName, password);
            } else if (serverInstance instanceof Sirius.server.registry.Registry) {
                ret = validateAdminUser(userName, password);
            } else {
                throw new Exception("UnknownServerType during authentication occurred: "
                            + serverInstance.getClass().getName() + ".");
            }
        } catch (final Exception t) {
            System.err.println("\nBeim Validieren des Benutzers " + userName + " ist ein Fehler aufgetreten.!!!\n");
            logger.fatal("Beim Validieren des Benutzers " + userName + " ist ein Fehler aufgetreten.", t);
        }

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   userName  DOCUMENT ME!
     * @param   password  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean validateAdminUser(final String userName, final String password) {
        final Properties prop = getRuntimeProperties();

        if ((this.webInfAdminUser == null)
                    && prop.containsKey("serverConsole.webinterface.adminCredentials.username")) {
            webInfAdminUser = prop.getProperty(
                    "serverConsole.webinterface.adminCredentials.username");
        }

        if ((this.webInfAdminPw == null)
                    && prop.containsKey("serverConsole.webinterface.adminCredentials.password")) {
            webInfAdminPw = prop.getProperty("serverConsole.webinterface.adminCredentials.password");
        }

        if ((webInfAdminUser != null) && (webInfAdminPw != null)) {
            if (userName.equals(webInfAdminUser) && webInfAdminPw.equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     */
    protected void shutdownMiniatureServer() {
        try {
            if (minuatureServerInstance != null) {
                minuatureServerInstance.stop();
            }
        } catch (Exception e) {
            logger.error("Fehler beim Stoppen Webservers.", e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void serverStopped() {
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    serverRunnin = false;
                    serverStartTime = -1;
                    if (startsWithGui) {
                        theGuiFrame.getLblAmpel().setIcon(theGuiFrame.getRed());
                        theGuiFrame.getPrbStatus().setIndeterminate(false);
                    }
                }
            });
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
     * DOCUMENT ME!
     */
    protected void displayServerStatus() {
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
            if (startsWithGui) {
                JOptionPane.showMessageDialog(theGuiFrame, "Serverstatus:\n" + statString);
            }
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
            serverStopped();
            if (startsWithGui) {
                theGuiFrame.getLblAmpel().setIcon(theGuiFrame.getYellow());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  theGuiComponent  DOCUMENT ME!
     * @param  hasGui           DOCUMENT ME!
     * @param  set              DOCUMENT ME!
     */
    protected void setMyOutStream(final JTextPane theGuiComponent, final Boolean hasGui, final SimpleAttributeSet set) {
        mySysOut = new MyPrintStream(System.out, logStringBuffer, theGuiComponent,
                false, hasGui, set);
        System.setOut(mySysOut);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  theGuiComponent  DOCUMENT ME!
     * @param  hasGui           DOCUMENT ME!
     * @param  set              DOCUMENT ME!
     */
    protected void setMyErrStream(final JTextPane theGuiComponent, final Boolean hasGui, final SimpleAttributeSet set) {
        mySysErr = new MyPrintStream(System.err, logStringBuffer, theGuiComponent,
                true, hasGui, set);
        System.setErr(mySysErr);
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
            if (serverLogFileName == null) {
                logHtml = "LOGFILE NOT AVAILABLE, CHECK YOUR RUNTIME PROPERTIES"; // NOI18N
            } else {
                final File logfile = new File(serverLogFileName.trim());
                fileReader = new BufferedReader(new FileReader(logfile));
                while ((line = fileReader.readLine()) != null) {
                    logHtml += (line + "\n");
                }
            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(HeadlessServerConsole.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.error("Could not read ServerLogFile", ex);
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(HeadlessServerConsole.class.getName())
                        .log(java.util.logging.Level.SEVERE, null, ex);
            }
        }

        return logHtml;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        final HeadlessServerConsole con = new HeadlessServerConsole(args);
        Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    System.out.println("Server beenden!");
                    con.shutdownServer();
                }
            });

        CismetThreadPool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (startMiniatureServer) {
                            con.startMiniatureServer();
                        }
                    } catch (Throwable e) {
                        System.out.println("Miniature Web Server konnte nicht gestartet werden!");
                        e.printStackTrace();
                    }
                }
            });

        con.startServer();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getLogMessages() {
        final String logMessages = logStringBuffer.toString();
        return logMessages;
    }

    /**
     * DOCUMENT ME!
     */
    public void clearLogMessages() {
        logStringBuffer.setLength(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   userName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isUserAdmin(final String userName) {
        boolean ret = false;
        try {
            Class.forName(getRuntimeProperties().getProperty("connection.driver_class"));
            final Connection dbCon = DriverManager.getConnection(
                    getRuntimeProperties().getProperty("connection.url"),
                    getRuntimeProperties().getProperty("connection.username"),
                    getRuntimeProperties().getProperty("connection.password"));
            final PreparedStatement stmnt = dbCon.prepareStatement("Select administrator from cs_usr where login_name = ?");
            stmnt.setString(1, userName);
            final ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                ret = rs.getBoolean("administrator");
            }
        } catch (Exception ex) {
            System.err.println("Anmeldung fehlgeschlagen \n"
                        + "User verfügt nicht über die benötigten Admin-Rechte");
            System.out.println(ex);
            ret = false;
        }
        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Properties getRuntimeProperties() {
        return runtimeProperties;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  runtimeProperties  DOCUMENT ME!
     */
    public void setRuntimeProperties(final Properties runtimeProperties) {
        this.runtimeProperties = runtimeProperties;
    }
}
