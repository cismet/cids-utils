/*
 * ConnectionTreeNodeModel.java
 *
 * Created on 7. August 2003, 11:42
 */

package de.cismet.common.gui.connectable;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The Cahnges in this model are reflected to a structure
 *
 * @author  Pascal
 */
public class ConnectionTreeNodeModel extends DefaultConnectionModel
{
    protected boolean showConnectablesAsRoots = false;
    
    protected final DefaultMutableTreeNode rootNode;
    //protected ArrayList rootTreeNodes;
    
    
    /** Creates a new instance of ConnectionTreeNodeModel */
    public ConnectionTreeNodeModel()
    {
        this(false);
    }
    
    /** Creates a new instance of ConnectionTreeNodeModel */
    public ConnectionTreeNodeModel(boolean showConnectablesAsRoots)
    {
        this.rootNode = new DefaultMutableTreeNode("root node");
        this.showConnectablesAsRoots = showConnectablesAsRoots;
        
        this.addPropertyChangeListener(new ConnectableChangeListener());
    }
    
    public DefaultMutableTreeNode getRootNode()
    {
        return rootNode;
    }
    
    public boolean isShowConnectablesAsRoots()
    {
        return this.showConnectablesAsRoots;
    }
    
    public void setShowConnectablesAsRoots(boolean showConnectablesAsRoots)
    {
        this.showConnectablesAsRoots = showConnectablesAsRoots;
    }
    
    // -------------------------------------------------------------------------
    
    protected void addLink(ConnectableTreeNode parentNode, ConnectionLink link)
    {
        boolean hasThisChild = false;
        Enumeration children = parentNode.children();
        
        if(!this.isShowConnectablesAsRoots())
        {
            this.removeRootNode(link.getTarget().getConnectable());
        }
        
        while(children.hasMoreElements())
        {
            ConnectableTreeNode childNode =(ConnectableTreeNode)children.nextElement();
            if(childNode.getUserObject() == link.getTarget().getConnectable())
            {
                hasThisChild = true;
            }
            
            this.addLink(childNode, link);
        }
        
        if(parentNode.isExplored() && !hasThisChild && parentNode.getUserObject() == link.getSource().getConnectable())
        {
            //System.out.println("parentNode: " + parentNode.getUserObject() + " source: " + link.getSource().getConnectable() + " target: " + link.getTarget().getConnectable());
            parentNode.add(new ConnectableTreeNode(link.getTarget().getConnectable()));
        }
    }
    
    protected void removeLink(ConnectableTreeNode parentNode, ConnectionLink link)
    {
        boolean removeChild = true;
        
        //first check if there is at least one link left
        
        System.out.println("parentNode.getUserObject(): " + parentNode.getUserObject() + " link.getSource().getConnectable(): " + link.getSource().getConnectable());
        if(parentNode.getUserObject() == link.getSource().getConnectable())
        {
            java.util.List childrenList = link.getSource().getConnectable().getTargetConnectables();
            Iterator iterator = childrenList.iterator();
            
            while(iterator.hasNext())
            {
                //System.out.println(" link.getTarget().getConnectable(): " + link.getTarget().getConnectable() + " == " + iterator.next());
                if(iterator.next() == link.getTarget().getConnectable())
                {
                    removeChild = false;
                    break;
                }
            }
        }
        
        for(int i = parentNode.getChildCount()-1; i >= 0; i--)
        {
            ConnectableTreeNode childNode = (ConnectableTreeNode)parentNode.getChildAt(i);
            if(removeChild && childNode.getUserObject() == link.getTarget().getConnectable())
            {
                parentNode.remove(i);
                
                if(!isShowConnectablesAsRoots() && childNode.getConnectable().getTargetLinkCount() == 0)
                {
                    this.addRootNode(childNode.getConnectable());
                }
            }
            else
            {
                this.removeLink(childNode, link);
            }
        }
    }
    
    /*protected void addConnectableNode(Connectable connectable)
    {
        this.rootNode.add(new ConnectableTreeNode(connectable));
    }*/
    
    
    
    
    // .........................................................................
        
    protected boolean isRootNode(ConnectableTreeNode treeNode)
    {
        Enumeration children = this.rootNode.children();
        while(children.hasMoreElements())
        {
            if(((ConnectableTreeNode)children.nextElement()).getUserObject() == treeNode.getUserObject())
            {
                return true;
            }
        }
        
        return false;
    }
    
