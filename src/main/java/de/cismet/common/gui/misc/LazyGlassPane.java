/*
 * LazyGlassPane.java
 *
 * Created on 11. September 2003, 16:49
 */

package de.cismet.common.gui.misc;

//package Sirius.Navigator.CustomWidgets;

/*******************************************************************************

 	Copyright (c)	:	EIG (Environmental Informatics Group)
						http://www.htw-saarland.de/eig
						Prof. Dr. Reiner Guettler
						Prof. Dr. Ralf Denzer

						HTWdS
						Hochschule fuer Technik und Wirtschaft des Saarlandes
						Goebenstr. 40
 						66117 Saarbruecken
 						Germany

	Programmers		:	Pascal

 	Project			:	WuNDA 2
 	Filename		:
	Version			:	1.0
 	Purpose			:
	Created			:	01.10.1999
	History			:

*******************************************************************************/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* LazyGlassPane ist ein GlassPane, das nicht auf Benutzereingaben reagiert.
* Es werden "dummy-Listener" hinzugefuegt, die keine Events verarbeiten.
*
* @see LazyPanel
*/

class LazyGlassPane extends JComponent
{
	private boolean blockEvents = false;   
    private final Color overlayColor;// = new Color(100, 0, 0, 75);

	/**
	* Dieser Konstruktor erzeugt ein neues LazyGlassPane.
	*
	* @param blockEvents Bei true werden die Events sofort blockiert.
	*/
	public LazyGlassPane()
	{
		super();
		         
        this.addMouseListener(new LazyMouseListener());
        this.addKeyListener(new LazyKeyListener());
        
        this.overlayColor = null;
	}
    
    public LazyGlassPane(Color overlayColor)
	{
		super();
		        
        this.addMouseListener(new LazyMouseListener());
        this.addKeyListener(new LazyKeyListener());
        
        this.overlayColor = overlayColor;
	}

	public void blockEvents(boolean blockEvents)
	{
		//System.out.println("[LazyGlassPane] blockEvents: " + blockEvents + " (" + this.blockEvents + ")");
        
        if(blockEvents && !this.blockEvents)
		{
			//System.out.println("[LazyGlassPane] blockEvents: " + blockEvents);
            
            this.blockEvents = blockEvents;
            this.setFocusTraversalKeysEnabled(!blockEvents);
            
            this.setVisible(blockEvents);
            this.requestFocus();
		}
		else if(!blockEvents && this.blockEvents)
		{
			//System.out.println("[LazyGlassPane] blockEvents: " + blockEvents);
            
            this.blockEvents = blockEvents;
            this.setFocusTraversalKeysEnabled(!blockEvents);
			
            this.setVisible(blockEvents);
		}
	}

	public boolean isFocusable()
	{
		return this.blockEvents;
	}
    
    public void paint(Graphics g)
    {
        if(overlayColor != null)
        {
            g.setColor(overlayColor);
            g.fillRect(0, 0 ,this.getWidth(), this.getHeight());
        }
    }
    
    // does not work:
    /*
    public void setVisible(boolean visible)
    {
        System.out.println("[LazyGlassPane] setVisible " + visible);
        this.blockEvents(visible);
    }*/
    
    class LazyMouseListener implements MouseListener, MouseMotionListener
    {
        public LazyMouseListener()
        {
            super();
        }
    
        public void mouseClicked(MouseEvent e) 
        {
            e.consume();
        }
        public void mouseDragged(MouseEvent e) 
        {
            e.consume();
        }
        
        public void mouseEntered(MouseEvent e) 
        {
            e.consume();
        }
        
        public void mouseExited(MouseEvent e) 
        {
            e.consume();
        }
        
        public void mousePressed(MouseEvent e) 
        {
            //LazyGlassPane.this.requestFocus();
            //LazyGlassPane.this.blockEvents(false);
            e.consume();
        }
        
        public void mouseReleased(MouseEvent e) 
        {
            e.consume();
        }
        
        public void mouseMoved(MouseEvent e) 
        {
            e.consume();
        }
        
    }
    
    class LazyKeyListener implements KeyListener
    {
        public LazyKeyListener()
        {
            super();
        }
    
        public void keyPressed(KeyEvent e) 
        {
            //System.out.println("LazyKeyListener KeyChar: " + e.getKeyChar());
            //System.out.println("LazyKeyListener KeyCode: " + e.getKeyCode());
            e.consume();
        }
        
        public void keyReleased(KeyEvent e) 
        {
            //System.out.println("LazyKeyListener: " + e.getKeyChar());
            e.consume();
        }
        
        public void keyTyped(KeyEvent e) 
        {
            //System.out.println("LazyKeyListener: " + e.getKeyChar());
            e.consume();
        }
    }
    
    /*
    class FocusThread extends Thread
    {
        public void run()
        {
            LazyGlassPane.this.requestFocus();
            //System.out.println("[FocusThread] start");
            SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					//System.out.println("[invokeLater] start");
                    LazyGlassPane.this.requestFocus();
                    //System.out.println("[invokeLater] stop");
				}
			});
            //System.out.println("[FocusThread] stop");
        }
    }*/
}
