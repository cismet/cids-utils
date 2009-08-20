package de.cismet.common.gui.sqlwizard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.datatransfer.*;


import de.cismet.common.gui.treetable.*;

/*
 * MetaDataTreeTable.java
 *
 * Created on 3. September 2003, 10:18
 */




/**
 *
 * @author  pascal
 */
public class MetaDataTreeTable extends JTreeTable
{
    private MetaDataHistory history;
    
    
    /** Utility field used by bound properties. */
    //private SwingPropertyChangeSupport propertyChangeSupport;
    //private PropertyChangeSupport propertyChangeSupport;
    
    //private Map sqlTypeMap = null;
    
    /** Creates a new instance of MetaDataTreeTable */
    public MetaDataTreeTable(DynamicTreeTableModel model, int maxHistorySize)
    {
        super(model);
        
        this.history = new MetaDataHistory(maxHistorySize);
        this.setDefaultRenderer(Object.class, new StringRenderer());
        this.getTree().setRootVisible(false);
        this.getTree().setShowsRootHandles(true);
        this.getColumn(this.getColumnName(0)).setMinWidth(200);
        
        // selection mode
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setCellSelectionEnabled(false);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(true);
        
        // drag & drop .........................................................
        // does not work :(
        // this.getTree().setDragEnabled(true);
        
        // works :-)
        this.setTransferHandler(new MetaDataTransferHandler());
    }
    
    public History getHistory()
    {
        return this.history;
    }
    
    /** Creates a new instance of MetaDataTreeTable */
    /*public MetaDataTreeTable(ResultSetMetaDataTreeTableModel resultSetMetaDataTreeTableModel)
    {
        super(resultSetMetaDataTreeTableModel);
     
        this.history = new LinkedList();
        this.getTree().setRootVisible(false);
    }*/
    
    /** Creates a new instance of MetaDataTreeTable */
    /*public MetaDataTreeTable(DatabaseMetaDataTreeTableModel databaseMetaDataTreeTableModel)
    {
        super(databaseMetaDataTreeTableModel);
     
        this.history = new LinkedList();
        this.getTree().setRootVisible(false);
    }*/
    
    public void reset()
    {
        //System.out.println(this.getTreeTableModel());
        //DynamicTreeTableModel model = (DynamicTreeTableModel)this.getTreeTableModel();
        //((DefaultMutableTreeNode)model.getRoot()).removeAllChildren();
        
        ((DynamicTreeTableModel)this.getTreeTableModel()).setRoot(new DefaultMutableTreeNode("invisible root node"));
        
        this.reload();
    }
    
    public void update(DatabaseMetaData metaData) throws SQLException
    {
        this.update(metaData, -1);
    }
    
    public void update(DatabaseMetaData metaData, int index) throws SQLException
    {
        if(this.getTreeTableModel() instanceof DatabaseMetaDataTreeTableModel)
        {
            ((DatabaseMetaDataTreeTableModel)this.getTreeTableModel()).update(metaData);
            history.setHistoryEntry(this.getTreeTableModel().getRoot(), index);
            this.reload();
        }
    }
    
    public void update(ResultSetMetaData metaData) throws SQLException
    {
        this.update(metaData, -1);
    }
    
    public void update(ResultSetMetaData metaData, int index) throws SQLException
    {
        if(this.getTreeTableModel() instanceof ResultSetMetaDataTreeTableModel)
        {
            ((ResultSetMetaDataTreeTableModel)this.getTreeTableModel()).update(metaData);
            history.setHistoryEntry(this.getTreeTableModel().getRoot(), index);
            this.reload();
        }
    }
    
    private void reload()
    {
        DynamicTreeTableModel model = (DynamicTreeTableModel)this.getTreeTableModel();
        model.nodeStructureChanged((TreeNode)model.getRoot());
        this.revalidate();
        this.repaint();
    }
    
    // .........................................................................
    
    public static class DatabaseMetaDataTreeTableModel extends DynamicTreeTableModel
    {
        public DatabaseMetaDataTreeTableModel()
        {
            super(new DatabaseMetaDataNode("invisible root node", "ROOT"),
            new String[]
            {"Name", "Type", "Remarks", "Size", "nullable"},
            new String[]
            {"toString", "getColumnTypeName", "getColumnRemarks", "getColumnSize", "isNullable"},
            new String[]
            {"", "", "", "", ""},
            new Class[]
            {TreeTableModel.class, String.class, String.class, String.class, String.class});
        }
        
