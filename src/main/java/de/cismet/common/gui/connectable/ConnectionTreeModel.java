/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ConnectionTreeModel.java
 *
 * Created on 7. August 2003, 09:51
 */
package de.cismet.common.gui.connectable;

import javax.swing.tree.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ConnectionTreeModel extends DefaultConnectionModel implements TreeModel {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ConnectionTreeModel.
     */
    public ConnectionTreeModel() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void addTreeModelListener(final javax.swing.event.TreeModelListener l) {
    }

    @Override
    public Object getChild(final Object parent, final int index) {
        return ((Connectable)parent).getConnectables().get(index);
    }

    @Override
    public int getChildCount(final Object parent) {
        return ((Connectable)parent).getLinkCount();
    }

    @Override
    public int getIndexOfChild(final Object parent, final Object child) {
        return ((Connectable)parent).getConnectables().indexOf(child);
    }

    @Override
    public Object getRoot() {
        return (super.connectableList.size() > 0) ? super.getConnectable(0) : null;
    }

    @Override
    public boolean isLeaf(final Object node) {
        return !((Connectable)node).isSource();
    }

    @Override
    public void removeTreeModelListener(final javax.swing.event.TreeModelListener l) {
    }

    @Override
    public void valueForPathChanged(final TreePath path, final Object newValue) {
    }
}
