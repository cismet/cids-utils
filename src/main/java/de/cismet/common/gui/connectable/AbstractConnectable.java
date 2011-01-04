/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * AbstractConnectable.java
 *
 * Created on 6. August 2003, 12:38
 */
package de.cismet.common.gui.connectable;

import java.util.*;

/**
 * Abstract implementation of the Connectable interface.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public abstract class AbstractConnectable implements Connectable {

    //~ Instance fields --------------------------------------------------------

    private final String name;
    private final LinkedHashMap linkMap;
    private final DefaultPointIterator pointIterator = new DefaultPointIterator();
    // private final LinkedList pointList;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of AbstractConnectable.
     *
     * @param  name  DOCUMENT ME!
     */
    public AbstractConnectable(final String name) {
        this.name = name;
        this.linkMap = new LinkedHashMap();
        // this.pointList = new LinkedList();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean addLink(final ConnectionLink link) {
        if (!linkMap.containsKey(link.getId())) {
            /*if(link.isSource(this))
             * { this.pointList.add(link.getSource()); this.linkMap.put(link.getId(), link);  return true; } else
             * if(link.isTarget(this)) { this.pointList.add(link.getTarget()); this.linkMap.put(link.getId(), link);
             * return true;}*/

            this.linkMap.put(link.getId(), link);
            return true;
        }

        return false;
    }

    @Override
    public boolean removeLink(final String id) {
        final Object object = linkMap.remove(id);
        if (object != null) {
            /*ConnectionLink link = (ConnectionLink)object;
             * if(link.isSource(this)) { this.pointList.remove(link.getSource()); } else if(link.isTarget(this)) {
             * this.pointList.remove(link.getTarget());}*/

            return true;
        }

        return false;
    }

    @Override
    public ConnectionLink getLink(final String id) {
        final Object object = linkMap.get(id);
        return (object != null) ? (ConnectionLink)object : null;
    }

    // .........................................................................

    @Override
    public int getLinkCount() {
        return this.linkMap.size();
    }

    @Override
    public int getSourceLinkCount() {
        int linkCout = 0;
        final Iterator iterator = this.getLinks().iterator();

        while (iterator.hasNext()) {
            if (((ConnectionLink)iterator.next()).isTarget(this)) {
                linkCout++;
            }
        }

        return linkCout;
    }

    @Override
    public int getTargetLinkCount() {
        int linkCout = 0;
        final Iterator iterator = this.getLinks().iterator();

        while (iterator.hasNext()) {
            if (((ConnectionLink)iterator.next()).isSource(this)) {
                linkCout++;
            }
        }

        return linkCout;
    }

    // .........................................................................

    @Override
    public Collection getLinks() {
        return Collections.unmodifiableCollection(linkMap.values());
    }

    @Override
    public Collection getSourceLinks() {
        final Collection links = this.getLinks();
        final ArrayList sourceLinks = new ArrayList(links.size());
        final Iterator iterator = links.iterator();

        while (iterator.hasNext()) {
            final ConnectionLink link = (ConnectionLink)iterator.next();
            if (link.isTarget(this)) {
                sourceLinks.add(link);
            }
        }

        sourceLinks.trimToSize();
        return sourceLinks;
    }

    @Override
    public Collection getTargetLinks() {
        final Collection links = this.getLinks();
        final ArrayList targetLinks = new ArrayList(links.size());
        final Iterator iterator = links.iterator();

        while (iterator.hasNext()) {
            final ConnectionLink link = (ConnectionLink)iterator.next();
            if (link.isSource(this)) {
                targetLinks.add(link);
            }
        }

        targetLinks.trimToSize();
        return targetLinks;
    }

    @Override
    public List getConnectables() {
        final Collection links = this.getLinks();
        final ArrayList connectables = new ArrayList(links.size());
        final Iterator iterator = links.iterator();

        while (iterator.hasNext()) {
            final ConnectionLink link = (ConnectionLink)iterator.next();
            if (link.isSource(this)) {
                connectables.add(link.getTarget().getConnectable());
            } else if (link.isTarget(this)) {
                connectables.add(link.getSource().getConnectable());
            }
        }

        return connectables;
    }

    @Override
    public List getSourceConnectables() {
        final Collection links = this.getLinks();
        final ArrayList sourceConnectables = new ArrayList(links.size());
        final Iterator iterator = links.iterator();

        while (iterator.hasNext()) {
            final ConnectionLink link = (ConnectionLink)iterator.next();
            if (link.isTarget(this)) {
                sourceConnectables.add(link.getSource().getConnectable());
            }
        }

        return sourceConnectables;
    }

    @Override
    public List getTargetConnectables() {
        final Collection links = this.getLinks();
        final ArrayList targetConnectables = new ArrayList(links.size());
        final Iterator iterator = links.iterator();

        while (iterator.hasNext()) {
            final ConnectionLink link = (ConnectionLink)iterator.next();
            if (link.isSource(this)) {
                targetConnectables.add(link.getTarget().getConnectable());
            }
        }

        return targetConnectables;
    }

    /*public List getPoints()
     * { return Collections.unmodifiableList(pointList);}*/

    @Override
    public PointIterator getPoints() {
        this.pointIterator.init(this.getLinks().iterator());
        return this.pointIterator;
    }

    // .........................................................................

    @Override
    public boolean isSource() {
        return this.getTargetLinks().size() > 0;
    }

    @Override
    public boolean isTarget() {
        return this.getSourceLinks().size() > 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    // .........................................................................

    @Override
    public abstract ConnectionPoint createPoint();

    //~ Inner Classes ----------------------------------------------------------

    /**
     * .........................................................................
     *
     * @version  $Revision$, $Date$
     */
    class DefaultPointIterator implements PointIterator {

        //~ Instance fields ----------------------------------------------------

        private Iterator iterator;
        private ConnectionPoint[] points = new ConnectionPoint[2];

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  iterator  DOCUMENT ME!
         */
        private void init(final Iterator iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public de.cismet.common.gui.connectable.ConnectionPoint[] nextPoints() {
            final ConnectionLink link = (ConnectionLink)iterator.next();

            if (link.isSource(AbstractConnectable.this)) {
                points[0] = link.getSource();
                points[1] = link.getTarget();
            } else if (link.isTarget(AbstractConnectable.this)) {
                points[0] = link.getTarget();
                points[1] = link.getSource();
            } else {
                throw new RuntimeException("ConnectionLink '" + link + "'");
            }

            return points;
        }
    }
}
