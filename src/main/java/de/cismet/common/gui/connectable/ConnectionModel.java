/*
 * ConnectionModel.java
 *
 * Created on 4. August 2003, 16:20
 */

package de.cismet.common.gui.connectable;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 *
 * @author  pascal
 */
public interface ConnectionModel
{
    public void addConnectable(Connectable connectable);
    
    public void removeConnectable(Connectable connectable);
    
    public void removeConnectable(int index);
    
    public Connectable getConnectable(int index);
    
    public List getConnectables();
    
    public void addLink(ConnectionLink link);
    
    public void removeLink(ConnectionLink link);
    
    public void removeLink(int index);
    
    public ConnectionLink getLink(int index);
    
    public List getLinks();
    
    public void addPropertyChangeListener(PropertyChangeListener listener);
    
    public void removePropertyChangeListener(PropertyChangeListener listener); 
    
    public void clear();
}
