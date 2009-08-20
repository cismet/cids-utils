/*
 * SQLWizard.java
 *
 * Created on 8. September 2003, 11:06
 */

package de.cismet.common.gui.sqlwizard;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.tree.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
public class BasicSQLWizard extends javax.swing.JPanel
{
    protected final Logger logger;
    
    protected int maxRows = 25;
    protected int historySize = 5;
    

    protected final MessageArea messageArea;
    protected final MetaDataTreeTable databaseMetaDataTreeTable, resultSetMetaDataTreeTable;
    protected final SQLEditor sqlEditor;
    protected final ResultSetTable resultSetTable;
    
    protected final HistorySelectionBox historySelectionBox;
    protected final CatalogSelectionBox catalogSelectionBox;
    
    /** Holds value of property connection. */
    protected java.sql.Connection connection;    

    /** Creates new form SQLWizard */
    public BasicSQLWizard()
    {
        this.logger = Logger.getLogger(this.getClass());
        
        this.messageArea = new MessageArea();
        this.databaseMetaDataTreeTable = new MetaDataTreeTable(new MetaDataTreeTable.DatabaseMetaDataTreeTableModel(), 0);
        this.resultSetMetaDataTreeTable = new MetaDataTreeTable(new MetaDataTreeTable.ResultSetMetaDataTreeTableModel(), historySize);
        this.sqlEditor = new SQLEditor(historySize, maxRows);
        this.resultSetTable = new ResultSetTable(historySize, maxRows);
        
        this.historySelectionBox = new HistorySelectionBox();
        this.catalogSelectionBox = new CatalogSelectionBox();
        
        this.messageArea.setRows(6);
        this.messageArea.setBackground(SystemColor.info);
        this.catalogSelectionBox.addItemListener(new CatalogSelectionListener());
        
        // enable d&d nodes
        this.databaseMetaDataTreeTable.setDragEnabled(true);
        
        this.initComponents();
        this.initToolBars();
        
        this.messageScrollPane.setViewportView(this.messageArea);
        this.dbMetaScrollPane.setViewportView(this.databaseMetaDataTreeTable);
        this.sqlEditorScrollPane.setViewportView(this.sqlEditor);
        this.resultMetaScrollPane.setViewportView(this.resultSetMetaDataTreeTable);
        this.resultScrollPane.setViewportView(this.resultSetTable);
        
        this.statusLabel.setText("no connected");

        // history stuff
        this.historySelectionBox.addHistory(this.sqlEditor.getHistory());
        this.historySelectionBox.addHistory(this.resultSetMetaDataTreeTable.getHistory());
        this.historySelectionBox.addHistory(this.resultSetTable.getHistory());
        
        // logging stuff
        this.logger.addAppender(this.messageArea.getAppender());
    }
    
    /**
     * Creates the EditorToolBar (Catalog ...) and the ActionToolBar (History ...).
     */
    protected void initToolBars()
    {
        ActionListener actionListener = new ButtonListener();
        
        // init EditorToolBar ..................................................
        this.editorToolBar.add(new JLabel("Catalog: "));
        this.editorToolBar.add(this.catalogSelectionBox);
        this.editorToolBar.addSeparator();
        JButton button = new JButton(this.getIcon("execute.gif"));
        button.setToolTipText("Execute SQL Command");
        button.setActionCommand("execute");
        button.addActionListener(actionListener);
        this.setComponentHeight(this.catalogSelectionBox, button);
        this.editorToolBar.add(button);
        
        // init ActionToolBar ..................................................
        this.actionToolBar.add(new JLabel(" History: "));
        this.actionToolBar.add(this.historySelectionBox);
        this.actionToolBar.addSeparator();
        /*button = new JButton(this.getIcon("delete.gif"));
        button.setToolTipText("Clear Log Window");
        button.setActionCommand("clear");
        this.setComponentHeight(this.historySelectionBox, button);
        this.actionToolBar.add(button);*/
        
    }
    
