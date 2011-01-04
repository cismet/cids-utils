/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ConnectableFrame.java
 *
 * Created on 11. August 2003, 17:47
 */
package de.cismet.common.gui.connectable;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ConnectableFrame extends JInternalFrame {

    //~ Instance fields --------------------------------------------------------

    protected final ConnectionModel model;
    protected final Connectable connectable;
    protected final Object userObject;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConnectableFrame object.
     *
     * @param  model       DOCUMENT ME!
     * @param  userObject  DOCUMENT ME!
     */
    public ConnectableFrame(final ConnectionModel model, final Object userObject) {
        this(model, userObject, false, false, false, false);
    }

    /**
     * Creates a new instance of ConnectableFrame.
     *
     * @param  model        DOCUMENT ME!
     * @param  userObject   DOCUMENT ME!
     * @param  resizable    DOCUMENT ME!
     * @param  closable     DOCUMENT ME!
     * @param  maximizable  DOCUMENT ME!
     * @param  iconifiable  DOCUMENT ME!
     */
    public ConnectableFrame(final ConnectionModel model,
            final Object userObject,
            final boolean resizable,
            final boolean closable,
            final boolean maximizable,
            final boolean iconifiable) {
        super(userObject.toString(), resizable, closable, maximizable, iconifiable);
        this.model = model;
        this.userObject = userObject;
        this.connectable = new DefaultConnectable(userObject.toString());

        this.model.addConnectable(this.connectable);
        this.addComponentListener(new DefaultComponentListener());
        this.addInternalFrameListener(new DefaultInternalFrameListener());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Connectable getConnectable() {
        return this.connectable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getUserObject() {
        return this.userObject;
    }

    @Override
    public String getName() {
        return this.getUserObject().toString();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * DOCUMENT ME!
     */
    protected void calculatePoints() {
        final Iterator iterator = connectable.getLinks().iterator();
        while (iterator.hasNext()) {
            final ConnectionLink link = (ConnectionLink)iterator.next();

            if (link.isSource(connectable)) {
                // link.getSource().getAnchorPoint().setLocation(ConnectableFrame.this.getLocation());
                link.getSource().getAnchorPoint().y = ConnectableFrame.this.getY() + ConnectableFrame.this.getHeight()
                            - 2;
                link.getSource().getAnchorPoint().x = ConnectableFrame.this.getX() + ConnectableFrame.this.getWidth()
                            - 2;
            } else if (link.isTarget(connectable)) {
                link.getTarget().getAnchorPoint().y = ConnectableFrame.this.getY() + 2;
                link.getTarget().getAnchorPoint().x = ConnectableFrame.this.getX() + 2;
            }
        }

        /*Connectable.PointIterator iterator = connectable.getPoints();
         * while(iterator.hasNext()) {
         * iterator.nextPoints()[0].getAnchorPoint().setLocation(ConnectableFrame.this.getLocation());}*/

        // System.out.println("Component moved to: " + e.getComponent().getY()+"/"+e.getComponent().getX());
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * .........................................................................
     *
     * @version  $Revision$, $Date$
     */
    protected class DefaultInternalFrameListener extends InternalFrameAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void internalFrameDeiconified(final InternalFrameEvent e) {
            final Connectable.PointIterator iterator = connectable.getPoints();
            while (iterator.hasNext()) {
                iterator.nextPoints()[0].setDisplayable(true);
            }

            calculatePoints();
            ConnectableFrame.this.getDesktopPane().repaint(250);

            /*Iterator iterator = connectable.getLinks().iterator();
             * while(iterator.hasNext()) { ((ConnectionLink)iterator.next()).setDisplayable(true);}*/

        }

        @Override
        public void internalFrameIconified(final InternalFrameEvent e) {
            final Connectable.PointIterator iterator = connectable.getPoints();
            while (iterator.hasNext()) {
                iterator.nextPoints()[0].setDisplayable(false);
            }

            ConnectableFrame.this.getDesktopPane().repaint(250);

            /*Iterator iterator = connectable.getLinks().iterator();
             * while(iterator.hasNext()) { ((ConnectionLink)iterator.next()).get setDisplayable(false);}*/
        }

        @Override
        public void internalFrameOpened(final InternalFrameEvent e) {
            final Connectable.PointIterator iterator = connectable.getPoints();
            while (iterator.hasNext()) {
                iterator.nextPoints()[0].setDisplayable(true);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class DefaultComponentListener extends ComponentAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void componentResized(final ComponentEvent e) {
            if (ConnectableFrame.this.isShowing()) {
                calculatePoints();
                ConnectableFrame.this.getDesktopPane().repaint(250);
            }
        }

        @Override
        public void componentMoved(final ComponentEvent e) {
            if (ConnectableFrame.this.isShowing()) {
                calculatePoints();
                ConnectableFrame.this.getDesktopPane().repaint(250);
            }
        }
    }

    /**
     * .........................................................................
     *
     * @version  $Revision$, $Date$
     */
    private class DefaultConnectable extends AbstractConnectable {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DefaultConnectable object.
         *
         * @param  name  DOCUMENT ME!
         */
        public DefaultConnectable(final String name) {
            super(name);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public de.cismet.common.gui.connectable.ConnectionPoint createPoint() {
            return new ConnectionPoint(
                    this,
                    null,
                    new Point(ConnectableFrame.this.getY(), ConnectableFrame.this.getX()));
        }
    }
}