        public boolean isCellEditable(Object node, int column)
        {
            return column == 0 ? true : false;
        }
        
        private void update(DatabaseMetaData metaData) throws SQLException
        {
            //((DefaultMutableTreeNode)this.getRoot()).removeAllChildren();
            this.setRoot(new DatabaseMetaDataNode("invisible root node", "ROOT"));
            
            ResultSet catalogs = metaData.getCatalogs();
            while(catalogs.next())
            {
                //System.out.println(catalogs.getString(1));
                //DatabaseMetaDataNode parentNode = new DatabaseMetaDataNode(catalogs.getString(1));
                String catalog = catalogs.getString(1);
                DatabaseMetaDataNode catalogueNode = new DatabaseMetaDataNode(catalog, "CATALOG");
                ((DatabaseMetaDataNode)this.getRoot()).add(catalogueNode);
                
                ResultSet tables = metaData.getTables(catalog, null, null, null);
                while(tables.next())
                {
                    //DatabaseMetaDataNode tableNode = new DatabaseMetaDataNode(tables.getString(3));
                    String table = tables.getString(3);
                    DatabaseMetaDataNode tableNode = new DatabaseMetaDataNode(table, "TABLE");
                    catalogueNode.add(tableNode);
                    
                    ResultSet columns = metaData.getColumns(catalog, null, table, null);
                    while(columns.next())
                    {
                        tableNode.add(new DatabaseMetaDataNode(
                        columns.getString(4),
                        columns.getString(6),
                        columns.getString(12),
                        columns.getString(7),
                        columns.getString(18)));
                    }
                    
                    columns.close();
                }
                
                tables.close();
            }
            
            catalogs.close();
        }
        
        public static class DatabaseMetaDataNode extends DefaultMutableTreeNode
        {
            /** Holds value of property columnTypeName. */
            private String columnTypeName;
            
            /** Holds value of property columnSize. */
            private String columnSize;
            
            /** Holds value of property columnRemarks. */
            private String columnRemarks;
            
            /** Holds value of property nullable. */
            private String nullable;
            
            private DatabaseMetaDataNode(String name, String columnTypeName)
            {
                this(name, columnTypeName, null, null, null);
            }
            
            private DatabaseMetaDataNode(String name, String columnTypeName, String columnRemarks, String columnSize, String nullable)
            {
                super(name);
                this.columnTypeName = columnTypeName;
                this.columnRemarks = columnRemarks;
                this.columnSize = columnSize;
                this.nullable = nullable;
            }
            
            /*public String getName()
            {
                return this.getUserObject().toString();
            }*/
            
            public String toString()
            {
                return this.getUserObject().toString();
            }
            
            /** Getter for property columnTypeName.
             * @return Value of property columnTypeName.
             *
             */
            public String getColumnTypeName()
            {
                //System.out.println(this.columnTypeName);
                return this.columnTypeName;
            }
            
            /** Getter for property columnSize.
             * @return Value of property columnSize.
             *
             */
            public String getColumnSize()
            {
                return this.columnSize;
            }
            
            /** Getter for property columnRemarks.
             * @return Value of property columnRemarks.
             *
             */
            public String getColumnRemarks()
            {
                return this.columnRemarks;
            }
            
            /** Getter for property nullable.
             * @return Value of property nullable.
             *
             */
            public String isNullable()
            {
                return this.nullable;
            }
        }
    }
    
    public static class ResultSetMetaDataTreeTableModel extends DynamicTreeTableModel
    {
        public ResultSetMetaDataTreeTableModel()
        {
            super(new ResultSetMetaDataNode("invisible root node", "ROOT"),
            new String[]
            {"Name", "Label", "Type", "Class", "read only"},
            new String[]
            {"toString", "getColumnLabel", "getColumnTypeName", "getColumnClassName", "isReadOnly"},
            null,
            new Class[]
            {TreeTableModel.class, String.class, String.class, String.class, String.class});
        }
        
        public boolean isCellEditable(Object node, int column)
        {
            return column == 0 ? true : false;
        }
        
