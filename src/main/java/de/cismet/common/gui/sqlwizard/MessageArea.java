/*
 * MessageArea.java
 *
 * Created on 5. September 2003, 10:32
 */

package de.cismet.common.gui.sqlwizard;

import javax.swing.*;

import org.apache.log4j.*;

/**
 *
 * @author  pascal
 */
public class MessageArea extends JTextArea
{
    private final Appender appender;

    /** Creates a new instance of MessageArea */
    public MessageArea()
    {
        this.appender = new MessageAppender();
        
        this.setEditable(false);
        this.setAutoscrolls(true);
    }
    
    public void reset()
    {
        this.setText(null);
    }
    
    public Appender getAppender()
    {
        return this.appender;
    }
    
    private class MessageAppender extends AppenderSkeleton
    {
        //private final PatternLayout layout;
        
        private int maxRows = 25;
        private int rows = 0;
        
        private MessageAppender()
        {
            super();
            //layout = new PatternLayout("%-4r [%t] %-5p %c %x - %m%n");
            //this.setLayout(new PatternLayout("%-4r [%t] %-5p %c %x - %m%n"));
        }
        
        protected void append(org.apache.log4j.spi.LoggingEvent loggingEvent)
        {  
            if (rows == maxRows)
            {
                MessageArea.this.reset();
                rows = 0;
            }
            
            rows++;
            MessageArea.this.append(loggingEvent.getRenderedMessage()+"\n");
        }

        public void close()
        {
        }
        
        public boolean requiresLayout()
        {
            return false;
        }
    }
}
