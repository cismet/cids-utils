/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * Anchor.java
 *
 * Created on 5. August 2003, 16:38
 */
package de.cismet.common.gui.connectable;

import java.awt.Point;

/**
 * This interface represents a connection point.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public class ConnectionPoint {

    //~ Instance fields --------------------------------------------------------

    private final Connectable connectable;
    private final Object anchorObject;
    private final Point anchorPoint;

    /** Holds value of property displayable. */
    private boolean displayable;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConnectionPoint object.
     *
     * @param  connectable   DOCUMENT ME!
     * @param  anchorObject  DOCUMENT ME!
     * @param  anchorPoint   DOCUMENT ME!
     */
    public ConnectionPoint(final Connectable connectable, final Object anchorObject, final Point anchorPoint) {
        this.connectable = connectable;
        this.anchorObject = anchorObject;
        this.anchorPoint = anchorPoint;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns the connectable associated with this anchor.
     *
     * @return  a Connectable object
     */
    public Connectable getConnectable() {
        return this.connectable;
    }

    /**
     * Returns the object associated with this anchor.
     *
     * @return  a Connectable object
     */
    public Object getAnchorObject() {
        return this.anchorObject;
    }

    /**
     * Returns the point associated with this anchor.
     *
     * <p>This point ist start or end of a connection line</p>
     *
     * @return  a Point object
     */
    public Point getAnchorPoint() {
        return this.anchorPoint;
    }

    /**
     * Getter for property dispayable.
     *
     * @return  Value of property dispayable.
     */
    public boolean isDisplayable() {
        return this.displayable;
    }

    /**
     * Setter for property dispayable.
     *
     * @param  displayable  New value of property dispayable.
     */
    public void setDisplayable(final boolean displayable) {
        this.displayable = displayable;
    }
}
