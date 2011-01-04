/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * CatalogSelectionBox.java
 *
 * Created on 8. September 2003, 17:44
 */
package de.cismet.common.gui.sqlwizard;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;

import java.util.*;

import javax.swing.*;

//import org.apache.log4j.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class CatalogSelectionBox extends JComboBox {
    //~ Static fields/initializers ---------------------------------------------

    // protected final Logger logger;

    public static final String DEFAULT_CATALOG = "Default Catalog";

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property connection. */
    private java.sql.Connection connection;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of CatalogSelectionBox.
     */
    public CatalogSelectionBox() {
        super();

        // this.logger = Logger.getLogger(this.getClass());
        this.setMaximumWitdh(225);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   connection  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    protected void update(final Connection connection) throws SQLException {
        final Vector catalogVector = new Vector();

        try {
            final ResultSet catalogs = connection.getMetaData().getCatalogs();

            while (catalogs.next()) {
                catalogVector.add(catalogs.getString(1));
            }

            catalogs.close();
        } catch (UnsupportedOperationException uoexp) {
            catalogVector.clear();
        }

        if (catalogVector.size() == 0) {
            catalogVector.add(DEFAULT_CATALOG);
        }

        this.setModel(new DefaultComboBoxModel(catalogVector));
        this.setSelectedIndex(0);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        this.setModel(new DefaultComboBoxModel());
        this.setSelectedIndex(-1);
        this.connection = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getSelectedCatalog() {
        return (this.getSelectedIndex() != -1) ? this.getSelectedItem().toString() : DEFAULT_CATALOG;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  width  DOCUMENT ME!
     */
    public void setMaximumWitdh(final int width) {
        final Dimension dimension = this.getPreferredSize();
        dimension.width = width;
        this.setPreferredSize(dimension);
        this.setMaximumSize(getPreferredSize());
    }

    /**
     * Getter for property connection.
     *
     * @return  Value of property connection.
     */
    public java.sql.Connection getConnection() {
        return this.connection;
    }

    /**
     * Setter for property connection.
     *
     * @param   connection  New value of property connection.
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public void setConnection(final java.sql.Connection connection) throws SQLException {
        this.connection = connection;
        this.update(connection);
    }

    /*protected void addAppender(org.apache.log4j.Appender appender)
     * { this.logger.addAppender(appender);}   */
}
