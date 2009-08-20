/*
 * ServerConsole.java
 * $Revision: 1.1.1.1 $
 * Created on 26. Februar 2004, 14:42
 */



package de.cismet.cids.admin.serverManagement;


import java.io.IOException;
import javax.swing.text.*;
import javax.swing.*;
import javax.swing.table.*;
import java.lang.reflect.*;
import Sirius.server.*;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBluer;
import java.util.*;
import java.awt.*;



/* MiniatureServer Code an neue MiniServerVersion angepasst.
 * Variable miniatureServerInstance wurde entfernt.
 *
 */






/**
 * The class ServerConsole starts a cids server specified by a class
 * parameter and creates a console to manage the server.
 * The console shows logging and runtime information, if needed a help file and
 * provides functions to shutdown and reset the server. There is also the
 * possibility to call the class FileEditor to edit the cids server's
 * configuration file.
 *
 * @author  hell, oaltpeter, jfischer
 */
public class ServerConsoleGui extends javax.swing.JFrame {
   
    protected static HeadlessServerConsole hsc;

    
    private ImageIcon red=new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/redled.png"));
    private ImageIcon yellow=new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/yellowled.png"));
    private ImageIcon green=new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/greenled.png"));
    
 
    
    public ServerConsoleGui(String[] args){
       initComponents();       
       hsc = new HeadlessServerConsole(args,true,getTxtLog(), this);       
       
        tblInfo.setModel(new DefaultTableModel(new Object[][]{
                {"Online seit:","-"},
                {"ohne Fehler seit:","-"}
            },new String[] {"",""}
            ));
            tblInfo.setTableHeader(null);
    }
    
   
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cmdQueue = new javax.swing.JButton();
        cmdSaveLogs = new javax.swing.JButton();
        cmdTray = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        pnlStatus = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        lblAmpel = new javax.swing.JLabel();
        prbStatus = new javax.swing.JProgressBar();
        pnlMain = new javax.swing.JPanel();
        pnlControlAndInfo = new javax.swing.JPanel();
        pnlInformation = new javax.swing.JPanel();
        tblInfo = new javax.swing.JTable();
        pnlControl = new de.cismet.cids.tools.gui.farnsworth.GradientJPanel();
        lblTitleControl = new javax.swing.JLabel();
        cmdShutdown = new javax.swing.JButton();
        cmdRestart = new javax.swing.JButton();
        cmdInfo = new javax.swing.JButton();
        cmdHelp = new javax.swing.JButton();
        cmdedit = new javax.swing.JButton();
        pnlLogging = new javax.swing.JPanel();
        pnlTable = new javax.swing.JPanel();
        spnTable = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        txtLog = new javax.swing.JTextPane();
        pnlTitleExport = new de.cismet.cids.tools.gui.farnsworth.GradientJPanel();
        lblExportTitle = new javax.swing.JLabel();
        cmdDeleteLogs = new javax.swing.JButton();
        gplTitle = new de.cismet.cids.tools.gui.farnsworth.GradientJPanel();
        lblName = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();

        cmdQueue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/time.png"))); // NOI18N
        cmdQueue.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmdQueue.setContentAreaFilled(false);
        cmdQueue.setDefaultCapable(false);
        cmdQueue.setFocusPainted(false);
        cmdQueue.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/time.png"))); // NOI18N

