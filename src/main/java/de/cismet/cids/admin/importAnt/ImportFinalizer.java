/*
 * ImportFinalizer.java
 *
 * Created on 27. Oktober 2003, 11:26
 */

package de.cismet.cids.admin.importAnt;
import java.util.Properties;
import java.util.Enumeration;
import java.lang.reflect.*;


/**
 *
 * @author  hell
 */
public class ImportFinalizer {
    /** Logger */    
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private static final org.apache.log4j.Logger sLog = org.apache.log4j.Logger.getLogger("de.cismet.cids.admin.importAnt.ImportFinalizer");
    String methodNameWichCausedError;
    Class finalizerClass;
    Object finalizerObject;
    Constructor constructor;
    IntermedTablesContainer intermedTables;
    
    /** Creates a new instance of ImportFinalizer */
    public ImportFinalizer(String finalizerClassName, 
                          IntermedTablesContainer intermedTables, 
                          Properties props) throws FinalizerException{

        //Abspeichern der intermediate Tables
        this.intermedTables=intermedTables; 

        try {
           //Suchen der Finsiher Klasse
           finalizerClass=Class.forName("de.cismet.cids.admin.importAnt.finalizer."+finalizerClassName);
           
           //Erzeugen eines Objektes des Finalizers
           constructor=finalizerClass.getConstructor(null);
           finalizerObject=constructor.newInstance(null);
           
           //Suchen und Aufruf der Property-Methoden
           Enumeration<String> propsEnum=(Enumeration<String>) props.propertyNames();
           while (propsEnum.hasMoreElements()) {
                String name=propsEnum.nextElement();
                methodNameWichCausedError="set"+name;
                Method m=finalizerClass.getMethod("set"+name, new Class[] {String.class});
                m.invoke(finalizerObject, new Object[]{props.getProperty(name)});
           }
        } 
        catch (NoSuchMethodException nsme) {
            throw new FinalizerException("Methode "+methodNameWichCausedError+"(String arg) nicht vorhanden!",nsme);
        }
        catch (InvocationTargetException invEx) {
            throw new FinalizerException("Fehler im Finalizer (Initialisierungsphase)!",invEx.getCause());
        }
        catch (Exception e) {
            throw new FinalizerException("Fehler im Konstruktor",e);
        }
        
        //Setzen der IntermedTables
        try {
            Method m=finalizerClass.getMethod("setIntermedTables",new Class[]{IntermedTablesContainer.class});
            m.invoke(finalizerObject, new Object[]{intermedTables});
        }
        catch (InvocationTargetException invEx) {
            throw new FinalizerException("Fehler im Finalizer! (Methode finalise()",invEx.getCause());
        }
        catch (Exception e) {
            throw new FinalizerException("Fehler bei finalise()",e);
        }
    }
    
    public Finalizer getFinalizer() {
        return (Finalizer)finalizerObject;
    }
    
    public long finalise() throws FinalizerException {
        try {
            Method m=finalizerClass.getMethod("finalise",null);
            return ((Long)m.invoke(finalizerObject, null)).longValue();
        }
        catch (InvocationTargetException invEx) {
             throw new FinalizerException("Fehler im Finalizer! (Methode finalise()",invEx.getCause());
        }
        catch (Exception e) {
            throw new FinalizerException("Fehler bei finalise()",e);
        }
    }
    
    public static void main(String[] args) {
        
        try {
            sLog.info("Start");
            Properties p=new Properties();
            p.setProperty("OutputDirectory", "C:\\");
            p.setProperty("FilePrefix", "Tester12");
            p.setProperty("Delimiter", "'");
            p.setProperty("Seperator",",");
            ImportFinalizer imf=new ImportFinalizer("SapDBImportFilesGenerator",null,p);
            imf.finalise();
            
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
}
