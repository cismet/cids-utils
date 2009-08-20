/*
 * DefaultConnectionLine.java
 *
 * Created on 11. August 2003, 16:07
 */

package de.cismet.common.gui.connectable;

import java.awt.*;
import java.awt.geom.*;


/**
 *
 * @author  pascal
 */
public class DefaultConnectionLine extends Line2D.Float implements ConnectionLine
{
    protected final ConnectionLink link;
    protected final Point startPoint;
    protected final Point endPoint;
    
    
    public DefaultConnectionLine(ConnectionLink link)
    {
        super(link.getSource().getAnchorPoint(), link.getTarget().getAnchorPoint());
        
        this.link = link;
        this.startPoint = link.getSource().getAnchorPoint();
        this.endPoint = link.getTarget().getAnchorPoint();

        //System.out.println(link.getSource().getAnchorPoint().y + "/" + link.getSource().getAnchorPoint().x);
        //System.out.println(this.y1 + "/" + this.x1 + " , " + this.y2 + "/" + this.x2); 
    }
    
    public boolean isChanged()
    {
        if(this.getP1().equals(link.getSource().getAnchorPoint()) && this.getP2().equals(link.getTarget().getAnchorPoint()))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public void redraw()
    {
        //System.out.println("redraw connector '" + this.getId() + "': " + this.y1 + "/" + this.x1 + " , " + this.y2 + "/" + this.x2); 
        this.setLine(link.getSource().getAnchorPoint(), link.getTarget().getAnchorPoint());
    } 
    
    public String getId()
    {
        return this.link.getId();
    }
    
    public boolean isDisplayable()
    {
        return this.link.isDisplayable();
    }
    
}
