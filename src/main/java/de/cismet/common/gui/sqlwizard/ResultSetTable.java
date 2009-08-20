/*
 * ResultSetTable.java
 *
 * Created on 2. September 2003, 12:03
 */

package de.cismet.common.gui.sqlwizard;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author  pascal
 */
public class ResultSetTable extends JTable // implements HistorySupport
{
    //private LinkedList history;
    
    private final History history;

    /** Holds value of property maxRows. */
    private int maxRows = 75;    
    
    /** Holds value of property maxHistorySize. */
    //private final int maxHistorySize;

    /** Holds value of property selectedIndex. */
    //private int selectedIndex = -1;
    
    private final ResultSetTableSorter tableSorter = new ResultSetTableSorter();
    
    /** Utility field used by bound properties. */
    //private final SwingPropertyChangeSupport propertyChangeSupport =  new SwingPropertyChangeSupport(this);
    
    /** Creates a new instance of ResultSetTable */
    public ResultSetTable(int maxHistorySize, int maxRows)
    {
        super();
        
        //this.maxHistorySize = maxHistorySize;
        this.maxRows = maxRows;
        //this.history = new LinkedList();      
        this.history = new ResultSetHistory(maxHistorySize);
        this.setModel(tableSorter);
        
        tableSorter.addMouseListenerToHeaderInTable(this);
    }
    
    // .........................................................................
    
    public void reset()
    {
        this.tableSorter.setModel(new DefaultTableModel());
    }
    
    public void update(ResultSet resultSet) throws SQLException
    {
        this.update(resultSet, -1, -1);
    }
    
    public void update(ResultSet resultSet, int index) throws SQLException
    {
        this.update(resultSet, -1, index);
    }
    
    public void update(ResultSet resultSet, int maxRows, int index) throws SQLException
    {
        if(maxRows != -1)
        {
            this.setMaxRows(maxRows);
        }

        ResultSetMetaData metaData = resultSet.getMetaData();
        String[] columnNames = new String[metaData.getColumnCount()];
        
        for(int i = 0; i < columnNames.length; i++)
        {
            columnNames[i] = metaData.getColumnName(i+1);
        }
        
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        int i = 1;
        
        
        while(resultSet.next() && i < this.maxRows)
        {
            Object[] objects = new Object[metaData.getColumnCount()];
            for(int j = 0; j < objects.length; j++)
            {
                objects[j] = resultSet.getObject(j+1);
            } 
            
            tableModel.addRow(objects);
            i++;
        }
        
        this.history.setHistoryEntry(tableModel, index);
        this.update(tableModel);
    }
    
    protected void update(DefaultTableModel tableModel)
    {
        this.tableSorter.setModel(tableModel);
        
        if(this.getColumnCount() > 8)
        {
            this.setAutoResizeMode(AUTO_RESIZE_OFF);
            Enumeration enum_ = this.getColumnModel().getColumns();
            while(enum_.hasMoreElements())
            {
                ((TableColumn)enum_.nextElement()).setMinWidth(150);
            }
        }
        else
        {
            this.setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }  
    }
    
    public History getHistory()
    {
        return this.history;
    }
    
    /*private void addHistoryEntry(int index)
    {
        if(index == -1)
        {
            if(this.maxHistorySize > 0)
            {
                if(this.history.size() == this.maxHistorySize)
                {
                    this.history.removeFirst();
                }

                this.history.add(this.tableSorter.getModel());
                this.selectedIndex = this.history.size()-1;
            }
        }
        else
        {
            if(this.maxHistorySize > 0)
            {
                if(index <= this.history.size())
                {
                    int i = this.history.size();
                    for(; i <= index; i++)
                    {
                        this.history.add(null);
                    }
                }
                
                this.history.set(index, this.tableSorter.getModel());
                this.selectedIndex = index;
            }
        }
    }*/
    
    
    
    /** Setter for property selectedIndex.
     * @param selectedIndex New value of property selectedIndex.
     *
     */
    /*public void setSelectedHistoryEntry(int selectedIndex)
    {
        Object tableModel = null;
        if(selectedIndex >= 0 && this.selectedIndex != selectedIndex && selectedIndex < this.history.size() && (tableModel = this.history.get(selectedIndex)) != null)
        {
            tableSorter.setModel((DefaultTableModel)tableModel);
            int oldSelectedHistoryEntry = this.selectedIndex;
            this.selectedIndex = selectedIndex;
            super.firePropertyChange(HistorySupport.ENTRY_CHANGED, new Integer(oldSelectedHistoryEntry), new Integer(selectedIndex));
        }   
    }*/
    
    /** Getter for property selectedIndex.
     * @return Value of property selectedIndex.
     *
     */
    /*public int getSelectedHistoryEntry()
    {
        return this.selectedIndex;
    }*/

    /**
     * Removes all elements from the history list
     */
    /*public void clearHistory()
    {
        this.history.clear();
        this.selectedIndex = -1;
    }*/

    /** 
     * Getter for property historySize.
     * @return Value of property historySize.
     *
     */
    /*public int getHistorySize()
    {
        return this.history.size();
    }*/
    
    /** Getter for property maxHistorySize.
     * @return Value of property maxHistorySize.
     *
     */
    /*public int getMaxHistorySize()
    {
        return this.maxHistorySize;
    }*/
    
    // .........................................................................
    
    /** Getter for property maxRows.
     * @return Value of property maxRows.
     *
     */
    public int getMaxRows()
    {
        return this.maxRows;
    }
    
    /** Setter for property maxRows.
     * @param maxRows New value of property maxRows.
     *
     */
    public void setMaxRows(int maxRows)
    {
        this.maxRows = maxRows > 0 ? maxRows : 1;
    }  

    /** Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     *
     */
    /*public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(l);
    }*/
    
    /** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     *
     */
    /*public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(l);
    }*/
        
    /*public void setMaxHistorySize(int maxHistorySize)
    {
    }*/
    
    private class ResultSetTableSorter extends de.cismet.common.gui.misc.TableSorter
    {
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    }
    
    private class ResultSetHistory extends AbstractHistory
    {
        public ResultSetHistory(int maxHistorySize)
        {
            super(maxHistorySize);
        }
        
        public void setSelectedIndex(int selectedIndex)
        {
            
            if(selectedIndex >= 0 && this.selectedIndex != selectedIndex && selectedIndex < this.maxHistorySize)
            {
                Object object = null;
                if( selectedIndex < this.historyList.size() && (object = this.historyList.get(selectedIndex)) != null)
                {
                   ResultSetTable.this.update(((DefaultTableModel)object)); 
                }
                else
                {
                    ResultSetTable.this.reset();
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
            // ResultSetTable.this.addPropertyChangeListener(l);
        }
        
        public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
        {
            // disabled
            // ResultSetTable.this.removePropertyChangeListener(l);
        }
    }
}


/*class ResultSetTableModel implements AbstractTableModel
{
    
    public int getColumnCount()
    {
    }
    
    public int getRowCount()
    {
    }
    
    public Object getValueAt(int rowIndex, int columnIndex)
    {
    }
    
}*/