        private void update(ResultSetMetaData metaData) throws SQLException
        {
            HashMap catalogueNodes = new HashMap();
            HashMap tableNodes = new HashMap();
            
            //((DefaultMutableTreeNode)this.getRoot()).removeAllChildren();
            this.setRoot(new ResultSetMetaDataNode("invisible root node", "ROOT"));
            
            for (int column = 1; column <= metaData.getColumnCount(); column++)
            {
                // create catalogue nodes ......................................
                String catalogName = metaData.getCatalogName(column);
                ResultSetMetaDataNode parentNode = null;
                if(catalogName != null && catalogName.length() > 0)
                {
                    if(catalogueNodes.containsKey(catalogName))
                    {
                        //parentNode = ((ResultSetMetaDataNode)tableNodes.get(catalogName));
                        parentNode = ((ResultSetMetaDataNode)tableNodes.get(catalogName));
                    }
                    else
                    {
                        //parentNode = new ResultSetMetaDataNode(catalogName);
                        parentNode = new ResultSetMetaDataNode(catalogName, "CATALOG");
                        catalogueNodes.put(catalogName, parentNode);
                        ((ResultSetMetaDataNode)this.getRoot()).add(parentNode);
                    }
                }
                else
                {
                    parentNode = (ResultSetMetaDataNode)this.getRoot();
                }
                
                // add table nodes .............................................
                String tableName = metaData.getTableName(column);
                if(tableNodes.containsKey(tableName))
                {
                    parentNode = ((ResultSetMetaDataNode)tableNodes.get(tableName));
                }
                else
                {
                    //ResultSetMetaDataNode tableNode = new ResultSetMetaDataNode(tableName);
                    ResultSetMetaDataNode tableNode = new ResultSetMetaDataNode(tableName, "TABLE");
                    parentNode.add(tableNode);
                    tableNodes.put(tableName, tableNode);
                    parentNode = tableNode;
                }
                
                // add column nodes ............................................
                parentNode.add(new ResultSetMetaDataNode(
                metaData.getColumnName(column),
                metaData.getColumnLabel(column),
                metaData.getColumnTypeName(column),
                metaData.getColumnClassName(column),
                metaData.isReadOnly(column) ? "TRUE" : "FALSE"));
            }
        }
        
        // ---------------------------------------------------------------------
        
        public static class ResultSetMetaDataNode extends DefaultMutableTreeNode
        {
            /** Holds value of property columnLabel. */
            private String columnLabel;
            
            /** Holds value of property columnTypeName. */
            private String columnTypeName;
            
            /** Holds value of property columnClassName. */
            private String columnClassName;
            
            /** Holds value of property readOnly. */
            private String readOnly;
            
            private ResultSetMetaDataNode(String name, String columnTypeName)
            {
                this(name, null, columnTypeName, null, null);
            }
            
            private ResultSetMetaDataNode(String name, String columnLabel, String columnTypeName, String columnClassName, String readOnly)
            {
                super(name);
                
                this.columnLabel = columnLabel;
                this.columnTypeName = columnTypeName;
                this.columnClassName = columnClassName;
                this.readOnly = readOnly;
            }
            
            /*public String getName()
            {
                return this.getUserObject().toString();
            }*/
            
            public String toString()
            {
                return this.getUserObject().toString();
            }
            
            /** Getter for property columnLabel.
             * @return Value of property columnLabel.
             *
             */
            public String getColumnLabel()
            {
                return this.columnLabel;
            }
            
            /** Getter for property columnTypeName.
             * @return Value of property columnTypeName.
             *
             */
            public String getColumnTypeName()
            {
                return this.columnTypeName;
            }
            
            /*public void setColumnTypeName(String columnTypeName)
            {
                this.columnTypeName = columnTypeName;
            }*/
            
            /** Getter for property columnClassName.
             * @return Value of property columnClassName.
             *
             */
            public String getColumnClassName()
            {
                return this.columnClassName;
            }
            
            /** Getter for property readOnly.
             * @return Value of property readOnly.
             *
             */
            public String isReadOnly()
            {
                return this.readOnly;
            }
            
        }
    }
    
    private class StringRenderer extends DefaultTableCellRenderer
    {
        public StringRenderer()
        {
            super();
        }
        
