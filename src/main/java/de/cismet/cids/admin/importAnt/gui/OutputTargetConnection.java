/*
 * OutputTargetConnection.java
 *
 * Created on 18. November 2003, 09:34
 */

package de.cismet.cids.admin.importAnt.gui;
import de.cismet.common.gui.sqlwizard.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.tree.*;
import de.cismet.cids.admin.importAnt.castorGenerated.*;

/**
 *
 * @author  hell
 */
public class OutputTargetConnection extends javax.swing.JInternalFrame {
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private Connection connection;
    /** Creates new form OutputTargetConnection */
    
    public OutputTargetConnection(Connection connection) {
        this.connection=connection;
        initComponents();
    }

    
    private DefaultMutableTreeNode getSystemRoot(Connection connection) {
        DefaultMutableTreeNode root=new DefaultMutableTreeNode("root");
        try { 
            DatabaseMetaData md=connection.getMetaData();
            //ResultSet rs=md.getTables(null, null, null, new String[] {"TABLE"});
            ResultSet rs=md.getColumns(null, null, null, null);
            String tablename="";
            TargetTable table=null;
            TargetField field=null;
            while (rs.next()) {
                if (!tablename.equals(rs.getString(3))) {
                    tablename=rs.getString(3);
                    table=new TargetTable(tablename);
                    root.add(table);
                }
                
                TargetField tf=new TargetField();
                tf.setName(rs.getString(4));
                tf.setTableName(tablename);
                tf.setType(rs.getString(6));
                
                table.add(tf);//new DefaultMutableTreeNode(rs.getString(4)+"  ["+rs.getString(6)+"("+rs.getString(7)+"), "+rs.getString(18)+"]"));

            }
        }
        catch (Exception e) {
            log.error("Fehler beim setzen der System-Metadaten.",e);
        }
        return root;
    }
    
