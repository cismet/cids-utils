/*
 * ConnectorPane.java
 *
 * Created on 11. August 2003, 15:51
 */

package de.cismet.common.gui.connectable;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.beans.*;


/**
 *
 * @author  pascal
 */
public class ConnectorPane extends javax.swing.JPanel
{
    protected ConnectionModel model;
    
    protected final GeneralPath connectorPath;
    protected final LinkedList connectorList;
    protected Stroke stroke;
    
    public ConnectorPane()
    {
        this(new DefaultConnectionModel());
    }

    /** Creates a new instance of ConnectorPane */
    public ConnectorPane(ConnectionModel model)
    {
        this.connectorList = new LinkedList();
        this.connectorPath = new GeneralPath();
        
        this.stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        
        this.setOpaque(false);
        this.setDoubleBuffered(true);
        this.setModel(model);
        
        this.getModel().addPropertyChangeListener(new ConnectorPane.ConnectionLineListener());
    }
    
    public void setModel(ConnectionModel model)
    {
        this.model = model;
        this.connectorList.clear();
        
        if(this.getModel().getLinks().size() > 0)
        {
            Iterator iterator = this.getModel().getLinks().iterator();
            while(iterator.hasNext())
            {
                this.addConnectionLine((ConnectionLink)iterator.next());
            }
        } 
    }
    
    public ConnectionModel getModel()
    {
        return this.model;
    }
    
    // .........................................................................
    
    protected void addConnectionLine(ConnectionLink link)
    { 
        this.connectorList.add(new DefaultConnectionLine(link));
    }
    
    protected void removeConnectionLine(ConnectionLink link)
    {
        Iterator iterator = this.connectorList.iterator();
        while(iterator.hasNext())
        {
            ConnectionLine line = (ConnectionLine)iterator.next();
            if(line.getId().equals(link.getId()))
            {
                iterator.remove();
                return;
            }
        }
    }

    protected void updatePath()
    {
        this.connectorPath.reset();
        
        Iterator iterator = connectorList.iterator();
        while(iterator.hasNext())
        {
            ConnectionLine line = (ConnectionLine)iterator.next();
            if(line.isDisplayable())
            {
                if(line.isChanged())
                {
                    line.redraw();
                    //System.out.println("redraw line: " + line);
                }

                connectorPath.append(line, false);
            }
        }
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //g.drawLine(50, 50, 500, 500);
        this.updatePath();
        ((Graphics2D)g).setStroke(stroke);
        ((Graphics2D)g).draw(this.connectorPath);
    }
    
    protected class ConnectionLineListener implements PropertyChangeListener
    {
        public void propertyChange(PropertyChangeEvent evt)
        {
            if(evt.getPropertyName().equals("addLink") && evt.getNewValue() != null)
            {
                addConnectionLine((ConnectionLink)evt.getNewValue());
                repaint();
            }
            else if(evt.getPropertyName().equals("removeLink") && evt.getOldValue() != null)
            {
                removeConnectionLine((ConnectionLink)evt.getOldValue());
                repaint();
            }
        }   
    }
    
    // #########################################################################
}

