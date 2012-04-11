/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.common.gui.connectable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The Cahnges in this model are reflected to a structure.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public class ConnectionTreeNodeModel extends DefaultConnectionModel {

    //~ Instance fields --------------------------------------------------------

    protected boolean showConnectablesAsRoots = false;

    protected final DefaultMutableTreeNode rootNode;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ConnectionTreeNodeModel.
     */
    public ConnectionTreeNodeModel() {
        this(false);
    }

    /**
     * Creates a new instance of ConnectionTreeNodeModel.
     *
     * @param  showConnectablesAsRoots  DOCUMENT ME!
     */
    public ConnectionTreeNodeModel(final boolean showConnectablesAsRoots) {
        this.rootNode = new DefaultMutableTreeNode("root node");
        this.showConnectablesAsRoots = showConnectablesAsRoots;

        this.addPropertyChangeListener(new ConnectableChangeListener());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DefaultMutableTreeNode getRootNode() {
        return rootNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isShowConnectablesAsRoots() {
        return this.showConnectablesAsRoots;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  showConnectablesAsRoots  DOCUMENT ME!
     */
    public void setShowConnectablesAsRoots(final boolean showConnectablesAsRoots) {
        this.showConnectablesAsRoots = showConnectablesAsRoots;
    }

    /**
     * -------------------------------------------------------------------------
     *
     * @param  parentNode  DOCUMENT ME!
     * @param  link        DOCUMENT ME!
     */
    protected void addLink(final ConnectableTreeNode parentNode, final ConnectionLink link) {
        boolean hasThisChild = false;
        final Enumeration children = parentNode.children();

        if (!this.isShowConnectablesAsRoots()) {
            this.removeRootNode(link.getTarget().getConnectable());
        }

        while (children.hasMoreElements()) {
            final ConnectableTreeNode childNode = (ConnectableTreeNode)children.nextElement();
            if (childNode.getUserObject() == link.getTarget().getConnectable()) {
                hasThisChild = true;
            }

            this.addLink(childNode, link);
        }

        if (parentNode.isExplored() && !hasThisChild
                    && (parentNode.getUserObject() == link.getSource().getConnectable())) {
            parentNode.add(new ConnectableTreeNode(link.getTarget().getConnectable()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  parentNode  DOCUMENT ME!
     * @param  link        DOCUMENT ME!
     */
    protected void removeLink(final ConnectableTreeNode parentNode, final ConnectionLink link) {
        boolean removeChild = true;

        // first check if there is at least one link left
        System.out.println("parentNode.getUserObject(): " + parentNode.getUserObject()
                    + " link.getSource().getConnectable(): " + link.getSource().getConnectable());
        if (parentNode.getUserObject() == link.getSource().getConnectable()) {
            final java.util.List childrenList = link.getSource().getConnectable().getTargetConnectables();
            final Iterator iterator = childrenList.iterator();

            while (iterator.hasNext()) {
                if (iterator.next() == link.getTarget().getConnectable()) {
                    removeChild = false;
                    break;
                }
            }
        }

        for (int i = parentNode.getChildCount() - 1; i >= 0; i--) {
            final ConnectableTreeNode childNode = (ConnectableTreeNode)parentNode.getChildAt(i);
            if (removeChild && (childNode.getUserObject() == link.getTarget().getConnectable())) {
                parentNode.remove(i);

                if (!isShowConnectablesAsRoots() && (childNode.getConnectable().getTargetLinkCount() == 0)) {
                    this.addRootNode(childNode.getConnectable());
                }
            } else {
                this.removeLink(childNode, link);
            }
        }
    }

    /**
     * .........................................................................
     *
     * @param   treeNode  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected boolean isRootNode(final ConnectableTreeNode treeNode) {
        final Enumeration children = this.rootNode.children();
        while (children.hasMoreElements()) {
            if (((ConnectableTreeNode)children.nextElement()).getUserObject() == treeNode.getUserObject()) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  connectable  DOCUMENT ME!
     */
    protected void addRootNode(final Connectable connectable) {
        boolean addNode = true;

        // remove children
        for (int i = 0; i < this.rootNode.getChildCount(); i++) {
            if (((DefaultMutableTreeNode)this.rootNode.getChildAt(i)).getUserObject() == connectable) {
                addNode = false;
                break;
            }
        }

        if (addNode) {
            this.rootNode.add(new ConnectableTreeNode(connectable));
        }
    }

    /**
     * Remove all link.
     *
     * @param  connectable  DOCUMENT ME!
     */
    protected void removeRootNode(final Connectable connectable) {
        // remove children
        for (int i = this.rootNode.getChildCount() - 1; i >= 0; i--) {
            if (((DefaultMutableTreeNode)this.rootNode.getChildAt(i)).getUserObject() == connectable) {
                System.out.println("remove root node " + connectable);
                this.rootNode.remove(i);
                return;
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * -------------------------------------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    protected class ConnectableChangeListener implements PropertyChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("addConnectable") && (evt.getNewValue() != null)) {
                addRootNode((Connectable)evt.getNewValue());
            } else if (evt.getPropertyName().equals("removeConnectable") && (evt.getOldValue() != null)) {
                removeRootNode((Connectable)evt.getOldValue());
            } else if (evt.getPropertyName().equals("addLink") && (evt.getNewValue() != null)) {
                final Enumeration children = rootNode.children();
                while (children.hasMoreElements()) {
                    addLink((ConnectableTreeNode)children.nextElement(), (ConnectionLink)evt.getNewValue());
                }
            } else if (evt.getPropertyName().equals("removeLink") && (evt.getOldValue() != null)) {
                final Enumeration children = rootNode.children();
                while (children.hasMoreElements()) {
                    removeLink((ConnectableTreeNode)children.nextElement(), (ConnectionLink)evt.getOldValue());
                }
            }
        }
    }

    /**
     * .........................................................................
     *
     * @version  $Revision$, $Date$
     */
    protected class ConnectableTreeNode extends DefaultMutableTreeNode {

        //~ Instance fields ----------------------------------------------------

        protected boolean explored = false;
        protected boolean leaf = true;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ConnectableTreeNode object.
         *
         * @param  connectable  DOCUMENT ME!
         */
        protected ConnectableTreeNode(final Connectable connectable) {
            super(connectable);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        public void explore() {
            if (!this.isExplored()) {
                final Iterator iterator = this.getConnectable().getLinks().iterator();
                while (iterator.hasNext()) {
                    final ConnectionLink link = (ConnectionLink)iterator.next();
                    if (this.getUserObject() == link.getSource().getConnectable()) {
                        boolean hasThisChild = false;
                        final Enumeration children = this.children();

                        while (children.hasMoreElements()) {
                            final ConnectableTreeNode childNode = (ConnectableTreeNode)children.nextElement();
                            if (childNode.getUserObject() == link.getTarget().getConnectable()) {
                                hasThisChild = true;
                            }
                        }

                        if (!hasThisChild) {
                            System.out.println("2: this == source: "
                                        + (this.getUserObject() == link.getSource().getConnectable()));
                            this.add(new ConnectableTreeNode(link.getTarget().getConnectable()));

                            if (!isShowConnectablesAsRoots()) {
                                removeRootNode(link.getTarget().getConnectable());
                            }
                        }
                    }
                }

                this.setExplored(true);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isExplored() {
            return this.explored;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  explored  DOCUMENT ME!
         */
        protected void setExplored(final boolean explored) {
            this.explored = explored;
        }

        @Override
        public boolean isLeaf() {
            return this.getConnectable().getTargetLinkCount() == 0;
        }

        @Override
        public boolean getAllowsChildren() {
            return !this.isLeaf();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Connectable getConnectable() {
            return (Connectable)this.getUserObject();
        }
    }
}