    protected void addRootNode(Connectable connectable)
    {
        boolean addNode = true;
        
        // remove children
        for(int i = 0; i < this.rootNode.getChildCount(); i++)
        {
            if(((DefaultMutableTreeNode)this.rootNode.getChildAt(i)).getUserObject() == connectable)
            {
                addNode = false;
                break;
            }
        }
        
        if(addNode)
        {
            this.rootNode.add(new ConnectableTreeNode(connectable));
        }
    }
    
    
    /**
     * Remove all link
     *
     */
    protected void removeRootNode(Connectable connectable)
    {
        // remove children
        for(int i = this.rootNode.getChildCount()-1; i >= 0; i--)
        {
            if(((DefaultMutableTreeNode)this.rootNode.getChildAt(i)).getUserObject() == connectable)
            {
                System.out.println("remove root node " + connectable);
                this.rootNode.remove(i);
                return;
            }
        }
        
        //@deprecated
        /*
        // remove all children in path
        Enumeration children = parentNode.children();
        while(children.hasMoreElements())
        {
            this.removeConnectableNode((ConnectableTreeNode)children.nextElement(), connectable);
        }*/
    }
    
    // -------------------------------------------------------------------------
    
    protected class ConnectableChangeListener implements PropertyChangeListener
    {
        
        public void propertyChange(PropertyChangeEvent evt)
        {
            if(evt.getPropertyName().equals("addConnectable") && evt.getNewValue() != null)
            {
                //if(isShowConnectablesAsRoots())
                //{
                    addRootNode((Connectable)evt.getNewValue());
                //}
                
                //addConnectableNode((Connectable)evt.getNewValue());
            }
            else if(evt.getPropertyName().equals("removeConnectable") && evt.getOldValue() != null)
            {
                
                removeRootNode((Connectable)evt.getOldValue());
                /*Enumeration children = rootNode.children();
                while(children.hasMoreElements())
                {
                    System.out.println("removeConnectableNode :" + evt.getOldValue());
                    removeRootNode((Connectable)evt.getOldValue());
                    
                    // removeConnectableNode((ConnectableTreeNode)children.nextElement(), (Connectable)evt.getOldValue());
                }*/
            }
            else if(evt.getPropertyName().equals("addLink") && evt.getNewValue() != null)
            {
                Enumeration children = rootNode.children();
                while(children.hasMoreElements())
                {
                    addLink((ConnectableTreeNode)children.nextElement(), (ConnectionLink)evt.getNewValue());
                }
            }
            else if(evt.getPropertyName().equals("removeLink") && evt.getOldValue() != null)
            {
                Enumeration children = rootNode.children();
                while(children.hasMoreElements())
                {
                    removeLink((ConnectableTreeNode)children.nextElement(), (ConnectionLink)evt.getOldValue());
                }
            }
        }
    }
    
    // .........................................................................
    
    protected class ConnectableTreeNode extends DefaultMutableTreeNode
    {
        protected boolean explored = false;
        protected boolean leaf = true;
        
        protected ConnectableTreeNode(Connectable connectable)
        {
            super(connectable);
        }
        
        public void explore()
        {
            if(!this.isExplored())
            {
                Iterator iterator = this.getConnectable().getLinks().iterator();
                while(iterator.hasNext())
                {
                    ConnectionLink link = (ConnectionLink)iterator.next();
                    //System.out.println(".......................................");
                    //System.out.println("this: " + this.getUserObject());
                    //System.out.println("source: " + link.getSource().getConnectable());
                    //System.out.println("target: " + link.getTarget().getConnectable());
                    //System.out.println("source == target: " + (link.getSource().getConnectable() == link.getTarget().getConnectable()));
                    //System.out.println("1: this == source: " + (this.getUserObject() == link.getSource().getConnectable()));
                    if(this.getUserObject() == link.getSource().getConnectable())
                    {
                        boolean hasThisChild = false;
                        Enumeration children = this.children();
                        
                        while(children.hasMoreElements())
                        {
                            ConnectableTreeNode childNode =(ConnectableTreeNode)children.nextElement();
                            if(childNode.getUserObject() == link.getTarget().getConnectable())
                            {
                                hasThisChild = true;
                            }
                        }
                        
                        if(!hasThisChild)
                        {
                            System.out.println("2: this == source: " + (this.getUserObject() == link.getSource().getConnectable()));
                            this.add(new ConnectableTreeNode(link.getTarget().getConnectable()));
                            
                            if(!isShowConnectablesAsRoots())
                            {
                                removeRootNode(link.getTarget().getConnectable());
                            }
                        }
                    }
                }
                
                this.setExplored(true);
            }
        }
        
        public boolean isExplored()
        {
            return this.explored;
        }
        
        protected void setExplored(boolean explored)
        {
            this.explored = explored;
        }
        
