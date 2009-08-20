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
 * @author  Pascal
 */
public class ConnectionPoint
{
    private final Connectable connectable;
    private final Object anchorObject;
    private final Point anchorPoint;
   
    /** Holds value of property displayable. */
    private boolean displayable;    
    
    
    public ConnectionPoint(Connectable connectable, Object anchorObject, Point anchorPoint)
    {
        this.connectable = connectable;
        this.anchorObject = anchorObject;
        this.anchorPoint = anchorPoint;
    }
    
    /**
     * Returns the connectable associated with this anchor
     *
     * @return a Connectable object
     */
    public Connectable getConnectable()
    {
        return this.connectable;
    }
    
    /**
     * Returns the  object associated with this anchor
     *
     * @return a Connectable object
     */
    public Object getAnchorObject()
    {
        return this.anchorObject;
    }
    
    /**
     * Returns the point associated with this anchor<p>
     * This point ist start or end of a connection line
     *
     * @return a Point object
     */
    public Point getAnchorPoint()
    {
        return this.anchorPoint;
    } 
    
    /** Getter for property dispayable.
     * @return Value of property dispayable.
     *
     */
    public boolean isDisplayable()
    {
        return this.displayable;
    }
    
    /** Setter for property dispayable.
     * @param dispayable New value of property dispayable.
     *
     */
    public void setDisplayable(boolean displayable)
    {
        this.displayable = displayable;
    }
    
}
