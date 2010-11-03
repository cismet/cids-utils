/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.admin.serverManagement;

import org.apache.log4j.Logger;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import de.cismet.cids.admin.serverManagement.servlet.FileEditorServlet;
import de.cismet.cids.admin.serverManagement.servlet.ServerLogFile;
import de.cismet.cids.admin.serverManagement.servlet.ServerManager;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class SimpleWebServer {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(SimpleWebServer.class);

    //~ Instance fields --------------------------------------------------------

    private Server server = null;
    private final transient int port;
    private boolean initialised;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SimpleWebServer object.
     */
    public SimpleWebServer() {
        this(8181);
    }

    /**
     * Creates a new SimpleWebServer object.
     *
     * @param  port  DOCUMENT ME!
     */
    public SimpleWebServer(final int port) {
        if (LOG.isInfoEnabled()) {
            LOG.info("creating new SimpleWebServer on port: " + port); // NOI18N
        }
        this.port = port;
        initialised = false;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("initialising webserver"); // NOI18N
        }
        // Create an instance of the jetty server
        server = new Server(port);

        final Context rootContext = new Context(server, "/", Context.SESSIONS); // NOI18N
        rootContext.setResourceBase("webinterface");                            // NOI18N
        rootContext.setHandler(new ResourceHandler());

        final Context editorContext = new Context(server, "/fe", Context.SESSIONS); // NOI18N
        final ServletHolder editorHolder = new ServletHolder(new FileEditorServlet());
        editorContext.addServlet(editorHolder, "/fileeditor");                    // NOI18N

        final Context managerContext = new Context(server, "/", Context.SESSIONS); // NOI18N
        final ServletHolder managerHolder = new ServletHolder(new ServerManager());
        managerContext.addServlet(managerHolder, "/cidsservermanager");            // NOI18N

        final Context logContext = new Context(server, "/lf", Context.SESSIONS); // NOI18N
        final ServletHolder logHolder = new ServletHolder(new ServerLogFile());
        logContext.addServlet(logHolder, "/serverlogfile");                    // NOI18N

        final Context imageContext = new Context(server, "/img", Context.SESSIONS); // NOI18N
        imageContext.setResourceBase("webinterface/img");                           // NOI18N
        imageContext.setHandler(new ResourceHandler());

        final Context logResourceContext = new Context(server, "/log", Context.SESSIONS); // NOI18N
        logResourceContext.setResourceBase("webinterface");                               // NOI18N
        logResourceContext.setHandler(new ResourceHandler());

        final ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(
            new Handler[] { rootContext, managerContext, editorContext, logContext, imageContext, logResourceContext });

        final HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { contexts, new DefaultHandler() });

        server.setHandlers(handlers.getHandlers());

        initialised = true;
        if (LOG.isDebugEnabled()) {
            LOG.debug("initialisation done"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void start() throws Exception {
        if (LOG.isInfoEnabled()) {
            LOG.info("starting webserver"); // NOI18N
        }
        // lazy init
        if (!initialised) {
            init();
        }
        server.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void stop() throws Exception {
        if (LOG.isInfoEnabled()) {
            LOG.info("stopping webserver"); // NOI18N
        }
        server.stop();
    }
}
