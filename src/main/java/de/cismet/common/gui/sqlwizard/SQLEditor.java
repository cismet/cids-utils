/*
 * SQLEditor.java
 *
 * Created on 4. September 2003, 15:05
 */

package de.cismet.common.gui.sqlwizard;

import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.sql.*;
import javax.swing.text.*;
import java.io.*;

//import org.apache.log4j.*;

/**
 *
 * @author  pascal
 */
public class SQLEditor extends JTextPane
{
    private final SQLEditorHistory history;
    
    //private final Logger logger;
    /** Holds value of property maxHistorySize. */
    //private final int maxHistorySize;
    
    /** Holds value of property selectedHistoryEntry. */
    //private int selectedHistoryEntry;
    
    /** Holds value of property maxRows. */
    private int maxRows = 75;
    
    /** Holds value of property statement. */
    private Statement statement = null;
    
    /** Utility field used by bound properties. */
    //private final SwingPropertyChangeSupport propertyChangeSupport =  new SwingPropertyChangeSupport(this);
    
    /** Creates new form SQLEditor */
    public SQLEditor(int maxHistorySize, int maxRows)
    {
        //this.maxHistorySize = maxHistorySize;
        this.maxRows = maxRows;
        this.history = new SQLEditorHistory(maxHistorySize);
        //unsinn:
        //this.setTransferHandler(new DatabaseMetaDataTransferHandler());
        //this.logger = Logger.getLogger(this.getClass());
    }
    
    public boolean isEmpty()
    {
        return this.statement == null || this.getDocument().getLength() <= 0;
    }
    
    public void reset()
    {
        this.setText(null);
    }
    
    
    public ResultSet execute() throws SQLException
    {
        return this.execute(-1);
    }
    
    public ResultSet execute(int index) throws SQLException
    {
        if(!this.isEmpty())
        {
            this.history.setHistoryEntry(this.getText(), index);
            
            try
            {
                this.statement.setMaxRows(this.maxRows);
            }
            catch(SQLException sqlexp)
            {}
            
            /*(this.statement.execute(this.getText()))
            {
                return this.statement.getResultSet();
            }*/
            
            return this.statement.executeQuery(this.getText());
        }
        
        return null;
    }
    
    // .........................................................................
    
    public History getHistory()
    {
        return this.history;
    }
    
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
        this.maxRows = maxRows;
    }
    
    /** Getter for property statement.
     * @return Value of property statement.
     *
     */
    public Statement getStatement()
    {
        return this.statement;
    }
    
    /** Setter for property statement.
     * @param statement New value of property statement.
     *
     */
    public void setStatement(Statement statement)
    {
        this.statement = statement;
    }
    
    private class SQLEditorHistory extends AbstractHistory
    {
        public SQLEditorHistory(int maxHistorySize)
        {
            super(maxHistorySize);
        }
        
        public void setSelectedIndex(int selectedIndex)
        {
            //System.out.println("selectedIndex " + selectedIndex);
            //System.out.println("this.selectedIndex " + this.selectedIndex);
            //System.out.println("this.maxHistorySize " + this.maxHistorySize);
            //System.out.println("this.historyList.size() " + this.historyList.size());
            //System.out.println(".......................................................");
            
            
            if(selectedIndex >= 0 && this.selectedIndex != selectedIndex && selectedIndex < this.maxHistorySize)
            {
                Object object = null;
                if( selectedIndex < this.historyList.size() && (object = this.historyList.get(selectedIndex)) != null)
                {
                    SQLEditor.this.setText(object.toString());
                }
                else
                {
                    SQLEditor.this.reset();
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
            // SQLEditor.this.addPropertyChangeListener(l);
        }
        
        public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
        {
            // disabled
            // SQLEditor.this.removePropertyChangeListener(l);
        }
    }
    
    /*private class DatabaseMetaDataTransferHandler extends TransferHandler
    {
        public void exportToClipboard(JComponent comp, Clipboard clipboard, int action)
        {
            if (comp instanceof JTextComponent)
            {
                JTextComponent text = (JTextComponent)comp;
                int p0 = text.getSelectionStart();
                int p1 = text.getSelectionEnd();
                if (p0 != p1)
                {
                    try
                    {
                        Document doc = text.getDocument();
                        String srcData = doc.getText(p0, p1 - p0);
                        StringSelection contents =new StringSelection(srcData);
                        clipboard.setContents(contents, null);
                        if (action == TransferHandler.MOVE)
                        {
                            doc.remove(p0, p1 - p0);
                        }
                    } catch (BadLocationException ble)
                    {}
                }
            }
        }
        public boolean importData(JComponent comp, Transferable t)
        {
            if (comp instanceof JTextComponent)
            {
                DataFlavor flavor = getFlavor(t.getTransferDataFlavors());
                System.out.println(t);
                
                
                if (flavor != null)
                {
                    try
                    {
                        String data = "$" + (String)t.getTransferData(flavor) + "$";
                        
                        ((JTextComponent)comp).replaceSelection(data);
                        return true;
                    } 
                    catch (UnsupportedFlavorException ufe)
                    {
                    } 
                    catch (IOException ioe)
                    {
                    }
                }
            }
            return false;
        }
        
        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
        {
            JTextComponent c = (JTextComponent)comp;
            if (!(c.isEditable() && c.isEnabled()))
            {
                return false;
            }
            return (getFlavor(transferFlavors) != null);
        }
        
        public int getSourceActions(JComponent c)
        {
            return NONE;
        }
        
        private DataFlavor getFlavor(DataFlavor[] flavors)
        {
            if (flavors != null)
            {
                for (int counter = 0; counter < flavors.length; counter++)
                {
                    System.out.println(flavors[counter]);
                    if (flavors[counter].equals(DataFlavor.stringFlavor))
                    {
                        return flavors[counter];
                    }
                }
            }
            return null;
        }
    }*/
}
