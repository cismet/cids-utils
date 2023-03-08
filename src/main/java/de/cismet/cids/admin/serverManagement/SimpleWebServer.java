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

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

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

        /*
         * generate Contexts with an ResourceHandler that simply deliver (css, js, img) files. This is necessary since
         * we forward requests for root to /cidsservermanager. In that case also the path for that (css, js, img) files
         * changes.
         *
         */
        final ServletContextHandler rootContext = new ServletContextHandler(
                server,
                "/",
                ServletContextHandler.SESSIONS); // NOI18N
        rootContext.setHandler(new ResourceHandler());
        rootContext.setResourceBase(resoursceBaseDir);
        // NOI18N
        final ServletContextHandler cssContext = new ServletContextHandler(
                server,
                "/css",
                ServletContextHandler.SESSIONS);               // NOI18N
        cssContext.setHandler(new ResourceHandler());
        cssContext.setResourceBase(resoursceBaseDir + "/css"); // NOI18N

        final ServletContextHandler jsContext = new ServletContextHandler(
                server,
                "/js",
                ServletContextHandler.SESSIONS);             // NOI18N
        jsContext.setHandler(new ResourceHandler());
        jsContext.setResourceBase(resoursceBaseDir + "/js"); // NOI18N

        final ServletContextHandler imgContext = new ServletContextHandler(
                server,
                "/img",
                ServletContextHandler.SESSIONS);               // NOI18N
        imgContext.setHandler(new ResourceHandler());
        imgContext.setResourceBase(resoursceBaseDir + "/img"); // NOI18N

        // generate the Context that is responsible for delivering the RESTfulServerManager jersey app
// final HashMap map = new HashMap<String, String>();
// map.put("org.eclipse.jetty.servlet.SessionCookie", "XSESSIONID" + port);
// map.put("org.eclipse.jetty.servlet.SessionURL", "xsessionid");

        final ServletHolder servlet = new ServletHolder(ServletContainer.class);
        servlet.setInitParameter(
            "com.sun.jersey.config.property.resourceConfigClass",
            "com.sun.jersey.api.core.PackagesResourceConfig");
        servlet.setInitParameter(
            "com.sun.jersey.config.property.packages",
            "de.cismet.cids.admin.serverManagement.servlet");
        // bind the REST web-app to /cidsservermanager path
        final ServletContextHandler managerContext = new ServletContextHandler(
                server,
                "/cidsservermanager",
                ServletContextHandler.SESSIONS); // NOI18N
        managerContext.addServlet(servlet, "/");
//        managerContext.setInitParams(map);                                                          // NOI18N
        managerContext.setInitParameter("org.eclipse.jetty.servlet.SessionCookie", "XSESSIONID" + port);
        managerContext.setInitParameter("org.eclipse.jetty.servlet.SessionURL", "xsessionid");

        // forward each request on root to the REST web-app
        final MovedContextHandler mch = new MovedContextHandler(server, "/", "cidsservermanager");

        // ContextHandlerCollection uses the longest prefix of the uri to determine the handler that handles the
        // request.
        final ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(
            new Handler[] {
                managerContext,
                cssContext,
                jsContext,
                imgContext,
                mch,
                rootContext,
            });

        server.setHandler(contexts);

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
