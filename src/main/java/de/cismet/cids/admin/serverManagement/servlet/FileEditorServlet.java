/*
 * FileEditor.java
 *
 * Created on 2. August 2004, 11:56
 */

package de.cismet.cids.admin.serverManagement.servlet;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

import de.cismet.cids.admin.serverManagement.*;
import org.apache.log4j.Logger;
import org.apache.log4j.*;

/**
 *
 * @author  oaltpeter
 * @version
 */
public class FileEditorServlet extends HttpServlet
{
    private HeadlessServerConsole serverCon = null;   
    private File file = null;
    private String httpPort = "";
    
    private ServerManager serverManager;
    private String serverIP = "";
    
    private String cancel = null;
    
    private String fileName = "Keine Konfigurationsdatei gefunden";
    private String configFileContent = null;    
    private String textToSave = null;
    
    Logger logger = Logger.getLogger(this.getClass());
    
    
    
    /**
     * Shows a JavaScript confirm box to warn the user from unsaved changes by
     * leaving the FileEditorServlet.
     *
     * @param out   a PrintWriter to create HTML code in this method     
     */
    private void unsavedChangesWarning(PrintWriter out)
    {
        out.println("<script type=\"text/javascript\">\n" +
                    "<!--\n" +                    
                    "check = confirm(\"Wollen Sie die \u00C4nderungen speichern?\\n\\nOK: \u00C4nderungen speichern.\\nCancel: FileEditor-Fenster ohne Speichern schlie\u00DFen.\");\n" +
                    "if (check == true) {\n" +
                        "SaveFile();\n" +
                    "} else {\n" +
                        "this.close();\n" +
                    "}//-->\n" +
                    "</script>\n");       
    }  
    
    
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);        
    }
    
    
    
    /** Destroys the servlet.
     */
    public void destroy()
    {
        
    }
    
    
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {               
        response.setContentType("text/html");        
        PrintWriter out = response.getWriter();
        
        if (serverCon == null)
        {
            serverCon = HeadlessServerConsole.getInstance();
        }                
        
        // variable configFileContent is allocated with an empty string, 
        // otherwise the whole content of the file would be added to the 
        // variable's text at every time the editor window is opened again:
        configFileContent = "";
                
        file = serverCon.getCidsServerConfigFile();        
        httpPort = serverCon.getMiniatureServerPort();
        serverIP = serverManager.getServerIP();            
        
        if (file != null) {            
            
            fileName = file.getName();
            
            textToSave = request.getParameter("editorArea");            
            cancel = request.getParameter("cancel");           
            
            if (textToSave != null && !cancel.equals("true"))
            {                   
                try
                {
                    if ( file.exists() || file.canRead() )
                    {
                        FileWriter f = new FileWriter(file);
                        f.write(textToSave);
                        f.close();
                    }
                } catch (Exception e)
                {
                    logger.error("Fehler beim Speichern der Datei " + file + ".", e);
                }          
            }
            
            if (cancel == null || cancel.equals("false"))
            {
                try
                {
                    BufferedReader in = new BufferedReader( new FileReader( file ));
                    String line;
                    while ((line = in.readLine()) != null)
                        configFileContent += (line + "\n");
                }
                catch ( Exception e )
                {
                    logger.error("Problem beim Laden der Konfigurationsdatei des cids Servers.", e);
                }
            } else
            
            //if (cancel.equals("true"))
            {
                configFileContent = textToSave;
            }            
            
        } else {
            configFileContent = "Keine Konfigurationsdatei gefunden.";
        }                  
        
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
        out.println("<HTML>\n" +
                    "<HEAD>\n" +
                        "<TITLE>FileEditor</TITLE>\n" +
                        "<meta http-equiv=\"pragma\" content=\"no-cache\">\n" +
                        "<meta http-equiv=\"cache-control\" content=\"no-cache\">\n" +
                        "<script type=\"text/javascript\">\n" +
                        "<!--\n" +                            
                            "function SaveFile() {\n" +                                
                                "document.editorFormular.cancel.value = \"false\";\n" +
                                "document.editorFormular.submit();\n" +                                                                
                            "}\n" +
                            "function SendWarning() {\n" +                                
                                "if (document.editorFormular.contentChanged.value == \"true\") {\n" +
                                    "document.editorFormular.cancel.value = \"true\";\n" +                                    
                                    "document.editorFormular.submit();\n" +
                                "} else {\n" +
                                    "this.close();\n" +
                                "}\n" +
                            "}\n" +
                        "//-->\n" +
                        "</script>\n" +
                        //"<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssStylesheet + "\">" +
                    "</HEAD>\n" +
                    "<BODY text=\"#6C779F\" bgcolor=\"#FFFFFF\" link=\"#6C779F\" alink=\"#6C779F\" vlink=\"#6C779F\">\n" +
                    "<table width=\"100%\" border=\"0\" background=\"/img/blueBG.gif\">\n" +
                        "<tr>\n" +
                            "<td width=\"60%\" height=\"10\" valign=\"middle\"><img src=\"/img/CidsLogo2.gif\" alt=\"cids Logo\" border=\"0\"><font size=\"+2\" face=\"Courier New\" color=\"#FFFFFF\"><b>&nbsp;Servermanager</b></font></td>\n" +
                            "<td width=\"40%\" height=\"10\" valign=\"middle\" align=\"right\"><img src=\"/img/CismetLogo.gif\" alt=\"cismet Logo\" border=\"0\"></td>\n" +
                        "</tr>\n" +
                    "</table>\n" +
                    "<img src=\"/img/top.gif\" border=\"0\">\n" +
                    "<br><font size=\"+1\" face=\"Courier New\"><b>" + fileName + " auf " + serverIP + "</b></font><br><br>\n" +
                    "<form name=\"editorFormular\" action=\"http://" + serverIP + ":" + httpPort + "/fileeditor\" method=\"post\">\n" +
                        "<textarea name=\"editorArea\" cols=\"88\" rows=\"22\" onKeyup=\"document.editorFormular.contentChanged.value=true\">\n" +
                            configFileContent +
                        "</textarea>\n" +
                        "<input type=\"hidden\" name=\"cancel\" value=\"false\">\n" +
                        "<input type=\"hidden\" name=\"contentChanged\" value=\"false\">\n" +
                        "<table width=\"100%\" border=\"0\">\n" +
                            "<tr>\n" +
                                "<td width=\"50%\" height=\"10\" align=\"center\"><a href=\"javascript:SaveFile()\"><img src=\"/img/save.png\" alt=\"Speichern\" border=\"0\">&nbsp;Speichern</a></td>\n" +
                                "<td width=\"50%\" height=\"10\" align=\"center\"><a href=\"javascript:SendWarning()\"><img src=\"/img/tray.png\" alt=\"Abbrechen\" border=\"0\">&nbsp;Abbrechen</a></td>\n" +
                            "</tr>\n" + 
                        "</table>\n" +
                    "</form>");
        
        if (cancel != null && cancel.equals("true"))
        {
            if (file != null) {
                unsavedChangesWarning(out);
            } else {
                out.println("<script type=\"text/javascript\">\n" +
                            "<!--\n" +
                            "this.close();\n" +
                            "//-->\n" +
                            "</script>\n");
            }
            
        }    
               
        out.println("</BODY>\n" +
                    "</HTML>");
        
        out.close();
    }
    
    
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Short description";
    }
    
}
