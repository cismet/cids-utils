/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.admin.serverManagement;

import com.sun.jersey.spi.container.servlet.ServletContainer;

import org.apache.log4j.Logger;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.*;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import java.util.HashMap;

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

        final String resoursceBaseDir = this.getClass()
                    .getClassLoader()
                    .getResource("de/cismet/cids/admin/serverManagement/webserverroot")
                    .toExternalForm();

        final Context rootContext = new Context(server, "/", Context.SESSIONS); // NOI18N
        rootContext.setHandler(new ResourceHandler());
//        rootContext.setResourceBase("webinterface");                            // NOI18N
        rootContext.setResourceBase(resoursceBaseDir);                          // NOI18N
        final MovedContextHandler mch = new MovedContextHandler(server, "/", "/cidsservermanager");
        final HashMap map = new HashMap<String, String>();
        map.put("org.mortbay.jetty.servlet.SessionCookie", "XSESSIONID" + port);
        map.put("org.mortbay.jetty.servlet.SessionURL", "xsessionid");

        final ServletHolder servlet = new ServletHolder(ServletContainer.class);

        servlet.setInitParameter(
            "com.sun.jersey.config.property.resourceConfigClass",
            "com.sun.jersey.api.core.PackagesResourceConfig");
        servlet.setInitParameter(
            "com.sun.jersey.config.property.packages",
            "de.cismet.cids.admin.serverManagement.servlet");
        final Context managerContext = new Context(server, "/cidsservermanager", Context.SESSIONS); // NOI18N
        managerContext.setResourceBase(resoursceBaseDir);
        managerContext.addServlet(servlet, "/");
        managerContext.setInitParams(map);                                                          // NOI18N

        final ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(
            new Handler[] {
                mch,
                rootContext,
                managerContext,
            });

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
