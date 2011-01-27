/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ServerManager.java
 *
 * Created on 7. April 2004, 09:40
 */
package de.cismet.cids.admin.serverManagement.servlet;

import org.apache.log4j.*;
import org.apache.log4j.Logger;

import java.io.*;

import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

import de.cismet.cids.admin.serverManagement.*;

/**
 * DOCUMENT ME!
 *
 * @author   oaltpeter
 * @version  DOCUMENT ME!
 */
public class ServerManager extends HttpServlet {

    //~ Static fields/initializers ---------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = -5869885937076876145L;
    private static String serverIP = "request.getServerName() fehlgeschlagen!";

    /* Attribute to indicate a valid user authentication
     * in the http-request-session
     */
    private static final String IS_VALID = "is_valid";
    private static final String VALIDATION_ATTRIBUTE = "validation_attribute";
    private static final String LOGIN_PARAMETER_NAME = "login";
    private static final String LOGIN_PARAMETER_VALUE = "validate";
    private static final String LOGOUT_PARAMETER_VALUE = "logout";

    //~ Instance fields --------------------------------------------------------

    // private String cssStylesheet = null;

    Logger logger = Logger.getLogger(this.getClass());

    private String servertask = null;

    private HeadlessServerConsole serverCon = null;
    private String serverType = "Cids Server Manager";
    private String serverStatus = "Fehler!";
    private String httpPort = "Fehler!";

    private File logfile = new File("");
    private String logMessages = "";

    private boolean autoRefresh = true;

    //~ Methods ----------------------------------------------------------------

    // *************************************************************************
    // Methods to control the cids server via a ServerConsole instance. -start-
    // *************************************************************************

    /**
     * Stops the cids server.
     */
    private void stopServer() {
        if (serverCon.isServerRunning()) {
            serverCon.shutdownServer();
        }
    }

    /**
     * Restarts the cids server.
     */
    private void restartServer() {
        if (serverCon.isServerRunning()) {
            serverCon.shutdownServer();
        }
        if (!serverCon.isServerRunning()) {
            serverCon.startServer();
        }
    }

    /**
     * Calls a servlet to edit the cids server's config file.
     *
     * @param  out  a PrintWriter to create HTML code in this method
     */
    private void editServerConfigFile(final PrintWriter out) {
        out.println("<script type=\"text/javascript\">\n"
                    + "<!--\n"
                    + "window.open(\"/fe/fileeditor\", \"EditorWindow" + httpPort
                    + "\", \"width=750,height=550,left=25,top=25,resizable=yes\");\n" // , location=yes, menubar=yes
                    + "//-->\n"
                    + "</script>\n");
    }

    /**
     * DOCUMENT ME!
     *
     * @param  out  DOCUMENT ME!
     */
    private void getServerLogFile(final PrintWriter out) {
        out.println("<script type=\"text/javascript\">\n"
                    + "<!--\n"
                    + "window.open(\"/lf/serverlogfile\", \"Serverlog" + httpPort
                    + "\", \"width=750,height=550,left=25,top=25,scrollbars=yes,resizable=yes\");\n"
                    + "//-->\n"
                    + "</script>\n");
    }

    /**
     * Refreshes the cids server's logging output within the iFrame.
     */
    private void refreshLogMessages() {
        logMessages = serverCon.getLogMessages();
        // replacement of "<" and ">" because the logging output will be transformed into a HTML file for the iframe:
        logMessages = logMessages.replaceAll("<", "&lt ");
        logMessages = logMessages.replaceAll(">", " &gt");
        // blank after "&lt", otherwise Mozilla would show "&lt" instead of "<"
        try {
            if (!logfile.exists()) {
                logfile = new File(serverCon.getlogOutputDirectory() + File.separator + "logOutput" + httpPort
                                + ".htm");
            }
            logfile.deleteOnExit();
            final FileWriter f = new FileWriter(logfile);

            if (autoRefresh) {
                f.write(
                    "<html><head><title>LogOutput</title><meta http-equiv=\"pragma\" content=\"no-cache\"><meta http-equiv=\"cache-control\" content=\"no-cache\"></head><body text=\"#000000\" bgcolor=\"#FFFFFF\"><pre>"
                            + logMessages
                            + "</pre><a name=\"anchor\"></a></body></html>");
                // meta tags "pragma" and "cache-control" avert the Browser from caching the site
            } else {
                f.write(
                    "<html><head><title>LogOutput</title><meta http-equiv=\"pragma\" content=\"no-cache\"><meta http-equiv=\"cache-control\" content=\"no-cache\"></head><body text=\"#606060\" bgcolor=\"#FFFFFF\"><pre>"
                            + logMessages
                            + "</pre><a name=\"anchor\"></a></body></html>");
                // meta tags "pragma" and "cache-control" avert the Browser from caching the site
            }

            f.close();
        } catch (Exception e) {
            logger.error("Fehler beim Erstellen der oder beim Schreiben in die Datei logOutput" + httpPort + ".htm!",
                e);
        }
    }

