/*
 * DefaultHistory.java
 *
 * Created on 9. September 2003, 16:15
 */

package de.cismet.common.gui.sqlwizard;

import java.beans.*;
import javax.swing.event.*;
import java.util.*;

/**
 *
 * @author  pascal
 */
public class DefaultHistory extends AbstractHistory
{
    protected final PropertyChangeSupport propertyChangeSupport;    
    
    /** Creates a new instance of DefaultHistory */
    public DefaultHistory()
    {
        this(8);
    }
    
    public DefaultHistory(int maxHistorySize)
    {
        super(maxHistorySize);
        this.propertyChangeSupport = new SwingPropertyChangeSupport(this);
        this.historyList = new LinkedList();
    }
    
    public DefaultHistory(int maxHistorySize, PropertyChangeSupport propertyChangeSupport)
    {
        super(maxHistorySize);
        this.propertyChangeSupport = propertyChangeSupport;
        this.historyList = new LinkedList();
    }
    
    public void setSelectedIndex(int selectedIndex)
    {
        if(selectedIndex >= 0 && this.selectedIndex != selectedIndex && selectedIndex < this.historyList.size() && this.historyList.get(selectedIndex) != null)
        {
            int oldSelectedIndex = this.selectedIndex;
            this.selectedIndex = selectedIndex;
            this.propertyChangeSupport.firePropertyChange(SELECTED_HISTORY_ENTRY, new Integer(oldSelectedIndex), new Integer(selectedIndex));
        }
    }
    
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        this.propertyChangeSupport.removePropertyChangeListener(l);
    }
    
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        this.propertyChangeSupport.addPropertyChangeListener(l);
    }
}
