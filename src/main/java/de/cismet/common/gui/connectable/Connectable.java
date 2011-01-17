/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * Connectable.java
 *
 * Created on 4. August 2003, 16:09
 */
package de.cismet.common.gui.connectable;

import java.util.Collection;
import java.util.List;

/**
 * Interface of (visually) connectable objects.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public interface Connectable {

    //~ Methods ----------------------------------------------------------------

    /**
     * Adds new link to the table of links.
     *
     * @param   link  the link object to be added
     *
     * @return  DOCUMENT ME!
     */
    boolean addLink(ConnectionLink link);

    /**
     * Removes a link from the table of links.
     *
     * <p>This method should only be called by the <code>destroy</code> method of the ConnectionLink interface!<br>
     * This method should also remove the anchor associated with the link.</p>
     *
     * @param   id  link the link object to be added
     *
     * @return  true if the was added successfully
     */
    boolean removeLink(String id);

    /**
     * Retrieves a link from to the table of links.
     *
     * @param   id  the id of the link object to be removed
     *
     * @return  true if the was removed successfully
     */
    ConnectionLink getLink(String id);

    // .........................................................................

    /**
     * Returns the number of links of this connectable.
     *
     * @return  numer of links
     */
    int getLinkCount();

    /**
     * Returns the number of links where this connectable is the target of the link.
     *
     * <p>Same as getParentCount</p>
     *
     * @return  numer of links
     */
    int getSourceLinkCount();

    /**
     * Returns the number of links where this connectable is the source of the link.
     *
     * <p>ame as getChildCount</p>
     *
     * @return  numer of links
     */
    int getTargetLinkCount();
    /**
     * Collections ......................................................................... Returns all links of this
     * connectable
     *
     * @return  DOCUMENT ME!
     */
    Collection getLinks();
    /**
     * Returns all links, where this connectable is the target of the link Same as getParents.
     *
     * @return  DOCUMENT ME!
     */
    Collection getSourceLinks();
    /**
     * Returns all links, where this connectable is the source of the link.
     *
     * <p>Same as getCildren.</p>
     *
     * @return  DOCUMENT ME!
     */
    Collection getTargetLinks();
    /**
     * Returns all connectables connected to this connectable.
     *
     * @return  DOCUMENT ME!
     */
    List getConnectables();
    /**
     * Returns all connectables, that are source of a link of this connectable.
     *
     * <p>Same as getParents.</p>
     *
     * @return  DOCUMENT ME!
     */
    List getSourceConnectables();
    /**
     * Returns all connectables, that are target of a link of this connectable. Same as getChildren.
     *
     * @return  DOCUMENT ME!
     */
    List getTargetConnectables();

    /**
     * Returns all points of this connectable.
     *
     * @return  an Iterator
     */
    PointIterator getPoints();

    /**
     * Returns true, if this connectable is source of at least one link.
     *
     * <p>Same as isParent or getAllowsChildren</p>
     *
     * @return  true or false
     */
    boolean isSource();

    /**
     * Returns true, if this connectable is target of at least one link.
     *
     * <p>Same as isChild or !isLeaf</p>
     *
     * @return  true or false
     */
    boolean isTarget();

    /**
     * Returns the name of this connectable.
     *
     * @return  the name of this connectable
     */
    String getName();

    // Factory Methods
    // .........................................................................

    /**
     * This factory method creates a new ConnectionAnchor object.
     *
     * @return  a new anchor of this connectable
     */
    ConnectionPoint createPoint();

    //~ Inner Interfaces -------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    interface PointIterator {

        //~ Methods ------------------------------------------------------------

        /**
         * Returns true if the iteration has more elements.
         *
         * @return  true or false
         */
        boolean hasNext();

        /**
         * Returns an 2-dimensional array of points.
         *
         * <p>. The first point in the array is the local point (the point that belongs to this connectable) the second
         * point is the remote point of a link.</p>
         *
         * @return  an array of ConnectionPoint Objects
         */
        ConnectionPoint[] nextPoints();
    }
}
