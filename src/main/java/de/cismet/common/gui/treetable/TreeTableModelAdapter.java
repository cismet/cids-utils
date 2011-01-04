/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.common.gui.treetable;

/*
 * Copyright 1997-1999 Sun Microsystems, Inc. All Rights Reserved.
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

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

/**
 * This is a wrapper class takes a TreeTableModel and implements the table model interface. The implementation is
 * trivial, with all of the event dispatching support provided by the superclass: the AbstractTableModel.
 *
 * @author   Philip Milne
 * @author   Scott Violet
 * @version  1.2 10/27/98
 */
public class TreeTableModelAdapter extends AbstractTableModel {

    //~ Instance fields --------------------------------------------------------

    JTree tree;
    TreeTableModel treeTableModel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TreeTableModelAdapter object.
     *
     * @param  treeTableModel  DOCUMENT ME!
     * @param  tree            DOCUMENT ME!
     */
    public TreeTableModelAdapter(final TreeTableModel treeTableModel, final JTree tree) {
        this.tree = tree;
        this.treeTableModel = treeTableModel;

        tree.addTreeExpansionListener(new TreeExpansionListener() {

                // Don't use fireTableRowsInserted() here; the selection model
                // would get updated twice.
                @Override
                public void treeExpanded(final TreeExpansionEvent event) {
                    fireTableDataChanged();
                }
                @Override
                public void treeCollapsed(final TreeExpansionEvent event) {
                    fireTableDataChanged();
                }
            });

        // Installs a TreeModelListener that can update the table when
        // the tree changes. We use delayedFireTableDataChanged as we can
        // not be guaranteed the tree will have finished processing
        // the event before us.
        treeTableModel.addTreeModelListener(new TreeModelListener() {

                @Override
                public void treeNodesChanged(final TreeModelEvent e) {
                    delayedFireTableDataChanged();
                }

                @Override
                public void treeNodesInserted(final TreeModelEvent e) {
                    delayedFireTableDataChanged();
                }

                @Override
                public void treeNodesRemoved(final TreeModelEvent e) {
                    delayedFireTableDataChanged();
                }

                @Override
                public void treeStructureChanged(final TreeModelEvent e) {
                    delayedFireTableDataChanged();
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    // Wrappers, implementing TableModel interface.

    @Override
    public int getColumnCount() {
        return treeTableModel.getColumnCount();
    }

    @Override
    public String getColumnName(final int column) {
        return treeTableModel.getColumnName(column);
    }

    @Override
    public Class getColumnClass(final int column) {
        return treeTableModel.getColumnClass(column);
    }

    @Override
    public int getRowCount() {
        return tree.getRowCount();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   row  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected Object nodeForRow(final int row) {
        final TreePath treePath = tree.getPathForRow(row);
        return treePath.getLastPathComponent();
    }

    @Override
    public Object getValueAt(final int row, final int column) {
        return treeTableModel.getValueAt(nodeForRow(row), column);
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return treeTableModel.isCellEditable(nodeForRow(row), column);
    }

    @Override
    public void setValueAt(final Object value, final int row, final int column) {
        treeTableModel.setValueAt(value, nodeForRow(row), column);
    }

    /**
     * Invokes fireTableDataChanged after all the pending events have been processed. SwingUtilities.invokeLater is used
     * to handle this.
     */
    protected void delayedFireTableDataChanged() {
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    fireTableDataChanged();
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public TreeTableModel getTreeTableModel() {
        return this.treeTableModel;
    }
}