    private DefaultMutableTreeNode getCidsRoot(Connection connection) {
        DefaultMutableTreeNode root=new DefaultMutableTreeNode("root");
        TargetTable targetTable=null;
        TargetField targetField=null;
        try {
            Statement stmnt=connection.createStatement();
            String tablename="";
            String primKey="";
            String sqlStatement="select a.id, c.name,c.table_name,c.primary_key_field,a.field_name,t.name,t.complex_type, a.foreign_key, c_zwo.table_name, c_zwo.primary_key_field from cs_class c ,cs_attr a,cs_type t, cs_class c_zwo where c.id=a.class_id and t.id=a.type_id and c.id=c_zwo.id and foreign_key_references_to is null "+
                                "union select a.id,c.name,c.table_name,c.primary_key_field,a.field_name,t.name,t.complex_type, a.foreign_key, c_zwo.table_name, c_zwo.primary_key_field from cs_class c ,cs_attr a,cs_type t, cs_class c_zwo where c.id=a.class_id and t.id=a.type_id and foreign_key_references_to=c_zwo.id  order by 1";
            ResultSet rs=stmnt.executeQuery(sqlStatement);
            
            //"select c.name,c.primary_key_field,a.field_name,t.name,t.complex_type from cs_class c ,cs_attr a,cs_type t where c.id=a.class_id and t.id=a.type_id");
            while (rs.next()) {
                if (!tablename.equals(rs.getString(2))) {
                    tablename=rs.getString(2);
                    primKey=rs.getString(4);
                    targetTable=new TargetTable(tablename);
                    root.add(targetTable);
                }
                targetField=new TargetField();
                
                targetField.setName(rs.getString(5));
                targetField.setTableName(tablename); 
                if (primKey.equals(rs.getString(5))) {
                    //field="*"+rs.getString(4);
                    targetField.setPrimaryKey(true);
                }
                else {
                    targetField.setPrimaryKey(false);
                }
                
                targetField.setType(rs.getString(6));
                
                
                if (rs.getString(7).toLowerCase().equals("t")) {
                    targetTable.setMasterTable(true);
                    targetField.setForeignKey(true);
                    targetField.setDetailTable(rs.getString(9));
                    targetField.setDetailField(rs.getString(10));
                    //field=field+"   -> "+rs.getString(4);
                }
                else {
                    targetField.setForeignKey(false);
                }

                targetTable.add(targetField);
            }
            
        } catch (Exception e) {
            log.error("kein cids-System weil Exception im Analyseprozess:",e);
            root.add(new DefaultMutableTreeNode("kein cids-System"));
        }
        return root;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        popMain = new javax.swing.JPopupMenu();
        mnuAddToMappings = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        mnuAddToRelations = new javax.swing.JMenuItem();
        tabMain = new javax.swing.JTabbedPane();
        scpSystem = new javax.swing.JScrollPane();
        trvSystem =         trvSystem=new JTree(new DefaultTreeModel(getSystemRoot(connection)));

        panCids = new javax.swing.JPanel();
        scpCids = new javax.swing.JScrollPane();
        trvCids = trvCids=new JTree(new DefaultTreeModel(getCidsRoot(connection)));

        mnuAddToMappings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/cfilter.gif")));
        mnuAddToMappings.setText("Mapping vorbereiten");
        mnuAddToMappings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAddToMappingsActionPerformed(evt);
            }
        });

        popMain.add(mnuAddToMappings);

        popMain.add(jSeparator1);

        mnuAddToRelations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/showparents_mode.gif")));
        mnuAddToRelations.setText("Beziehungen vorbereiten");
        mnuAddToRelations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAddToRelationsActionPerformed(evt);
            }
        });

        popMain.add(mnuAddToRelations);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Zielsystem");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/defaults_ps.gif")));
        setMinimumSize(new java.awt.Dimension(150, 100));
        setPreferredSize(new java.awt.Dimension(180, 180));
        tabMain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMainMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabMainMouseReleased(evt);
            }
        });

        scpSystem.setName("system");
        trvSystem.setFont(new java.awt.Font("Dialog", 0, 10));
        trvSystem.setRootVisible(false);
        DefaultTreeCellRenderer systemRenderer=new DefaultTreeCellRenderer();
        systemRenderer.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/sysdb.gif")));
        systemRenderer.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/sysdb_c.gif")));
        systemRenderer.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/rem_co.gif")));
        trvSystem.setCellRenderer(systemRenderer);
        trvSystem.setName("system");
        trvSystem.setRootVisible(false);
        trvSystem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                trvSystemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                trvSystemMouseReleased(evt);
            }
        });

        scpSystem.setViewportView(trvSystem);

        tabMain.addTab("System", scpSystem);

        panCids.setLayout(new java.awt.BorderLayout());

        panCids.setName("cids");
        trvCids.setRootVisible(false);

        trvCids.setFont(new java.awt.Font("Dialog", 0, 10));
        trvCids.setRootVisible(false);
        DefaultTreeCellRenderer cidsRenderer=new DefaultTreeCellRenderer();
        cidsRenderer.setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/class_obj.gif")));
        cidsRenderer.setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/innerclass_protected_obj.gif")));
        cidsRenderer.setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/admin/res/protected_co.gif")));
        trvCids.setCellRenderer(cidsRenderer);
        trvCids.setName("cids");
        trvCids.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                trvCidsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                trvCidsMouseReleased(evt);
            }
        });

        scpCids.setViewportView(trvCids);

        panCids.add(scpCids, java.awt.BorderLayout.CENTER);

        tabMain.addTab("cids", panCids);

        getContentPane().add(tabMain, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void mnuAddToMappingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAddToMappingsActionPerformed
        // Add your handling code here:
        
        TreePath tp=null;
        if (tabMain.getSelectedComponent().getName().equals("system")){
            log.debug("SYSTEM");
            tp=trvSystem.getSelectionPath();
            
        }
        else {
            log.debug("cids");
            tp=trvCids.getSelectionPath();
        }
        Object selection=tp.getLastPathComponent();
        TargetTable t=(TargetTable)selection;

        PreProcessingAndMapping maps=new PreProcessingAndMapping();
        for (int i=0; i<t.getChildCount();++i) {
            TargetField f=(TargetField)t.getChildAt(i);
            Mapping newMapping=new Mapping();
            newMapping.setTargetField(f.getName());
            newMapping.setTargetTable(f.getTableName());
            if (f.isStringVal()) {
                newMapping.setEnclosingChar("'");
            }
            maps.addMapping(newMapping);
        }
        getMainFrame().addMappings(maps);
            
        
    }//GEN-LAST:event_mnuAddToMappingsActionPerformed

    private void mnuAddToRelationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAddToRelationsActionPerformed
        log.debug("addRelations");
        TreePath tp=trvCids.getSelectionPath();
        Object selection=tp.getLastPathComponent();
        TargetTable t=(TargetTable)selection;
        Relations rel=new Relations();
        Relation r;
        for (int i=0; i<t.getChildCount();++i) {
             TargetField f=(TargetField)t.getChildAt(i);
             if (f.isForeignKey()) {
                 r=new Relation();
                 r.setMasterTable(f.getTableName());
                 r.setMasterTableForeignKey(f.getName());
                 r.setDetailTable(f.getDetailTable());
                 r.setDetailTableKey(f.getDetailField());
                 rel.addRelation(r);
             }
        }
        getMainFrame().addRelations(rel);
         
    }//GEN-LAST:event_mnuAddToRelationsActionPerformed

    private void trvSystemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trvSystemMouseReleased
        if (evt.isPopupTrigger()) {
            TreePath tp=trvSystem.getSelectionPath();
            handlePopupEventForTree(tp,evt);
        }
    }//GEN-LAST:event_trvSystemMouseReleased

    private void trvSystemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trvSystemMousePressed
        if (evt.isPopupTrigger()) {
            TreePath tp=trvSystem.getSelectionPath();
            handlePopupEventForTree(tp,evt);
        }

    }//GEN-LAST:event_trvSystemMousePressed

    private void tabMainMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMainMouseReleased
    }//GEN-LAST:event_tabMainMouseReleased

    private void tabMainMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMainMousePressed
    }//GEN-LAST:event_tabMainMousePressed

    private void trvCidsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trvCidsMouseReleased
        if (evt.isPopupTrigger()) {
            TreePath tp=trvCids.getSelectionPath();
            handlePopupEventForTree(tp,evt);
        }
    }//GEN-LAST:event_trvCidsMouseReleased


    
    private void trvCidsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trvCidsMousePressed
        if (evt.isPopupTrigger()) {
           TreePath tp=trvCids.getSelectionPath();
           handlePopupEventForTree(tp,evt);
        }
    }//GEN-LAST:event_trvCidsMousePressed

     private void handlePopupEventForTree(TreePath tp,java.awt.event.MouseEvent evt) {
        if (tp!=null) {
            Object selection=tp.getLastPathComponent();
            if (selection instanceof TargetTable) {
                TargetTable t=(TargetTable)selection;
                mnuAddToMappings.setEnabled(true);
                if (t.isMasterTable()) {
                    this.mnuAddToRelations.setEnabled(true);
                }
                else {
                    mnuAddToRelations.setEnabled(false);
                }
            }
            else if (evt.getComponent().getName().equals("system")){
                mnuAddToMappings.setEnabled(true);
                mnuAddToRelations.setEnabled(false);
            }
            else {
                mnuAddToMappings.setEnabled(false);
                mnuAddToRelations.setEnabled(false);
            }
        }
        else {
            mnuAddToMappings.setEnabled(false);                
            mnuAddToRelations.setEnabled(false);
        }
        this.popMain.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
     private Import getMainFrame() {
        return ((Import)(getTopLevelAncestor()));
    }

        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem mnuAddToMappings;
    private javax.swing.JMenuItem mnuAddToRelations;
    private javax.swing.JPanel panCids;
    private javax.swing.JPopupMenu popMain;
    private javax.swing.JScrollPane scpCids;
    private javax.swing.JScrollPane scpSystem;
    private javax.swing.JTabbedPane tabMain;
    private javax.swing.JTree trvCids;
    private javax.swing.JTree trvSystem;
    // End of variables declaration//GEN-END:variables
    
    
    
    
}

