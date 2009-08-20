/*
 * ConnectionManager.java
 *
 * Created on 5. September 2003, 12:16
 */

package de.cismet.common.gui.sqlwizard;

import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import org.apache.log4j.*;

/**
 *
 * @author  pascal
 */
public class ConnectionManager extends javax.swing.JPanel
{
    private final Logger logger;
    
    private final Properties driverProperties;
    private Connection connection = null;
    
    
    /** Creates new form ConnectionManager */
    public ConnectionManager()
    {
        this.logger = Logger.getLogger(this.getClass());
        this.driverProperties = new Properties();
        
        initComponents();
        
        ActionListener actionListener = new ButtonListener();
        this.connectButton.addActionListener(actionListener);
        this.cancelButton.addActionListener(actionListener);
        
        this.update();
    }
    
    
    public void update()
    {
        this.driverBox.removeAllItems();
        Enumeration drivers = DriverManager.getDrivers();
        
        if(!drivers.hasMoreElements())
        {
            try
            {
                logger.warn("no JDBC drivers found, loading default ODBC driver");
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                drivers = DriverManager.getDrivers();
            }
            catch(Exception exp)
            {
                logger.error("Default ODBC driver not found.", exp);
            }
        }
        
        while(drivers.hasMoreElements())
        {
            this.driverBox.addItem((Driver)drivers.nextElement());
        }
        
        if(this.driverBox.getItemCount() > 0)
        {
            this.setStatus(this.driverBox.getItemCount() + " JDBC driver(s) found.", false);
            logger.debug(this.statusLabel.getText());
        }
        else
        {
            this.setStatus("No JDBC drivers could be found.", true);
            logger.error(this.statusLabel.getText());
        } 
    }
    
    public void reset()
    {
        this.driverBox.setSelectedIndex(0);
        this.usernamelField.setText(null);
        this.passwordField.setText(null);
        this.urlField.setText(null);
    }
    
    protected void addAppender(org.apache.log4j.Appender appender)
    {
        this.logger.addAppender(appender);
    }
    
    private void connect()
    {
        if(this.driverBox.getSelectedIndex() < 0)
        {
            this.setStatus("No JDBC Driver selected.", true);
        }
        else if (this.urlField.getText().length() <= 0)
        {
            this.setStatus("URL field can't be empty.", true);
        }
        else
        {
            this.connectButton.setEnabled(false); 
            this.cancelButton.setEnabled(true); 
            
            try
            {
                Driver driver = (Driver)this.driverBox.getSelectedItem();
                if(driver.acceptsURL(this.urlField.getText()))
                {
                    this.setStatus("Connecting to '" + this.urlField.getText() + "'.", false);
                    logger.info(this.statusLabel.getText());
                    this.connectButton.setEnabled(false);
                    this.driverProperties.setProperty("username", this.usernamelField.getText());
                    this.driverProperties.setProperty("password", new String(this.passwordField.getPassword()));

                    this.setConnection(driver.connect(this.urlField.getText(),  this.driverProperties));

                    if(this.isConnected())
                    {
                        this.setStatus("Successfull connected to '" + this.urlField.getText() + "'.", false);
                        logger.info(this.statusLabel.getText());
                    }
                    else
                    { 
                        this.setStatus("Connection failed: unknown reason.", true);
                        logger.error(this.statusLabel.getText());
                    }
                }
                else
                {
                    this.setStatus("Wrong URL format '" + this.urlField.getText() + "'.", true);
                    logger.warn(this.statusLabel.getText());
                }   
            }
            catch(SQLException sqlexp)
            { 
                this.setConnection(null);
                this.setStatus(sqlexp.getMessage(), true);
                logger.error(this.statusLabel.getText(), sqlexp);
            }
            
            this.connectButton.setEnabled(true); 
            this.cancelButton.setEnabled(false); 
        }
    }
    
    public String getDriverName()
    {
        if(this.driverBox.getSelectedIndex() < 0)
        {
            return new String();
        }
        else
        {
            return this.driverBox.getSelectedItem().getClass().getName();
        }
    }
    
    public String getConnectionString()
    {
        return this.urlField.getText();
    }
    
    public String getUsername()
    {
        return this.usernamelField.getText();
    }
    
    public String getPassword()
    {
        return new String(this.passwordField.getPassword());
    }
    
    /** Setter for property username.
     * @param username New value of property username.
     *
     */
    public void setUsername(String username)
    {
        if(username != null)
        {
            this.usernamelField.setText(username);
        }
    }    

