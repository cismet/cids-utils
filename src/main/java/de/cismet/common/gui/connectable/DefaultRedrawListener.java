/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultRedrawListener.java
 *
 * Created on 11. August 2003, 17:30
 */
package de.cismet.common.gui.connectable;

import java.awt.event.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class DefaultRedrawListener implements ComponentListener {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of DefaultRedrawListener.
     */
    public DefaultRedrawListener() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void componentHidden(final ComponentEvent e) {
    }

    @Override
    public void componentMoved(final ComponentEvent e) {
        System.out.println("Component moved to: " + e.getComponent().getY() + "/" + e.getComponent().getX());
    }

    @Override
    public void componentResized(final ComponentEvent e) {
    }

    @Override
    public void componentShown(final ComponentEvent e) {
    }
}
