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

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
import javax.swing.tree.*;

/**
 * Parses the Netscape bookmarks file (NETSCAPE-Bookmark-file-1) into BookmarkEntries's and BookmarkDirectories.
 *
 * <p>For the time being, this is a hack, there needs to be a more generic API to this to allow adding/removing
 * bookmarks and pulling from different sources other than just Netscape. But for this example, this is plenty good.</p>
 *
 * <p>While a hack, this is interesting in that it shows how you can use the parser provided in the
 * javax.swing.text.html.parser package outside of the HTML package. The netscape format is a pseudo HTML file, pseudo
 * in that there is no head/body. All the bookmarks are presented as DT's in a DL and the name of the directory is a DT.
 * An instance of the parser is created, a callback is registered, and as the parser parses the file, nodes of the
 * correct type are created.</p>
 *
 * @author   Scott Violet
 * @version  $Revision$, $Date$
 */
public class Bookmarks {

    //~ Static fields/initializers ---------------------------------------------

    private static final short NO_ENTRY = 0;
    private static final short BOOKMARK_ENTRY = 2;
    private static final short DIRECTORY_ENTRY = 3;

    //~ Instance fields --------------------------------------------------------

    /** The root node the bookmarks are added to. */
    private BookmarkDirectory root;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Bookmarks object, with the entries coming from <code>path</code>.
     *
     * @param  path  DOCUMENT ME!
     */
    public Bookmarks(final String path) {
        root = new BookmarkDirectory("Bookmarks");
        if (path != null) {
            parse(path);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns the root of the bookmarks.
     *
     * @return  DOCUMENT ME!
     */
    public BookmarkDirectory getRoot() {
        return root;
    }

    /**
     * Adds the bookmarks in the file at <code>path</code> to the current root. This creates a ParserDelegator and uses
     * a CallbackHandler to do the parser.
     *
     * @param  path  DOCUMENT ME!
     */
    protected void parse(final String path) {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(path));

            new ParserDelegator().parse(reader, new CallbackHandler(), true);
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe);
            JOptionPane.showMessageDialog(
                null,
                "Load Bookmarks",
                "Unable to load bookmarks",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * The heart of the parsing. The parser parses the data and notifies a delegate, via the
     * HTMLEditorKit.ParserCallback interface, as the data is parsed. When CallbackHandler receives notification it
     * creates new BookmarkEntries or BookmarkDirectories.
     *
     * @version  $Revision$, $Date$
     */
    private class CallbackHandler extends HTMLEditorKit.ParserCallback {

        //~ Instance fields ----------------------------------------------------

        /** Parent node that new entries are added to. */
        private BookmarkDirectory parent;
        /** The most recently parsed tag. */
        private HTML.Tag tag;
        /** The last tag encountered. */
        private HTML.Tag lastTag;
        /** The state, will be one of NO_ENTRY, DIRECTORY_ENTRY, or BOOKMARK_ENTRY. */
        private short state;
        /** Date for the next BookmarkDirectory node. */
        private Date parentDate;

        /**
         * The values from the attributes are placed in here. When the text is encountered this is added to the node
         * hierarchy and a new instance is created.
         */
        private BookmarkEntry lastBookmark;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates the CallbackHandler.
         */
        public CallbackHandler() {
            parent = root;
            lastBookmark = new BookmarkEntry();
        }

        //~ Methods ------------------------------------------------------------

        //
        // HTMLEditorKit.ParserCallback methods
        //

        /**
         * Invoked when text in the html document is encountered. Based on the current state, this will either: do
         * nothing (state == NO_ENTRY), create a new BookmarkEntry (state == BOOKMARK_ENTRY) or create a new
         * BookmarkDirectory (state == DIRECTORY_ENTRY). If state is != NO_ENTRY, it is reset to NO_ENTRY after this is
         * invoked.
         *
         * @param  data  DOCUMENT ME!
         * @param  pos   DOCUMENT ME!
         */
        @Override
        public void handleText(final char[] data, final int pos) {
            switch (state) {
                case NO_ENTRY: {
                    break;
                }
                case BOOKMARK_ENTRY:
                // URL.
                {
                    lastBookmark.setName(new String(data));
                    parent.add(lastBookmark);
                    lastBookmark = new BookmarkEntry();
                }
                break;
                case DIRECTORY_ENTRY:
                // directory.
                {
                    final BookmarkDirectory newParent = new BookmarkDirectory(new String(data));
                    newParent.setCreated(parentDate);
                    parent.add(newParent);
                    parent = newParent;
                }
                break;
                default: {
                    break;
                }
            }
            state = NO_ENTRY;
        }

        /**
         * Invoked when a start tag is encountered. Based on the tag this may update the BookmarkEntry and state, or
         * update the parentDate.
         *
         * @param  t    DOCUMENT ME!
         * @param  a    DOCUMENT ME!
         * @param  pos  DOCUMENT ME!
         */
        @Override
        public void handleStartTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
            lastTag = tag;
            tag = t;
            if ((t == HTML.Tag.A) && (lastTag == HTML.Tag.DT)) {
                long lDate;

                // URL
                URL url;
                try {
                    url = new URL((String)a.getAttribute(HTML.Attribute.HREF));
                } catch (MalformedURLException murle) {
                    url = null;
                }
                lastBookmark.setLocation(url);

                // created
                Date date;
                try {
                    lDate = Long.parseLong((String)a.getAttribute("add_date"));
                    if (lDate != 0L) {
                        date = new Date(1000L * lDate);
                    } else {
                        date = null;
                    }
                } catch (Exception ex) {
                    date = null;
                }
                lastBookmark.setCreated(date);

                // last visited
                try {
                    lDate = Long.parseLong((String)a.getAttribute("last_visit"));
                    if (lDate != 0L) {
                        date = new Date(1000L * lDate);
                    } else {
                        date = null;
                    }
                } catch (Exception ex) {
                    date = null;
                }
                lastBookmark.setLastVisited(date);

                state = BOOKMARK_ENTRY;
            } else if ((t == HTML.Tag.H3) && (lastTag == HTML.Tag.DT)) {
                // new node.
                try {
                    parentDate = new Date(1000L * Long.parseLong((String)a.getAttribute("add_date")));
                } catch (Exception ex) {
                    parentDate = null;
                }
                state = DIRECTORY_ENTRY;
            }
        }

        /**
         * Invoked when the end of a tag is encountered. If the tag is a DL, this will set the node that parents are
         * added to the current nodes parent.
         *
         * @param  t    DOCUMENT ME!
         * @param  pos  DOCUMENT ME!
         */
        @Override
        public void handleEndTag(final HTML.Tag t, final int pos) {
            if ((t == HTML.Tag.DL) && (parent != null)) {
                parent = (BookmarkDirectory)parent.getParent();
            }
        }
    }

    /**
     * BookmarkDirectory represents a directory containing other BookmarkDirectory's as well as BookmarkEntry's. It adds
     * a name and created property to DefaultMutableTreeNode.
     *
     * @version  $Revision$, $Date$
     */
    public static class BookmarkDirectory extends DefaultMutableTreeNode {

        //~ Instance fields ----------------------------------------------------

        /** Dates created. */
        private Date created;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new BookmarkDirectory object.
         *
         * @param  name  DOCUMENT ME!
         */
        public BookmarkDirectory(final String name) {
            super(name);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  name  DOCUMENT ME!
         */
        public void setName(final String name) {
            setUserObject(name);
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getName() {
            return (String)getUserObject();
        }

        /**
         * DOCUMENT ME!
         *
         * @param  date  DOCUMENT ME!
         */
        public void setCreated(final Date date) {
            this.created = date;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Date getCreated() {
            return created;
        }
    }

    /**
     * BookmarkEntry represents a bookmark. It contains a URL, a user definable string, and two dates, one giving the
     * date the URL was last visited and the other giving the date the bookmark was created.
     *
     * @version  $Revision$, $Date$
     */
    public static class BookmarkEntry extends DefaultMutableTreeNode {

        //~ Instance fields ----------------------------------------------------

        /** User description of the string. */
        private String name;
        /** The URL the bookmark represents. */
        private URL location;
        /** Dates the URL was last visited. */
        private Date lastVisited;
        /** Date the URL was created. */
        private Date created;

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  name  DOCUMENT ME!
         */
        public void setName(final String name) {
            this.name = name;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getName() {
            return name;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  location  DOCUMENT ME!
         */
        public void setLocation(final URL location) {
            this.location = location;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public URL getLocation() {
            return location;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  date  DOCUMENT ME!
         */
        public void setLastVisited(final Date date) {
            lastVisited = date;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Date getLastVisited() {
            return lastVisited;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  date  DOCUMENT ME!
         */
        public void setCreated(final Date date) {
            this.created = date;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Date getCreated() {
            return created;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
