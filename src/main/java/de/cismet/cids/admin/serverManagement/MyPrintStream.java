/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.admin.serverManagement;


import java.awt.EventQueue;
import java.io.PrintStream;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;

class MyPrintStream extends PrintStream { 
    PrintStream systemOutStream;
    StringBuffer buf;
    long lastInput = -1;
    Boolean logLastInput;
    Boolean hasGui = false;
    JTextPane theGuiComponent = null;
    SimpleAttributeSet set = null;
    
    
    
    public MyPrintStream(PrintStream out, StringBuffer buf, JTextPane component,
            Boolean logLastInput, Boolean hasGui, SimpleAttributeSet set ) {
        super(out);
        this.logLastInput=logLastInput;
        this.systemOutStream = out;
        this.lastInput = lastInput;
        this.buf = buf;
        this.theGuiComponent = component;
        this.hasGui = hasGui;
        this.set = set;
        
        
    } 
    
    public MyPrintStream(PrintStream out, StringBuffer buf, SimpleAttributeSet set)
    {
        super(out);
        systemOutStream = out;        
        this.buf = buf;        
        this.set = set;
    }
    
    public long getLastInputTime() {
        return lastInput;
    }
    
    
    @Override
    public void write(byte[] buf, int off, int len) {
        final byte[] subBuf = new byte[len];
        for (int i = off; i < len; i++) {
            subBuf[i] = buf[off + i];
        }
        final String s = new String(subBuf);
        if (logLastInput) {
            lastInput=System.currentTimeMillis();
        }
        this.buf.append(s);
        if(hasGui)
        {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        theGuiComponent.getDocument().insertString(
                                theGuiComponent.getDocument().getLength(),
                                s, set);
                        }
                    catch ( javax.swing.text.BadLocationException ble) {
                        // no printstacktrace possible else chain reaction
                    }
                }
            });
        }
        else
            systemOutStream.write(buf, off, len);

    }
    

    
}