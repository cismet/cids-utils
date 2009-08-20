/*
 * ConnectableFrame.java
 *
 * Created on 11. August 2003, 17:47
 */

package de.cismet.common.gui.connectable;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

/**
 *
 * @author  pascal
 */
public class ConnectableFrame extends JInternalFrame
{
    protected final ConnectionModel model;
    protected final Connectable connectable;
    protected final Object userObject;
    
    
    public ConnectableFrame(ConnectionModel model, Object userObject)
    {
        this(model, userObject, false, false, false, false);
    }
    
    /** Creates a new instance of ConnectableFrame */
    public ConnectableFrame(ConnectionModel model, Object userObject, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable)
    {
        super(userObject.toString(), resizable, closable, maximizable, iconifiable);
        this.model = model;
        this.userObject = userObject;
        this.connectable = new DefaultConnectable(userObject.toString());
        
        this.model.addConnectable(this.connectable);
        this.addComponentListener(new DefaultComponentListener());
        this.addInternalFrameListener(new DefaultInternalFrameListener());
    }
    
    public Connectable getConnectable()
    {
        return this.connectable;
    }
    
    public Object getUserObject()
    {
        return this.userObject;
    }
    
    public String getName()
    {
        return this.getUserObject().toString();
    }
    
    public String toString()
    {
        return this.getName();
    }
    
    protected void calculatePoints()
    {
        
        Iterator iterator = connectable.getLinks().iterator();
        while(iterator.hasNext())
        {
            ConnectionLink link = (ConnectionLink)iterator.next();
            
            if(link.isSource(connectable))
            {
                //link.getSource().getAnchorPoint().setLocation(ConnectableFrame.this.getLocation());
                link.getSource().getAnchorPoint().y = ConnectableFrame.this.getY() + ConnectableFrame.this.getHeight() - 2;
                link.getSource().getAnchorPoint().x = ConnectableFrame.this.getX() + ConnectableFrame.this.getWidth() - 2;
                
            }
            else if(link.isTarget(connectable))
            {
                link.getTarget().getAnchorPoint().y = ConnectableFrame.this.getY() + 2;
                link.getTarget().getAnchorPoint().x = ConnectableFrame.this.getX() + 2;
            }
        }
        
                /*Connectable.PointIterator iterator = connectable.getPoints();
                while(iterator.hasNext())
                {
                    iterator.nextPoints()[0].getAnchorPoint().setLocation(ConnectableFrame.this.getLocation());
                }*/
        
        //System.out.println("Component moved to: " + e.getComponent().getY()+"/"+e.getComponent().getX());
        
    }
    
    // .........................................................................
    
    protected class DefaultInternalFrameListener extends InternalFrameAdapter
    {
        public void internalFrameDeiconified(InternalFrameEvent e)
        {
            Connectable.PointIterator iterator = connectable.getPoints();
            while(iterator.hasNext())
            {
                iterator.nextPoints()[0].setDisplayable(true);
            }
            
            calculatePoints();
            ConnectableFrame.this.getDesktopPane().repaint(250);
            
            
            /*Iterator iterator = connectable.getLinks().iterator();
            while(iterator.hasNext())
            {
                ((ConnectionLink)iterator.next()).setDisplayable(true);
            }*/
            
            
        }
        
        public void internalFrameIconified(InternalFrameEvent e)
        {
            Connectable.PointIterator iterator = connectable.getPoints();
            while(iterator.hasNext())
            {
                iterator.nextPoints()[0].setDisplayable(false);
            }
            
            ConnectableFrame.this.getDesktopPane().repaint(250);
            
            /*Iterator iterator = connectable.getLinks().iterator();
            while(iterator.hasNext())
            {
                ((ConnectionLink)iterator.next()).get setDisplayable(false);
            }*/
        }
        
        public void internalFrameOpened(InternalFrameEvent e)
        {
            Connectable.PointIterator iterator = connectable.getPoints();
            while(iterator.hasNext())
            {
                iterator.nextPoints()[0].setDisplayable(true);
            }
        }
    }
    
    protected class DefaultComponentListener extends ComponentAdapter
    {
        public void componentResized(ComponentEvent e)
        {
            if(ConnectableFrame.this.isShowing())
            {
                calculatePoints();
                ConnectableFrame.this.getDesktopPane().repaint(250);
            }
        }
        
        public void componentMoved(ComponentEvent e)
        {
            if(ConnectableFrame.this.isShowing())
            {
                calculatePoints();
                ConnectableFrame.this.getDesktopPane().repaint(250);
            }
        }
    }
    
    // .........................................................................
    
    private class DefaultConnectable extends AbstractConnectable
    {
        public DefaultConnectable(String name)
        {
            super(name);
        }
        
        public de.cismet.common.gui.connectable.ConnectionPoint createPoint()
        {
            return new ConnectionPoint(this, null, new Point(ConnectableFrame.this.getY(), ConnectableFrame.this.getX()));
        }
        
    }
    
}
