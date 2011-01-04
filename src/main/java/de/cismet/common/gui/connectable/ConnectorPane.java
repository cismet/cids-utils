/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ConnectorPane.java
 *
 * Created on 11. August 2003, 15:51
 */
package de.cismet.common.gui.connectable;

import java.awt.*;
import java.awt.geom.*;

import java.beans.*;

import java.util.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ConnectorPane extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    protected ConnectionModel model;

    protected final GeneralPath connectorPath;
    protected final LinkedList connectorList;
    protected Stroke stroke;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConnectorPane object.
     */
    public ConnectorPane() {
        this(new DefaultConnectionModel());
    }

    /**
     * Creates a new instance of ConnectorPane.
     *
     * @param  model  DOCUMENT ME!
     */
    public ConnectorPane(final ConnectionModel model) {
        this.connectorList = new LinkedList();
        this.connectorPath = new GeneralPath();

        this.stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);

        this.setOpaque(false);
        this.setDoubleBuffered(true);
        this.setModel(model);

        this.getModel().addPropertyChangeListener(new ConnectorPane.ConnectionLineListener());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  model  DOCUMENT ME!
     */
    public void setModel(final ConnectionModel model) {
        this.model = model;
        this.connectorList.clear();

        if (this.getModel().getLinks().size() > 0) {
            final Iterator iterator = this.getModel().getLinks().iterator();
            while (iterator.hasNext()) {
                this.addConnectionLine((ConnectionLink)iterator.next());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ConnectionModel getModel() {
        return this.model;
    }

    /**
     * .........................................................................
     *
     * @param  link  DOCUMENT ME!
     */
    protected void addConnectionLine(final ConnectionLink link) {
        this.connectorList.add(new DefaultConnectionLine(link));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  link  DOCUMENT ME!
     */
    protected void removeConnectionLine(final ConnectionLink link) {
        final Iterator iterator = this.connectorList.iterator();
        while (iterator.hasNext()) {
            final ConnectionLine line = (ConnectionLine)iterator.next();
            if (line.getId().equals(link.getId())) {
                iterator.remove();
                return;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void updatePath() {
        this.connectorPath.reset();

        final Iterator iterator = connectorList.iterator();
        while (iterator.hasNext()) {
            final ConnectionLine line = (ConnectionLine)iterator.next();
            if (line.isDisplayable()) {
                if (line.isChanged()) {
                    line.redraw();
                    // System.out.println("redraw line: " + line);
                }

                connectorPath.append(line, false);
            }
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        // g.drawLine(50, 50, 500, 500);
        this.updatePath();
        ((Graphics2D)g).setStroke(stroke);
        ((Graphics2D)g).draw(this.connectorPath);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class ConnectionLineListener implements PropertyChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("addLink") && (evt.getNewValue() != null)) {
                addConnectionLine((ConnectionLink)evt.getNewValue());
                repaint();
            } else if (evt.getPropertyName().equals("removeLink") && (evt.getOldValue() != null)) {
                removeConnectionLine((ConnectionLink)evt.getOldValue());
                repaint();
            }
        }
    }

    // #########################################################################
}
