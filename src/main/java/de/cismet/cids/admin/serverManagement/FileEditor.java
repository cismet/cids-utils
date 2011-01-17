/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * FileEditor.java
 *
 * Created on 6. August 2004, 12:50
 */
package de.cismet.cids.admin.serverManagement;

import org.apache.log4j.*;
import org.apache.log4j.Logger;

import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * The class FileEditor creates a GUI to edit a text file.
 *
 * @author   oaltpeter
 * @version  $Revision$, $Date$
 */
public class FileEditor extends javax.swing.JFrame {

    //~ Instance fields --------------------------------------------------------

    Logger logger = Logger.getLogger(this.getClass());
    private File file;
    private String textToSave = null;
    private boolean textChanged = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdSave;
    private de.cismet.cids.tools.gui.farnsworth.GradientJPanel gplTitle;
    private javax.swing.JTextArea jtaEditing;
    private javax.swing.JLabel lblExportTitle;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblName;
    private de.cismet.cids.tools.gui.farnsworth.GradientJPanel pnlEdit;
    private javax.swing.JPanel pnlEditing;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JScrollPane spnTable;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form FileEditor.
     */
    public FileEditor() {
        initComponents();

        try {
            this.setIconImage(new javax.swing.ImageIcon(
                    getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/edit.png")).getImage());
        } catch (Exception e) {
            logger.error("Icon in Titelleiste konnte nicht gesetzt werden.", e);
        }

        this.setSize(550, 550);
        this.setLocationRelativeTo(null);
        this.setLocation(this.getLocation().x + 20, this.getLocation().y - 20);
        if (file != null) {
            this.setTitle("FileEditor - " + file.getName());
        }
        this.show();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Sets the file to edit in the JTextArea. If the file is not null its name will be displayed on the JLabel over the
     * JTextArea and the file's content will be shown on the JTextArea.
     *
     * <p>To warn the user from unsaved changes by leaving the program a DocumentListener is added to the JTextArea.</p>
     *
     * @param  file  the file to edit
     */
    public void setFile(final File file) {
        this.file = file;

        if (file != null) {
            lblName.setText(file.getPath());

            jtaEditing.setText(readFile());

            jtaEditing.getDocument().addDocumentListener(new JtaDocumentListener());
        }
    }

    /**
     * Reads in the file that is set in the FileEditor object and returns the content as a String.
     *
     * @return  the file's content
     */
    public String readFile() {
        String fileContent = "";
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                fileContent += (line + "\n");
            }
        } catch (Exception e) {
            fileContent = "Keine Konfigurationsdatei gefunden.";
            logger.error("Problem beim Laden der Konfigurationsdatei des cids Servers.", e);
        }
        return fileContent;
    }

    /**
     * Saves the file that is set in the FileEditor object.
     */
    public void saveFile() {
        final String textToSave = jtaEditing.getText();
        try {
            if ((file != null) || file.exists() || file.canRead()) {
                final FileWriter f = new FileWriter(file);
                f.write(textToSave);
                f.close();
                textChanged = false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources").getString(
                    "Die_Aenderungen_konnten__nicht_gespeichert_werden"),
                "FileEditor",
                JOptionPane.ERROR_MESSAGE);
            logger.error("Fehler beim Speichern der Datei " + file + ".", e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    private void initComponents() //GEN-BEGIN:initComponents
    {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlEditing = new javax.swing.JPanel();
        pnlTable = new javax.swing.JPanel();
        spnTable = new javax.swing.JScrollPane();

        jtaEditing = new javax.swing.JTextArea();

        pnlEdit = new de.cismet.cids.tools.gui.farnsworth.GradientJPanel();
        lblExportTitle = new javax.swing.JLabel();
        cmdSave = new javax.swing.JButton();
        gplTitle = new de.cismet.cids.tools.gui.farnsworth.GradientJPanel();
        lblName = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("FileEditor");
        addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(final java.awt.event.WindowEvent evt) {
                    exitForm(evt);
                }
            });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlEditing.setLayout(new java.awt.BorderLayout());

        pnlEditing.setBorder(new javax.swing.border.CompoundBorder(
                null,
                new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED)));
        pnlEditing.setMinimumSize(new java.awt.Dimension(96, 96));
        pnlEditing.setPreferredSize(new java.awt.Dimension(96, 56));
        pnlTable.setLayout(new java.awt.BorderLayout());

        pnlTable.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        pnlTable.setMinimumSize(new java.awt.Dimension(24, 25));
        pnlTable.setPreferredSize(new java.awt.Dimension(11, 25));
        spnTable.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        spnTable.setForeground(new java.awt.Color(51, 51, 51));
        spnTable.setMinimumSize(new java.awt.Dimension(40, 40));
        spnTable.setPreferredSize(new java.awt.Dimension(40, 40));
        jtaEditing.setColumns(30);
        jtaEditing.setLineWrap(true);
        jtaEditing.setRows(15);
        jtaEditing.setMinimumSize(new java.awt.Dimension(10, 10));
        jtaEditing.setPreferredSize(new java.awt.Dimension(240, 350));
        spnTable.setViewportView(jtaEditing);

        pnlTable.add(spnTable, java.awt.BorderLayout.CENTER);

        pnlEditing.add(pnlTable, java.awt.BorderLayout.CENTER);

        pnlEdit.setLayout(new java.awt.GridBagLayout());

        pnlEdit.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        lblExportTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblExportTitle.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/res/pack_empty_co.gif")));
        lblExportTitle.setText("Editieren");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 10);
        pnlEdit.add(lblExportTitle, gridBagConstraints);

