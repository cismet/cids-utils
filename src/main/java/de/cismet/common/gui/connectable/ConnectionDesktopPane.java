/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ConnectionDesktopPane.java
 *
 * Created on 11. August 2003, 17:46
 */
package de.cismet.common.gui.connectable;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ConnectionDesktopPane extends JDesktopPane {

    //~ Instance fields --------------------------------------------------------

    protected ConnectionModel model;
    protected ConnectorPane connectorPane;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConnectionDesktopPane object.
     */
    public ConnectionDesktopPane() {
        this(new DefaultConnectionModel());
    }

    /**
     * Creates a new instance of ConnectionDesktopPane.
     *
     * @param  model  DOCUMENT ME!
     */
    public ConnectionDesktopPane(final ConnectionModel model) {
        super();

        this.model = model;
        this.connectorPane = new ConnectorPane(model);
        this.add(connectorPane, this.DEFAULT_LAYER, -1);

        // this.setDragMode(this.OUTLINE_DRAG_MODE );
        this.addComponentListener(new ResizeListener());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ConnectionModel getModel() {
        return this.model;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  connectableFrame  DOCUMENT ME!
     */
    public void addConnectableFrame(final ConnectableFrame connectableFrame) {
        this.add(connectableFrame, this.DEFAULT_LAYER, 0);
    }

    @Override
    public void removeAll() {
        final JInternalFrame[] frames = this.getAllFramesInLayer(this.DEFAULT_LAYER.intValue());
        if ((frames != null) && (frames.length > 0)) {
            for (int i = 0; i < frames.length; i++) {
                frames[i].setVisible(false);
                frames[i].setClosable(true);
                this.remove(frames[i]);

                try {
                    frames[i].setClosed(true);
                } catch (java.beans.PropertyVetoException pvexp) {
                    throw new RuntimeException(pvexp);
                }
            }
        }

        // this.getModel().clear();
    }

    // .........................................................................

    //~ Inner Classes ----------------------------------------------------------

    /**
     * This ComponentListener will resize the ConnectorPane to match the ConnectionDesktopPane's size .
     *
     * @version  $Revision$, $Date$
     */
    private class ResizeListener extends ComponentAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void componentResized(final ComponentEvent e) {
            connectorPane.setSize(ConnectionDesktopPane.this.getSize());
        }
    }

    // #########################################################################

    /*public static void main(String args[])
     * { final ConnectionTreeNodeModel ctm = new ConnectionTreeNodeModel(false);
     * //ConnectionTreeNodeModel.DefaultConnectable dc_1 = new ConnectionTreeNodeModel.DefaultConnectable("#01");
     * //ConnectionTreeNodeModel.DefaultConnectable dc_2 = new ConnectionTreeNodeModel.DefaultConnectable("#02");
     * //ConnectionTreeNodeModel.DefaultConnectable dc_3 = new ConnectionTreeNodeModel.DefaultConnectable("#03");
     * //ConnectionTreeNodeModel.DefaultConnectable dc_4 = new ConnectionTreeNodeModel.DefaultConnectable("#04");
     * //ConnectionLink l_1 = new ConnectionLink(dc_1.createPoint(), dc_2.createPoint()); //ConnectionLink l_2 = new
     * ConnectionLink(dc_2.createPoint(), dc_3.createPoint()); //ConnectionLink l_3 = new
     * ConnectionLink(dc_3.createPoint(), dc_4.createPoint());
     *
     * //ctm.addConnectable(dc_1); //ctm.addConnectable(dc_2); //ctm.addConnectable(dc_3); //ctm.addConnectable(dc_4);
     * //ctm.addLink(l_1); //ctm.addLink(l_2); //ctm.addLink(l_3);  //l_3.getSource().getAnchorPoint().y = 200;
     * //l_3.getSource().getAnchorPoint().x = 0; //l_3.getTarget().getAnchorPoint().y = 200;
     * //l_3.getTarget().getAnchorPoint().x = 600;  //ctm.removeLink(l_3);     ConnectableFrame cf_1 = new
     * ConnectableFrame(ctm, "Frame #1"); cf_1.setSize(200,200); cf_1.getContentPane().add(new JButton("Frame #1"));
     * cf_1.setVisible(true);  ConnectableFrame cf_2 = new ConnectableFrame(ctm, "Frame #2"); cf_2.setSize(320,240);
     * cf_2.getContentPane().add(new JButton("Frame #2")); cf_2.setVisible(true);  ConnectableFrame cf_3 = new
     * ConnectableFrame(ctm, "Frame #3"); cf_3.setSize(100,100); cf_3.getContentPane().add(new JButton("Frame #3"));
     * cf_3.setVisible(true);  ConnectableFrame cf_4 = new ConnectableFrame(ctm, "Frame #4"); cf_4.setSize(300,200);
     * cf_4.getContentPane().add(new JButton("Frame #4")); cf_4.setVisible(true);  final DefaultTreeModel dtm = new
     * DefaultTreeModel(ctm.getRootNode(), true); JTree jt = new JTree(dtm); jt.addTreeExpansionListener(new
     * TreeExpansionListener() {     public void treeCollapsed(TreeExpansionEvent event)     {}          public void
     * treeExpanded(TreeExpansionEvent event)     {         Object object = event.getPath().getLastPathComponent();
     * if(object instanceof ConnectionTreeNodeModel.ConnectableTreeNode)         {
     * ((ConnectionTreeNodeModel.ConnectableTreeNode)object).explore(); //dtm.nodeStructureChanged(ctm.getRootNode());
     * dtm.nodeStructureChanged((ConnectionTreeNodeModel.ConnectableTreeNode)object);             //dtm.reload(); } }
     *   });   JInternalFrame jif = new JInternalFrame("Tree View", true, true, true, true); jif.setSize(200,400);
     * jif.getContentPane().add(jt); jif.setVisible(true);  ConnectionDesktopPane cdp = new ConnectionDesktopPane(ctm);
     * cdp.add(cf_1, cdp.DEFAULT_LAYER, 0); cdp.add(cf_2, cdp.DEFAULT_LAYER, 0); cdp.add(cf_3, cdp.DEFAULT_LAYER, 0);
     * cdp.add(cf_4, cdp.DEFAULT_LAYER, 0); cdp.add(jif, cdp.PALETTE_LAYER); ConnectionLink l_1 = new
     * ConnectionLink(cf_1.getConnectable().createPoint(), cf_2.getConnectable().createPoint()); ConnectionLink l_2 =
     * new ConnectionLink(cf_3.getConnectable().createPoint(), cf_4.getConnectable().createPoint()); ConnectionLink l_3
     * = new ConnectionLink(cf_1.getConnectable().createPoint(), cf_4.getConnectable().createPoint()); ConnectionLink
     * l_4 = new ConnectionLink(cf_2.getConnectable().createPoint(), cf_4.getConnectable().createPoint());
     * ConnectionLink l_5 = new ConnectionLink(cf_1.getConnectable().createPoint(),
     * cf_3.getConnectable().createPoint()); ctm.addLink(l_1); ctm.addLink(l_2); ctm.addLink(l_3); ctm.addLink(l_4);
     * ctm.addLink(l_5);  //JInternalFrame jif = new JInternalFrame("Blah"); //jif.getContentPane().add(new
     * JButton("Blah!")); //cdp.add(jif ); //jif.setVisible(true); //jif.setSize(200,300);  JFrame jf = new
     * JFrame("ConnectorPane Test Frame"); jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE); jf.setSize(640, 480);
     * jf.setLocationRelativeTo(null); jf.getContentPane().setLayout(new BorderLayout()); jf.getContentPane().add(cdp,
     * BorderLayout.CENTER);
     * dtm.nodeStructureChanged(ctm.getRootNode()); jf.setVisible(true);      } */
}
