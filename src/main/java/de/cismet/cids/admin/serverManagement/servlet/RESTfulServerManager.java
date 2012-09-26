/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.admin.serverManagement.servlet;

import org.apache.log4j.Logger;

import org.openide.util.Exceptions;

import java.io.*;

import java.net.URL;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.ws.rs.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import de.cismet.cids.admin.serverManagement.HeadlessServerConsole;

/**
 * DOCUMENT ME!
 *
 * @author   dmeiers
 * @version  $Revision$, $Date$
 */
@Path("/")
public class RESTfulServerManager {

    //~ Static fields/initializers ---------------------------------------------

    /*
     * Attribute to indicate a valid user authentication in the http-request-session
     */
    private static final String IS_VALID = "is_valid";
    private static final String VALIDATION_ATTRIBUTE = "validation_attribute";
    private static final String LOGIN_PARAMETER_NAME = "username";
    private static final String LOGOUT_PARAMETER_VALUE = "logout";

    //~ Instance fields --------------------------------------------------------

    Logger logger = Logger.getLogger(this.getClass());
    private File baseFile;
    private HeadlessServerConsole serverCon;
    private boolean developmentFlag = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RESTfulServerManager object.
     */
    public RESTfulServerManager() {
        serverCon = HeadlessServerConsole.getInstance();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Checks wether the user has already been authenticated. Within the authentication process a special attribute of
     * the user's http session will be added. This allows to check if the user has already entered a valid user/pass
     * combo.
     *
     * @param   request  The user's request object
     *
     * @return  true, if the user has already been authenticated
     */
    private boolean isValidatedSession(final HttpServletRequest request) {
        boolean ret = false;

        if ((request != null)
                    && (request.getSession() != null)
                    && (request.getSession().getAttribute(VALIDATION_ATTRIBUTE) != null)) {
            final String validationAttribute = (String)request.getSession().getAttribute(VALIDATION_ATTRIBUTE);
            if (validationAttribute.equals(IS_VALID)) {
                ret = true;
            }
        }

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   user      DOCUMENT ME!
     * @param   password  DOCUMENT ME!
     * @param   request   DOCUMENT ME!
     *
     * @throws  Exception              UnauthorizedException DOCUMENT ME!
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    @POST
    @Path("/login")
    @Produces("text/plain")
    public void validateUser(@FormParam("user") final String user,
            @FormParam("password") final String password,
            @Context final HttpServletRequest request) throws Exception {
        if (request != null) {
            if (serverCon != null) {
                if (serverCon.validateUser("", user, password)) {
                    // fine
                    final HttpSession session = request.getSession(true);
                    session.setAttribute(VALIDATION_ATTRIBUTE, IS_VALID);
                    session.setMaxInactiveInterval(500);

                    // remove disabled status from buttons...
// final org.jsoup.select.Elements elems = doc.getElementsByClass("switch");

// elems.removeClass(DISABLED);
                } else {
                    throw new IllegalStateException("Wrong username and / or password");
                }
            } else {
                logger.error("SERVLET::Keine Referenz auf Serverkonsole");
            }
        }
    }

    /**
     * Checks wether a logout is in progress. This is done by checking the parameter <b>login<b>. If the parameter has
     * the value <code>logout</code> a logout is in progress.</b></b>
     *
     * @param   request  DOCUMENT ME!
     *
     * @return  true if a logout is in progress
     */
    private boolean isLogoutInProgress(final HttpServletRequest request) {
        boolean ret = false;

        final String loginParam = request.getParameter(LOGIN_PARAMETER_NAME);
        if ((loginParam != null) && loginParam.equals(LOGOUT_PARAMETER_VALUE)) {
            ret = true;
        }
        return ret;
    }

    /**
     * Invalidates the ongoing user session.
     *
     * @param  request  DOCUMENT ME!
     */
    @POST
    @Path("/logout")
    public void performLogout(@Context final HttpServletRequest request) {
        request.getSession(false).invalidate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   request  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @GET
    @Produces("text/plain")
    @Path("/checkValidationStatus")
    public String checkValidationStatus(@Context final HttpServletRequest request) {
        if (isValidatedSession(request)) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @GET
    @Produces("text/html")
    public String showMainApp() {
        // logger.info("get cidsservermanager - return html site");
        BufferedReader bfr;
        String easyDevelopmentProperty = null;
        if (serverCon.getRuntimeProperties().containsKey("serverConsole.easyDevelopment")) {
            easyDevelopmentProperty = serverCon.getRuntimeProperties().getProperty("serverConsole.easyDevelopment");
        } else if (serverCon.getRuntimeProperties().containsKey("serverConsole.webinterface.easyDevelopment")) {
            easyDevelopmentProperty = serverCon.getRuntimeProperties().getProperty("serverConsole.easyDevelopment");
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("could not find easyDevelopmentProperty");
            }
        }
        if (easyDevelopmentProperty != null) {
            if (easyDevelopmentProperty.equals("true") || easyDevelopmentProperty.equals(1)) {
                developmentFlag = true;
            } else {
                developmentFlag = false;
            }
        }

        try {
            if (developmentFlag) {
                baseFile = new File("./webinterface/serverConsole.html");
                final FileReader fr = new FileReader(baseFile);
                bfr = new BufferedReader(fr);
            } else {
                final URL url = this.getClass()
                            .getClassLoader()
                            .getResource("de/cismet/cids/admin/serverManagement/webserverroot/serverConsole.html");
                baseFile = new File(url.getFile());

                bfr = new BufferedReader(new InputStreamReader(
                            this.getClass().getClassLoader().getResourceAsStream(
                                "de/cismet/cids/admin/serverManagement/webserverroot/serverConsole.html")));
            }
            String result = "";
            String line = bfr.readLine();
            while (line != null) {
                result += line + "\n";
                line = bfr.readLine();
            }
            return result;
        } catch (IOException ex) {
            logger.error("fehler beim laden des html file", ex);
        }
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param   request  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @GET
    @Path("/runtime.properties")
    @Produces("text/plain")
    public String getRuntimeProperties(@Context final HttpServletRequest request) {
        if (isValidatedSession(request)) {
            FileReader fr = null;
            try {
                final File f = new File("runtime.properties");
                fr = new FileReader(f);
                final BufferedReader bfr = new BufferedReader(fr);
                String result = "";
                try {
                    String line = bfr.readLine();
                    while (line != null) {
                        result += line + "\n";
                        line = bfr.readLine();
                    }
                    return result;
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                } finally {
                    try {
                        bfr.close();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                try {
                    fr.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param   request  DOCUMENT ME!
     * @param   name     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @GET
    @Path("/serverLogFile")
    @Produces("text/html")
    public String getServerLogFile(@Context final HttpServletRequest request,
            @QueryParam("name") final String name) {
//        String logFile = "";
        String result = "";
        if (isValidatedSession(request)) {
            if (serverCon != null) {
                FileReader fr = null;
                try {
//                     logFile = serverCon.getServerLogFile();
                    final File logDir = serverCon.getlogOutputDirectory();
                    final File logFile = new File(logDir.getCanonicalPath() + System.getProperty("file.separator")
                                    + name);

                    if (logFile == null) {
                        throw new IllegalStateException("LogFile " + name + " does not exist");
                    }
                    fr = new FileReader(logFile);
                    final BufferedReader bfr = new BufferedReader(fr);
                    String line = bfr.readLine();
                    while (line != null) {
                        result += line + "\n";
                        line = bfr.readLine();
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                } finally {
                    try {
                        fr.close();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }
        return result;
//        return logFile;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Path("/serverTitle")
    @GET
    @Produces("text/plain")
    public String getServerTitle() throws IOException {
        final Properties runtimeProps = serverCon.getRuntimeProperties();

        String serverTitle = "";
        if (runtimeProps.containsKey("serverConsole.serverTitle")) {
            serverTitle = runtimeProps.getProperty("serverConsole.serverTitle");
        } else if (runtimeProps.containsKey("serverTitle")) {
            serverTitle = runtimeProps.getProperty("serverTitle");
        }
        return serverTitle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @GET
    @Path("/dbConnection")
    @Produces("text/plain")
    public String getDBConnection() throws IOException {
        final Properties runtimeProps = serverCon.getRuntimeProperties();

        if (runtimeProps.containsKey("connection.url")) {
            final String connectionURL = runtimeProps.getProperty("connection.url");

            return connectionURL;
        }
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @GET
    @Path("/serverFriends")
    @Produces("text/plain")
    public String getServerConsoleFriends() throws IOException {
        final Properties runtimeProps = serverCon.getRuntimeProperties();
        String serverConsoleFriends = "";

        if (runtimeProps.containsKey("serverConsole.webinterface.friends")) {
            serverConsoleFriends = runtimeProps.getProperty("serverConsole.webinterface.friends");
        } else if (runtimeProps.containsKey("serverConsole.friends")) {
            serverConsoleFriends = runtimeProps.getProperty("serverConsole.friends");
        }

        return serverConsoleFriends;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @GET
    @Path("/serverLogfiles")
    @Produces("text/plain")
    public String getServerLogfiles() throws IOException {
        final Properties runtimeProps = serverCon.getRuntimeProperties();
        String serverLogFiles = "";

        if (runtimeProps.containsKey("serverConsole.webinterface.logfiles")) {
            serverLogFiles = runtimeProps.getProperty("serverConsole.webinterface.logfiles");
        } else if (runtimeProps.containsKey("serverConsole.Logfiles")) {
            serverLogFiles = runtimeProps.getProperty("serverConsole.Logfiles");
        }

        return serverLogFiles;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @GET
    @Path("/serverConsole")
    @Produces("text/plain")
    public String getServerConsoleOutput() {
        return serverCon.getLogMessages();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  textToSave  DOCUMENT ME!
     */
    @POST
    @Path("/saveRuntimeProps")
    public void saveRuntimeProperties(@FormParam("textarea") final String textToSave) {
//            final File file = serverCon.getCidsServerConfigFile();
        final File file = new File("runtime.properties");

        if ((textToSave != null)) {
            FileWriter fw = null;
            BufferedWriter f = null;
            try {
                // logger.info(file.getCanonicalPath());
                if (file.exists() || file.canRead()) {
                    fw = new FileWriter(file.getAbsoluteFile());
                    f = new BufferedWriter(fw);
                    f.write(textToSave);
                    f.close();
                }
            } catch (Exception e) {
                logger.error("Fehler beim Speichern der Datei " + file + ".", e);
            } finally {
                try {
                    f.close();
                    fw.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  request  DOCUMENT ME!
     */
    @POST
    @Path("/clearConsoleOutput")
    public void clearConsoleOutput(@Context final HttpServletRequest request) {
        if (isValidatedSession(request) && (serverCon != null)) {
            serverCon.clearLogMessages();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  request DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @GET
    @Path("/isReachable")
    @Produces("text/plain")
    public String isReachable(@QueryParam("url") final String url) {
        return url;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  server   DOCUMENT ME!
     * @param  request  DOCUMENT ME!
     */
    @POST
    public void doServerTask(@FormParam("server") final String server, @Context final HttpServletRequest request) {
        if (isValidatedSession(request)) {
            if (server != null) {
                if (server.equals("stop")) {
                    serverCon.shutdownServer();
                } else if (server.equals("restart")) {
                    if (!serverCon.isServerRunning()) {
                        serverCon.startServer();
                    }
                }
            }
        }
    }
}
