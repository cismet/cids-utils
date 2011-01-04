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

import java.lang.reflect.*;

import javax.swing.tree.*;

/**
 * An implementation of TreeTableModel that uses reflection to answer TableModel methods. This works off a handful of
 * values. A TreeNode is used to answer all the TreeModel related methods (similiar to AbstractTreeTableModel and
 * DefaultTreeModel). The column names are specified in the constructor. The values for the columns are dynamically
 * obtained via reflection, you simply provide the method names. The methods used to set a particular value are also
 * specified as an array of method names, a null method name, or null array indicates the column isn't editable. And the
 * class types, used for the TableModel method getColumnClass are specified in the constructor.
 *
 * @author   Scott Violet
 * @version  $Revision$, $Date$
 */
public class DynamicTreeTableModel extends AbstractTreeTableModel {

    //~ Instance fields --------------------------------------------------------

    /** Names of the columns, used for the TableModel getColumnName method. */
    private String[] columnNames;
    /** Method names used to determine a particular value. Used for the TableModel method getValueAt. */
    private String[] methodNames;
    /**
     * Setter method names, used to set a particular value. Used for the TableModel method setValueAt. A null entry, or
     * array, indicates the column is not editable.
     */
    private String[] setterMethodNames;
    /** Column classes, used for the TableModel method getColumnClass. */
    private Class[] cTypes;

    //~ Constructors -----------------------------------------------------------

    /**
     * Constructor for creating a DynamicTreeTableModel.
     *
     * @param  root               DOCUMENT ME!
     * @param  columnNames        DOCUMENT ME!
     * @param  getterMethodNames  DOCUMENT ME!
     * @param  setterMethodNames  DOCUMENT ME!
     * @param  cTypes             DOCUMENT ME!
     */
    public DynamicTreeTableModel(final TreeNode root,
            final String[] columnNames,
            final String[] getterMethodNames,
            final String[] setterMethodNames,
            final Class[] cTypes) {
        super(root);
        this.columnNames = columnNames;
        this.methodNames = getterMethodNames;
        this.setterMethodNames = setterMethodNames;
        this.cTypes = cTypes;
    }

    //~ Methods ----------------------------------------------------------------

    //
    // TreeModel interface
    //

    /**
     * TreeModel method to return the number of children of a particular node. Since <code>node</code> is a TreeNode,
     * this can be answered via the TreeNode method <code>getChildCount</code>.
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int getChildCount(final Object node) {
        return ((TreeNode)node).getChildCount();
    }

    /**
     * TreeModel method to locate a particular child of the specified node. Since <code>node</code> is a TreeNode, this
     * can be answered via the TreeNode method <code>getChild</code>.
     *
     * @param   node  DOCUMENT ME!
     * @param   i     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Object getChild(final Object node, final int i) {
        return ((TreeNode)node).getChildAt(i);
    }

    /**
     * TreeModel method to determine if a node is a leaf. Since <code>node</code> is a TreeNode, this can be answered
     * via the TreeNode method <code>isLeaf</code>.
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isLeaf(final Object node) {
        return ((TreeNode)node).isLeaf();
    }

    //
    // The TreeTable interface.
    //

    /**
     * Returns the number of column names passed into the constructor.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns the column name passed into the constructor.
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getColumnName(final int column) {
        if ((cTypes == null) || (column < 0) || (column >= cTypes.length)) {
            return null;
        }
        return columnNames[column];
    }

    /**
     * Returns the column class for column <code>column</code>. This is set in the constructor.
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Class getColumnClass(final int column) {
        if ((cTypes == null) || (column < 0) || (column >= cTypes.length)) {
            return null;
        }
        return cTypes[column];
    }

    /**
     * Returns the value for the column <code>column</code> and object <code>node</code>. The return value is determined
     * by invoking the method specified in constructor for the passed in column.
     *
     * @param   node    DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Object getValueAt(final Object node, final int column) {
        try {
            final Method method = node.getClass().getMethod(methodNames[column],
                    null);
            if (method != null) {
                return method.invoke(node, null);
            }
        } catch (Throwable th) {
        }

        return null;
    }

    /**
     * Returns true if there is a setter method name for column <code>column</code>. This is set in the constructor.
     *
     * @param   node    DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isCellEditable(final Object node, final int column) {
        return ((setterMethodNames != null)
                        && (setterMethodNames[column] != null));
    }

    /**
     * Sets the value to <code>aValue</code> for the object <code>node</code> in column <code>column</code>. This is
     * done by using the setter method name, and coercing the passed in value to the specified type.
     *
     * @param  aValue  DOCUMENT ME!
     * @param  node    DOCUMENT ME!
     * @param  column  DOCUMENT ME!
     */
    // Note: This looks up the methods each time! This is rather inefficient;
    // it should really be changed to cache matching methods/constructors
    // based on <code>node</code>'s class, and <code>aValue</code>'s class.
    @Override
    public void setValueAt(Object aValue, final Object node, final int column) {
        boolean found = false;
        try {
            // We have to search through all the methods since the
            // types may not match up.
            final Method[] methods = node.getClass().getMethods();

            for (int counter = methods.length - 1; counter >= 0; counter--) {
                if (methods[counter].getName().equals(setterMethodNames[column])
                            && (methods[counter].getParameterTypes() != null)
                            && (methods[counter].getParameterTypes().length == 1)) {
                    // We found a matching method
                    final Class param = methods[counter].getParameterTypes()[0];
                    if (!param.isInstance(aValue)) {
                        // Yes, we can use the value passed in directly,
                        // no coercision is necessary!
                        if ((aValue instanceof String)
                                    && (((String)aValue).length() == 0)) {
                            // Assume an empty string is null, this is
                            // probably bogus for here.
                            aValue = null;
                        } else {
                            // Have to attempt some sort of coercision.
                            // See if the expected parameter type has
                            // a constructor that takes a String.
                            final Constructor cs = param.getConstructor(new Class[] { String.class });
                            if (cs != null) {
                                aValue = cs.newInstance(new Object[] { aValue });
                            } else {
                                aValue = null;
                            }
                        }
                    }
                    // null either means it was an empty string, or there
                    // was no translation. Could potentially deal with these
                    // differently.
                    methods[counter].invoke(node, new Object[] { aValue });
                    found = true;
                    break;
                }
            }
        } catch (Throwable th) {
            System.out.println("exception: " + th);
        }
        if (found) {
            // The value changed, fire an event to notify listeners.
            final TreeNode parent = ((TreeNode)node).getParent();
            fireTreeNodesChanged(
                this,
                getPathToRoot(parent),
                new int[] { getIndexOfChild(parent, node) },
                new Object[] { node });
        }
    }
}