        cmdSave.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/save.png")));
        cmdSave.setToolTipText("Speichern");
        cmdSave.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        cmdSave.setContentAreaFilled(false);
        cmdSave.setDefaultCapable(false);
        cmdSave.setFocusPainted(false);
        cmdSave.setPreferredSize(new java.awt.Dimension(16, 16));
        cmdSave.setPressedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/contrast/save.png")));
        cmdSave.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cmdSaveActionPerformed(evt);
                }
            });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEdit.add(cmdSave, gridBagConstraints);

        pnlEditing.add(pnlEdit, java.awt.BorderLayout.NORTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 0, 5);
        pnlMain.add(pnlEditing, gridBagConstraints);

        gplTitle.setLayout(new java.awt.GridBagLayout());

        gplTitle.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        gplTitle.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        lblName.setFont(new java.awt.Font("Courier New", 1, 12));
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblName.setText("Keine Konfigurationsdatei gefunden.");
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

        lblIcon.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/cids/admin/serverConsole/buttons/configfile.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 0);
        gplTitle.add(lblIcon, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlMain.add(gplTitle, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

        pack();
    } //GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cmdSaveActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_cmdSaveActionPerformed
    {                                                                         //GEN-HEADEREND:event_cmdSaveActionPerformed

        saveFile();
    } //GEN-LAST:event_cmdSaveActionPerformed

    /**
     * Exit the Application.
     *
     * @param  evt  DOCUMENT ME!
     */
    private void exitForm(final java.awt.event.WindowEvent evt) //GEN-FIRST:event_exitForm
    {
        if (!textChanged) {
            this.setVisible(false);
            this.dispose();
        } else {
            final int answer = JOptionPane.showConfirmDialog(
                    this,
                    java.util.ResourceBundle.getBundle("de/cismet/cids/admin/serverManagement/resources").getString(
                        "Wollen_Sie_die_Aenderungen_speichern"),
                    "FileEditor",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                saveFile();
                this.setVisible(false);
                this.dispose();
            } else if (answer == JOptionPane.NO_OPTION) {
                this.setVisible(false);
                this.dispose();
            } else if (answer == JOptionPane.CANCEL_OPTION) {
                this.show();
            }
        }
    }                                                           //GEN-LAST:event_exitForm

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        /*FileEditor fileEditor = new FileEditor();
         * fileEditor.setSize(550,550); fileEditor.setLocationRelativeTo(null);
         * fileEditor.setLocation(fileEditor.getLocation().x+20, fileEditor.getLocation().y-20);fileEditor.show();*/
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class JtaDocumentListener implements DocumentListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void insertUpdate(final DocumentEvent e) {
            textChanged = true;
        }
        @Override
        public void removeUpdate(final DocumentEvent e) {
            textChanged = true;
        }
        @Override
        public void changedUpdate(final DocumentEvent e) {
        }
    }
}
