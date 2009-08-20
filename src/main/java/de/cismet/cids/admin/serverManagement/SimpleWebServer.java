/*
 * SimpleWebServer.java
 *
 * Created on 16. Juni 2005, 16:18
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package de.cismet.cids.admin.serverManagement;

import java.io.FileInputStream;
import java.net.*;
import java.util.Properties;
import org.mortbay.http.*;
import org.mortbay.jetty.servlet.*;
import org.mortbay.http.handler.*;

public class SimpleWebServer {
    
    private HttpServer server = null;
    private int port = 8181;
    
    public SimpleWebServer() throws Exception {
        init();
        
    }
    
    public SimpleWebServer(int port) throws Exception {
        this.port = port;
        init();
    }
    
    private void init() throws Exception {
        
        
        // Create an instance of the jetty server
        server = new HttpServer();
        
        
        // Create a port listener
        //SocketListener listener=new SocketListener();
        //listener.setPort(port);
        
        
        server.addListener(InetAddress.getLocalHost().getHostAddress()+":"+ port);
        server.addListener("127.0.0.1:"+ port);
        
        // Create a context
        HttpContext context = new HttpContext();
        context.setContextPath("/");
        server.addContext(context);
        
        // Root
        HttpContext rootContext = new HttpContext();
        rootContext.setContextPath("/");
        rootContext.setResourceBase("./webinterface/");
        rootContext.addHandler(new ResourceHandler() );
        server.addContext(rootContext);
        
        // Images
        HttpContext imgContext = new HttpContext();
        imgContext.setContextPath("/img/*");
        imgContext.setResourceBase("./webinterface/img" +
                "");
        imgContext.addHandler(new ResourceHandler() );
        server.addContext(imgContext);
        
        // Logs
        HttpContext logContext = new HttpContext();
        logContext.setContextPath("/log/*");
        logContext.setResourceBase("./webinterface/");
        logContext.addHandler(new ResourceHandler() );
        server.addContext(logContext);
        
        
        // Create a servlet container
        ServletHandler servlets = new ServletHandler();
        context.addHandler(servlets);
        
        // Map a servlet onto the container
        Properties runtimeProperties = new Properties();
        runtimeProperties.load(new FileInputStream("runtime.properties"));
                
        String consoleType = runtimeProperties.
                getProperty("servlet./cidsservermanager.code");
        servlets.addServlet("cidsservermanager","/cidsservermanager/*",
                "de.cismet.cids.admin.serverManagement.servlet.ServerManager");
        servlets.addServlet("fileeditor","/fileeditor/*",
                "de.cismet.cids.admin.serverManagement.servlet.FileEditorServlet");
        servlets.addServlet("serverlogfile","/serverlogfile/*",
                "de.cismet.cids.admin.serverManagement.servlet.ServerLogFile");

        
        // Serve static content from the context
//    String home = System.getProperty("jetty.home",".");
//    context.setResourceBase(home+"/demo/webapps/jetty/tut/");
//    context.addHandler(new ResourceHandler());
        
        
    }
    
    public void start() throws Exception {
        // Start the http server
        server.start();
    }
    
    public void stop() throws InterruptedException {
        server.stop();
    }
}