    /**
     * Clears the content of the iFrame.
     */
    private void clearLogMessages() {
        serverCon.clearLogMessages();
    }

    /*private void killServer(PrintWriter out) {
     *  if (serverCon.isServerRunning()) {     out.println("<script type=\"text/javascript\">\n" + "<!--\n" +     "Check
     * = confirm(\"Der Serverprozess l&auml;uft noch! Wenn Sie das Servermanagement jetzt beenden, wird der Server evtl.
     * nicht ordnungsgem&auml;&szlig; heruntergefahren.\\nDiese Managementkonsole wird auch beendet werden!\\nWollen Sie
     * das Servermanagement trotzdem beenden?\");\n" +                 "if (Check == true) {\n" +
     * "window.open(\"http://" + serverIP + ":" + httpPort + "/cidsservermanager?server=kill-now\", \"_self\");\n" +
     *        "};\n" +                 "//-->\n" +           "</script>\n"); } if (!serverCon.isServerRunning()) {
     * out.println("<script type=\"text/javascript\">\n" +                 "<!--\n" +                 "Check =
     * confirm(\"\\nWenn Sie das Servermanagement beenden, wird auch diese Managementkonsole beendet werden!\\nWollen
     * Sie das Servermanagement wirklich beenden?\");\n" +                 "if (Check == true) {\n" +
     * "window.open(\"http://" + serverIP + ":" + httpPort + "cidsservermanager?server=kill-now\", \"_self\");\n" +
     *       "};\n" +
     *             "//-->\n" +                 "</script>\n"); }}*/

    /*private void killServerNow() {
     *  serverCon.exit();}*/

    // *************************************************************************
    // Methods to control the cids server via a ServerConsole instance. -end-
    // *************************************************************************

    /**
     * Initializes the servlet.
     *
     * @param   config  DOCUMENT ME!
     *
     * @throws  ServletException  DOCUMENT ME!
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Destroys the servlet.
     */
    @Override
    public void destroy() {
    }

