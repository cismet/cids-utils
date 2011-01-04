/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * LazyGlassPane.java
 *
 * Created on 11. September 2003, 16:49
 */
package de.cismet.common.gui.misc;

//package Sirius.Navigator.CustomWidgets;

/*******************************************************************************

        Copyright (c)   :       EIG (Environmental Informatics Group)
                                                http://www.htw-saarland.de/eig
                                                Prof. Dr. Reiner Guettler
                                                Prof. Dr. Ralf Denzer

                                                HTWdS
                                                Hochschule fuer Technik und Wirtschaft des Saarlandes
                                                Goebenstr. 40
                                                66117 Saarbruecken
                                                Germany

        Programmers             :       Pascal

        Project                 :       WuNDA 2
        Filename                :
        Version                 :       1.0
        Purpose                 :
        Created                 :       01.10.1999
        History                 :

*******************************************************************************/
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * LazyGlassPane ist ein GlassPane, das nicht auf Benutzereingaben reagiert. Es werden "dummy-Listener" hinzugefuegt,
 * die keine Events verarbeiten.
 *
 * @version  $Revision$, $Date$
 * @see      LazyPanel
 */

class LazyGlassPane extends JComponent {

    //~ Instance fields --------------------------------------------------------

    private boolean blockEvents = false;
    private final Color overlayColor; // = new Color(100, 0, 0, 75);

    //~ Constructors -----------------------------------------------------------

    /**
     * Dieser Konstruktor erzeugt ein neues LazyGlassPane.
     */
    public LazyGlassPane() {
        super();

        this.addMouseListener(new LazyMouseListener());
        this.addKeyListener(new LazyKeyListener());

        this.overlayColor = null;
    }

    /**
     * Creates a new LazyGlassPane object.
     *
     * @param  overlayColor  DOCUMENT ME!
     */
    public LazyGlassPane(final Color overlayColor) {
        super();

        this.addMouseListener(new LazyMouseListener());
        this.addKeyListener(new LazyKeyListener());

        this.overlayColor = overlayColor;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  blockEvents  DOCUMENT ME!
     */
    public void blockEvents(final boolean blockEvents) {
        // System.out.println("[LazyGlassPane] blockEvents: " + blockEvents + " (" + this.blockEvents + ")");

        if (blockEvents && !this.blockEvents) {
            // System.out.println("[LazyGlassPane] blockEvents: " + blockEvents);

            this.blockEvents = blockEvents;
            this.setFocusTraversalKeysEnabled(!blockEvents);

            this.setVisible(blockEvents);
            this.requestFocus();
        } else if (!blockEvents && this.blockEvents) {
            // System.out.println("[LazyGlassPane] blockEvents: " + blockEvents);

            this.blockEvents = blockEvents;
            this.setFocusTraversalKeysEnabled(!blockEvents);

            this.setVisible(blockEvents);
        }
    }

    @Override
    public boolean isFocusable() {
        return this.blockEvents;
    }

    @Override
    public void paint(final Graphics g) {
        if (overlayColor != null) {
            g.setColor(overlayColor);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * does not work: public void setVisible(boolean visible) { System.out.println("[LazyGlassPane] setVisible " +
     * visible); this.blockEvents(visible); }
     *
     * @version  $Revision$, $Date$
     */
    class LazyMouseListener implements MouseListener, MouseMotionListener {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyMouseListener object.
         */
        public LazyMouseListener() {
            super();
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void mouseClicked(final MouseEvent e) {
            e.consume();
        }
        @Override
        public void mouseDragged(final MouseEvent e) {
            e.consume();
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
            e.consume();
        }

        @Override
        public void mouseExited(final MouseEvent e) {
            e.consume();
        }

        @Override
        public void mousePressed(final MouseEvent e) {
            // LazyGlassPane.this.requestFocus();
            // LazyGlassPane.this.blockEvents(false);
            e.consume();
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            e.consume();
        }

        @Override
        public void mouseMoved(final MouseEvent e) {
            e.consume();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class LazyKeyListener implements KeyListener {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyKeyListener object.
         */
        public LazyKeyListener() {
            super();
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void keyPressed(final KeyEvent e) {
            // System.out.println("LazyKeyListener KeyChar: " + e.getKeyChar());
            // System.out.println("LazyKeyListener KeyCode: " + e.getKeyCode());
            e.consume();
        }

        @Override
        public void keyReleased(final KeyEvent e) {
            // System.out.println("LazyKeyListener: " + e.getKeyChar());
            e.consume();
        }

        @Override
        public void keyTyped(final KeyEvent e) {
            // System.out.println("LazyKeyListener: " + e.getKeyChar());
            e.consume();
        }
    }

    /*
     * class FocusThread extends Thread { public void run() {     LazyGlassPane.this.requestFocus();
     * //System.out.println("[FocusThread] start");     SwingUtilities.invokeLater(new Runnable()         {
     *    public void run()                 {                         //System.out.println("[invokeLater] start");
     *       LazyGlassPane.this.requestFocus();             //System.out.println("[invokeLater] stop");
     * }         });     //System.out.println("[FocusThread] stop"); }}*/
}