    /**
     * Sets the height of component b to the height of component a. 
     * @param a a JComponent
     * @param b a JComponent
     */
    protected void setComponentHeight(JComponent a, JComponent b)
    {
        Dimension dimension = b.getPreferredSize();
        dimension.height = a.getPreferredSize().height;
        b.setPreferredSize(dimension);
    }
    
    /**
     * Loads an image from a jar file or from a romte/local filesystem.
     *
     * @return an icon image
     */
    protected Icon getIcon(String name)
    {
        //System.out.println("/de/cismet/common/gui/sqlwizard/images/" + name);
        //System.out.println(this.getClass().getResource("/de/cismet/common/gui/sqlwizard/images/" + name));
        return new ImageIcon(this.getClass().getResource("/de/cismet/common/gui/sqlwizard/images/" + name));
    }
    
    
    /**
     * Executes the sql statement, fills the ResultSet and the  ResultSetMetaData
     * tables
     *
     * @return the result of the sql command
     * @throws SQLException
     */
    protected ResultSet execute() throws SQLException
    {
        ResultSet resultSet = this.sqlEditor.execute(this.historySelectionBox.getSelectedIndex());
        this.resultSetMetaDataTreeTable.update(resultSet.getMetaData(), this.historySelectionBox.getSelectedIndex());
        this.resultSetTable.update(resultSet, this.historySelectionBox.getSelectedIndex());
        this.tabbedPane.setSelectedIndex(1);
        
        return resultSet;
    }
    
    /**
     * Clears the ResultSet and the ResultSetMetaData tables.
     *
     * @param SQLException the SQLException
     */
    protected void handleSQLException(SQLException sqlexp) 
    {
        //this.clearResults(this.historySelectionBox.getSelectedIndex());
        
        this.resultSetMetaDataTreeTable.reset();
        this.resultSetMetaDataTreeTable.getHistory().setHistoryEntry(null, this.historySelectionBox.getSelectedIndex());

        this.resultSetTable.reset();
        this.resultSetTable.getHistory().setHistoryEntry(null, this.historySelectionBox.getSelectedIndex());
        
        this.tabbedPane.setSelectedIndex(0);
        this.logger.error(sqlexp.getMessage(), sqlexp);
    }
    
    /*protected void clearResults(int index)
    {
        this.resultSetMetaDataTreeTable.reset();
        this.resultSetMetaDataTreeTable.getHistory().setHistoryEntry(null, index);

        this.resultSetTable.reset();
        this.resultSetTable.getHistory().setHistoryEntry(null, index);
    }*/
    
    /**
     * Calls <code>execute()</code> and <code>handleSQLException()</code>.
     */
    protected void executeActionPerformed()
    {
        if(!this.sqlEditor.isEmpty())
        {
            try
            {
                this.execute().close();
            }
            catch(SQLException sqlexp)
            {
               this.handleSQLException(sqlexp);
            }
            
            //this.historySelectionBox.repaint();
        }
        else
        {
            logger.error("empty statement or no connection");
        }
    }
    
    /** Getter for property connection.
     * @return Value of property connection.
     *
     */
    public java.sql.Connection getConnection()
    {
        return this.connection;
    }
    
    /** 
     * Setter for property connection.<p>
     * Performs initialization of tables.
     *
     * @param connection New value of property connection.
     */
    public void setConnection(java.sql.Connection connection, String url)
    {
        try
        {
            if(this.connection != null && this.connection != connection && !this.connection.isClosed())
            {
                this.logger.info("closing connection");
                this.connection.close();
            }
            
            this.connection = connection;
            
            this.resultSetTable.reset();
            this.resultSetMetaDataTreeTable.reset();
            this.sqlEditor.reset();

            this.catalogSelectionBox.setConnection(connection);
            this.sqlEditor.setStatement(connection.createStatement());
            
            if(this.connection.isClosed())
            {
                this.statusLabel.setText("WARNING: connection to '" + url + "' is closed");
            }
            else
            {
                this.statusLabel.setText("connected to '" + url + "'");
                
                try
                {
                    this.databaseMetaDataTreeTable.update(connection.getMetaData());
                }
                catch(UnsupportedOperationException uoexp)
                {
                    logger.warn("could not load metadata: \n" + uoexp.getMessage());
                }
            }

            this.tabbedPane.setSelectedIndex(0); 
        }
        catch(SQLException sqlexp)
        {
            this.connection = null;
            logger.error(sqlexp.getMessage(), sqlexp);
        }
    }
    