        public boolean isLeaf()
        {
            //System.out.println(this.getUserObject() + " getTargetLinkCount(): " + this.getConnectable().getTargetLinkCount());
            return this.getConnectable().getTargetLinkCount() == 0;
        }
        
        public boolean getAllowsChildren()
        {
            return !this.isLeaf();
        }
        
        public Connectable getConnectable()
        {
            return (Connectable)this.getUserObject();
        }
    }
    
    // =========================================================================
    
   /* public static void main(String args[])
    {
        final ConnectionTreeNodeModel ctm = new ConnectionTreeNodeModel();
        
        final DefaultConnectable dc_1 = new DefaultConnectable("#01");
        ctm.addConnectable(dc_1);
        final DefaultConnectable dc_2 = new DefaultConnectable("#02");
        ctm.addConnectable(dc_2);
        final DefaultConnectable dc_3 = new DefaultConnectable("#03");
        ctm.addConnectable(dc_3);
        
        final ConnectionLink l_1 = new ConnectionLink(dc_1.createPoint(), dc_3.createPoint());
        ctm.addLink(l_1);
        final ConnectionLink l_2 = new ConnectionLink(dc_1.createPoint(), dc_3.createPoint());
        ctm.addLink(l_2);
        //final ConnectionLink l_2 = new ConnectionLink(dc_2.createPoint(), dc_3.createPoint());
        //ctm.addLink(l_2);
        //ConnectionLink l_3 = new ConnectionLink(dc_2.createPoint(), dc_1.createPoint());
        //ctm.addLink(l_3);
        
        final ConnectionLink l_4 = new ConnectionLink(dc_3.createPoint(), dc_2.createPoint());
        //ctm.addLink(l_4);
        
        //ctm.removeLink(l_4);
        //ctm.addLink(l_4);
        
        System.out.println("l_1: " + l_1.getId());
        System.out.println("l_4: " + l_4.getId());
        
        
        System.out.println("dc_1.getLinkCount(): " + dc_1.getLinkCount());
        System.out.println("dc_1.getTargetCount(): " + dc_1.getTargetLinkCount());
        System.out.println("dc_1.getSourceCount(): " + dc_1.getSourceLinkCount());
        
        System.out.println("dc_3.getLinkCount(): " + dc_3.getLinkCount());
        System.out.println("dc_3.getTargetCount(): " + dc_3.getTargetLinkCount());
        System.out.println("dc_3.getSourceCount(): " + dc_3.getSourceLinkCount());
        
        //ctm.removeLink(l_1);
        
        
        
        final DefaultTreeModel dtm = new DefaultTreeModel(ctm.getRootNode(), true);
        final JTree jt = new JTree(dtm);
        jt.addTreeExpansionListener(new TreeExpansionListener()
        {
            public void treeCollapsed(TreeExpansionEvent event)
            {}
            
            public void treeExpanded(TreeExpansionEvent event)
            {
                Object object = event.getPath().getLastPathComponent();
                if(object instanceof ConnectableTreeNode)
                {
                    ((ConnectableTreeNode)object).explore();
                    dtm.nodeStructureChanged((ConnectableTreeNode)object);
                }
            }
            
        });
        
        final JButton jb1 = new JButton("Remove Link");
        jb1.addActionListener(new ActionListener()
        {
            public void  actionPerformed(ActionEvent e)
            {
                //ctm.removeLink(l_2);
                //ctm.removeLink(0);,
                ctm.addLink(l_4);
                dtm.nodeChanged(ctm.getRootNode());
                dtm.reload();
            }
        });
        
        final JButton jb2 = new JButton("Remo ve Node");
        jb2.addActionListener(new ActionListener()
        {
            public void  actionPerformed(ActionEvent e)
            {
                //ctm.removeConnectable(1);
                ctm.removeConnectable(dc_2);
                dtm.nodeChanged(ctm.getRootNode());
                dtm.reload();
            }
        });
        
        JFrame jf = new JFrame("T est");
        jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
        jf.setSize(250, 600);
        jf.setLocationRelativeTo(null);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(jt, BorderLayout.CENTER);
        jf.getContentPane().add(jb1, BorderLayout.SOUTH);
        jf.getContentPane().add(jb2, BorderLayout.NORTH);
        jf.setVisible(true);
    }
    
    static class DefaultConnectable extends AbstractConnectable
    {
        public DefaultConnectable(String name)
        {
            super(name);
        }

        public de.cismet.common.gui.connectable.ConnectionPoint createPoint()
        {
            return new ConnectionPoint(this, null, new Point((int)(Math.random()*200), (int)(Math.random()*300)));
        }

    }*/
}


