/*
 * DefaultConnectionModel.java
 *
 * Created on 6. August 2003, 17:42
 */

package de.cismet.common.gui.connectable;

import java.beans.PropertyChangeListener;
import java.util.*;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 *
 * @author  pascal
 */
public class DefaultConnectionModel implements ConnectionModel
{
    private final SwingPropertyChangeSupport propertyChangeSupport;
    
    protected final ArrayList connectableList;
    protected final ArrayList linkList;

    
    /** Creates a new instance of DefaultConnectionModel */
    public DefaultConnectionModel()
    {
        this.connectableList = new ArrayList();
        this.linkList = new ArrayList();
        this.propertyChangeSupport = new SwingPropertyChangeSupport(this);
    }
    
    public void addConnectable(Connectable connectable)
    {
        this.connectableList.add(connectable);
        this.propertyChangeSupport.firePropertyChange("addConnectable", null, connectable);
    }
    
    public void removeConnectable(Connectable connectable)
    {
        this.connectableList.remove(connectable);
        this.removeLinks(connectable);
        
        this.propertyChangeSupport.firePropertyChange("removeConnectable", connectable, null);
    }
    
    public void removeConnectable(int index)
    {
        Object object = connectableList.remove(index);
        if(object != null)
        {
            this.removeLinks((Connectable)object);
        }

        propertyChangeSupport.firePropertyChange("removeConnectable", object, null);
    }
    
    public Connectable getConnectable(int index)
    {
         Object object = connectableList.get(index);
         return object != null ? (Connectable)object : null;
    }
    
    public List getConnectables()
    {
        return Collections.unmodifiableList(this.connectableList);
    }
    
    public void addLink(ConnectionLink link)
    {
        link.link();
        linkList.add(link);
        
        propertyChangeSupport.firePropertyChange("addLink", null, link);
    }
    
    public void removeLink(ConnectionLink link)
    {
        linkList.remove(link);
        link.unlink();
        
        propertyChangeSupport.firePropertyChange("removeLink", link, null);
    }
    
    public void removeLink(int index)
    {
        Object object = linkList.remove(index);
        if(object != null)
        {
            ((ConnectionLink)object).unlink();
        }
        
        propertyChangeSupport.firePropertyChange("removeLink", object, null);
    }
    
    public ConnectionLink getLink(int index)
    {
         Object object = linkList.get(index);
         return object != null ? (ConnectionLink)object : null;
    }
    
    public List getLinks()
    {
        return Collections.unmodifiableList(this.linkList);
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    public void clear()
    {
        LinkedList connectableList = new LinkedList();
        connectableList.addAll(this.getConnectables());
        
        Iterator iterator = connectableList.iterator();
        while(iterator.hasNext())
        {
            this.removeConnectable((Connectable)iterator.next());
        }
        
        this.connectableList.clear();
        this.linkList.clear();
    }
    
    // .........................................................................
    
    protected void removeLinks(Connectable connectable)
    {
        Iterator iterator = new LinkedList(connectable.getLinks()).iterator();
        while(iterator.hasNext())
        {
            ConnectionLink link = (ConnectionLink)iterator.next();
            this.removeLink(link);
        }
    }
}