    /**
     * Depending on the given serverTask the corresponding method will be invoked.
     *
     * @param  servertask  serverType ActionString indicating what to do
     */
    private void dispatchAction(final String servertask) {
        if (servertask != null) {
            if (servertask.equals("stop")) {
                stopServer();
            }

            if (servertask.equals("restart")) {
                restartServer();
            }

            // call for editServerConfigFile(out) in HTML part because of the
            // JavaScript code that is written in this method!

            if (servertask.equals("refresh")) {
                refreshLogMessages();
            }

            if (servertask.equals("clear")) {
                clearLogMessages();
            }

            if (servertask.equals("autoRefresh_off")) {
                autoRefresh = false;
            }

            if (servertask.equals("autoRefresh_on")) {
                autoRefresh = true;
            }
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param   request   servlet request
     * @param   response  servlet response
     *
     * @throws  ServletException  DOCUMENT ME!
     * @throws  IOException       DOCUMENT ME!
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        final PrintWriter out = response.getWriter();

        final HttpSession session = request.getSession(true);

        // ServerConsole
        if (serverCon == null) {
            serverCon = HeadlessServerConsole.getInstance();
        }

        printHeader(out);

        if (!isValidatedSession(request)) {
            /**
             * Either show the login page or valide
             * the login
             */
            if (isValidationInProgress(request)) {
                // Let's validate!
                if (validateLogin(request)) {
                    // Show the main app
                    printServerManager(request, response, out);
                } else {
                    printLogin(out, "Die Anmeldung ist fehlgeschlagen.");
                }
            } else {
                // No validation in progress, thus show login
                printHeader(out);
                printLogin(out);
                printFooter(out);
            }
        } else {
            if (isLogoutInProgress(request)) {
                performLogout(request, out);
            } else {
                // Show the main app
                printServerManager(request, response, out);
            }
        }
        out.close();
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
     * @param  out      DOCUMENT ME!
     */
    private void performLogout(final HttpServletRequest request, final PrintWriter out) {
        request.getSession(false).invalidate();

        out.println("Sie wurden erfolgreich ausgeloggt.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param  request   DOCUMENT ME!
     * @param  response  DOCUMENT ME!
     * @param  out       DOCUMENT ME!
     */
    private void printServerManager(final HttpServletRequest request,
            final HttpServletResponse response,
            final PrintWriter out) {
        // authentication ok, go ahead

        httpPort = serverCon.getMiniatureServerPort();
        serverType = serverCon.getServerType();

        try {
            serverIP = request.getServerName();
        } catch (Exception e) {
            logger.error("Die IP des Rechners mit dem cids Server konnte nicht ermittelt werden.", e);
        }

        servertask = request.getParameter("server");

        // *********************************************************************
        // Check the request parameter. -start-
        // *********************************************************************

        dispatchAction(servertask);

        /*if (servertask != null && servertask.equals("kill-now")) {
         *  logger.debug("killServerNow() aufgerufen."); killServerNow();}*/

        // *********************************************************************
        // Check the request parameter. -end-
        // *********************************************************************

        // set the serverStatus variable:
        if (serverCon.isServerRunning()) {
            this.serverStatus = "l&auml;uft";
        } else {
            this.serverStatus = "ist gestoppt";
        }

        refreshLogMessages();

        printHeader(out);

        printBody(out);

        printOutputTable(out);

        // End of the output table

        if ((servertask != null) && servertask.equals("edit")) {
            editServerConfigFile(out);
        }

        if ((servertask != null) && servertask.equals("serverlog")) {
            getServerLogFile(out);
        }

        /*if (servertask != null && servertask.equals("kill")) {
         *  logger.debug("killServer(out) aufgerufen."); killServer(out);}*/

        printFooter(out);
    }

    /**
     * Prints the html header of the server manager.
     *
     * @param  out  DOCUMENT ME!
     */
    private void printHeader(final PrintWriter out) {
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
        out.println("<HTML>\n"
                    + "<HEAD>\n"
                    + "<TITLE>" + serverType + "</TITLE>\n"
                    + "<meta http-equiv=\"pragma\" content=\"no-cache\">\n"
                    + "<meta http-equiv=\"cache-control\" content=\"no-cache\">\n");
        // if (autoRefresh) { out.println("<meta http-equiv=\"refresh\" content=\"5; URL=http://" + serverIP + ":" +
        // httpPort + "/" + "cidsservermanager\">\n"); }
        out.println("<script type=\"text/javascript\">\n"
                    + "<!--\n"
                    + "function Refresh() {\n"
                    + "window.setTimeout(\"document.refreshFormular.submit();\", 5000);"
                    + "}\n"
                    + "//-->\n"
                    + "</script>\n" // "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssStylesheet + "\">" +
                    + "</HEAD>\n");
    }

    /**
     * Prints the beginning of the HTML-Body including the beginning of the ouput table.
     *
     * @param  out  DOCUMENT ME!
     */
    private void printBody(final PrintWriter out) {
        if (autoRefresh) {
            out.println(
                "<BODY text=\"#6C779F\" bgcolor=\"#FFFFFF\" link=\"#6C779F\" alink=\"#6C779F\" vlink=\"#6C779F\" onload=\"javascript:Refresh()\">\n");
        } else {
            out.println(
                "<BODY text=\"#6C779F\" bgcolor=\"#FFFFFF\" link=\"#6C779F\" alink=\"#6C779F\" vlink=\"#6C779F\">\n");
        }
        out.println("<form name=\"refreshFormular\" action=\"cidsservermanager\" method=\"get\"></form>\n"
                    + "<table width=\"100%\" border=\"0\" background=\"/img/blueBG.gif\">\n"
                    + "<tr>\n"
                    + "<td width=\"60%\" height=\"10\" valign=\"middle\"><img src=\"/img/CidsLogo2.gif\" alt=\"cids Logo\" border=\"0\"><font size=\"+2\" face=\"Courier New\" color=\"#FFFFFF\"><b>&nbsp;Servermanager</b></font></td>\n"
                    + "<td width=\"40%\" height=\"10\" valign=\"middle\" align=\"right\"><img src=\"/img/CismetLogo.gif\" alt=\"cismet Logo\" border=\"0\"></td>\n"
                    + "</tr>\n"
                    + "</table>\n"
                    + "<img src=\"/img/top.gif\" border=\"0\">\n"
                    + "<br><font size=\"+1\" face=\"Courier New\"><b>" + serverType + " auf " + serverIP
                    + "</b></font>\n"
                    + "<table width=\"100%\" border=\"1\" rules=\"rows\" bgcolor=\"#FFFFFF\">\n"
                    + "<tr>\n"
                    + "<td width=\"25%\" bgcolor=\"#737DA4\"><font face=\"Courier New\" color=\"#FFFFFF\"><b>Status &amp; Bedienung</b></font></td>\n"
                    + "<td width=\"75%\" bgcolor=\"#737DA4\"></td>\n"
                    + "</tr>\n"
                    + "<tr>\n");
        if (autoRefresh) {
            out.println("<td valign=\"middle\"><font face=\"Courier New\" size=\"-1\">Der Server " + serverStatus
                        + ".</font></td>\n");
        } else {
            out.println("<td valign=\"middle\"><font face=\"Courier New\" size=\"-1\" color=\"#606060\">Der Server "
                        + serverStatus + ".</font></td>\n");
        }
        out.println("<td valign=\"middle\">\n"
                    + "<a href=\"cidsservermanager?server=stop\">\n"
                    + "<img src=\"/img/exit_space.png\" alt=\"Server beenden\" border=\"0\">&nbsp;Server beenden</a>&nbsp;&nbsp;&nbsp;\n"
                    + "<a href=\"cidsservermanager?server=restart\">\n"
                    + "<img src=\"/img/restart_space.png\" alt=\"Server neustarten\" border=\"0\">&nbsp;Server neustarten</a>&nbsp;&nbsp;&nbsp;\n"
                    + "<a href=\"cidsservermanager?server=edit\">\n"
                    + "<img src=\"/img/edit_space.png\" alt=\"Konfigurationsdatei editieren\" border=\"0\">&nbsp;Konfigurationsdatei editieren</a>&nbsp;&nbsp;&nbsp;\n"
                    + "<a href=\"cidsservermanager?login="
                    + LOGOUT_PARAMETER_VALUE + "\">\n"
                    + "<img src=\"/img/logout.png\" alt=\"Ausloggen\" border=\"0\">&nbsp;Ausloggen</a>&nbsp;&nbsp;&nbsp;\n"
                    + "<a href=\"cidsservermanager?server=serverlog\">\n"
                    + "<img src=\"/img/serverlogfile.png\" alt=\"Serverlogfile\" border=\"0\">&nbsp;Server Logfile</a>&nbsp;&nbsp;&nbsp;\n"
                    + "</td>\n"
                    + "</tr>\n"
                    + "</table>\n"
                    + "<br>\n"
                    +

                    // Beginning of the output Table
                    "<table width=\"100%\" border=\"1\" rules=\"rows\" bgcolor=\"#FFFFFF\">\n");
    }

    /**
     * Prints most of the output table (containing the server log).
     *
     * @param  out  HTML-Out print writer
     */
    private void printOutputTable(final PrintWriter out) {
        out.println(
            "<tr>\n"
                    + "<td width=\"15%\" bgcolor=\"#737DA4\"><font face=\"Courier New\" color=\"#FFFFFF\"><b>Ausgabe</b></font></td>\n"
                    + "<td width=\"85%\" bgcolor=\"#737DA4\">&nbsp;</td>\n"
                    + "</tr>\n"
                    + "<tr>\n"
                    + "<td align=\"center\" valign=\"top\">\n"
                    + "<br><a href=\"cidsservermanager?server=refresh\">\n"
                    + "<img src=\"/img/reload.png\" alt=\"Aktualisieren\" border=\"0\"><br>Aktualisieren</a><br>\n"
                    + "<a href=\"cidsservermanager?server=clear\"><br>\n"
                    + "<img src=\"/img/delete.png\" alt=\"Ausgabefenster l&ouml;schen\" border=\"0\"><br>Ausgabefenster l&ouml;schen</a><br>\n");
        if (autoRefresh) {
            out.println("<a href=\"cidsservermanager?server=autoRefresh_off\"><br>\n"
                        + "<img src=\"/img/stop_auto_refresh.png\" alt=\"Automatisches Scrollen ausschalten\" border=\"0\"><br>Automatisches Scrollen ausschalten</a>\n");
        } else {
            out.println("<a href=\"cidsservermanager?server=autoRefresh_on\"><br>\n"
                        + "<img src=\"/img/start_auto_refresh.png\" alt=\"Automatisches Scrollen anschalten\" border=\"0\"><br>Automatisches Scrollen anschalten</a>\n");
        }
        out.println("</td>\n"
                    +

                    "<td><font face=\"Courier New\" size=\"-1\"><br>\n"
                    +

                    "<script type=\"text/javascript\">\n"
                    + "<!--\n"
                    + "if (screen.width <= 1024)\n"
                    + " {document.write(\"<iframe src=\\\"/log/logOutput" + httpPort
                    + ".htm#anchor\\\" width=\\\"825\\\" height=\\\"330\\\" name=\\\"logOutput\\\">\");}\n"
                    + "else \n"
                    + " {if (screen.width <= 1152)\n"
                    + "  {document.write(\"<iframe src=\\\"/log/logOutput" + httpPort
                    + ".htm#anchor\\\" width=\\\"920\\\" height=\\\"420\\\" name=\\\"logOutput\\\">\");}\n"
                    + " else \n"
                    + "  {document.write(\"<iframe src=\\\"/log/logOutput" + httpPort
                    + ".htm#anchor\\\" width=\\\"1030\\\" height=\\\"515\\\" name=\\\"logOutput\\\">\");}\n"
                    + "}"
                    + "//-->\n"
                    + "</script>\n"
                    +

                    "<p>Die Logging-Ausgabe sollte hier in einem eingebetteten Frame angezeigt werden. "
                    + "Ihr Browser kann jedoch leider keine eingebetteten Frames anzeigen. "
                    + "Bitte benutzen Sie als Browser z.B. Mozilla, Netscape ab Version 5 "
                    + "oder MS Internet Explorer ab Version 2!</p>"
                    + "</iframe><br><br>"
                    +

                    "</td>\n");
    }

    /**
     * DOCUMENT ME!
     *
     * @param  out  DOCUMENT ME!
     */
    private void printLogin(final PrintWriter out) {
        printLogin(out, "");
    }

    /**
     * Prints a login form.
     *
     * @param  out  DOCUMENT ME!
     * @param  msg  Message to display at the top of the login page
     */
    private void printLogin(final PrintWriter out, final String msg) {
        out.println("<form name=\"refreshFormular\" action=\"cidsservermanager\" method=\"get\"></form>\n"
                    + "<table width=\"100%\" border=\"0\" background=\"/img/blueBG.gif\">\n"
                    + "<tr>\n"
                    + "<td width=\"60%\" height=\"10\" valign=\"middle\"><img src=\"/img/CidsLogo2.gif\" alt=\"cids Logo\" border=\"0\"><font size=\"+2\" face=\"Courier New\" color=\"#FFFFFF\"><b>&nbsp;Servermanager</b></font></td>\n"
                    + "<td width=\"40%\" height=\"10\" valign=\"middle\" align=\"right\"><img src=\"/img/CismetLogo.gif\" alt=\"cismet Logo\" border=\"0\"></td>\n"
                    + "</tr>\n"
                    + "</table>\n");

        out.println(
            "<h3>"
                    + msg
                    + "<h3>"
                    + "<table align=\"center\"><tr><td><form name=\"loginForm\" method=\"get\" action=\"cidsservermanager\">"
                    + "<input type=\"hidden\" name=\"login\" value=\"validate\">"
                    + "<table width=\"50%\" border=\"0\">\n"
//                + "<tr>\n"
//                + "<td>Benutzergruppe:</td>\n"
//                + "<td align=\"left\">\n"
//                + "<input type=\"text\" name=\"usergroup\">\n"
//                + "</td>\n"
//                + "</tr>\n"
                    + "<tr>\n"
                    + "<td>Domainserver:</b></td>\n"
                    + "<td align=\"right\">\n"
                    + serverCon.getRuntimeProperties().getProperty("serverName")
                    + "</td>\n"
                    + "</tr>\n"
                    + "<tr>\n"
                    + "<td>Benutzername:</td>\n"
                    + "<td align=\"left\">\n"
                    + "<input type=\"text\" name=\"username\">\n"
                    + "</td>\n"
                    + "</tr>\n"
                    + "<tr>\n"
                    + "<td>Passwort:</td>\n"
                    + "<td><input type=\"password\" name=\"password\"></td>\n"
                    + "</tr>\n"
                    + "<tr><td colspan=\"2\"><input type=\"submit\" value=\"Login\"><td>\n"
                    + "</table>\n"
                    + "</form></td>\n");
    }

    /**
     * Prints the HTML-Footer including the end of the output table.
     *
     * @param  out  DOCUMENT ME!
     */
    private void printFooter(final PrintWriter out) {
        out.println(
            "</tr>\n"
                    + "</table>\n"
                    + "</BODY>\n"
                    + "</HTML>");
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param   request   servlet request
     * @param   response  servlet response
     *
     * @throws  ServletException  DOCUMENT ME!
     * @throws  IOException       DOCUMENT ME!
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
        IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param   request   servlet request
     * @param   response  servlet response
     *
     * @throws  ServletException  DOCUMENT ME!
     * @throws  IOException       DOCUMENT ME!
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
        IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    /**
     * Gets the IP address under which the server is running.
     *
     * @return  the IP address under which the server is running
     */
    public static String getServerIP() {
        return serverIP;
    }

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
     * Checks if a validation is in progress. A validation is considered to be in progress if the corresponding action
     * parameter is found.
     *
     * @param   request  DOCUMENT ME!
     *
     * @return  true if a validation is in progress
     */
    private boolean isValidationInProgress(final HttpServletRequest request) {
        boolean ret = false;

        final String loginParamValue = request.getParameter(LOGIN_PARAMETER_NAME);

        if ((loginParamValue != null) && loginParamValue.equals(LOGIN_PARAMETER_VALUE)) {
            ret = true;
        }

        return ret;
    }

    /**
     * Performs the actual user validation by comparing the given usergroup, username, password combination with the
     * corresponding database.
     *
     * @param   request  HTTP-Request containing the needed informations as request parameters.
     *
     * @return  DOCUMENT ME!
     */
    private boolean validateLogin(final HttpServletRequest request) {
        boolean ret = false;

        if (request != null) {
            final String user = request.getParameter("username");
            final String userGroup = request.getParameter("usergroup");
            final String password = request.getParameter("password");

            if (serverCon != null) {
                if (serverCon.validateUser(userGroup, user, password)) {
                    // fine
                    final HttpSession session = request.getSession(true);
                    session.setAttribute(VALIDATION_ATTRIBUTE, IS_VALID);
                    session.setMaxInactiveInterval(500);

                    ret = true;
                }
            } else {
                System.err.println("SERVLET::Keine Referenz auf Serverkonsole");
            }
        }
        return ret;
    }
}
