/*
 * CatalogSelectionBox.java
 *
 * Created on 8. September 2003, 17:44
 */

package de.cismet.common.gui.sqlwizard;

import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

//import org.apache.log4j.*;

/**
 *
 * @author  pascal
 */
public class CatalogSelectionBox extends JComboBox
{
    //protected final Logger logger;
    
    public final static String DEFAULT_CATALOG = "Default Catalog";
    
    /** Holds value of property connection. */
    private java.sql.Connection connection;
    
    /** Creates a new instance of CatalogSelectionBox */
    public CatalogSelectionBox()
    {
        super();
        
       // this.logger = Logger.getLogger(this.getClass());
        this. setMaximumWitdh(225);
    }
    
    protected void update(Connection connection) throws SQLException
    { 
        Vector catalogVector = new Vector();
        
        try
        {
            ResultSet catalogs = connection.getMetaData().getCatalogs();

            while(catalogs.next())
            {
                catalogVector.add(catalogs.getString(1));
            }

            catalogs.close();
        }
        catch(UnsupportedOperationException uoexp)
        {
            catalogVector.clear();
        }

        if(catalogVector.size() == 0)
        {
            catalogVector.add(DEFAULT_CATALOG);
        }
        
        this.setModel(new DefaultComboBoxModel(catalogVector));
        this.setSelectedIndex(0);
    }
    
    public void reset()
    {
        this.setModel(new DefaultComboBoxModel());
        this.setSelectedIndex(-1);
        this.connection = null;
    }
    
    public String getSelectedCatalog()
    {
        return this.getSelectedIndex() != -1 ? this.getSelectedItem().toString() : DEFAULT_CATALOG;
    }
    
    public void setMaximumWitdh(int width)
    {
        Dimension dimension = this.getPreferredSize();
        dimension.width = width;
        this.setPreferredSize(dimension);
        this.setMaximumSize(getPreferredSize());
    }
    
    /** Getter for property connection.
     * @return Value of property connection.
     *
     */
    public java.sql.Connection getConnection()
    {
        return this.connection;
    }
    
    /** Setter for property connection.
     * @param connection New value of property connection.
     *
     */
    public void setConnection(java.sql.Connection connection) throws SQLException
    {
        this.connection = connection;
        this.update(connection);
    }
    
    /*protected void addAppender(org.apache.log4j.Appender appender)
    {
        this.logger.addAppender(appender);
    }   */
}
