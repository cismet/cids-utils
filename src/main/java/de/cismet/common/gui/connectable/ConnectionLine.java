/*
 * ConnectionLine.java
 *
 * Created on 6. August 2003, 12:37
 */



package de.cismet.common.gui.connectable;

import java.awt.*;

/**
 *
 * @author  pascal
 */
public interface ConnectionLine extends Shape
{
    public boolean isChanged();
    
    public boolean isDisplayable();
    
    public void redraw();
    
    public String getId();
}
