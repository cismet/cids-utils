/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultConnectionModel.java
 *
 * Created on 6. August 2003, 17:42
 */
package de.cismet.common.gui.connectable;

import java.beans.PropertyChangeListener;

import java.util.*;

import javax.swing.event.SwingPropertyChangeSupport;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class DefaultConnectionModel implements ConnectionModel {

    //~ Instance fields --------------------------------------------------------

    protected final ArrayList connectableList;
    protected final ArrayList linkList;
    private final SwingPropertyChangeSupport propertyChangeSupport;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of DefaultConnectionModel.
     */
    public DefaultConnectionModel() {
        this.connectableList = new ArrayList();
        this.linkList = new ArrayList();
        this.propertyChangeSupport = new SwingPropertyChangeSupport(this);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void addConnectable(final Connectable connectable) {
        this.connectableList.add(connectable);
        this.propertyChangeSupport.firePropertyChange("addConnectable", null, connectable);
    }

    @Override
    public void removeConnectable(final Connectable connectable) {
        this.connectableList.remove(connectable);
        this.removeLinks(connectable);

        this.propertyChangeSupport.firePropertyChange("removeConnectable", connectable, null);
    }

    @Override
    public void removeConnectable(final int index) {
        final Object object = connectableList.remove(index);
        if (object != null) {
            this.removeLinks((Connectable)object);
        }

        propertyChangeSupport.firePropertyChange("removeConnectable", object, null);
    }

    @Override
    public Connectable getConnectable(final int index) {
        final Object object = connectableList.get(index);
        return (object != null) ? (Connectable)object : null;
    }

    @Override
    public List getConnectables() {
        return Collections.unmodifiableList(this.connectableList);
    }

    @Override
    public void addLink(final ConnectionLink link) {
        link.link();
        linkList.add(link);

        propertyChangeSupport.firePropertyChange("addLink", null, link);
    }

    @Override
    public void removeLink(final ConnectionLink link) {
        linkList.remove(link);
        link.unlink();

        propertyChangeSupport.firePropertyChange("removeLink", link, null);
    }

    @Override
    public void removeLink(final int index) {
        final Object object = linkList.remove(index);
        if (object != null) {
            ((ConnectionLink)object).unlink();
        }

        propertyChangeSupport.firePropertyChange("removeLink", object, null);
    }

    @Override
    public ConnectionLink getLink(final int index) {
        final Object object = linkList.get(index);
        return (object != null) ? (ConnectionLink)object : null;
    }

    @Override
    public List getLinks() {
        return Collections.unmodifiableList(this.linkList);
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void clear() {
        final LinkedList connectableList = new LinkedList();
        connectableList.addAll(this.getConnectables());

        final Iterator iterator = connectableList.iterator();
        while (iterator.hasNext()) {
            this.removeConnectable((Connectable)iterator.next());
        }

        this.connectableList.clear();
        this.linkList.clear();
    }

    /**
     * .........................................................................
     *
     * @param  connectable  DOCUMENT ME!
     */
    protected void removeLinks(final Connectable connectable) {
        final Iterator iterator = new LinkedList(connectable.getLinks()).iterator();
        while (iterator.hasNext()) {
            final ConnectionLink link = (ConnectionLink)iterator.next();
            this.removeLink(link);
        }
    }
}
