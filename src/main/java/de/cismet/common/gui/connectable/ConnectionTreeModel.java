/*
 * ConnectionTreeModel.java
 *
 * Created on 7. August 2003, 09:51
 */

package de.cismet.common.gui.connectable;

import javax.swing.tree.*;

/**
 *
 * @author  pascal
 */
public class ConnectionTreeModel extends DefaultConnectionModel implements TreeModel
{
    
    /** Creates a new instance of ConnectionTreeModel */
    public ConnectionTreeModel()
    {
    }
    
    public void addTreeModelListener(javax.swing.event.TreeModelListener l)
    {
    }
    
    public Object getChild(Object parent, int index)
    {
        return ((Connectable)parent).getConnectables().get(index);
    }
    
    public int getChildCount(Object parent)
    {
        return ((Connectable)parent).getLinkCount();
    }
    
    public int getIndexOfChild(Object parent, Object child)
    {
        return ((Connectable)parent).getConnectables().indexOf(child);
    }
    
    public Object getRoot()
    {
        return super.connectableList.size() > 0 ? super.getConnectable(0) : null;         
    }
    
    public boolean isLeaf(Object node)
    {
        return !((Connectable)node).isSource();
    }
    
    public void removeTreeModelListener(javax.swing.event.TreeModelListener l)
    {
        
    }
    
    public void valueForPathChanged(TreePath path, Object newValue)
    {
        
    }
    
}
