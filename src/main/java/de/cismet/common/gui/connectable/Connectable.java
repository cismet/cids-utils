/*
 * Connectable.java
 *
 * Created on 4. August 2003, 16:09
 */

package de.cismet.common.gui.connectable;

import java.util.List;
import java.util.Collection;

/**
 * Interface of (visually) connectable objects.
 * 
 * @author  Pascal
 */
public interface Connectable
{
    /**
     * Adds new link to the table of links.
     *
     * @param link the link object to be added
     */
    public boolean addLink(ConnectionLink link);
    
    /**
     * Removes a link from the table of links.<p>
     * This method should only be called by the <code>destroy</code> method of
     * the ConnectionLink interface!<br>
     * This method should also remove the anchor associated with the link.
     *
     * @param link the link object to be added
     * @return true if the was added successfully
     */
    public boolean removeLink(String id);
    
    /**
     * Retrieves a link from to the table of links.
     *
     * @param id the id of the link object to be removed
     * @return true if the was removed successfully
     */
    public ConnectionLink getLink(String id);
    
    // .........................................................................
    
    /**
     * Returns the number of links of this connectable.
     *
     * @return numer of links
     */
    public int getLinkCount();
    
     /**
     * Returns the number of links where this connectable is the target of the 
     * link. <p>
     * Same as getParentCount
     *
     * @return numer of links
     */
    public int getSourceLinkCount();
    
     /**
     * Returns the number of links where this connectable is the source of the 
     * link.<p>
     * ame as getChildCount
     *
     * @return numer of links
     */
    public int getTargetLinkCount();
    
    // Collections
    // .........................................................................
        
    /*
     * Returns all links of this connectable 
     *
     * @return a Collection of ConnectionLink objects
     */
    public Collection getLinks();
    
    /*
     * Returns all links, where this connectable is the target of the link
     * Same as getParents.
     *
     * @return a Collection of ConnectionLink objects
     */
    public Collection getSourceLinks();
    
    /*
     * Returns all links, where this connectable is the source of the link<p>
     * Same as getCildren.
     *
     * @return a Collection of ConnectionLink objects
     */
    public Collection getTargetLinks();
    
    /*
     * Returns all connectables connected to this connectable.
     *
     * @return a Collection of Connectable objects
     */
    public List getConnectables();
    
    /*
     * Returns all connectables, that are source of a link of this connectable.<p>
     * Same as getParents.
     *
     * @return a Collection of Connectable objects
     */
    public List getSourceConnectables();
    
    /*
     * Returns all connectables, that are target of a link of this connectable.
     * Same as getChildren.
     *
     * @return a Collection of Connectable objects
     */
    public List getTargetConnectables();
    
    /**
     * Returns all points of this connectable.<p>
     *
     * @return an Iterator
     */
    public PointIterator getPoints();

    /**
     * Returns true, if this connectable is source of at least one link.<p>
     * Same as isParent or getAllowsChildren
     *
     * @return true or false
     */
    public boolean isSource();
    
    /**
     * Returns true, if this connectable is target of at least one link.<p>
     * Same as isChild or !isLeaf
     *
     * @return true or false
     */
    public boolean isTarget();
    
    /**
     * Returns the name of this connectable
     *
     * @return the name of this connectable
     */
    public String getName();
    
    
    
    // Factory Methods
    // .........................................................................
    
    /**
     * This factory method creates a new ConnectionAnchor object.
     *
     * @return a new anchor of this connectable
     */
    public ConnectionPoint createPoint();
    
    /**
     *
     *
     *
     */
    interface PointIterator
    {
        /**
         * Returns true if the iteration has more elements
         *
         * @return true or false
         */
        public boolean hasNext();
        
        /**
         * Returns an 2-dimensional array of points<p>.
         * The first point in the array is the local point (the point that belongs
         * to this connectable) the second point is the remote point of a link.
         *
         * @return an array of ConnectionPoint Objects
         */
        public ConnectionPoint[] nextPoints();
    }
}
