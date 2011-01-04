/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.common.gui.treetable;

/*
 * Copyright 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.text.DateFormat;

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;

/**
 * Assembles the UI. The UI consists of a JTreeTable and a menu. The JTreeTable uses a BookmarksModel to visually
 * represent a bookmarks file stored in the Netscape file format.
 *
 * @author   Scott Violet
 * @version  $Revision$, $Date$
 */
public class TreeTableExample3 {

    //~ Static fields/initializers ---------------------------------------------

    /** Number of instances of TreeTableExample3. */
    private static int ttCount;

    //~ Instance fields --------------------------------------------------------

    /** Used to represent the model. */
    private JTreeTable treeTable;
    /** Frame containing everything. */
    private JFrame frame;
    /** Path created for. */
    private String path;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a TreeTableExample3, loading the bookmarks from the file at <code>path</code>.
     *
     * @param  path  DOCUMENT ME!
     */
    public TreeTableExample3(final String path) {
        this.path = path;
        ttCount++;

        frame = createFrame();

        final Container cPane = frame.getContentPane();
        final JMenuBar mb = createMenuBar();
        final TreeTableModel model = createModel(path);

        treeTable = createTreeTable(model);
        final JScrollPane sp = new JScrollPane(treeTable);
        sp.getViewport().setBackground(Color.white);
        cPane.add(sp);

        frame.setJMenuBar(mb);
        frame.pack();
        frame.show();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Creates and returns the instanceof JTreeTable that will be used.
     *
     * @param   model  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected JTreeTable createTreeTable(final TreeTableModel model) {
        final JTreeTable treeTable = new JTreeTable(model);

        treeTable.setDefaultRenderer(Date.class, new BookmarksDateRenderer());
        treeTable.setDefaultRenderer(Object.class,
            new BookmarksStringRenderer());
        return treeTable;
    }

    /**
     * Creates the BookmarksModel for the file at <code>path</code>.
     *
     * @param   path  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected TreeTableModel createModel(final String path) {
        final Bookmarks bookmarks = new Bookmarks(path);
        return new BookmarksModel(bookmarks.getRoot());
    }

    /**
     * Creates the JFrame that will contain everything.
     *
     * @return  DOCUMENT ME!
     */
    protected JFrame createFrame() {
        final JFrame retFrame = new JFrame("TreeTable III -- " + path);

        retFrame.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(final WindowEvent we) {
                    frame.dispose();
                    if (--ttCount == 0) {
                        System.exit(0);
                    }
                }
            });
        return retFrame;
    }

    /**
     * Creates a menu bar.
     *
     * @return  DOCUMENT ME!
     */
    protected JMenuBar createMenuBar() {
        final JMenu fileMenu = new JMenu("File");
        JMenuItem menuItem;

        menuItem = new JMenuItem("Open");
        menuItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent ae) {
                    final JFileChooser fc = new JFileChooser(path);
                    final int result = fc.showOpenDialog(frame);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        final String newPath = fc.getSelectedFile().getPath();

                        new TreeTableExample3(newPath);
                    }
                }
            });
        fileMenu.add(menuItem);
        fileMenu.addSeparator();

        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent ae) {
                    System.exit(0);
                }
            });
        fileMenu.add(menuItem);

        // Create a menu bar
        final JMenuBar menuBar = new JMenuBar();

        menuBar.add(fileMenu);

        // Menu for the look and feels (lafs).
        final UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        final ButtonGroup lafGroup = new ButtonGroup();

        final JMenu optionsMenu = new JMenu("Options");

        menuBar.add(optionsMenu);

        for (int i = 0; i < lafs.length; i++) {
            final JRadioButtonMenuItem rb = new JRadioButtonMenuItem(lafs[i].getName());
            optionsMenu.add(rb);
            rb.setSelected(UIManager.getLookAndFeel().getName().equals(lafs[i].getName()));
            rb.putClientProperty("UIKey", lafs[i]);
            rb.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(final ItemEvent ae) {
                        final JRadioButtonMenuItem rb2 = (JRadioButtonMenuItem)ae.getSource();
                        if (rb2.isSelected()) {
                            final UIManager.LookAndFeelInfo info = (UIManager.LookAndFeelInfo)rb2.getClientProperty(
                                    "UIKey");
                            try {
                                UIManager.setLookAndFeel(info.getClassName());
                                SwingUtilities.updateComponentTreeUI(frame);
                            } catch (Exception e) {
                                System.err.println("unable to set UI "
                                            + e.getMessage());
                            }
                        }
                    }
                });
            lafGroup.add(rb);
        }
        return menuBar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        if (args.length > 0) {
            // User is specifying the bookmark file to show.
            for (int counter = args.length - 1; counter >= 0; counter--) {
                new TreeTableExample3(args[counter]);
            }
        } else {
            // No file specified, see if the user has one in their home
            // directory.
            String path;

            try {
                path = System.getProperty("user.home");
                if (path != null) {
                    path += File.separator + ".netscape" + File.separator
                                + "bookmarks.html";
                    final File file = new File(path);
                    if (!file.exists()) {
                        path = null;
                    }
                }
            } catch (Throwable th) {
                path = null;
            }
            if (path == null) {
                // None available, use a default.
                path = "bookmarks.html";
            }
            new TreeTableExample3(path);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * The renderer used for Dates in the TreeTable. The only thing it does, is to format a null date as '---'.
     *
     * @version  $Revision$, $Date$
     */
    private static class BookmarksDateRenderer extends DefaultTableCellRenderer {

        //~ Instance fields ----------------------------------------------------

        DateFormat formatter;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new BookmarksDateRenderer object.
         */
        public BookmarksDateRenderer() {
            super();
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void setValue(final Object value) {
            if (formatter == null) {
                formatter = DateFormat.getDateInstance();
            }
            setText((value == null) ? "---" : formatter.format(value));
        }
    }

    /**
     * The renderer used for String in the TreeTable. The only thing it does, is to format a null String as '---'.
     *
     * @version  $Revision$, $Date$
     */
    static class BookmarksStringRenderer extends DefaultTableCellRenderer {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new BookmarksStringRenderer object.
         */
        public BookmarksStringRenderer() {
            super();
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void setValue(final Object value) {
            setText((value == null) ? "---" : value.toString());
        }
    }
}
