/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * GradientJPanel.java
 *
 * Created on 6. November 2003, 11:14
 */
package de.cismet.cids.tools.gui.farnsworth;
import java.awt.*;

import javax.swing.*;
/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class GradientJPanel extends JPanel {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of GradientJPanel.
     */
    public GradientJPanel() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isOpaque() {
        return true;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Color left = this.getForeground();
        final Color right = this.getBackground();

        final int w = getWidth();
        final int h = getHeight();
        final Graphics2D g2d = (Graphics2D)(g);
        g2d.setPaint(new GradientPaint(0, 0, left, w, h, right, true));
        g2d.fill(new Rectangle(0, 0, w, h));
    }
}