    /** Setter for property driverName.
     * @param driverName New value of property driverName.
     *
     */
    public void setDriverName(String driverName)
    {
        if(driverName != null)
        {
            this.driverBox.setSelectedItem(driverName);
        }
    }    
    
    /** Setter for property connectionString.
     * @param connectionString New value of property connectionString.
     *
     */
    public void setConnectionString(String connectionString)
    {
        this.urlField.setText(connectionString);
    }

    public Connection getConnection()
    {
        return this.connection;
    }
    
    public void setConnection(Connection connection)
    {
        Connection oldConnection = this.connection;
        this.connection = connection;
        
        this.firePropertyChange("connection", oldConnection, connection);
    }
    
    public boolean isConnected()
    {
        if(this.connection != null)
        {
            try
            {
                return !this.connection.isClosed();
            }
            catch(SQLException sqlexp){}
        }
        
        return false;
    }
    
    private void setStatus(final String message, final boolean error)
    {
        if(SwingUtilities.isEventDispatchThread())
        {
            this.statusLabel.setForeground(error ? Color.RED : Color.BLUE);
            this.statusLabel.setText(message);
        }
        else
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    statusLabel.setForeground(error ? Color.RED : Color.BLUE);
                    statusLabel.setText(message);
                }
            });
        }
    }
    
    // .........................................................................
    
    private class DriverListRenderer extends DefaultListCellRenderer
    {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
        {
            return super.getListCellRendererComponent(list, (value != null ? value.getClass().getName() : "null"), index, isSelected, cellHasFocus);
        }
    }
    
    private class ButtonListener implements ActionListener
    { 
        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().equalsIgnoreCase("connect"))
            {
                connect();
            }
            else
            {
                setConnection(null);
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        javax.swing.JLabel driverLabel;
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JLabel passwordLabel;
        javax.swing.JLabel urlLabel;
        javax.swing.JLabel usernamelLabel;

        driverLabel = new javax.swing.JLabel();
        driverBox = new javax.swing.JComboBox();
        addDriverButton = new javax.swing.JButton();
        urlLabel = new javax.swing.JLabel();
        urlField = new javax.swing.JTextField();
        usernamelLabel = new javax.swing.JLabel();
        usernamelField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        buttonPanel = new javax.swing.JPanel();
        connectButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        driverLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        driverLabel.setText("JDBC Driver:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        add(driverLabel, gridBagConstraints);

        driverBox.setRenderer(new DriverListRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(driverBox, gridBagConstraints);

        addDriverButton.setText("ADD");
        addDriverButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        addDriverButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(addDriverButton, gridBagConstraints);

        urlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        urlLabel.setText("Database URL:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        add(urlLabel, gridBagConstraints);

        urlField.setColumns(18);
        urlField.setMargin(new java.awt.Insets(1, 2, 1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(urlField, gridBagConstraints);

        usernamelLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        usernamelLabel.setText("Username:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        add(usernamelLabel, gridBagConstraints);

        usernamelField.setColumns(18);
        usernamelField.setMargin(new java.awt.Insets(1, 2, 1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(usernamelField, gridBagConstraints);

        passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        passwordLabel.setText("Password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        add(passwordLabel, gridBagConstraints);

        passwordField.setColumns(18);
        passwordField.setMargin(new java.awt.Insets(1, 2, 1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(passwordField, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        connectButton.setText("Connect");
        connectButton.setActionCommand("connect");
        buttonPanel.add(connectButton);

        cancelButton.setText("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.setEnabled(false);
        buttonPanel.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(buttonPanel, gridBagConstraints);

        statusLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusLabel.setText(" ");
        statusLabel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED), new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 2, 0, 2))));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(statusLabel, gridBagConstraints);

    }//GEN-END:initComponents

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDriverButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton connectButton;
    private javax.swing.JComboBox driverBox;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField urlField;
    private javax.swing.JTextField usernamelField;
    // End of variables declaration//GEN-END:variables
    
    
    /*public static void main(String args[])
    {
        ConnectionManager cm = new ConnectionManager();

        MessageArea ma = new MessageArea();
        ma.setRows(5);
        
        cm.logger.addAppender(ma.getAppender());
        cm.update();
        
        JFrame jf = new JFrame("ConnectionManager");
        jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(cm, BorderLayout.CENTER);
        jf.getContentPane().add(new JScrollPane(ma), BorderLayout.SOUTH);
        jf.pack();
        jf.setVisible(true);
    }*/
}
