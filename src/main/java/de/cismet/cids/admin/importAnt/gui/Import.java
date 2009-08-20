/*
 * Import.java
 *
 * Created on 13. November 2003, 15:04
 *
 */

package de.cismet.cids.admin.importAnt.gui;
import com.jgoodies.looks.BorderStyle;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBluer;
import javax.swing.*;
import java.io.*;
import java.util.*;
import de.cismet.cids.admin.importAnt.castorGenerated.*;
import de.cismet.cids.admin.importAnt.*;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.prefs.*;

/**
 *
 * @author  hell
 */
public class Import extends javax.swing.JFrame {
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    
    JFileChooser filechooser;
    JFileChooser importFilechooser;
    JFileChooser cidsLibDirFilechooser;
    File thisFile=null;
    Preferences prefs=null;
    String fileDialogLocation=null;
    String importFileDialogLocation=null;
    String cidsLibDirLocation=null;
    
    /** Creates new form Import */
    public Import() {
       Log4JQuickConfig.configure4LumbermillOnLocalhost();
        try {
            //ClearLookManager.setMode(ClearLookMode.ON);
            //ToDo check with thorsten
            Plastic3DLookAndFeel.setCurrentTheme(new SkyBluer());
            javax.swing.UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
            //javax.swing.UIManager.setLookAndFeel(new PlasticLookAndFeel());
        } catch (Exception e) {
            log.error("Fehler beim Umschalten des Look&Feels",e);
        }
        
        
        
        
        
        //Auslesen der Registry oder einem anderen BackingStore
        prefs=Preferences.userNodeForPackage(this.getClass());
        
        fileDialogLocation=prefs.get("fileDialogLocation",null);
        importFileDialogLocation=prefs.get("importFileDialogLocation",null);
        cidsLibDirLocation=prefs.get("cidsLibDirLocation",null);
        
        filechooser=new JFileChooser(fileDialogLocation);
        importFilechooser = new JFileChooser(importFileDialogLocation);
        cidsLibDirFilechooser=new JFileChooser(cidsLibDirLocation);
        cidsLibDirFilechooser.setDialogTitle("W\u00E4hlen Sie die cids Installation aus, mit der der Batchimport durchgef\u00FChrt werden soll.");
        cidsLibDirFilechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        
        initComponents();
        setTitle("JPresso");
        if (cidsLibDirLocation==null) {
            this.mniSaveBatchFile.setEnabled(false);
        }
        
        
        
        mnuMain.putClientProperty(com.jgoodies.looks.Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        this.tbaTools.putClientProperty(com.jgoodies.looks.Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        mnuMain.putClientProperty(com.jgoodies.looks.Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
        mnuMain.putClientProperty(PlasticLookAndFeel.BORDER_STYLE_KEY, BorderStyle.SEPARATOR);
        newFile();
        
        KeyStroke configLoggerKeyStroke = KeyStroke.getKeyStroke('L',InputEvent.CTRL_MASK);
        Action configAction = new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        Log4JQuickConfig.getSingletonInstance().setVisible(true);
                    }
                });
            }
        };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(configLoggerKeyStroke, "CONFIGLOGGING");
        getRootPane().getActionMap().put("CONFIGLOGGING", configAction);
        
        
        
    }
    
    private boolean somethingChangedReminder() {
        return true;
    }
    
    
    private void newFile() {
        if (somethingChangedReminder()) {
            thisFile=null;
            myConnectionEditor.setContent(null);
            myMappingEditor.setContent(null);
            myRelationsEditor.setContent(null);
            myCodeEditor.setContent(null,null);
            myOptionsEditor.setContent(null,null);
        }
    }
    
    public void setSourceFields(String[] s) {
        this.myMappingEditor.setSourceFieldNames(s);
    }
    
    private void openFile() {
        if (somethingChangedReminder()) {
            if (filechooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
                prbStatus.setIndeterminate(true);
                File file = filechooser.getSelectedFile();
                thisFile=file;
                FileReader r=null;
                try {
                    r=new FileReader(file);
                } catch (java.io.FileNotFoundException fnfEx) {
                    log.error("Fehler beim \u00D6ffnen der Datei.",fnfEx);
                    JOptionPane.showMessageDialog(this,"Fehler beim \u00D6ffnen der Datei.","Fehler",JOptionPane.ERROR_MESSAGE);
                    this.setTitle("cidsImportAnt");
                    prbStatus.setIndeterminate(false);
                    return;
                }
                ImportRules impRules=new ImportRules();
                this.setTitle("cidsImportAnt :: "+file.getAbsolutePath());
                
                //FileReader r=new FileReader("C:\\importTest.xml");
                // aus dem XML File die entsprechende Datenstruktur machen (CASTOR)
                try {
                    impRules=ImportRules.unmarshal(r);
                    makeTablesUpperCase(impRules);
                } catch ( Exception mEx) {
                    log.error("Syntax Fehler beim \u00D6ffnen der Datei.",mEx);
                    JOptionPane.showMessageDialog(this,"Syntax Fehler beim \u00D6ffnen der Datei.\n("+mEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
                    prbStatus.setIndeterminate(false);
                    this.setTitle("cidsImportAnt");
                    return;
                }
                
                //setzen der Werte
                myConnectionEditor.setContent(impRules.getConnectionInfo());
                myMappingEditor.setContent(impRules.getPreProcessingAndMapping());
                myRelationsEditor.setContent(impRules.getRelations());
                myOptionsEditor.setTables(this.getAllUsedTargetTables());
                myOptionsEditor.setContent(impRules.getOptions(),impRules.getRuntimeProps());
                
                myCodeEditor.setContent(impRules.getCode(),impRules.getRuntimeProps());
//                impRules.getRelations();
//                impRules.getOptions();
//                impRules.getCode();
                prbStatus.setIndeterminate(false);
            }
        }
    }
    
    private void importDatasource() {
        if (importFilechooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            prbStatus.setIndeterminate(true);
            File file = importFilechooser.getSelectedFile();
            FileReader r=null;
            try {
                r=new FileReader(file);
            } catch (java.io.FileNotFoundException fnfEx) {
                log.error("Fehler beim \u00D6ffnen der Datei.",fnfEx);
                JOptionPane.showMessageDialog(this,"Fehler beim \u00D6ffnen der Datei.","Fehler",JOptionPane.ERROR_MESSAGE);
                return;
            }
            ImportRules impRules=new ImportRules();
            //FileReader r=new FileReader("C:\\importTest.xml");
            // aus dem XML File die entsprechende Datenstruktur machen (CASTOR)
            try {
                impRules=ImportRules.unmarshal(r);
            } catch ( Exception mEx) {
                log.error("Syntax Fehler beim \u00D6ffnen der Datei.",mEx);
                JOptionPane.showMessageDialog(this,"Syntax Fehler beim \u00D6ffnen der Datei.\n("+mEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
                prbStatus.setIndeterminate(false);
                return;
            }
            
            //setzen der Werte
            ConnectionInfo ci=impRules.getConnectionInfo();
            ci.setTargetJdbcConnectionInfo(null);
            myConnectionEditor.setContent(ci);
            prbStatus.setIndeterminate(false);
        }
    }
    
    private void importTargetSystem() {
        if (importFilechooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            prbStatus.setIndeterminate(true);
            File file = importFilechooser.getSelectedFile();
            FileReader r=null;
            try {
                r=new FileReader(file);
            } catch (java.io.FileNotFoundException fnfEx) {
                log.error("Fehler beim \u00D6ffnen der Datei.",fnfEx);
                JOptionPane.showMessageDialog(this,"Fehler beim \u00D6ffnen der Datei.","Fehler",JOptionPane.ERROR_MESSAGE);
                return;
            }
            ImportRules impRules=new ImportRules();
            //FileReader r=new FileReader("C:\\importTest.xml");
            // aus dem XML File die entsprechende Datenstruktur machen (CASTOR)
            try {
                impRules=ImportRules.unmarshal(r);
            } catch ( Exception mEx) {
                log.error("Syntax Fehler beim \u00D6ffnen der Datei.",mEx);
                JOptionPane.showMessageDialog(this,"Syntax Fehler beim \u00D6ffnen der Datei.\n("+mEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
                prbStatus.setIndeterminate(false);
                return;
            }
            
            //setzen der Werte
            ConnectionInfo ci=impRules.getConnectionInfo();
            ci.setSourceJdbcConnectionInfo(null);
            myConnectionEditor.setContent(ci);
            prbStatus.setIndeterminate(false);
        }
        
    }
    
    private void importCode() {
        if (importFilechooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            prbStatus.setIndeterminate(true);
            File file = importFilechooser.getSelectedFile();
            FileReader r=null;
            try {
                r=new FileReader(file);
            } catch (java.io.FileNotFoundException fnfEx) {
                log.error("Fehler beim \u00D6ffnen der Datei.",fnfEx);
                JOptionPane.showMessageDialog(this,"Fehler beim \u00D6ffnen der Datei.","Fehler",JOptionPane.ERROR_MESSAGE);
                return;
            }
            ImportRules impRules=new ImportRules();
            //FileReader r=new FileReader("C:\\importTest.xml");
            // aus dem XML File die entsprechende Datenstruktur machen (CASTOR)
            try {
                impRules=ImportRules.unmarshal(r);
            } catch ( Exception mEx) {
                log.error("Syntax Fehler beim \u00D6ffnen der Datei.",mEx);
                JOptionPane.showMessageDialog(this,"Syntax Fehler beim \u00D6ffnen der Datei.\n("+mEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
                prbStatus.setIndeterminate(false);
                return;
            }
            
            //setzen der Werte
            myCodeEditor.setContent(impRules.getCode(),impRules.getRuntimeProps());
            prbStatus.setIndeterminate(false);
        }
    }
    
    
    
    private void exportDatasource() {
        File givenFile=null;
        File file=givenFile;
        if (givenFile==null) {
            if (importFilechooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) {
                return;
            }
            file = importFilechooser.getSelectedFile();
            if (file.exists()) {
                if (JOptionPane.showConfirmDialog(this,"Datei existiert schon. Wollen Sie sie ersetzen?","Speichern",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }
        
        prbStatus.setIndeterminate(true);
        ImportRules impRules=new ImportRules();
        ConnectionInfo ci=new ConnectionInfo();
        ci.setSourceJdbcConnectionInfo(getImportRules().getConnectionInfo().getSourceJdbcConnectionInfo());
        impRules.setConnectionInfo(ci);
        log.debug(impRules);
        // ...
        FileWriter w=null;
        try {
            w=new FileWriter(file);
        } catch (java.io.IOException ioEx) {
            log.error("Datei kann nicht geschrieben werden.",ioEx);
            JOptionPane.showMessageDialog(this,"Datei kann nicht geschrieben werden.","Fehler",JOptionPane.ERROR_MESSAGE);
            prbStatus.setIndeterminate(false);
            return;
        }
        
        try {
            impRules.marshal(w);
            w.close();
        } catch (Exception mEx) {
            log.error("Fehler beim Umwandeln nach XML.",mEx);
            JOptionPane.showMessageDialog(this,"Fehler beim Umwandeln nach XML.\n("+mEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            prbStatus.setIndeterminate(false);
            return;
        }
        prbStatus.setIndeterminate(false);
        
    }
    
    private void exportTargetSystem() {
        File givenFile=null;
        File file=givenFile;
        if (givenFile==null) {
            if (importFilechooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) {
                return;
            }
            file = importFilechooser.getSelectedFile();
            if (file.exists()) {
                if (JOptionPane.showConfirmDialog(this,"Datei existiert schon. Wollen Sie sie ersetzen?","Speichern",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }
        
        prbStatus.setIndeterminate(true);
        ImportRules impRules=new ImportRules();
        ConnectionInfo ci=new ConnectionInfo();
        ci.setTargetJdbcConnectionInfo(getImportRules().getConnectionInfo().getTargetJdbcConnectionInfo());
        impRules.setConnectionInfo(ci);
        log.debug(impRules);
        // ...
        FileWriter w=null;
        try {
            w=new FileWriter(file);
        } catch (java.io.IOException ioEx) {
            log.error("Datei kann nicht geschrieben werden.",ioEx);
            JOptionPane.showMessageDialog(this,"Datei kann nicht geschrieben werden.","Fehler",JOptionPane.ERROR_MESSAGE);
            prbStatus.setIndeterminate(false);
            return;
        }
        
        try {
            impRules.marshal(w);
            w.close();
        } catch (Exception mEx) {
            log.error("Fehler beim Umwandeln nach XML.",mEx);
            JOptionPane.showMessageDialog(this,"Fehler beim Umwandeln nach XML.\n("+mEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            prbStatus.setIndeterminate(false);
            return;
        }
        prbStatus.setIndeterminate(false);
        
        
    }
    
    private void exportCode() {
        File givenFile=null;
        File file=givenFile;
        if (givenFile==null) {
            if (importFilechooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) {
                return;
            }
            file = importFilechooser.getSelectedFile();
            if (file.exists()) {
                if (JOptionPane.showConfirmDialog(this,"Datei existiert schon. Wollen Sie sie ersetzen?","Speichern",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }
        
        prbStatus.setIndeterminate(true);
        ImportRules impRules=new ImportRules();
        if (myCodeEditor.getContent().getFunction().length!=0) {
            impRules.setCode(myCodeEditor.getContent());
        }
        log.debug(impRules);
        // ...
        FileWriter w=null;
        try {
            w=new FileWriter(file);
            
        } catch (java.io.IOException ioEx) {
            log.error("Datei kann nicht geschrieben werden.",ioEx);
            JOptionPane.showMessageDialog(this,"Datei kann nicht geschrieben werden.","Fehler",JOptionPane.ERROR_MESSAGE);
            prbStatus.setIndeterminate(false);
            return;
        }
        
        try {
            impRules.marshal(w);
            w.close();
        } catch (Exception mEx) {
            log.error("Fehler beim Umwandeln nach XML.",mEx);
            JOptionPane.showMessageDialog(this,"Fehler beim Umwandeln nach XML.\n("+mEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            prbStatus.setIndeterminate(false);
            return;
        }
        prbStatus.setIndeterminate(false);
        
    }
    
    private void saveBatchFile() {
        String batchFile=thisFile.getAbsolutePath();
        batchFile=batchFile+".cmd";
        File file=new File(batchFile);
        
        FileWriter w=null;
        try {
            w=new FileWriter(file);
            w.write("cd "+cidsLibDirLocation+"\\utils\n");
            w.write("call "+cidsLibDirLocation+"\\utils\\BatchImport -x -m \""+thisFile.getAbsolutePath()+"\" >> \""+thisFile.getAbsolutePath()+".log\" \n");
            //w.write("pause\n");
            w.flush();
            w.close();
        } catch (IOException ioex) {
            
        }
    }
    
    
    private ImportRules getImportRules() {
        ImportRules impRules=new ImportRules();
        impRules.setConnectionInfo(myConnectionEditor.getContent());
        impRules.setPreProcessingAndMapping(myMappingEditor.getContent());
        if (myRelationsEditor.getContent().getRelation().length!=0) {
            impRules.setRelations(myRelationsEditor.getContent());
        }
        if (myOptionsEditor.getOptionsContent().getNormalize().length!=0) {
            impRules.setOptions(myOptionsEditor.getOptionsContent());
        }
        impRules.setRuntimeProps(myOptionsEditor.getRuntimePropsContent());
        if (myCodeEditor.getContent().getFunction().length!=0) {
            impRules.setCode(myCodeEditor.getContent());
        }
        return impRules;
    }
    
    private void saveFile() {
        saveFile(thisFile);
    }
    
    private void saveFile(File givenFile) {
        File file=givenFile;
        if (givenFile==null) {
            if (filechooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) {
                return;
            }
            file = filechooser.getSelectedFile();
            if (file.exists()) {
                if (JOptionPane.showConfirmDialog(this,"Datei existiert schon. Wollen Sie sie ersetzen?","Speichern",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }
        
        prbStatus.setIndeterminate(true);
        ImportRules impRules=getImportRules();
        makeTablesUpperCase(impRules);
        log.debug(impRules);
        // ...
        FileWriter w=null;
        try {
            w=new FileWriter(file);
        } catch (java.io.IOException ioEx) {
            log.error("Datei kann nicht geschrieben werden.",ioEx);
            JOptionPane.showMessageDialog(this,"Datei kann nicht geschrieben werden.","Fehler",JOptionPane.ERROR_MESSAGE);
            prbStatus.setIndeterminate(false);
            return;
        }
        
        try {
            
            impRules.marshal(w);
            w.close();
        } catch (Exception mEx) {
            log.error("Fehler beim Umwandeln nach XML.",mEx);
            JOptionPane.showMessageDialog(this,"Fehler beim Umwandeln nach XML.\n("+mEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            prbStatus.setIndeterminate(false);
            return;
        }
        this.setTitle("cidsImportAnt :: "+file.getAbsolutePath());
        prbStatus.setIndeterminate(false);
        thisFile=file;
    }
    
    private void makeTablesUpperCase(ImportRules ir) {
        PreProcessingAndMapping maps=ir.getPreProcessingAndMapping();
        Relations rel=ir.getRelations();
        de.cismet.cids.admin.importAnt.castorGenerated.Options opt=ir.getOptions();
        
        if (maps!=null) {
            Enumeration e=maps.enumerateMapping();
            while (e.hasMoreElements()) {
                Mapping m=(Mapping)e.nextElement();
                m.setTargetTable(m.getTargetTable().toUpperCase());
            }
        }
        
        if (rel!=null) {
            Enumeration e=rel.enumerateRelation();
            while (e.hasMoreElements()) {
                Relation r=(Relation)e.nextElement();
                r.setDetailTable(r.getDetailTable().toUpperCase());
                r.setMasterTable(r.getMasterTable().toUpperCase());
            }
        }
        
        if (opt!=null) {
            Enumeration e=opt.enumerateNormalize();
            while (e.hasMoreElements()) {
                String n=(String)e.nextElement();
                n=n.toUpperCase();
            }
        }
    }
    
    public void addOutput(JInternalFrame out) {
        int xpos=dpaOutput.getAllFrames().length*10;
        int ypos=dpaOutput.getAllFrames().length*10;
        double width=(int)out.getPreferredSize().getWidth();
        double height=(int)out.getPreferredSize().getHeight();
        if (width+xpos> dpaOutput.getWidth()) {
            width=dpaOutput.getWidth()*0.9-xpos;
        }
        if (height+ypos> dpaOutput.getHeight()) {
            height=dpaOutput.getHeight()*0.9-ypos;
        }
        if (width<10) {
            width=(int)out.getPreferredSize().getWidth()/2;
        }
        if (height<10) {
            height=(int)out.getPreferredSize().getHeight()/2;
        }
        dpaOutput.add(out, javax.swing.JLayeredPane.DEFAULT_LAYER);
        out.setBounds(xpos,ypos,(int)width,(int)height);
        //out.pack();
        out.setVisible(true);
        
        
//        try {out.setMaximum(true);
//        }catch (Exception e) {}
    }
    
    public void addMappings(PreProcessingAndMapping maps) {
        myMappingEditor.addMappings(maps);
    }
    public void addRelations(Relations rel) {
        myRelationsEditor.addRelations(rel);
    }
    
    
    private String[] getAllUsedTargetTables() {
        PreProcessingAndMapping m=myMappingEditor.getContent();
        HashSet hs=new HashSet();
        Relations r=myRelationsEditor.getContent();
        Enumeration e=m.enumerateMapping();
        while (e.hasMoreElements()) {
            Mapping mapping=((Mapping)(e.nextElement()));
            hs.add(mapping.getTargetTable());
        }
        
        e=r.enumerateRelation();
        while (e.hasMoreElements()) {
            Relation relation=((Relation)(e.nextElement()));
            hs.add(relation.getDetailTable());
            hs.add(relation.getMasterTable());
        }
        
        
        String[] ret=new String[hs.size()];
        int i=0;
        Iterator it=hs.iterator();
        while (it.hasNext()) {
            ret[i++]=(String)it.next();
        }
        return ret;
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panToolbar = new javax.swing.JPanel();
        tbaTools = new javax.swing.JToolBar();
        cmdNew = new javax.swing.JButton();
        cmdOpen = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        cmdPreview = new javax.swing.JButton();
        cmdRunImport = new javax.swing.JButton();
        spnMain = new javax.swing.JSplitPane();
        panEditors = new javax.swing.JPanel();
        tbpEditors = new javax.swing.JTabbedPane();
        panConnections = new javax.swing.JPanel();
        myConnectionEditor = new de.cismet.cids.admin.importAnt.gui.ConnectionEditor();
        panMappings = new javax.swing.JPanel();
        myMappingEditor = new de.cismet.cids.admin.importAnt.gui.MappingEditor();
        panRelations = new javax.swing.JPanel();
        myRelationsEditor = new de.cismet.cids.admin.importAnt.gui.RelationsEditor();
        panOptions = new javax.swing.JPanel();
        myOptionsEditor = new de.cismet.cids.admin.importAnt.gui.OptionsEditor();
        panCode = new javax.swing.JPanel();
        myCodeEditor = new de.cismet.cids.admin.importAnt.gui.CodeEditor();
        panOutput = new javax.swing.JPanel();
        gradientJPanel1 = new de.cismet.cids.tools.gui.farnsworth.GradientJPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        dpaOutput = new javax.swing.JDesktopPane();
        panStatus = new javax.swing.JPanel();
        lblStatus1 = new javax.swing.JLabel();
        prbStatus = new javax.swing.JProgressBar();
        lblStatus2 = new javax.swing.JLabel();
        mnuMain = new javax.swing.JMenuBar();
        mnuFiles = new javax.swing.JMenu();
        mniNew = new javax.swing.JMenuItem();
        mniOpen = new javax.swing.JMenuItem();
        mniSave = new javax.swing.JMenuItem();
        mniSaveAs = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        mnuImport = new javax.swing.JMenu();
        mniImportDatasource = new javax.swing.JMenuItem();
        mniImportTargetSystem = new javax.swing.JMenuItem();
        mniImportUserDefinedCode = new javax.swing.JMenuItem();
        mnuExport = new javax.swing.JMenu();
        mniExportDatasource = new javax.swing.JMenuItem();
        mniExportTargetSystem = new javax.swing.JMenuItem();
        mniExportUserDefinedCode = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        mniExit = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();
        mniGotoConnections = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        mniGotoMappings = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        mniGotoRelations = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        mniGotoOptions = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        mniGotoCode = new javax.swing.JMenuItem();
        mnuExtras = new javax.swing.JMenu();
        mnuCheckDBConnections = new javax.swing.JMenu();
        mniCheckSourceDBConnection = new javax.swing.JMenuItem();
        mniCheckSourceDBConnection1 = new javax.swing.JMenuItem();
        mniCheckTargetDBConnections = new javax.swing.JMenuItem();
        mniCheckCompleteSyntax = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JSeparator();
        mniPreview = new javax.swing.JMenuItem();
        mniRunImport = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        mniSaveBatchFile = new javax.swing.JMenuItem();
        mniChooseLibDir = new javax.swing.JMenuItem();
        mnuHelp = new javax.swing.JMenu();
        mniContents = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        mniAbout = new javax.swing.JMenuItem();

        setTitle("cids Import");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        panToolbar.setLayout(new java.awt.GridBagLayout());

        tbaTools.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
        tbaTools.setRollover(true);
        cmdNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/file_obj.gif")));
        cmdNew.setToolTipText("Neu");
        cmdNew.setFocusPainted(false);
        cmdNew.setMargin(new java.awt.Insets(2, 7, 1, 7));
        cmdNew.setRequestFocusEnabled(false);
        cmdNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNewActionPerformed(evt);
            }
        });

        tbaTools.add(cmdNew);

        cmdOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/importdir_wiz.gif")));
        cmdOpen.setToolTipText("Laden");
        cmdOpen.setMargin(new java.awt.Insets(2, 7, 1, 7));
        cmdOpen.setRequestFocusEnabled(false);
        cmdOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFile(evt);
            }
        });

        tbaTools.add(cmdOpen);

        cmdSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/save_edit.gif")));
        cmdSave.setToolTipText("Speichern");
        cmdSave.setMargin(new java.awt.Insets(2, 7, 1, 7));
        cmdSave.setRequestFocusEnabled(false);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFile(evt);
            }
        });

        tbaTools.add(cmdSave);

        cmdPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/preview.gif")));
        cmdPreview.setToolTipText("Preview");
        cmdPreview.setMargin(new java.awt.Insets(2, 7, 1, 7));
        cmdPreview.setRequestFocusEnabled(false);
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        tbaTools.add(cmdPreview);

        cmdRunImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/run_tool.gif")));
        cmdRunImport.setToolTipText("Import durchf\u00fchren");
        cmdRunImport.setMargin(new java.awt.Insets(2, 7, 1, 7));
        cmdRunImport.setRequestFocusEnabled(false);
        cmdRunImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRunImportActionPerformed(evt);
            }
        });

        tbaTools.add(cmdRunImport);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        panToolbar.add(tbaTools, gridBagConstraints);

        getContentPane().add(panToolbar, java.awt.BorderLayout.NORTH);

        spnMain.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        spnMain.setDividerLocation(300);
        spnMain.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        spnMain.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        spnMain.setOneTouchExpandable(true);
        spnMain.setAutoscrolls(true);
        panEditors.setLayout(new java.awt.BorderLayout());

        panEditors.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        panEditors.setFocusable(false);
        tbpEditors.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 0, 0, 0)));
        tbpEditors.setFocusable(false);
        tbpEditors.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbpEditorsPropertyChange(evt);
            }
        });

        panConnections.setLayout(new java.awt.GridBagLayout());

        myConnectionEditor.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panConnections.add(myConnectionEditor, gridBagConstraints);

        tbpEditors.addTab("Datenbankverbindungen", new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/defaults_ps.gif")), panConnections);

        panMappings.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMappings.add(myMappingEditor, gridBagConstraints);

        tbpEditors.addTab("Mappings", new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/cfilter.gif")), panMappings);

        panRelations.setLayout(new java.awt.GridBagLayout());

        myRelationsEditor.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        myRelationsEditor.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                myRelationsEditorComponentShown(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRelations.add(myRelationsEditor, gridBagConstraints);

        tbpEditors.addTab("Beziehungen", new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/showparents_mode.gif")), panRelations);

        panOptions.setLayout(new java.awt.GridBagLayout());

        panOptions.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                panOptionsComponentShown(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOptions.add(myOptionsEditor, gridBagConstraints);

        tbpEditors.addTab("Optionen", new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/prefs_misc.gif")), panOptions);

        panCode.setLayout(new java.awt.GridBagLayout());

        myCodeEditor.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10, 10, 10)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCode.add(myCodeEditor, gridBagConstraints);

        tbpEditors.addTab("Code", new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/java_app.gif")), panCode);

        panEditors.add(tbpEditors, java.awt.BorderLayout.CENTER);

        spnMain.setLeftComponent(panEditors);

        panOutput.setLayout(new java.awt.BorderLayout());

        panOutput.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        gradientJPanel1.setLayout(new java.awt.GridBagLayout());

        gradientJPanel1.setForeground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("Ausgabe");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        gradientJPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gradientJPanel1.add(jSeparator2, gridBagConstraints);

        panOutput.add(gradientJPanel1, java.awt.BorderLayout.NORTH);

        dpaOutput.setBackground(new java.awt.Color(204, 204, 204));
        dpaOutput.setMinimumSize(new java.awt.Dimension(20, 50));
        panOutput.add(dpaOutput, java.awt.BorderLayout.CENTER);

        spnMain.setRightComponent(panOutput);

        getContentPane().add(spnMain, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        panStatus.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(0, 5, 1, 0)));
        lblStatus1.setFont(new java.awt.Font("Dialog", 0, 11));
        lblStatus1.setText("\u00a9 2004 cismet GmbH");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);

        prbStatus.setFont(new java.awt.Font("Dialog", 1, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panStatus.add(prbStatus, gridBagConstraints);

        lblStatus2.setFont(new java.awt.Font("Dialog", 0, 11));
        lblStatus2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panStatus.add(lblStatus2, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mnuFiles.setMnemonic('D');
        mnuFiles.setText("Datei");
        mniNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/file_obj.gif")));
        mniNew.setMnemonic('N');
        mniNew.setText("Neu");
        mniNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniNewActionPerformed(evt);
            }
        });

        mnuFiles.add(mniNew);

        mniOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/importdir_wiz.gif")));
        mniOpen.setMnemonic('f');
        mniOpen.setText("\u00d6ffnen");
        mniOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniOpenActionPerformed(evt);
            }
        });

        mnuFiles.add(mniOpen);

        mniSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/save_edit.gif")));
        mniSave.setMnemonic('S');
        mniSave.setText("Speichern");
        mniSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniSaveActionPerformed(evt);
            }
        });

        mnuFiles.add(mniSave);

        mniSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/save_edit.gif")));
        mniSaveAs.setMnemonic('u');
        mniSaveAs.setText("Speichern unter");
        mniSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniSaveAsActionPerformed(evt);
            }
        });

        mnuFiles.add(mniSaveAs);

        mnuFiles.add(jSeparator1);

        mnuImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/importdir_wiz.gif")));
        mnuImport.setMnemonic('I');
        mnuImport.setText("Import");
        mniImportDatasource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/plugins.gif")));
        mniImportDatasource.setMnemonic('D');
        mniImportDatasource.setText("Datenquelle");
        mniImportDatasource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniImportDatasourceActionPerformed(evt);
            }
        });

        mnuImport.add(mniImportDatasource);

        mniImportTargetSystem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/plugins.gif")));
        mniImportTargetSystem.setMnemonic('Z');
        mniImportTargetSystem.setText("Zielsystem");
        mniImportTargetSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniImportTargetSystemActionPerformed(evt);
            }
        });

        mnuImport.add(mniImportTargetSystem);

        mniImportUserDefinedCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/java_app.gif")));
        mniImportUserDefinedCode.setMnemonic('C');
        mniImportUserDefinedCode.setText("Code");
        mniImportUserDefinedCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniImportUserDefinedCodeActionPerformed(evt);
            }
        });

        mnuImport.add(mniImportUserDefinedCode);

        mnuFiles.add(mnuImport);

        mnuExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/save_edit.gif")));
        mnuExport.setMnemonic('x');
        mnuExport.setText("Export");
        mniExportDatasource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/plugins.gif")));
        mniExportDatasource.setMnemonic('D');
        mniExportDatasource.setText("Datenquelle");
        mniExportDatasource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniExportDatasourceActionPerformed(evt);
            }
        });

        mnuExport.add(mniExportDatasource);

        mniExportTargetSystem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/plugins.gif")));
        mniExportTargetSystem.setMnemonic('Z');
        mniExportTargetSystem.setText("Zielsystem");
        mniExportTargetSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniExportTargetSystemActionPerformed(evt);
            }
        });

        mnuExport.add(mniExportTargetSystem);

        mniExportUserDefinedCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/java_app.gif")));
        mniExportUserDefinedCode.setMnemonic('C');
        mniExportUserDefinedCode.setText("Code");
        mniExportUserDefinedCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniExportUserDefinedCodeActionPerformed(evt);
            }
        });

        mnuExport.add(mniExportUserDefinedCode);

        mnuFiles.add(mnuExport);

        mnuFiles.add(jSeparator10);

        mniExit.setMnemonic('E');
        mniExit.setText("Ende");
        mniExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniExitActionPerformed(evt);
            }
        });

        mnuFiles.add(mniExit);

        mnuMain.add(mnuFiles);

        mnuEdit.setMnemonic('B');
        mnuEdit.setText("Bearbeiten");
        mniGotoConnections.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/defaults_ps.gif")));
        mniGotoConnections.setText("Datenbankverbindungen");
        mniGotoConnections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniGotoConnectionsActionPerformed(evt);
            }
        });

        mnuEdit.add(mniGotoConnections);

        mnuEdit.add(jSeparator4);

        mniGotoMappings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/cfilter.gif")));
        mniGotoMappings.setText("Mappings");
        mniGotoMappings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniGotoMappingsActionPerformed(evt);
            }
        });

        mnuEdit.add(mniGotoMappings);

        mnuEdit.add(jSeparator5);

        mniGotoRelations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/showparents_mode.gif")));
        mniGotoRelations.setText("Relationen");
        mniGotoRelations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniGotoRelationsActionPerformed(evt);
            }
        });

        mnuEdit.add(mniGotoRelations);

        mnuEdit.add(jSeparator6);

        mniGotoOptions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/prefs_misc.gif")));
        mniGotoOptions.setText("Optionen");
        mnuEdit.add(mniGotoOptions);

        mnuEdit.add(jSeparator7);

        mniGotoCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/java_app.gif")));
        mniGotoCode.setText("Code");
        mniGotoCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniGotoCodeActionPerformed(evt);
            }
        });

        mnuEdit.add(mniGotoCode);

        mnuMain.add(mnuEdit);

        mnuExtras.setMnemonic('E');
        mnuExtras.setText("Extras");
        mnuCheckDBConnections.setText("\u00dcberpr\u00fcfe Datenbankverbindungen");
        mniCheckSourceDBConnection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/plugins.gif")));
        mniCheckSourceDBConnection.setText("\u00dcberpr\u00fcfe Verbindung mit Datenquelle");
        mniCheckSourceDBConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniCheckSourceDBConnectionActionPerformed(evt);
            }
        });

        mnuCheckDBConnections.add(mniCheckSourceDBConnection);

        mniCheckSourceDBConnection1.setIcon(new javax.swing.ImageIcon(""));
        mniCheckSourceDBConnection1.setText("Zeige alle Datens\u00e4tze der Datenquelle");
        mniCheckSourceDBConnection1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniCheckSourceDBConnection1ActionPerformed(evt);
            }
        });

        mnuCheckDBConnections.add(mniCheckSourceDBConnection1);

        mniCheckTargetDBConnections.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/plugins.gif")));
        mniCheckTargetDBConnections.setText("\u00dcberpr\u00fcfe Verbindung mit Zielsystem");
        mniCheckTargetDBConnections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniCheckTargetDBConnectionsActionPerformed(evt);
            }
        });

        mnuCheckDBConnections.add(mniCheckTargetDBConnections);

        mnuExtras.add(mnuCheckDBConnections);

        mniCheckCompleteSyntax.setText("\u00dcberpr\u00fcfe Format");
        mniCheckCompleteSyntax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniCheckCompleteSyntaxActionPerformed(evt);
            }
        });

        mnuExtras.add(mniCheckCompleteSyntax);

        mnuExtras.add(jSeparator8);

        mniPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/preview.gif")));
        mniPreview.setText("Preview");
        mniPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniPreviewActionPerformed(evt);
            }
        });

        mnuExtras.add(mniPreview);

        mniRunImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/run_tool.gif")));
        mniRunImport.setText("Import");
        mniRunImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniRunImportActionPerformed(evt);
            }
        });

        mnuExtras.add(mniRunImport);

        mnuExtras.add(jSeparator9);

        mniSaveBatchFile.setText("Erstelle Batchdatei");
        mniSaveBatchFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniSaveBatchFileActionPerformed(evt);
            }
        });

        mnuExtras.add(mniSaveBatchFile);

        mniChooseLibDir.setText("cids Installation f\u00fcr Batchbetrieb");
        mniChooseLibDir.setActionCommand("cids Installationsverzeichniss");
        mniChooseLibDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniChooseLibDirActionPerformed(evt);
            }
        });

        mnuExtras.add(mniChooseLibDir);

        mnuMain.add(mnuExtras);

        mnuHelp.setMnemonic('H');
        mnuHelp.setText("Hilfe");
        mniContents.setText("Inhalt");
        mniContents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniContentsActionPerformed(evt);
            }
        });

        mnuHelp.add(mniContents);

        mnuHelp.add(jSeparator3);

        mniAbout.setText("\u00dcber");
        mniAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniAboutActionPerformed(evt);
            }
        });

        mnuHelp.add(mniAbout);

        mnuMain.add(mnuHelp);

        setJMenuBar(mnuMain);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1024)/2, (screenSize.height-768)/2, 1024, 768);
    }//GEN-END:initComponents
    
    private void mniChooseLibDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniChooseLibDirActionPerformed
        chooseLibDir();
    }//GEN-LAST:event_mniChooseLibDirActionPerformed
    
    private void mniExportUserDefinedCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniExportUserDefinedCodeActionPerformed
        exportCode();
    }//GEN-LAST:event_mniExportUserDefinedCodeActionPerformed
    
    private void mniExportTargetSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniExportTargetSystemActionPerformed
        exportTargetSystem();
    }//GEN-LAST:event_mniExportTargetSystemActionPerformed
    
    private void mniExportDatasourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniExportDatasourceActionPerformed
        exportDatasource();
    }//GEN-LAST:event_mniExportDatasourceActionPerformed
    
    private void mniImportUserDefinedCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniImportUserDefinedCodeActionPerformed
        importCode();
    }//GEN-LAST:event_mniImportUserDefinedCodeActionPerformed
    
    private void mniImportTargetSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniImportTargetSystemActionPerformed
        importTargetSystem();
    }//GEN-LAST:event_mniImportTargetSystemActionPerformed
    
    private void mniImportDatasourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniImportDatasourceActionPerformed
        importDatasource();
    }//GEN-LAST:event_mniImportDatasourceActionPerformed
    
    private void mniAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniAboutActionPerformed
        JOptionPane.showMessageDialog(this,"Funktion steht im Moment noch nicht zur Verf\u00FCgung","Sorry ;-)",JOptionPane.ERROR_MESSAGE);
        
    }//GEN-LAST:event_mniAboutActionPerformed
    
    private void mniSaveBatchFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniSaveBatchFileActionPerformed
        saveBatchFile();
    }//GEN-LAST:event_mniSaveBatchFileActionPerformed
    
    private void mniRunImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniRunImportActionPerformed
        this.runImport();
    }//GEN-LAST:event_mniRunImportActionPerformed
    
    private void mniSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniSaveAsActionPerformed
        File f=null;
        saveFile(f);
    }//GEN-LAST:event_mniSaveAsActionPerformed
    
    private void mniPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniPreviewActionPerformed
        fillIntermedTables(true);
    }//GEN-LAST:event_mniPreviewActionPerformed
    
    private void mniCheckSourceDBConnection1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniCheckSourceDBConnection1ActionPerformed
        myConnectionEditor.checkSourceConnection(1000000);// Add your handling code here:
    }//GEN-LAST:event_mniCheckSourceDBConnection1ActionPerformed
    public void setStatusText(String s) {
        lblStatus2.setText(s);
    }
    
    private void chooseLibDir() {
        //this.cidsLibDirLocation;
        if (cidsLibDirFilechooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            cidsLibDirLocation=cidsLibDirFilechooser.getSelectedFile().getAbsolutePath();
            this.mniSaveBatchFile.setEnabled(true);
        }
    }
    
    
    
    private Importer checkCompleteSyntax(boolean preview ) {
        final ImportRules ir=getImportRules();
        Object result=null;
        setStatusText("Vorbereitung des Imports ...");
        this.setWait(true,true);
        try {
            result=foxtrot.Worker.post(new foxtrot.Task() {
                public Object run() throws Exception {
                    ///////
                    
                    Importer i=new Importer(ImportAnt.parsXMLRuntimeProps(ir.getRuntimeProps()),getImportRules());
                    
                    ///////
                    return i;
                }
            });
        } catch (InitializingException e) {
            this.setWait(false,false);
            this.addOutput(new OutputInitializing(e));
            JOptionPane.showMessageDialog(this,"Fehler beim \u00DCberpr\u00FCfen der Importeinstellungen\n("+e.toString()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            log.error("Fehler beim \u00DCberpr\u00FCfen der Importeinstellungen",e);
            return null;
        } catch (Exception e) {
            this.setWait(false,false);
            JOptionPane.showMessageDialog(this,"Fehler beim \u00DCberpr\u00FCfen der Importeinstellungen ("+e.toString()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            log.error("Fehler beim \u00DCberpr\u00FCfen der Importeinstellungen",e);
            return null;
        }
        this.setWait(false,false);
        if (!preview) {
            String initLog=((Importer)result).getInitializeLog();
            this.addOutput(new OutputInitializing(initLog));
            this.setWait(false,false);
            
            JOptionPane.showMessageDialog(this,"\u00DCberpr\u00FCfen der Importeinstellungen erfolgreich","",JOptionPane.INFORMATION_MESSAGE);
        }
        return ((Importer)result);
    }
    
    private Importer fillIntermedTables(boolean preview) {
        setWait(true,false);
        final Importer i=checkCompleteSyntax(true);
        if (i==null) return null;
        final JProgressBar pro=this.prbStatus;
        setStatusText("Auslesen der Daten ...");
        Object result=null;
        try {
            result=foxtrot.Worker.post(new foxtrot.Task() {
                public Object run() throws Exception {
                    ///////
                    
                    i.runImport(pro);
                    
                    ///////
                    return i;
                }
            });
        } catch (Exception e) {
            this.setWait(false,false);
            JOptionPane.showMessageDialog(this,"Fehler beim Import \n("+e.toString()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            log.error("Fehler beim Import",e);
            setWait(false,false);
            return null;
        }
        if (preview) {
            IntermedTablesContainer itc=i.getIntermedTables();
            Iterator it=itc.getIntermedTablesIterator();
            while (it.hasNext()) {
                IntermedTable intermed=(IntermedTable)(it.next());
                this.addOutput(new OutputIntermedTable(intermed));
            }
        }
        prbStatus.setValue(0);
        return i;
        
    }
    
    public void runImport() {
        final Importer i=this.fillIntermedTables(false);
        if (i==null) return;
        setStatusText("Schreiben der Daten ...");
        
        setWait(true,false);
        Object errorCounter=null;
        final RuntimeProps rP=myOptionsEditor.getRuntimePropsContent();
        
        try {
            errorCounter=foxtrot.Worker.post(new foxtrot.Task() {
                public Object run() throws Exception {
                    ///////
                    
                    RuntimeProperties r=ImportAnt.parsXMLRuntimeProps(rP);
                    
                    String finalizerClass=r.getFinalizerClass();
                    
                    ImportFinalizer finalizer=new ImportFinalizer(finalizerClass,
                            i.getIntermedTables(),
                            r.getFinalizerProperties());
                    OutputFinalizer outF=new OutputFinalizer(finalizer.getFinalizer());
                    addOutput(outF);
                    long errors=finalizer.finalise();
                    
                    outF.setLog(finalizer.getFinalizer().getLogs());
                    
                    
                    ///////
                    return new Long(errors);   //finalizer.getFinalizer().getLogs();
                }
            });
            Long ec=((Long)errorCounter);
            JOptionPane.showMessageDialog(this,"Import abgeschlossen. ("+ec.toString()+" Fehler)","",JOptionPane.INFORMATION_MESSAGE);
            
        } catch (InitializingException iEx) {
            JOptionPane.showMessageDialog(this,"Fehler beim \u00FCberpr\u00FCfen der Importeinstellungen ("+iEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            log.error("InitializingException",iEx);
            setWait(false,false);
            return;
        } catch (FinalizerException fEx) {
            JOptionPane.showMessageDialog(this,"Fehler beim Import ("+fEx.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            log.error("FinalizerException",fEx);
            setWait(false,false);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"Fehler beim Import ("+ex.getMessage()+")","Fehler",JOptionPane.ERROR_MESSAGE);
            log.error("sonst Exception",ex);
            setWait(false,false);
        } finally {
            setWait(false,false);
        }
        
    }
    
    
    private void mniCheckCompleteSyntaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniCheckCompleteSyntaxActionPerformed
        checkCompleteSyntax(false);
    }//GEN-LAST:event_mniCheckCompleteSyntaxActionPerformed
    
    private void cmdRunImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRunImportActionPerformed
        
        this.runImport();
    }//GEN-LAST:event_cmdRunImportActionPerformed
    
    private void mniCheckTargetDBConnectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniCheckTargetDBConnectionsActionPerformed
        myConnectionEditor.checkTargetConnection();
    }//GEN-LAST:event_mniCheckTargetDBConnectionsActionPerformed
    
    private void mniCheckSourceDBConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniCheckSourceDBConnectionActionPerformed
        myConnectionEditor.checkSourceConnection();
    }//GEN-LAST:event_mniCheckSourceDBConnectionActionPerformed
    
    private void panOptionsComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panOptionsComponentShown
        de.cismet.cids.admin.importAnt.castorGenerated.Options o=myOptionsEditor.getOptionsContent();
        myOptionsEditor.setTables(this.getAllUsedTargetTables());
        myOptionsEditor.setContent(o);
    }//GEN-LAST:event_panOptionsComponentShown
    
    private void tbpEditorsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbpEditorsPropertyChange
        
    }//GEN-LAST:event_tbpEditorsPropertyChange
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        fillIntermedTables(true);
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    private void myRelationsEditorComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_myRelationsEditorComponentShown
        tbpEditors.setSelectedIndex(3);
    }//GEN-LAST:event_myRelationsEditorComponentShown
    
    private void mniGotoCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniGotoCodeActionPerformed
        tbpEditors.setSelectedIndex(4);
    }//GEN-LAST:event_mniGotoCodeActionPerformed
    
    private void mniGotoRelationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniGotoRelationsActionPerformed
        tbpEditors.setSelectedIndex(2);
    }//GEN-LAST:event_mniGotoRelationsActionPerformed
    
    private void mniGotoMappingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniGotoMappingsActionPerformed
        tbpEditors.setSelectedIndex(1);
    }//GEN-LAST:event_mniGotoMappingsActionPerformed
    
    private void mniGotoConnectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniGotoConnectionsActionPerformed
        tbpEditors.setSelectedIndex(0);
    }//GEN-LAST:event_mniGotoConnectionsActionPerformed
    
    private void mniExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniExitActionPerformed
        exitForm(null);
    }//GEN-LAST:event_mniExitActionPerformed
    
    private void mniSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniSaveActionPerformed
        saveFile();
    }//GEN-LAST:event_mniSaveActionPerformed
    
    private void mniOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniOpenActionPerformed
        openFile();
    }//GEN-LAST:event_mniOpenActionPerformed
    
    private void mniNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniNewActionPerformed
        newFile();
    }//GEN-LAST:event_mniNewActionPerformed
    
    private void saveFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFile
        saveFile();
    }//GEN-LAST:event_saveFile
    
    private void openFile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFile
        openFile();
    }//GEN-LAST:event_openFile
    
    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        newFile();
    }//GEN-LAST:event_cmdNewActionPerformed
    
    private void mniContentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniContentsActionPerformed
        JOptionPane.showMessageDialog(this,"Funktion steht im Moment noch nicht zur Verf\u00FCgung","Sorry ;-)",JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_mniContentsActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        prefs.put("importFileDialogLocation",this.importFilechooser.getCurrentDirectory().getAbsolutePath());
        prefs.put("fileDialogLocation",this.filechooser.getCurrentDirectory().getAbsolutePath());
        if (cidsLibDirLocation!=null) {
            prefs.put("cidsLibDirLocation",cidsLibDirLocation);
        }
        try {
            prefs.flush();
        } catch (BackingStoreException bse) {
            log.error("Fehler beim Speichern in den BackingStore)",bse);
        }
        if (somethingChangedReminder()) {
            System.exit(0);
        }
    }//GEN-LAST:event_exitForm
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception{
        try {
            org.apache.log4j.PropertyConfigurator.configure(ClassLoader.getSystemResource("cids.log4j.properties"));
        } catch (Exception e) {
            
        }
        Import i = new Import();
        i.log.debug("Import Gui gestartet");
        i.show();
    }
    
    public void setWait(boolean hourglass,boolean indeterminatePB) {
        this.prbStatus.setIndeterminate(indeterminatePB);
        if (hourglass) {
            this.getGlassPane().setVisible(true);
            this.getGlassPane().setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        } else {
            this.getGlassPane().setVisible(false);
            this.getGlassPane().setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
        
        if (indeterminatePB==false&&hourglass==false) {
            setStatusText("");
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdOpen;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdRunImport;
    private javax.swing.JButton cmdSave;
    private javax.swing.JDesktopPane dpaOutput;
    private de.cismet.cids.tools.gui.farnsworth.GradientJPanel gradientJPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblStatus2;
    private javax.swing.JMenuItem mniAbout;
    private javax.swing.JMenuItem mniCheckCompleteSyntax;
    private javax.swing.JMenuItem mniCheckSourceDBConnection;
    private javax.swing.JMenuItem mniCheckSourceDBConnection1;
    private javax.swing.JMenuItem mniCheckTargetDBConnections;
    private javax.swing.JMenuItem mniChooseLibDir;
    private javax.swing.JMenuItem mniContents;
    private javax.swing.JMenuItem mniExit;
    private javax.swing.JMenuItem mniExportDatasource;
    private javax.swing.JMenuItem mniExportTargetSystem;
    private javax.swing.JMenuItem mniExportUserDefinedCode;
    private javax.swing.JMenuItem mniGotoCode;
    private javax.swing.JMenuItem mniGotoConnections;
    private javax.swing.JMenuItem mniGotoMappings;
    private javax.swing.JMenuItem mniGotoOptions;
    private javax.swing.JMenuItem mniGotoRelations;
    private javax.swing.JMenuItem mniImportDatasource;
    private javax.swing.JMenuItem mniImportTargetSystem;
    private javax.swing.JMenuItem mniImportUserDefinedCode;
    private javax.swing.JMenuItem mniNew;
    private javax.swing.JMenuItem mniOpen;
    private javax.swing.JMenuItem mniPreview;
    private javax.swing.JMenuItem mniRunImport;
    private javax.swing.JMenuItem mniSave;
    private javax.swing.JMenuItem mniSaveAs;
    private javax.swing.JMenuItem mniSaveBatchFile;
    private javax.swing.JMenu mnuCheckDBConnections;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenu mnuExport;
    private javax.swing.JMenu mnuExtras;
    private javax.swing.JMenu mnuFiles;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenu mnuImport;
    private javax.swing.JMenuBar mnuMain;
    private de.cismet.cids.admin.importAnt.gui.CodeEditor myCodeEditor;
    private de.cismet.cids.admin.importAnt.gui.ConnectionEditor myConnectionEditor;
    private de.cismet.cids.admin.importAnt.gui.MappingEditor myMappingEditor;
    private de.cismet.cids.admin.importAnt.gui.OptionsEditor myOptionsEditor;
    private de.cismet.cids.admin.importAnt.gui.RelationsEditor myRelationsEditor;
    private javax.swing.JPanel panCode;
    private javax.swing.JPanel panConnections;
    private javax.swing.JPanel panEditors;
    private javax.swing.JPanel panMappings;
    private javax.swing.JPanel panOptions;
    private javax.swing.JPanel panOutput;
    private javax.swing.JPanel panRelations;
    private javax.swing.JPanel panStatus;
    private javax.swing.JPanel panToolbar;
    private javax.swing.JProgressBar prbStatus;
    private javax.swing.JSplitPane spnMain;
    private javax.swing.JToolBar tbaTools;
    private javax.swing.JTabbedPane tbpEditors;
    // End of variables declaration//GEN-END:variables
    
}

class NodeInfo {
    Icon icon;
    String text;
    public NodeInfo(String text, Icon icon) {
        this.text=text;
        this.icon=icon;
    }
    public String toString() {
        return text;
    }
    public Icon getIcon() {
        return icon;
    }
}
