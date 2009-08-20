/*
 * AbstractConnectable.java
 *
 * Created on 6. August 2003, 12:38
 */

package de.cismet.common.gui.connectable;

import java.util.*;

/**
 * Abstract implementation of the Connectable interface<p>
 * 
 *
 * @author  Pascal
 */
public abstract class AbstractConnectable implements Connectable
{
    private final String name;
    private final LinkedHashMap linkMap;
    private final DefaultPointIterator pointIterator = new DefaultPointIterator();
    //private final LinkedList pointList;
    
    /** Creates a new instance of AbstractConnectable */
    public AbstractConnectable(String name)
    {
        this.name = name;
        this.linkMap = new LinkedHashMap();
        //this.pointList = new LinkedList();
    }
    
    public boolean addLink(ConnectionLink link)
    {
        if(!linkMap.containsKey(link.getId()))
        {
            /*if(link.isSource(this))
            {
                this.pointList.add(link.getSource());
                this.linkMap.put(link.getId(), link);
                
                return true;
            }
            else if(link.isTarget(this))
            {
                this.pointList.add(link.getTarget());
                this.linkMap.put(link.getId(), link);
                
                return true;
            }*/
            
            this.linkMap.put(link.getId(), link);
            return true;
        }
        
        return false;
    }

    public boolean removeLink(String id)
    {
        Object object = linkMap.remove(id);
        if(object != null)
        {
            /*ConnectionLink link = (ConnectionLink)object;
            if(link.isSource(this))
            {
                this.pointList.remove(link.getSource());
            }
            else if(link.isTarget(this))
            {
                this.pointList.remove(link.getTarget());
            }*/
            
            return true;
        }
        
        return false;
    }
    
    public ConnectionLink getLink(String id)
    {
        Object object = linkMap.get(id);
        return object != null ? (ConnectionLink)object : null;
    }
    
    // .........................................................................
    
    public int getLinkCount()
    {
        return this.linkMap.size();
    }
    
    public int getSourceLinkCount()
    {
        int linkCout = 0;
        Iterator iterator = this.getLinks().iterator();
        
        while(iterator.hasNext())
        {
            if(((ConnectionLink)iterator.next()).isTarget(this))
            {
                linkCout++;
            }
        }
        
        return linkCout;
    }    
    
    public int getTargetLinkCount()
    {
        int linkCout = 0;
        Iterator iterator = this.getLinks().iterator();
        
        while(iterator.hasNext())
        {
            if(((ConnectionLink)iterator.next()).isSource(this))
            {
                linkCout++;
            }
        }
        
        return linkCout;
    }  
    
    // .........................................................................
    
    public Collection getLinks()
    {
        return Collections.unmodifiableCollection(linkMap.values());
    }
    
    public Collection getSourceLinks()
    {
        Collection links = this.getLinks();
        ArrayList sourceLinks = new ArrayList(links.size());
        Iterator iterator = links.iterator();
        
        while(iterator.hasNext())
        {
            ConnectionLink link = (ConnectionLink)iterator.next();
            if(link.isTarget(this))
            {
                sourceLinks.add(link);
            }
        }
        
        sourceLinks.trimToSize();
        return sourceLinks;
    }
    
    public Collection getTargetLinks()
    {
        Collection links = this.getLinks();
        ArrayList targetLinks = new ArrayList(links.size());
        Iterator iterator = links.iterator();
        
        while(iterator.hasNext())
        {
            ConnectionLink link = (ConnectionLink)iterator.next();
            if(link.isSource(this))
            {
                targetLinks.add(link);
            }
        }
        
        targetLinks.trimToSize();
        return targetLinks;
    }
    
    public List getConnectables()
    {
        Collection links = this.getLinks();
        ArrayList connectables = new ArrayList(links.size());
        Iterator iterator = links.iterator();
        
        while(iterator.hasNext())
        {
            ConnectionLink link = (ConnectionLink)iterator.next();
            if(link.isSource(this))
            {
                connectables.add(link.getTarget().getConnectable());
            }
            else if(link.isTarget(this))
            {
                connectables.add(link.getSource().getConnectable());
            }
        }
        
        return connectables;
    }
    
    public List getSourceConnectables()
    {
        Collection links = this.getLinks();
        ArrayList sourceConnectables = new ArrayList(links.size());
        Iterator iterator = links.iterator();
        
        while(iterator.hasNext())
        {
            ConnectionLink link = (ConnectionLink)iterator.next();
            if(link.isTarget(this))
            {
                sourceConnectables.add(link.getSource().getConnectable());
            }
        }
        
        return sourceConnectables;
    }
    
    public List getTargetConnectables()
    {
        Collection links = this.getLinks();
        ArrayList targetConnectables = new ArrayList(links.size());
        Iterator iterator = links.iterator();
        
        while(iterator.hasNext())
        {
            ConnectionLink link = (ConnectionLink)iterator.next();
            if(link.isSource(this))
            {
                targetConnectables.add(link.getTarget().getConnectable());
            }
        }
        
        return targetConnectables;
    }
    
    /*public List getPoints()
    {
        return Collections.unmodifiableList(pointList);
    }*/
    
    public PointIterator getPoints()
    {
        this.pointIterator.init(this.getLinks().iterator());
        return this.pointIterator;
    }

    // .........................................................................
    
    public boolean isSource()
    {
        return this.getTargetLinks().size() > 0;
    }
    
    public boolean isTarget()
    {
        return this.getSourceLinks().size() > 0;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String toString()
    {
        return this.getName();
    }
    
    // .........................................................................

    public abstract ConnectionPoint createPoint();
    
    // .........................................................................
    
    class DefaultPointIterator implements PointIterator
    {
        private Iterator iterator;
        private ConnectionPoint[] points = new ConnectionPoint[2];
        
        private void init(Iterator iterator)
        {
            this.iterator = iterator;
        }
        
        public boolean hasNext()
        {
            return this.iterator.hasNext();
        }        
        
        public de.cismet.common.gui.connectable.ConnectionPoint[] nextPoints()
        {
            ConnectionLink link = (ConnectionLink)iterator.next();
            
            if(link.isSource(AbstractConnectable.this))
            {
                points[0] = link.getSource();
                points[1] = link.getTarget();
            }
            else if(link.isTarget(AbstractConnectable.this))
            {
                points[0] = link.getTarget();
                points[1] = link.getSource();
            }
            else
            {
                throw new RuntimeException("ConnectionLink '" + link + "'");
            }  
            
            return points;
        }    
    }
}