class TargetTable extends DefaultMutableTreeNode {
    
    public TargetTable(String name) {
        super();
        this.name=name;
    }
    
    /** Holds value of property masterTable. */
    private boolean masterTable=false;    
    
    /** Holds value of property name. */
    private String name="";
    
    /** Getter for property masterTable.
     * @return Value of property masterTable.
     *
     */
    public boolean isMasterTable() {
        return this.masterTable;
    }
    
    /** Setter for property masterTable.
     * @param masterTable New value of property masterTable.
     *
     */
    public void setMasterTable(boolean masterTable) {
        this.masterTable = masterTable;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName() {
        return this.name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String toString() {
        return name;
    }
}

class TargetField extends DefaultMutableTreeNode{
    
    /** Holds value of property name. */
    private String name="";
    
    /** Holds value of property tableName. */
    private String tableName="";
    
    /** Holds value of property stringVal. */
    private boolean stringVal=false;
    
    /** Holds value of property foreignKey. */
    private boolean foreignKey=false;
    
    /** Holds value of property type. */
    private String type="";
    
    /** Holds value of property detailTable. */
    private String detailTable="";
    
    /** Holds value of property detailField. */
    private String detailField="";
    
    /** Holds value of property primaryKey. */
    private boolean primaryKey=false;
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName() {
        return this.name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** Getter for property tableName.
     * @return Value of property tableName.
     *
     */
    public String getTableName() {
        return this.tableName;
    }
    
    /** Setter for property tableName.
     * @param tableName New value of property tableName.
     *
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    /** Getter for property stringVal.
     * @return Value of property stringVal.
     *
     */
    public boolean isStringVal() {
        if (
            (getType().toLowerCase().indexOf("char")!=-1)||
            (getType().toLowerCase().indexOf("varchar")!=-1)||
            (getType().toLowerCase().indexOf("date")!=-1)||
            (getType().toLowerCase().indexOf("time")!=-1)||
            (getType().toLowerCase().indexOf("timestamp")!=-1)
            ) {
            return true;
        } 
        else {
            return false;
        }
    }
    
    
    /** Getter for property foreignKey.
     * @return Value of property foreignKey.
     *
     */
    public boolean isForeignKey() {
        return this.foreignKey;
    }
    
    /** Setter for property foreignKey.
     * @param foreignKey New value of property foreignKey.
     *
     */
    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }
    
    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public String getType() {
        return this.type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /** Getter for property detailTable.
     * @return Value of property detailTable.
     *
     */
    public String getDetailTable() {
        return this.detailTable;
    }
    
    /** Setter for property detailTable.
     * @param detailTable New value of property detailTable.
     *
     */
    public void setDetailTable(String detailTable) {
        this.detailTable = detailTable;
    }
    
    /** Getter for property detailField.
     * @return Value of property detailField.
     *
     */
    public String getDetailField() {
        return this.detailField;
    }
    
    /** Setter for property detailField.
     * @param detailField New value of property detailField.
     *
     */
    public void setDetailField(String detailField) {
        this.detailField = detailField;
    }
    
    /** Getter for property primaryKey.
     * @return Value of property primaryKey.
     *
     */
    public boolean isPrimaryKey() {
        return this.primaryKey;
    }
    
    /** Setter for property primaryKey.
     * @param primaryKey New value of property primaryKey.
     *
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public String toString() {
        String ret="";
        if (isPrimaryKey()) {
            ret+="*"+name;
        }
        else {
            ret+=" "+name;
        }
        if (isForeignKey()) {
            ret+="      ["+this.getType()+"]   >> "+getDetailTable()+"."+getDetailField();
        }    
        else {
            ret+="      ["+this.getType()+"]";
        }
        return ret;
    }
    
}

