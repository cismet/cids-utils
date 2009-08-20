/*
 * ImportAnt.java
 *
 * Created on 3. November 2003, 12:18
 */

package de.cismet.cids.admin.importAnt;
import com.martiansoftware.jsap.*;
import com.martiansoftware.jsap.stringparsers.*;
import com.martiansoftware.jsap.defaultsources.PropertyDefaultSource;
import java.util.*;
import java.io.*;
import de.cismet.cids.admin.importAnt.castorGenerated.*;

/**
 *
 * @author  hell
 */
public class ImportAnt {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImportAnt.class);
    
    private static final String usage=
    "Syntax:\n"+
    "        ImportAnt mappingFile";
    
    /** Creates a new instance of ImportAnt */
    public ImportAnt() {
    }
    
    public static void main(String[] args)  {
        try {
            org.apache.log4j.PropertyConfigurator.configure(ClassLoader.getSystemResource("cids.log4j.properties"));
        }
        catch (Exception e) {
            //todo ExceptionBehandlung wenn log4j.propertie file nicht gefunden
            System.err.println("cids.log4j.properties nicht gefunden"); // nixschlimm
        }
        Importer imp=null;
        try {
            JSAP jsap=new JSAP();
            
            FlaggedOption mappingFile=new FlaggedOption("mappingFile",
            new StringStringParser(),
            "mappingFile.xml",
            true,
            'm',
            "mappingFile");
            FlaggedOption fin=new FlaggedOption("finalizer",
            new StringStringParser(),
            null,
            false,
            'f',
            "finalizer");
            
            FlaggedOption tmpDir=new FlaggedOption("tmpDir",
            new StringStringParser(),
            "",
            true,
            'd',
            "tmpDir");
            
            Switch check=new Switch("check",'c',"check");
            
            Switch allInXml=new Switch("xmlRun",'x',"xmlRun");
            
            UnflaggedOption finProps=new UnflaggedOption("finProps",
            new StringStringParser(),
            null,
            false,
            true);
            
            Switch keepAssigner=new Switch("keepCode",'k',"keepCode");
            
            jsap.registerParameter(mappingFile);
            jsap.registerParameter(tmpDir);
            jsap.registerParameter(check);
            jsap.registerParameter(allInXml);
            jsap.registerParameter(fin);
            jsap.registerParameter(finProps);
            jsap.registerParameter(keepAssigner);
            
            JSAPResult res=jsap.parse(args);
            
            if (res.success()) {
                RuntimeProperties rProp=new RuntimeProperties();
                rProp.setImportFileName(res.getString("mappingFile"));
                log.info("Starte Import von: "+rProp.getImportFileName());
                Date now=new Date();
                log.info(now);
                FileReader r=new FileReader(rProp.getImportFileName());
                //            initializeLog+="Import-Beschreibung wird ge\u00F6ffnet\n";
                
                // aus dem XML File die entsprechende Datenstruktur machen (CASTOR)
                ImportRules impRules=ImportRules.unmarshal(r);
                
                
                
                if (res.getBoolean("xmlRun")) { // alle Notwendigen Parameter sind in der XML Datei (hoffentlich)
                    rProp.setImportFileName(res.getString("mappingFile"));
                    RuntimeProps rp=impRules.getRuntimeProps();
                    rProp=parsXMLRuntimeProps(rp);
                }
                else { // alle RuntimeParameter werden in der Kommandozeile \u00FCbergeben
                    rProp.setFinalizerClass(res.getString("finalizer"));
                    rProp.setTmpDirectory(res.getString("tmpDir"));
                    rProp.setKeepAssigner(res.getBoolean("keepCode"));
                    Properties finP=parseFinalizerProps(res.getStringArray("finProps"));
                    rProp.setFinalizerProperties(finP);
                    
                }
                
                // jetzt gehts los
                imp=new Importer(rProp,impRules);
                
                log.info("Zuweisungen, Formatumwandlungen, Normalisierung ...");
                imp.runImport();
                
                log.info("Schreiben in die Datenbank ...");
                ImportFinalizer finalizer=new ImportFinalizer("JDBCImportExecutor",
                imp.getIntermedTables(),
                rProp.getFinalizerProperties());
                long errorCount;
                if (!res.getBoolean("check")) {
                    errorCount=finalizer.finalise();
                    System.out.println("Import durchgefuehrt:\n\t"+errorCount+" Fehler!");
                }else {
                    System.out.println("Check durchgefuehrt!");
                }
            }
            else {
                System.err.println("Fehler in der Kommandozeile");
            }
        }
        catch (JSAPException jEx) {
            System.err.println("Fehler in der Kommandozeile :-(");
            jEx.printStackTrace();
        }
        catch (InitializingException iEx) {
            //Fehlerausgabe
            //iEx.printStackTrace();
            System.out.println("Fehler beim Initialisieren :-(\nNaehere Beschreibung:\n---Log--\n"+iEx.getInitializeLog()+"\n---Fehlermeldung--\n"+iEx.getMessage()+"\n---Detail--\n"+iEx.getCause().toString());
        }
        catch (CidsImportAntException cEx) {
            //Fehlerausgabe
            System.out.println("Fehler im Finalizer :-(\n(Initialisierung erfolgreich)\n---Fehlermeldung--\n"+cEx.getMessage()+"\n---Detail--\n"+cEx.getCause().toString());
        }
        catch (java.io.FileNotFoundException fnfEx) {
            System.out.println("Fehler beim Lesen der Mapping Datei! ("+fnfEx.getMessage()+")");
            //,initializeLog,fnfEx
        }
        catch (org.exolab.castor.xml.MarshalException mEx) {
            System.out.println("Fehler in der Mapping Datei! ("+mEx.getMessage()+")");
            //,initializeLog,mEx
        }
        catch (org.exolab.castor.xml.ValidationException mEx) {
            System.out.println("Fehler in der Mapping Datei! ("+mEx.getMessage()+")");
            //,initializeLog,mEx
        }
    }
    
    static private Properties parseFinalizerProps(String[] finProps) throws InitializingException{
        Properties p=new Properties();
        for (int i=0; i<finProps.length;++i) {
            String[] pair=finProps[i].split("=");
            if (pair.length==2) {
                p.setProperty(pair[0],pair[1]);
            }
        }
        return p;
    }
    static public RuntimeProperties parsXMLRuntimeProps(RuntimeProps rp ) throws InitializingException {
        RuntimeProperties rProp=new RuntimeProperties();
        if (rp!=null) {
            de.cismet.cids.admin.importAnt.castorGenerated.Finalizer finalizer = rp.getFinalizer();
            String finalizerClass=finalizer.getClassName();
            
            rProp.setFinalizerClass(finalizerClass);
            if (rp.getTmpDir()!=null) rProp.setTmpDirectory(rp.getTmpDir());
            rProp.setKeepAssigner(rp.getKeepCode());
            
            Properties finP=new Properties();
            Enumeration<Prop> enum_ =finalizer.enumerateProp();
            while (enum_.hasMoreElements()) {
                Prop p=enum_.nextElement();
                String key=p.getKey();
                String content=p.getContent();
                finP.setProperty(key,content);
            }
            rProp.setFinalizerProperties(finP);
        }
        else {
            throw new InitializingException("keine Runtime Properties in XML-Datei","");
        }
        return rProp;
    }
}
