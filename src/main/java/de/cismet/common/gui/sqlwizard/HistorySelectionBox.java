/*
 * HistorySelectionBox.java
 *
 * Created on 8. September 2003, 14:40
 */

package de.cismet.common.gui.sqlwizard;

import java.beans.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;



/**
 *
 * @author  pascal
 */
public class HistorySelectionBox extends JComboBox
{   
    private final LinkedList historySupportList;
    //private final PropertyChangeListener historyObserver;

    /** 
     * Creates a new instance of HistorySelectionBox.<p>
     * Creates five default entries (Query #n).
     */
    public HistorySelectionBox()
    {
        this(new String[] {"Query #1", "Query #2", "Query #3", "Query #4", "Query #5"});
    }
    
    
    /** 
     * Creates a new instance of HistorySelectionBox.
     *
     * @param entries the default entries of this box.
     */
    public HistorySelectionBox(String[] entries)
    {
        super(entries);
        
        this.historySupportList = new LinkedList();
        //this.historyObserver = new HistoryObserver();
        
        this.setRenderer(new HistoryListCellRenderer());
        this.addItemListener(new HistorySelectionListener());
        
        // no good idea:
        //this.setMaximumWitdh(175);		
    }
    
    /**
     * Sets new entries of this box.<p>
     * Clears all registred history lists.
     *
     * @param entries the new entries of this box.
     */
    public void update(String[] entries)
    {
        this.setModel(new DefaultComboBoxModel(entries));
        this.setSelectedIndex(-1);
        
        Iterator iterator = this.historySupportList.iterator();
        while(iterator.hasNext())
        {
            ((History)iterator.next()).clearHistory();
        }
        
    }
    
    /**
     * Sets the maximum witdh of this box (in pixels).
     *
     * @param width the new maximum witdh of this box
     * @deprecated don't use this method
     */
    protected void setMaximumWitdh(int width)
    {
        Dimension dimension = this.getPreferredSize();
	dimension.width = width;
	this.setPreferredSize(dimension);
        this.setMaximumSize(getPreferredSize());
    }
    
    /**
     * Selectes a new history entry.
     *
     * @param index the index of the history entry in the history list
     */
    public void setSelectedHistoryEntry(int index)
    {
        if(index < this.getItemCount())
        {
            this.setSelectedIndex(index);
            this.selectHistoryEntry(index);
            
            /*if(this.getSelectedIndex() != index)
            {
                this.setSelectedIndex(index);
            }
            else
            {
                this.selectHistroyEntry(index);
            } */
        }
    }
    
    /**
     * Selectes a new history entry, notifies all history objects.
     *
     * @param name the name of the history object.
     */
    public void setSelectedHistoryEntry(String name)
    {
        for(int i = 0; i < this.getItemCount(); i++)
        {
            if(this.getItemAt(i).toString().equalsIgnoreCase(name))
            {
                this.setSelectedHistoryEntry(i);
                break;
            }
        }
    }
    
    /**
     * Adds a new history to the history list.
     *
     * @param history a new history object.
     */
    protected void addHistory(History history) throws IllegalArgumentException
    {
        if(history.getMaxHistorySize() < this.getItemCount())
        {
            throw new IllegalArgumentException("max history size: " + history.getMaxHistorySize() + " dos not match history box size: " + this.getItemCount());
        }
        else if(!this.historySupportList.contains(history))
        {
            //history.addPropertyChangeListener(this.historyObserver);
            this.historySupportList.add(history);
        }
    }
    
    /**
     * Adds a new history to the history list.
     *
     * @param history a new history object.
     */
    protected void removeHistory(History history)
    {
        if(this.historySupportList.contains(history))
        {
            //history.removePropertyChangeListener(this.historyObserver);
            this.historySupportList.remove(history);
        }
    } 

    /**
     * Selectes a new history entry, notifies all history objects.
     */
    private void selectHistoryEntry(int selectedHistoryEntry)
    {
        //System.out.println("selectHistoryEntry " + selectedHistroyEntry);
        Iterator iterator = this.historySupportList.iterator();
        while(iterator.hasNext())
        {
            ((History)iterator.next()).setSelectedIndex(selectedHistoryEntry);
        }
    }
    
    private boolean isValid(int index)
    {
        Iterator iterator = this.historySupportList.iterator();
        while(iterator.hasNext())
        {
            History history = ((History)iterator.next());
            if(index < 0 || index >= history.getHistorySize() || history.getHistroyEntry(index) == null)
            {
                return false;
            }
        }
        
        return true;
    }
    
    // -------------------------------------------------------------------------
    
    private class HistorySelectionListener implements ItemListener
    {
        public void itemStateChanged(ItemEvent e)
        {
            if(e.getStateChange() == ItemEvent.SELECTED)
            {
                //System.out.println("itemStateChanged " + getSelectedIndex());
                selectHistoryEntry(HistorySelectionBox.this.getSelectedIndex());
            }
        } 
    }
    
    private class HistoryListCellRenderer extends DefaultListCellRenderer
    {
        protected final ImageIcon validIcon;
        protected final ImageIcon invalidIcon;
        
        
        protected HistoryListCellRenderer()
        {
            super();
            
            validIcon = new ImageIcon(this.getClass().getResource("/de/cismet/common/gui/sqlwizard/images/valid.gif"));
            invalidIcon = new ImageIcon(this.getClass().getResource("/de/cismet/common/gui/sqlwizard/images/invalid.gif"));
        }
        
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
        {
            JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            int selectedIndex = index > -1 ? index : HistorySelectionBox.this.getSelectedIndex();

            if(HistorySelectionBox.this.isValid(selectedIndex))
            {
                label.setIcon(validIcon);
            }
            else
            {
                label.setIcon(invalidIcon);
            }
            
            return label;
        }
    }
    
    /*private class HistoryObserver implements PropertyChangeListener
    {
        public void propertyChange(PropertyChangeEvent evt)
        {
            if(evt.getPropertyName().equals(History.SELECTED_HISTORY_ENTRY))
            {
               selectHistoryEntry(((Integer)evt.getNewValue()).intValue());
            }
        }   
    }*/
}
