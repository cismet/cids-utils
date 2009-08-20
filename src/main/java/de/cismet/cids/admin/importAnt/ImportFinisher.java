/*
 * ImportFinisher.java
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
public class ImportFinisher {
    /** Logger */    
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private static final org.apache.log4j.Logger sLog = org.apache.log4j.Logger.getLogger("de.cismet.cids.admin.importAnt.ImportFinisher");
    String methodNameWichCausedError;
    Class finisherClass;
    Object finisherObject;
    Constructor constructor;
    IntermedTablesContainer intermedTables;
    
    /** Creates a new instance of ImportFinisher */
    public ImportFinisher(String finisherClassName, 
                          IntermedTablesContainer intermedTables, 
                          Properties props) throws FinisherException{

        //Abspeichern der intermediate Tables
        this.intermedTables=intermedTables; 

        try {
           //Suchen der Finsiher Klasse
           finisherClass=Class.forName("de.cismet.cids.admin.importAnt.finisher."+finisherClassName);
           
           //Erzeugen eines Objektes des Finishers
           constructor=finisherClass.getConstructor(null);
           finisherObject=constructor.newInstance(null);
           
           //Suchen und Aufruf der Property-Methoden
           Enumeration<String> propsEnum=(Enumeration<String>) props.propertyNames();
           while (propsEnum.hasMoreElements()) {
                String name=propsEnum.nextElement();
                methodNameWichCausedError="set"+name;
                Method m=finisherClass.getMethod("set"+name, new Class[] {String.class});
                m.invoke(finisherObject, new Object[]{props.getProperty(name)});
           }
        } 
        catch (NoSuchMethodException nsme) {
            throw new FinisherException("Methode "+methodNameWichCausedError+"(String arg) nicht vorhanden!",nsme);
        }
        catch (InvocationTargetException invEx) {
            throw new FinisherException("Fehler im Finisher (Initialisierungsphase)!",invEx.getCause());
        }
        catch (Exception e) {
            throw new FinisherException("Fehler im Konstruktor",e);
        }
        
        //Setzen der IntermedTables
        try {
            Method m=finisherClass.getMethod("setIntermedTables",new Class[]{IntermedTablesContainer.class});
            m.invoke(finisherObject, new Object[]{intermedTables});
        }
        catch (InvocationTargetException invEx) {
            throw new FinisherException("Fehler im Finisher! (Methode finish()",invEx.getCause());
        }
        catch (Exception e) {
            throw new FinisherException("Fehler bei Finish()",e);
        }
    }
    
    
    public void finish() throws FinisherException {
        try {
            Method m=finisherClass.getMethod("finish",null);
            m.invoke(finisherObject, null);
        }
        catch (InvocationTargetException invEx) {
             throw new FinisherException("Fehler im Finisher! (Methode finish()",invEx.getCause());
        }
        catch (Exception e) {
            throw new FinisherException("Fehler bei Finish()",e);
        }
    }
    
    public static void main(String[] args) {
        
        try {
            
            Properties p=new Properties();
            p.setProperty("OutputDirectory", "C:\\");
            p.setProperty("FilePrefix", "Tester12");
            p.setProperty("Delimiter", "'");
            p.setProperty("Seperator",",");
            ImportFinisher imf=new ImportFinisher("SapDBImportFilesGenerator",null,p);
            imf.finish();
            
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
}