        cmdSaveLogs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/save.png"))); // NOI18N
        cmdSaveLogs.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmdSaveLogs.setContentAreaFilled(false);
        cmdSaveLogs.setDefaultCapable(false);
        cmdSaveLogs.setFocusPainted(false);
        cmdSaveLogs.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/save.png"))); // NOI18N
        cmdSaveLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveLogsActionPerformed(evt);
            }
        });

        cmdTray.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/tray.png"))); // NOI18N
        cmdTray.setToolTipText("Minimieren in die System Tray");
        cmdTray.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmdTray.setContentAreaFilled(false);
        cmdTray.setDefaultCapable(false);
        cmdTray.setEnabled(false);
        cmdTray.setFocusPainted(false);
        cmdTray.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/tray.png"))); // NOI18N
        cmdTray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTrayActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("cids ServerConsole");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new java.awt.BorderLayout());

        pnlStatus.setLayout(new java.awt.GridBagLayout());

        lblStatus.setText("cismet GmbH");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlStatus.add(lblStatus, gridBagConstraints);

        lblAmpel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/yellowled.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlStatus.add(lblAmpel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        pnlStatus.add(prbStatus, gridBagConstraints);

        jPanel2.add(pnlStatus, java.awt.BorderLayout.SOUTH);

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlControlAndInfo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));
        pnlControlAndInfo.setLayout(new java.awt.BorderLayout());

        spnTable.setBorder(null);
        pnlInformation.setLayout(new java.awt.GridBagLayout());

        tblInfo.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        tblInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInfo.setEnabled(false);
        tblInfo.setShowHorizontalLines(false);
        tblInfo.setShowVerticalLines(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlInformation.add(tblInfo, gridBagConstraints);

        pnlControlAndInfo.add(pnlInformation, java.awt.BorderLayout.CENTER);

        pnlControl.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        pnlControl.setLayout(new java.awt.GridBagLayout());

        lblTitleControl.setForeground(new java.awt.Color(255, 255, 255));
        lblTitleControl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/pack_empty_co.gif"))); // NOI18N
        lblTitleControl.setText("Status & Bedienung");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        pnlControl.add(lblTitleControl, gridBagConstraints);

        cmdShutdown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/exit.png"))); // NOI18N
        cmdShutdown.setToolTipText("Server beenden");
        cmdShutdown.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmdShutdown.setContentAreaFilled(false);
        cmdShutdown.setDefaultCapable(false);
        cmdShutdown.setFocusPainted(false);
        cmdShutdown.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/exit.png"))); // NOI18N
        cmdShutdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShutdownActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdShutdown, gridBagConstraints);

        cmdRestart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/restart.png"))); // NOI18N
        cmdRestart.setToolTipText("Server neustarten");
        cmdRestart.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmdRestart.setContentAreaFilled(false);
        cmdRestart.setDefaultCapable(false);
        cmdRestart.setFocusPainted(false);
        cmdRestart.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/restart.png"))); // NOI18N
        cmdRestart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRestartActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdRestart, gridBagConstraints);

        cmdInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/info.png"))); // NOI18N
        cmdInfo.setToolTipText("Informationen \u00FCber den Server-Status");
        cmdInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmdInfo.setContentAreaFilled(false);
        cmdInfo.setDefaultCapable(false);
        cmdInfo.setFocusPainted(false);
        cmdInfo.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/info.png"))); // NOI18N
        cmdInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdInfoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdInfo, gridBagConstraints);

        cmdHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/help.png"))); // NOI18N
        cmdHelp.setToolTipText("Hilfedatei anzeigen");
        cmdHelp.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cmdHelp.setContentAreaFilled(false);
        cmdHelp.setDefaultCapable(false);
        cmdHelp.setFocusPainted(false);
        cmdHelp.setMaximumSize(new java.awt.Dimension(16, 16));
        cmdHelp.setMinimumSize(new java.awt.Dimension(16, 16));
        cmdHelp.setPreferredSize(new java.awt.Dimension(16, 16));
        cmdHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdHelp, gridBagConstraints);

        cmdedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/edit.png"))); // NOI18N
        cmdedit.setToolTipText("Konfigurationsdatei des Servers editieren");
        cmdedit.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cmdedit.setContentAreaFilled(false);
        cmdedit.setDefaultCapable(false);
        cmdedit.setFocusPainted(false);
        cmdedit.setPreferredSize(new java.awt.Dimension(16, 16));
        cmdedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlControl.add(cmdedit, gridBagConstraints);

        pnlControlAndInfo.add(pnlControl, java.awt.BorderLayout.NORTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlMain.add(pnlControlAndInfo, gridBagConstraints);

        pnlLogging.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));
        pnlLogging.setLayout(new java.awt.BorderLayout());

        pnlTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pnlTable.setLayout(new java.awt.GridBagLayout());

        spnTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        spnTable.setForeground(new java.awt.Color(51, 51, 51));

        jPanel1.setLayout(new java.awt.BorderLayout());

        txtLog.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        txtLog.setEditable(false);
        jPanel1.add(txtLog, java.awt.BorderLayout.CENTER);

        spnTable.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlTable.add(spnTable, gridBagConstraints);

        pnlLogging.add(pnlTable, java.awt.BorderLayout.CENTER);

        pnlTitleExport.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        pnlTitleExport.setLayout(new java.awt.GridBagLayout());

        lblExportTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblExportTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/pack_empty_co.gif"))); // NOI18N
        lblExportTitle.setText("Ausgabe");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 10);
        pnlTitleExport.add(lblExportTitle, gridBagConstraints);

        cmdDeleteLogs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/delete.png"))); // NOI18N
        cmdDeleteLogs.setToolTipText("Ausgabefenster l\u00F6schen");
        cmdDeleteLogs.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmdDeleteLogs.setContentAreaFilled(false);
        cmdDeleteLogs.setDefaultCapable(false);
        cmdDeleteLogs.setFocusPainted(false);
        cmdDeleteLogs.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/delete.png"))); // NOI18N
        cmdDeleteLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteLogsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlTitleExport.add(cmdDeleteLogs, gridBagConstraints);

        pnlLogging.add(pnlTitleExport, java.awt.BorderLayout.NORTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlMain.add(pnlLogging, gridBagConstraints);

        gplTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gplTitle.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        gplTitle.setLayout(new java.awt.GridBagLayout());

        lblName.setFont(new java.awt.Font("Courier New", 1, 24));
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblName.setText("export");
        lblName.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 9, 0, 0);
        gplTitle.add(lblName, gridBagConstraints);

        lblIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/32/default.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        gplTitle.add(lblIcon, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlMain.add(gplTitle, gridBagConstraints);

        jPanel2.add(pnlMain, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdEditActionPerformed
    {//GEN-HEADEREND:event_cmdEditActionPerformed
        
        if (hsc.fileEditor == null) {
            hsc.fileEditor = new FileEditor();
        }
        hsc.fileEditor.setFile(HeadlessServerConsole.cidsServerConfigFile);
        if (!hsc.fileEditor.isShowing()) {
            hsc.fileEditor.setVisible(true);
        }
        
    }//GEN-LAST:event_cmdEditActionPerformed
    
    private void cmdHelpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdHelpActionPerformed
    {//GEN-HEADEREND:event_cmdHelpActionPerformed
        
        showHelp(HeadlessServerConsole.MAIN_HELP_FILE);
        
    }//GEN-LAST:event_cmdHelpActionPerformed
    
    private void cmdSaveLogsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdSaveLogsActionPerformed
    {//GEN-HEADEREND:event_cmdSaveLogsActionPerformed
        
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmdSaveLogsActionPerformed
    
    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
        
    }//GEN-LAST:event_formWindowIconified
    
    private void cmdTrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTrayActionPerformed
        
        this.setVisible(false);
        
    }//GEN-LAST:event_cmdTrayActionPerformed
    
    private void cmdDeleteLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteLogsActionPerformed
        
        this.getTxtLog().setText("");
        
    }//GEN-LAST:event_cmdDeleteLogsActionPerformed
    
    private void cmdInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInfoActionPerformed
        
        if (hsc.serverRunnin) {
            hsc.displayServerStatus();
        }
        
    }//GEN-LAST:event_cmdInfoActionPerformed
    
    private void cmdRestartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRestartActionPerformed
        
        if (hsc.serverRunnin) {
            shutdownServer();
        }
        if (!hsc.serverRunnin) {
            startServer();
        }
        
    }//GEN-LAST:event_cmdRestartActionPerformed
    
    private void cmdShutdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShutdownActionPerformed
        
        if (hsc.serverRunnin) {
            shutdownServer();
        }
        
    }//GEN-LAST:event_cmdShutdownActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        
        if (hsc.serverRunnin) {
            int answer=JOptionPane.showConfirmDialog(this,java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources").getString("Warnung_cids_Server_laeuft_noch"),"cids ServerConsole",JOptionPane.YES_NO_OPTION);
            if (answer==JOptionPane.YES_OPTION) {
                shutdownServer();
                if (HeadlessServerConsole.startMiniatureServer) {
                    shutdownMiniatureServer();
                }
                System.exit(0);
            } else {
                this.show();
            }
        } else {
            if (HeadlessServerConsole.startMiniatureServer) {
                shutdownMiniatureServer();
            }
            System.exit(0);
        }
        
    }//GEN-LAST:event_exitForm
    

    public void startServer() {        
        hsc.startServer();
    } 
    
    public void startMiniatureServer(){
        hsc.startMiniatureServer();
    }
    
    public void serverStopped(){
        hsc.serverStopped();        
    }
   
    public void shutdownServer(){
        hsc.shutdownServer();        
    }
    
    public void shutdownMiniatureServer(){
        hsc.shutdownMiniatureServer();
        
    }
    
        /**
     * Exits the application.
     * This method can be used by the ServerManager servlet to exit the server
     * management. It shuts down the Miniature Web Server and the cids server if
     * it's still running before exiting the ServerConsole.
     *
     */
    public void exit() {
        if (hsc.serverRunnin) {
            shutdownServer();
        }
        if (HeadlessServerConsole.startMiniatureServer) {
            shutdownMiniatureServer();
        }
        System.exit(0);
    }
    
    
    private void refreshInfo() {
        String online="-";
        String withoutErrors="-";
        if (hsc.serverRunnin && hsc.serverStartTime!=-1) {
            long seconds=System.currentTimeMillis()-hsc.serverStartTime;
            online= HeadlessServerConsole.getDuration(seconds);
        }
        
        if (hsc.serverRunnin && HeadlessServerConsole.mySysErr.getLastInputTime()!=-1) {
            long seconds=System.currentTimeMillis()-HeadlessServerConsole.mySysErr.getLastInputTime();
            withoutErrors=HeadlessServerConsole.getDuration(seconds);
        } else {
            withoutErrors=online;
        }
        DefaultTableModel model=((DefaultTableModel)this.getTblInfo().getModel());
        
        model.setValueAt(online, 0, 1);
        model.setValueAt(withoutErrors, 1, 1);
        model.fireTableDataChanged();
    }

