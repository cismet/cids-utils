/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.admin.serverManagement;

import java.awt.EventQueue;

import java.io.PrintStream;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class MyPrintStream extends PrintStream {

    //~ Instance fields --------------------------------------------------------

    PrintStream systemOutStream;
    StringBuffer buf;
    long lastInput = -1;
    Boolean logLastInput;
    Boolean hasGui = false;
    JTextPane theGuiComponent = null;
    SimpleAttributeSet set = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MyPrintStream object.
     *
     * @param  out  DOCUMENT ME!
     * @param  buf  DOCUMENT ME!
     * @param  set  DOCUMENT ME!
     */
    public MyPrintStream(final PrintStream out, final StringBuffer buf, final SimpleAttributeSet set) {
        super(out);
        systemOutStream = out;
        this.buf = buf;
        this.set = set;
    }

    /**
     * Creates a new MyPrintStream object.
     *
     * @param  out           DOCUMENT ME!
     * @param  buf           DOCUMENT ME!
     * @param  component     DOCUMENT ME!
     * @param  logLastInput  DOCUMENT ME!
     * @param  hasGui        DOCUMENT ME!
     * @param  set           DOCUMENT ME!
     */
    public MyPrintStream(final PrintStream out,
            final StringBuffer buf,
            final JTextPane component,
            final Boolean logLastInput,
            final Boolean hasGui,
            final SimpleAttributeSet set) {
        super(out);
        this.logLastInput = logLastInput;
        this.systemOutStream = out;
        this.lastInput = lastInput;
        this.buf = buf;
        this.theGuiComponent = component;
        this.hasGui = hasGui;
        this.set = set;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public long getLastInputTime() {
        return lastInput;
    }

    @Override
    public void write(final byte[] buf, final int off, final int len) {
        final byte[] subBuf = new byte[len];
        for (int i = off; i < len; i++) {
            subBuf[i] = buf[off + i];
        }
        final String s = new String(subBuf);
        if (logLastInput) {
            lastInput = System.currentTimeMillis();
        }
        this.buf.append(s);
        if (hasGui) {
            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            theGuiComponent.getDocument()
                                    .insertString(
                                        theGuiComponent.getDocument().getLength(),
                                        s,
                                        set);
                        } catch (javax.swing.text.BadLocationException ble) {
                            // no printstacktrace possible else chain reaction
                        }
                    }
                });
        } else {
            systemOutStream.write(buf, off, len);
        }
    }
}
