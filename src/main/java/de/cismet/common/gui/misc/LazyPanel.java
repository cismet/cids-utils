/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * LazyPanel.java
 *
 * Created on 11. September 2003, 16:48
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
        Version                 :       2.0
        Purpose                 :
        Created                 :       01.10.1999
        History                 :   01.10.2002

*******************************************************************************/
import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class LazyPanel extends JPanel {

    //~ Instance fields --------------------------------------------------------

    private LazyGlassPane lazyGlassPane;
    private JRootPane rootPane;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LazyPanel object.
     */
    public LazyPanel() {
        super();
        lazyGlassPane = new LazyGlassPane();
        rootPane = new JRootPane();
        rootPane.setLayeredPane(new JLayeredPane());
        rootPane.setGlassPane(lazyGlassPane);
        this.add(rootPane);
        this.setLayout(new GridLayout(1, 1));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  content  DOCUMENT ME!
     */
    public void setContent(final JComponent content) {
        rootPane.setContentPane(content);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  blockEvents  DOCUMENT ME!
     */
    public synchronized void blockEvents(final boolean blockEvents) {
        // System.out.println("[LazyPanel] blockEvents: " + blockEvents + " | visible: " + this.isVisible());
        if (this.isVisible()) {
            // System.out.println("[LazyPanel] isEventDispatchThread: " + SwingUtilities.isEventDispatchThread());
            if (SwingUtilities.isEventDispatchThread()) {
                ((LazyGlassPane)rootPane.getGlassPane()).blockEvents(blockEvents);
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            // System.out.println("[invokeLater] start");
                            ((LazyGlassPane)rootPane.getGlassPane()).blockEvents(blockEvents);
                            // System.out.println("[invokeLater] stop");
                        }
                    });
            }
        }
    }

    /* public static void main (String args[])
     * {  JFrame testFrame = new JFrame("Lazy Frame");  final LazyPanel lazyPanel = new LazyPanel();
     * lazyPanel.setPreferredSize(new Dimension(320,240));    JPanel panel = new JPanel(new GridLayout(1,2));    JButton
     * lazyButton = new JButton("Lazy Button 1");  lazyButton.addActionListener(new ActionListener()  {      public void
     * actionPerformed(ActionEvent e)      {          lazyPanel.blockEvents(true);      }  });    panel.add(lazyButton);
     *  panel.add(new JButton("Lazy Button 2"));  JPanel content = new JPanel(new BorderLayout());
     * content.add(lazyPanel, BorderLayout.CENTER);  content.add(new JButton("Button"),  BorderLayout.SOUTH);
     * content.add(new JButton("Button"),  BorderLayout.NORTH);  lazyPanel.setContent(panel);
     * testFrame.setContentPane(content);    //testFrame.setPreferredSize(320,240);  testFrame.pack();
     * testFrame.setVisible(true);            // lazyPanel.blockEvents(true); }*/
}