/**
     * Opens a JOptionPane with a help HTML file.
     *
     */
    public void showHelp(int helpfile) {
        
        java.net.URL hilfeHTML = null;
        JEditorPane hilfeEditorPane = new JEditorPane();
        hilfeEditorPane.setEditable(false);
        JPanel hilfePanel = new JPanel(new BorderLayout());
        
        if (helpfile == HeadlessServerConsole.START_HELP_FILE) {
            hilfeHTML = this.getClass().getResource("/de/cismet/cids/admin/serverManagement/help/serverConsoleHelpStart.html");
        } else if (helpfile == HeadlessServerConsole.MAIN_HELP_FILE) {
            hilfeHTML = this.getClass().getResource("/de/cismet/cids/admin/serverManagement/help/serverConsoleHelpMain.html");
        }
        
        if (hilfeHTML != null) {
            try {
                hilfeEditorPane.setPage(hilfeHTML);
            } catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + hilfeHTML);
            }
        } else {
            System.err.println("Die Hilfedatei konnte nicht gefunden werden.");
        }
        
        hilfeEditorPane.setBackground(new Color(172, 210, 248));
        hilfeEditorPane.setOpaque(true);
        
        JScrollPane hilfeScrollPane = new JScrollPane(hilfeEditorPane);
        
        hilfePanel = new JPanel(new BorderLayout());
        hilfePanel.setPreferredSize(new Dimension(524,471));
        hilfePanel.add(hilfeScrollPane, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(this, hilfePanel, "cids ServerConsole - Kurzanleitung", JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Displays the server's status with a JOptionPane.
     *
     */
    
    
    
    
    
    
 // <editor-fold defaultstate="collapsed" desc=" Generated Code "> 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdDeleteLogs;
    private javax.swing.JButton cmdHelp;
    private javax.swing.JButton cmdInfo;
    private javax.swing.JButton cmdQueue;
    private javax.swing.JButton cmdRestart;
    private javax.swing.JButton cmdSaveLogs;
    private javax.swing.JButton cmdShutdown;
    private javax.swing.JButton cmdTray;
    private javax.swing.JButton cmdedit;
    private de.cismet.cids.tools.gui.farnsworth.GradientJPanel gplTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAmpel;
    private javax.swing.JLabel lblExportTitle;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitleControl;
    private de.cismet.cids.tools.gui.farnsworth.GradientJPanel pnlControl;
    private javax.swing.JPanel pnlControlAndInfo;
    private javax.swing.JPanel pnlInformation;
    private javax.swing.JPanel pnlLogging;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlStatus;
    private javax.swing.JPanel pnlTable;
    private de.cismet.cids.tools.gui.farnsworth.GradientJPanel pnlTitleExport;
    private javax.swing.JProgressBar prbStatus;
    private javax.swing.JScrollPane spnTable;
    private javax.swing.JTable tblInfo;
    private javax.swing.JTextPane txtLog;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
    
    
    /**
     * Initializes the System Tray menu.
     *
     */
    public void initSysTray(String serverClassName, String serverType) {
        if(serverClassName != null && serverType != null && getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/16/"+serverClassName+".ico") != null) {
//            stIcon=new SysTrayMenuIcon(getClass().getResource("/de/cismet/cids/admin/serverConsole/serverSymbols/16/"+serverClassName+".ico"));
//            stIcon.addSysTrayMenuListener(this);
//            stMenu = new SysTrayMenu(stIcon,serverType);
        } else {
            System.err.println("System tray konnte nicht initialisiert werden! ");
            HeadlessServerConsole.logger.debug("System tray konnte nicht initialisiert werden!");
            cmdTray.setEnabled(false);
        }
    }
    
    
//    
//    @Override   
//    public void menuItemSelected(SysTrayMenuEvent arg0) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void iconLeftClicked(SysTrayMenuEvent arg0) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void iconLeftDoubleClicked(SysTrayMenuEvent arg0) {
//        this.setVisible(true);
////        throw new UnsupportedOperationException("Not supported yet.");
//    }   
    
    public static void main(String args[]) {
        
        try {
            PlasticLookAndFeel.setPlasticTheme(new SkyBluer());
            javax.swing.UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        final ServerConsoleGui conGui = new ServerConsoleGui(args);       
        
        conGui.setSize(550,550);
        conGui.setLocationRelativeTo(null);
        conGui.show();
        
        try {
            if (HeadlessServerConsole.startMiniatureServer) {
                conGui.startMiniatureServer();
            }
        } catch (Throwable e) {
            System.out.println("Miniature Web Server konnte nicht gestartet werden!");
            e.printStackTrace();
        }       
        conGui.startServer();
        
     
        
        Runnable refreshingGui=new Runnable() {
            public void run() {
                while (true) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            conGui.refreshInfo();
                        }
                    });
                    
                    try {
                        java.lang.Thread.sleep(1000);
                    } catch (java.lang.InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        };        
        new Thread(refreshingGui , "Refresh GUI").start();
        
    }

    protected de.cismet.cids.tools.gui.farnsworth.GradientJPanel getGplTitle() {
        return gplTitle;
    }

    protected void setGplTitle(de.cismet.cids.tools.gui.farnsworth.GradientJPanel gplTitle) {
        this.gplTitle = gplTitle;
    }

    protected javax.swing.JPanel getJPanel1() {
        return jPanel1;
    }

    protected void setJPanel1(javax.swing.JPanel jPanel1) {
        this.jPanel1 = jPanel1;
    }

    protected javax.swing.JPanel getJPanel2() {
        return jPanel2;
    }

    protected void setJPanel2(javax.swing.JPanel jPanel2) {
        this.jPanel2 = jPanel2;
    }

    protected javax.swing.JLabel getLblAmpel() {
        return lblAmpel;
    }

    protected void setLblAmpel(javax.swing.JLabel lblAmpel) {
        this.lblAmpel = lblAmpel;
    }

    protected javax.swing.JLabel getLblExportTitle() {
        return lblExportTitle;
    }

    protected void setLblExportTitle(javax.swing.JLabel lblExportTitle) {
        this.lblExportTitle = lblExportTitle;
    }

    protected javax.swing.JLabel getLblIcon() {
        return lblIcon;
    }

    protected void setLblIcon(javax.swing.JLabel lblIcon) {
        this.lblIcon = lblIcon;
    }

    protected javax.swing.JLabel getLblName() {
        return lblName;
    }

    protected void setLblName(javax.swing.JLabel lblName) {
        this.lblName = lblName;
    }

    protected javax.swing.JLabel getLblStatus() {
        return lblStatus;
    }

    protected void setLblStatus(javax.swing.JLabel lblStatus) {
        this.lblStatus = lblStatus;
    }

    protected javax.swing.JLabel getLblTitleControl() {
        return lblTitleControl;
    }

    protected void setLblTitleControl(javax.swing.JLabel lblTitleControl) {
        this.lblTitleControl = lblTitleControl;
    }

    protected de.cismet.cids.tools.gui.farnsworth.GradientJPanel getPnlControl() {
        return pnlControl;
    }

    protected void setPnlControl(de.cismet.cids.tools.gui.farnsworth.GradientJPanel pnlControl) {
        this.pnlControl = pnlControl;
    }

    protected javax.swing.JPanel getPnlControlAndInfo() {
        return pnlControlAndInfo;
    }

    protected void setPnlControlAndInfo(javax.swing.JPanel pnlControlAndInfo) {
        this.pnlControlAndInfo = pnlControlAndInfo;
    }

    protected javax.swing.JPanel getPnlInformation() {
        return pnlInformation;
    }

    protected void setPnlInformation(javax.swing.JPanel pnlInformation) {
        this.pnlInformation = pnlInformation;
    }

    protected javax.swing.JPanel getPnlLogging() {
        return pnlLogging;
    }

    protected void setPnlLogging(javax.swing.JPanel pnlLogging) {
        this.pnlLogging = pnlLogging;
    }

    protected javax.swing.JPanel getPnlMain() {
        return pnlMain;
    }

    protected void setPnlMain(javax.swing.JPanel pnlMain) {
        this.pnlMain = pnlMain;
    }

    protected javax.swing.JPanel getPnlStatus() {
        return pnlStatus;
    }

    protected void setPnlStatus(javax.swing.JPanel pnlStatus) {
        this.pnlStatus = pnlStatus;
    }

    protected javax.swing.JPanel getPnlTable() {
        return pnlTable;
    }

    protected void setPnlTable(javax.swing.JPanel pnlTable) {
        this.pnlTable = pnlTable;
    }

    protected de.cismet.cids.tools.gui.farnsworth.GradientJPanel getPnlTitleExport() {
        return pnlTitleExport;
    }

    protected void setPnlTitleExport(de.cismet.cids.tools.gui.farnsworth.GradientJPanel pnlTitleExport) {
        this.pnlTitleExport = pnlTitleExport;
    }

    protected javax.swing.JProgressBar getPrbStatus() {
        return prbStatus;
    }

    protected void setPrbStatus(javax.swing.JProgressBar prbStatus) {
        this.prbStatus = prbStatus;
    }

    protected javax.swing.JScrollPane getSpnTable() {
        return spnTable;
    }

    protected void setSpnTable(javax.swing.JScrollPane spnTable) {
        this.spnTable = spnTable;
    }

    protected javax.swing.JTable getTblInfo() {
        return tblInfo;
    }

    protected void setTblInfo(javax.swing.JTable tblInfo) {
        this.tblInfo = tblInfo;
    }

    protected javax.swing.JTextPane getTxtLog() {
        return txtLog;
    }

    protected void setTxtLog(javax.swing.JTextPane txtLog) {
        this.txtLog = txtLog;
    }

    protected ImageIcon getRed() {
        return red;
    }

    protected ImageIcon getYellow() {
        return yellow;
    }

    protected ImageIcon getGreen() {
        return green;
    }

    public javax.swing.JButton getCmdTray() {
        return cmdTray;
    }

//    protected SysTrayMenuIcon getStIcon() {
//        return stIcon;
//    }
//
//    protected SysTrayMenu getStMenu() {
//        return stMenu;
//    }
//    
    
    
    
}

