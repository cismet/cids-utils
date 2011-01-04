/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultConnectionLine.java
 *
 * Created on 11. August 2003, 16:07
 */
package de.cismet.common.gui.connectable;

import java.awt.*;
import java.awt.geom.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class DefaultConnectionLine extends Line2D.Float implements ConnectionLine {

    //~ Instance fields --------------------------------------------------------

    protected final ConnectionLink link;
    protected final Point startPoint;
    protected final Point endPoint;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultConnectionLine object.
     *
     * @param  link  DOCUMENT ME!
     */
    public DefaultConnectionLine(final ConnectionLink link) {
        super(link.getSource().getAnchorPoint(), link.getTarget().getAnchorPoint());

        this.link = link;
        this.startPoint = link.getSource().getAnchorPoint();
        this.endPoint = link.getTarget().getAnchorPoint();

        // System.out.println(link.getSource().getAnchorPoint().y + "/" + link.getSource().getAnchorPoint().x);
        // System.out.println(this.y1 + "/" + this.x1 + " , " + this.y2 + "/" + this.x2);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isChanged() {
        if (this.getP1().equals(link.getSource().getAnchorPoint())
                    && this.getP2().equals(link.getTarget().getAnchorPoint())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void redraw() {
        // System.out.println("redraw connector '" + this.getId() + "': " + this.y1 + "/" + this.x1 + " , " + this.y2
        // + "/" + this.x2);
        this.setLine(link.getSource().getAnchorPoint(), link.getTarget().getAnchorPoint());
    }

    @Override
    public String getId() {
        return this.link.getId();
    }

    @Override
    public boolean isDisplayable() {
        return this.link.isDisplayable();
    }
}
