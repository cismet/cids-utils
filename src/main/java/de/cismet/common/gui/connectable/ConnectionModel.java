/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ConnectionModel.java
 *
 * Created on 4. August 2003, 16:20
 */
package de.cismet.common.gui.connectable;

import java.beans.PropertyChangeListener;

import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface ConnectionModel {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  connectable  DOCUMENT ME!
     */
    void addConnectable(Connectable connectable);

    /**
     * DOCUMENT ME!
     *
     * @param  connectable  DOCUMENT ME!
     */
    void removeConnectable(Connectable connectable);

    /**
     * DOCUMENT ME!
     *
     * @param  index  DOCUMENT ME!
     */
    void removeConnectable(int index);

    /**
     * DOCUMENT ME!
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Connectable getConnectable(int index);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List getConnectables();

    /**
     * DOCUMENT ME!
     *
     * @param  link  DOCUMENT ME!
     */
    void addLink(ConnectionLink link);

    /**
     * DOCUMENT ME!
     *
     * @param  link  DOCUMENT ME!
     */
    void removeLink(ConnectionLink link);

    /**
     * DOCUMENT ME!
     *
     * @param  index  DOCUMENT ME!
     */
    void removeLink(int index);

    /**
     * DOCUMENT ME!
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    ConnectionLink getLink(int index);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List getLinks();

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * DOCUMENT ME!
     */
    void clear();
}
