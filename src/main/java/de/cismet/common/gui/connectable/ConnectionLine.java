/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ConnectionLine.java
 *
 * Created on 6. August 2003, 12:37
 */
package de.cismet.common.gui.connectable;

import java.awt.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface ConnectionLine extends Shape {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isChanged();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isDisplayable();

    /**
     * DOCUMENT ME!
     */
    void redraw();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getId();
}