        public void setValue(Object value)
        {
            setText((value == null) ? "---" : value.toString());
        }
    }
    
    private class MetaDataHistory extends AbstractHistory
    {
        public MetaDataHistory(int maxHistorySize)
        {
            super(maxHistorySize);
        }
        
        public void setSelectedIndex(int selectedIndex)
        {
            //System.out.println("selectedIndex " + selectedIndex);
            //System.out.println("this.selectedIndex " + this.selectedIndex);
            //System.out.println("this.maxHistorySize " + this.maxHistorySize);
            //System.out.println("this.historyList.size() " + this.historyList.size());
            
            if(selectedIndex >= 0 && this.selectedIndex != selectedIndex && selectedIndex < this.maxHistorySize)
            {
                
                Object rootNode = null;
                if( selectedIndex < this.historyList.size() && (rootNode = this.historyList.get(selectedIndex)) != null)
                {
                    ((DynamicTreeTableModel)getTreeTableModel()).setRoot((TreeNode)rootNode);
                    reload();
                }
                else
                {
                    MetaDataTreeTable.this.reset();
                }
                
                int oldSelectedHistoryEntry = this.selectedIndex;
                this.selectedIndex = selectedIndex;
                
                // disabled
                // firePropertyChange(SELECTED_HISTORY_ENTRY, new Integer(oldSelectedHistoryEntry), new Integer(selectedIndex));
            }
        }
        
        public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
        {
            // disabled
            // MetaDataTreeTable.this.addPropertyChangeListener(l);
        }
        
        public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
        {
            // disabled
            // MetaDataTreeTable.this.removePropertyChangeListener(l);
        }
    }
    
    // #########################################################################
    
    private class MetaDataTransferHandler extends TransferHandler
    {
        /**
         * Create a Transferable to use as the source for a data transfer.
         *
         * @param c  The component holding the data to be transfered.  This
         *  argument is provided to enable sharing of TransferHandlers by
         *  multiple components.
         * @return  The representation of the data to be transfered.
         *
         */
        protected Transferable createTransferable(JComponent c)
        {
            if (c instanceof JTable)
            {
                JTable table = (JTable) c;                
                if (table.getRowSelectionAllowed() && table.getSelectedRowCount() > 0)
                {
                    Object obj = table.getValueAt(table.getSelectedRow(), 0);
                    return new StringTransferable(obj);
                }
            }
            
            return null;
        }
        
        public int getSourceActions(JComponent c)
        {
            return COPY;
        }
        
        private class StringTransferable implements Transferable
        {
            private final String data;
            private final DataFlavor[] transferDataFlavors = new DataFlavor[]{DataFlavor.stringFlavor};
            
            private StringTransferable(Object dataObject)
            {
                if(dataObject != null)
                {
                    this.data = dataObject.toString();
                }
                else
                {
                    this.data = new String();
                }
            }
            
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, java.io.IOException
            {
                if (!isDataFlavorSupported(flavor)) 
                {
                    throw new UnsupportedFlavorException(flavor);
                }
                
                return data;
            }
            
            public DataFlavor[] getTransferDataFlavors()
            {
                return this.transferDataFlavors;
            }
            
            public boolean isDataFlavorSupported(DataFlavor flavor)
            {
               return flavor.equals(DataFlavor.stringFlavor);
            }
            
        }
        
    }
    
}

// #########################################################################

/**
 * This method returns the name of a JDBC type.
 * Returns null if jdbcType is not recognized.
 */
    /*private String getSQLTypeName(int sqlType)
    {
        // Use reflection to populate a map of int values to names
        if (this.sqlTypeMap == null)
        {
            this.sqlTypeMap = new HashMap();
     
            // Get all field in java.sql.Types
            Field[] fields = java.sql.Types.class.getFields();
            for (int i=0; i<fields.length; i++)
            {
                try
                {
                    // Get field name
                    String name = fields[i].getName();
     
                    // Get field value
                    Integer value = (Integer)fields[i].get(null);
     
                    // Add to map
                    sqlTypeMap.put(value, name);
                } catch (IllegalAccessException e)
                {
                }
            }
        }
     
        // Return the JDBC type name
        return (String)sqlTypeMap.get(new Integer(sqlType));
    }*/