    private class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().equals("execute"))
            {
                logger.info("executing sql statement '" + sqlEditor.getText() + "'");
                executeActionPerformed();
            }
        } 
    }
    
    private class CatalogSelectionListener implements ItemListener
    { 
        public void itemStateChanged(ItemEvent e)
        {
            if(!e.getItem().equals(CatalogSelectionBox.DEFAULT_CATALOG))
            {
                try
                {
                    logger.info("selecting database catalog '" + e.getItem().toString() + "'");
                    connection.setCatalog(e.getItem().toString());
                }
                catch(SQLException sqlexp)
                {
                    logger.error(sqlexp.getMessage(), sqlexp);
                }
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
        javax.swing.JPanel dbMetaPanel;
        javax.swing.JPanel mainQueryPanel;
        javax.swing.JPanel mainResultPanel;
        javax.swing.JSplitPane mainSplitPane;
        javax.swing.JPanel messagePanel;
        javax.swing.JSplitPane querySplitPane;
        javax.swing.JPanel resultMetaPanel;
        javax.swing.JPanel resultPanel;
        javax.swing.JSplitPane resultSplitPane;
        javax.swing.JPanel sqlEditorPanel;

        mainSplitPane = new javax.swing.JSplitPane();
        tabbedPane = new javax.swing.JTabbedPane();
        mainQueryPanel = new javax.swing.JPanel();
        querySplitPane = new javax.swing.JSplitPane();
        dbMetaPanel = new javax.swing.JPanel();
        dbMetaScrollPane = new javax.swing.JScrollPane();
        sqlEditorPanel = new javax.swing.JPanel();
        editorToolBar = new javax.swing.JToolBar();
        sqlEditorScrollPane = new javax.swing.JScrollPane();
        mainResultPanel = new javax.swing.JPanel();
        resultSplitPane = new javax.swing.JSplitPane();
        resultMetaPanel = new javax.swing.JPanel();
        resultMetaScrollPane = new javax.swing.JScrollPane();
        resultPanel = new javax.swing.JPanel();
        resultScrollPane = new javax.swing.JScrollPane();
        messagePanel = new javax.swing.JPanel();
        actionToolBar = new javax.swing.JToolBar();
        messageScrollPane = new javax.swing.JScrollPane();
        statusLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setResizeWeight(1.0);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setEnabled(false);
        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        mainQueryPanel.setLayout(new java.awt.BorderLayout());

        mainQueryPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        querySplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        querySplitPane.setResizeWeight(0.5);
        querySplitPane.setOneTouchExpandable(true);
        dbMetaPanel.setLayout(new java.awt.BorderLayout());

        dbMetaPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)), new javax.swing.border.TitledBorder("Database Meta Data")), new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 5, 5, 5))));
        dbMetaPanel.setPreferredSize(new java.awt.Dimension(500, 150));
        dbMetaPanel.add(dbMetaScrollPane, java.awt.BorderLayout.CENTER);

        querySplitPane.setTopComponent(dbMetaPanel);

        sqlEditorPanel.setLayout(new java.awt.BorderLayout(5, 5));

        sqlEditorPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)), new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder("SQL Editor"), new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)))));
        sqlEditorPanel.setPreferredSize(new java.awt.Dimension(500, 150));
        editorToolBar.setFloatable(false);
        editorToolBar.setRollover(true);
        sqlEditorPanel.add(editorToolBar, java.awt.BorderLayout.NORTH);

        sqlEditorPanel.add(sqlEditorScrollPane, java.awt.BorderLayout.CENTER);

        querySplitPane.setBottomComponent(sqlEditorPanel);

        mainQueryPanel.add(querySplitPane, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Query", mainQueryPanel);

        mainResultPanel.setLayout(new java.awt.BorderLayout());

        mainResultPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        resultSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        resultSplitPane.setResizeWeight(0.5);
        resultSplitPane.setOneTouchExpandable(true);
        resultMetaPanel.setLayout(new java.awt.BorderLayout());

        resultMetaPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)), new javax.swing.border.TitledBorder("ResultSet Meta Data")), new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 5, 5, 5))));
        resultMetaPanel.setPreferredSize(new java.awt.Dimension(500, 150));
        resultMetaPanel.add(resultMetaScrollPane, java.awt.BorderLayout.CENTER);

        resultSplitPane.setTopComponent(resultMetaPanel);

        resultPanel.setLayout(new java.awt.BorderLayout());

        resultPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)), new javax.swing.border.TitledBorder("ResultSet")), new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 5, 5, 5))));
        resultPanel.setPreferredSize(new java.awt.Dimension(500, 150));
        resultPanel.add(resultScrollPane, java.awt.BorderLayout.CENTER);

        resultSplitPane.setBottomComponent(resultPanel);

        mainResultPanel.add(resultSplitPane, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Results", mainResultPanel);

        mainSplitPane.setTopComponent(tabbedPane);

        messagePanel.setLayout(new java.awt.BorderLayout());

        messagePanel.setMinimumSize(new java.awt.Dimension(300, 75));
        actionToolBar.setFloatable(false);
        actionToolBar.setRollover(true);
        messagePanel.add(actionToolBar, java.awt.BorderLayout.NORTH);

        messageScrollPane.setMinimumSize(null);
        messageScrollPane.setPreferredSize(null);
        messagePanel.add(messageScrollPane, java.awt.BorderLayout.CENTER);

        mainSplitPane.setRightComponent(messagePanel);

        add(mainSplitPane, java.awt.BorderLayout.CENTER);

        statusLabel.setFont(new java.awt.Font("Dialog", 1, 10));
        statusLabel.setText(" ");
        statusLabel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED), new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 3, 1, 2))));
        add(statusLabel, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar actionToolBar;
    private javax.swing.JScrollPane dbMetaScrollPane;
    private javax.swing.JToolBar editorToolBar;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JScrollPane resultMetaScrollPane;
    private javax.swing.JScrollPane resultScrollPane;
    private javax.swing.JScrollPane sqlEditorScrollPane;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    public static void main(String args[])
    {
        final BasicSQLWizard sw = new BasicSQLWizard();

        //MessageArea ma = new MessageArea();
        //ma.setRows(5);
        
        //cm.logger.addAppender(ma.getAppender());
        //cm.update();

        final JFrame jf = new JFrame("SQL Wizard");
        jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
        //jf.setLocationRelativeTo(null);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(sw, BorderLayout.CENTER);
        //jf.getContentPane().add(new JScrollPane(ma), BorderLayout.SOUTH);
        //jf.pack();
        jf.setSize(800,600);
        jf.setVisible(true);
        
        
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                    sw.setConnection(DriverManager.getConnection("jdbc:odbc:metadb"), "jdbc:odbc:metadb");
                    
                    //JTree jt = new JTree(new DefaultTreeModel((TreeNode)sw.databaseMetaDataTreeTable.getTreeTableModel().getRoot()));
                    //jf.getContentPane().add(new JScrollPane(jt), BorderLayout.CENTER);
                    //jf.repaint();
                }
                catch(Exception exp)
                {
                    sw.logger.fatal(exp.getMessage(), exp);
                }
            }
        });
    }  
}
