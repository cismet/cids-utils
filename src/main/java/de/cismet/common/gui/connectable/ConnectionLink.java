/*
 * ConnectorLink.java
 *
 * Created on 5. August 2003, 11:36
 */

package de.cismet.common.gui.connectable;

import java.awt.*;

/**
 *
 * @author  pascal
 */
public class ConnectionLink
{
    private static long time = System.currentTimeMillis();
    
    private final String id;
    
    /** Holds value of property source. */
    private final ConnectionPoint source;    
    
    /** Holds value of property target. */
    private final ConnectionPoint target;
    
    /** Holds value of property displayable. */
    private boolean displayable;
    
    /** Creates a new instance of ConnectorLink */
    public ConnectionLink(String id, ConnectionPoint source, ConnectionPoint target)
    {
        this.id = id;
        this.source = source;
        this.target = target;
    }
    
    /** Creates a new instance of ConnectorLink */
    public ConnectionLink(ConnectionPoint source, ConnectionPoint target)
    {
        this.id = Long.toHexString(++time);
        this.source = source;
        this.target = target;
        
        //this.link();
    }
    
    public boolean link()
    {
        return source.getConnectable().addLink(this) & target.getConnectable().addLink(this);
    }
    
    /**
     * Destroy this link<p>
     * Removes this link both from the source and the target connectable.
     */
    public boolean unlink()
    {
        return source.getConnectable().removeLink(this.getId()) & target.getConnectable().removeLink(this.getId());
    }
    
    // .........................................................................
    
    /**
     * Is the connectable the source of this link?
     */
    public boolean isSource(Connectable connectable)
    {
        return connectable == this.getSource().getConnectable() ? true : false;
    }
    
    /**
     * Is the connectable the target of this link?
     */
    public boolean isTarget(Connectable connectable)
    {
        return connectable == this.getTarget().getConnectable() ? true : false;
    }

    // get Methods
    // .........................................................................
    
    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public String getId()
    {
        return this.id;
    }
    
    /** Getter for property source.
     * @return Value of property source.
     *
     */
    public ConnectionPoint getSource()
    {
        return this.source;
    }
    
    /** Getter for property target.
     * @return Value of property target.
     *
     */
    public ConnectionPoint getTarget()
    {
        return this.target;
    }
    
    /** Getter for property displayable.
     * @return Value of property displayable.
     *
     */
    public boolean isDisplayable()
    {
        //return this.displayable;
        
        return this.getSource().isDisplayable() & this.getTarget().isDisplayable();
    }
    
    /** Setter for property displayable.
     * @param displayable New value of property displayable.
     *
     */
    /*public void setDisplayable(boolean displayable)
    {
        this.displayable = displayable;
    }*/
    
}
