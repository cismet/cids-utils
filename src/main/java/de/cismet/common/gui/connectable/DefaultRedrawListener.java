/*
 * DefaultRedrawListener.java
 *
 * Created on 11. August 2003, 17:30
 */

package de.cismet.common.gui.connectable;

import java.awt.event.*;

/**
 *
 * @author  pascal
 */
public class DefaultRedrawListener implements ComponentListener
{
    
    /** Creates a new instance of DefaultRedrawListener */
    public DefaultRedrawListener()
    {
    }
    
    public void componentHidden(ComponentEvent e)
    {
        
    }
    
    public void componentMoved(ComponentEvent e)
    {
        System.out.println("Component moved to: " + e.getComponent().getY()+"/"+e.getComponent().getX());
    }
    
    public void componentResized(ComponentEvent e)
    {
        
    }
    
    public void componentShown(ComponentEvent e)
    {
    }
    
}
