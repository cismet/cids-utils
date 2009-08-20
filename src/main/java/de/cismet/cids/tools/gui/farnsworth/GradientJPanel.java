/*
 * GradientJPanel.java
 *
 * Created on 6. November 2003, 11:14
 */

package de.cismet.cids.tools.gui.farnsworth;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author  hell
 */
public class GradientJPanel extends JPanel{
    
    /** Creates a new instance of GradientJPanel */
    public GradientJPanel() {
        super();
    }
    public boolean isOpaque() {
        return true;
    }
    
    protected void paintComponent(Graphics g) {
        Color left=this.getForeground();
        Color right=this.getBackground();
        
        int w=getWidth();
        int h=getHeight();
        Graphics2D g2d=(Graphics2D)(g);
        g2d.setPaint(new GradientPaint(0,0,left,w,h,right,true));
        g2d.fill(new Rectangle(0,0,w,h));
    }

    
}
