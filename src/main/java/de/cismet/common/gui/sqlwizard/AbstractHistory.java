/*
 * AbstractHistory.java
 *
 * Created on 9. September 2003, 16:43
 */

package de.cismet.common.gui.sqlwizard;


import java.beans.*;
import javax.swing.event.*;
import java.util.*;

/**
 *
 * @author  pascal
 */
public abstract class AbstractHistory implements History
{   
    protected int maxHistorySize = 0;
    protected LinkedList historyList;
    
    protected int selectedIndex = -1;
    
    public AbstractHistory(int maxHistorySize)
    {
        this.maxHistorySize = maxHistorySize;
        this.historyList = new LinkedList();
    }
    
    public void clearHistory()
    {
        this.historyList.clear();
        this.selectedIndex = -1;
    }
    
    public int getHistorySize()
    {
        return this.historyList.size();
    }
    
    public int getMaxHistorySize()
    {
        return this.maxHistorySize;
    }
    
    public abstract void setSelectedIndex(int selectedIndex);

    public void addHistoryEntry(Object historyEntry)
    {
        this.setHistoryEntry(historyEntry, -1);
    }
    
    public void setHistoryEntry(Object historyEntry, int index)
    {
        if(index == -1)
        {
            if(this.maxHistorySize > 0)
            {
                if(this.historyList.size() == this.maxHistorySize)
                {
                    this.historyList.removeFirst();
                }

                this.historyList.add(historyEntry);
                this.selectedIndex = this.historyList.size()-1;
            }
        }
        else
        {
            if(this.maxHistorySize > 0)
            {
                if(index >= this.historyList.size())
                {
                    int i = this.historyList.size();
                    for(; i <= index; i++)
                    {
                        //System.out.println("add" + i + " " + index);
                        this.historyList.add(null);
                    }
                }
                
                this.historyList.set(index, historyEntry);
                this.selectedIndex = index;
            }
        }
    }
    
    public int getSelectedIndex()
    {
        return this.selectedIndex;
    }
    
    public Object getHistroyEntry(int index)
    {
        return this.historyList.get(index);
    }
    
    public Object getSelectedHistoryEntry()
    {
        return this.historyList.get(this.selectedIndex);
    }
    
    public java.util.Iterator getHistoryEntries()
    {
        return this.historyList.iterator();
    }
    
    public abstract void removePropertyChangeListener(java.beans.PropertyChangeListener l);
    
    public abstract void addPropertyChangeListener(java.beans.PropertyChangeListener l);
}
