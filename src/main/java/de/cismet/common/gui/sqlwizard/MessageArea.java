/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * MessageArea.java
 *
 * Created on 5. September 2003, 10:32
 */
package de.cismet.common.gui.sqlwizard;

import org.apache.log4j.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class MessageArea extends JTextArea {

    //~ Instance fields --------------------------------------------------------

    private final Appender appender;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of MessageArea.
     */
    public MessageArea() {
        this.appender = new MessageAppender();

        this.setEditable(false);
        this.setAutoscrolls(true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        this.setText(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Appender getAppender() {
        return this.appender;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class MessageAppender extends AppenderSkeleton {
        //~ Instance fields ----------------------------------------------------

        // private final PatternLayout layout;

        private int maxRows = 25;
        private int rows = 0;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new MessageAppender object.
         */
        private MessageAppender() {
            super();
            // layout = new PatternLayout("%-4r [%t] %-5p %c %x - %m%n");
            // this.setLayout(new PatternLayout("%-4r [%t] %-5p %c %x - %m%n"));
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected void append(final org.apache.log4j.spi.LoggingEvent loggingEvent) {
            if (rows == maxRows) {
                MessageArea.this.reset();
                rows = 0;
            }

            rows++;
            MessageArea.this.append(loggingEvent.getRenderedMessage() + "\n");
        }

        @Override
        public void close() {
        }

        @Override
        public boolean requiresLayout() {
            return false;
        }
    }
}
