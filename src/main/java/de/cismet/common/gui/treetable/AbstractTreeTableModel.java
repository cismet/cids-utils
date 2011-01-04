/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.common.gui.treetable;

/*
 * Copyright 1997-1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * DOCUMENT ME!
 *
 * @author   Philip Milne
 * @version  1.2 10/27/98 An abstract implementation of the TreeTableModel interface, handling the list of listeners.
 */

public abstract class AbstractTreeTableModel implements TreeTableModel {

    //~ Instance fields --------------------------------------------------------

    protected Object root;
    protected EventListenerList listenerList = new EventListenerList();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractTreeTableModel object.
     *
     * @param  root  DOCUMENT ME!
     */
    public AbstractTreeTableModel(final Object root) {
        this.root = root;
    }

    //~ Methods ----------------------------------------------------------------

    //
    // Default implementations for methods in the TreeModel interface.
    //

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(final Object node) {
        return getChildCount(node) == 0;
    }

    @Override
    public void valueForPathChanged(final TreePath path, final Object newValue) {
    }

    // This is not called in the JTree's default mode:
    // use a naive implementation.
    @Override
    public int getIndexOfChild(final Object parent, final Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
            if (getChild(parent, i).equals(child)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(final TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener(final TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }
    /**
     * Notifies all listeners that have registered interest for notification on this event type. The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param  source        DOCUMENT ME!
     * @param  path          DOCUMENT ME!
     * @param  childIndices  DOCUMENT ME!
     * @param  children      DOCUMENT ME!
     *
     * @see    EventListenerList
     */
    protected void fireTreeNodesChanged(final Object source,
            final Object[] path,
            final int[] childIndices,
            final Object[] children) {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener)listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }
    /**
     * Notifies all listeners that have registered interest for notification on this event type. The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param  source        DOCUMENT ME!
     * @param  path          DOCUMENT ME!
     * @param  childIndices  DOCUMENT ME!
     * @param  children      DOCUMENT ME!
     *
     * @see    EventListenerList
     */
    protected void fireTreeNodesInserted(final Object source,
            final Object[] path,
            final int[] childIndices,
            final Object[] children) {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener)listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }
    /**
     * Notifies all listeners that have registered interest for notification on this event type. The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param  source        DOCUMENT ME!
     * @param  path          DOCUMENT ME!
     * @param  childIndices  DOCUMENT ME!
     * @param  children      DOCUMENT ME!
     *
     * @see    EventListenerList
     */
    protected void fireTreeNodesRemoved(final Object source,
            final Object[] path,
            final int[] childIndices,
            final Object[] children) {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener)listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }
    /**
     * Notifies all listeners that have registered interest for notification on this event type. The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param  source        DOCUMENT ME!
     * @param  path          DOCUMENT ME!
     * @param  childIndices  DOCUMENT ME!
     * @param  children      DOCUMENT ME!
     *
     * @see    EventListenerList
     */
    protected void fireTreeStructureChanged(final Object source,
            final Object[] path,
            final int[] childIndices,
            final Object[] children) {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener)listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    //
    // Default impelmentations for methods in the TreeTableModel interface.
    //

    @Override
    public Class getColumnClass(final int column) {
        return Object.class;
    }

    /**
     * By default, make the column with the Tree in it the only editable one. Making this column editable causes the
     * JTable to forward mouse and keyboard events in the Tree column to the underlying JTree.
     *
     * @param   node    DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isCellEditable(final Object node, final int column) {
        return getColumnClass(column) == TreeTableModel.class;
    }

    @Override
    public void setValueAt(final Object aValue, final Object node, final int column) {
    }

    // Left to be implemented in the subclass:

    /*
     *   public Object getChild(Object parent, int index)  public int getChildCount(Object parent)  public int
     * getColumnCount()  public String getColumnName(Object node, int column)  public Object getValueAt(Object node, int
     * column)
     */

    // -------------------------------------------------------------------------

    /**
     * Invoke this method if you've modified the TreeNodes upon which this model depends. The model will notify all of
     * its listeners that the model has changed.
     */
    public void reload() {
        reload((TreeNode)root);
    }

    /**
     * Invoke this method if you've modified the TreeNodes upon which this model depends. The model will notify all of
     * its listeners that the model has changed below the node <code>node</code> (PENDING).
     *
     * @param  node  DOCUMENT ME!
     */
    public void reload(final TreeNode node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  root  DOCUMENT ME!
     */
    public void setRoot(final TreeNode root) {
        final Object oldRoot = this.root;
        this.root = root;
        if ((root == null) && (oldRoot != null)) {
            fireTreeStructureChanged(this, null);
        } else {
            nodeStructureChanged(root);
        }
    }

    /**
     * Invoke this method if you've totally changed the children of node and its childrens children... This will post a
     * treeStructureChanged event.
     *
     * @param  node  DOCUMENT ME!
     */
    public void nodeStructureChanged(final TreeNode node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Builds the parents of the node up to and including the root node, where the original node is the last element in
     * the returned array. The length of the returned array gives the node's depth in the tree.
     *
     * @param   aNode  the TreeNode to get the path for
     *
     * @return  DOCUMENT ME!
     */
    public TreeNode[] getPathToRoot(final TreeNode aNode) {
        return getPathToRoot(aNode, 0);
    }

    /**
     * Builds the parents of the node up to and including the root node, where the original node is the last element in
     * the returned array. The length of the returned array gives the node's depth in the tree.
     *
     * @param   aNode  the TreeNode to get the path for
     * @param   depth  an int giving the number of steps already taken towards the root (on recursive calls), used to
     *                 size the returned array
     *
     * @return  an array of TreeNodes giving the path from the root to the specified node
     */
    protected TreeNode[] getPathToRoot(final TreeNode aNode, int depth) {
        TreeNode[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
         *they passed in an element that isn't rooted at root. */
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new TreeNode[depth];
            }
        } else {
            depth++;
            if (aNode == root) {
                retNodes = new TreeNode[depth];
            } else {
                retNodes = getPathToRoot(aNode.getParent(), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }
    /**
     * Notifies all listeners that have registered interest for notification on this event type. The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param  source  the node where the tree model has changed
     * @param  path    the path to the root node
     *
     * @see    EventListenerList
     */
    private void fireTreeStructureChanged(final Object source, final TreePath path) {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path);
                }
                ((TreeModelListener)listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }
}
