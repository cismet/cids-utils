/*
 * SimpleAnalyzer.java
 *
 * Created on 4. Februar 2004, 13:37
 */

package de.cismet.cids.admin.analyze;
import java.sql.Connection;
import de.cismet.cids.admin.analyze.castorGenerated.*;
import java.util.*;
import java.lang.reflect.*;
/**
 *
 * @author  hell
 */
public class MasterAnalyzer {
    Connection metaDB;
    AnalyzerInformation info;
    /** Creates a new instance of SimpleAnalyzer */
    public MasterAnalyzer(Connection conn,AnalyzerInformation anInfo) {
        metaDB=conn;
        info=anInfo;
    }

    public AnalyseResult analyse() {
        Class analyzerClass;
        Object analyzerObject;
        Constructor analyzerConstructor;
        Method analzingMethod;
        AnalyseResult result=new AnalyseResult();
        Enumeration enumGroups=info.enumerateGroup();
        while (enumGroups.hasMoreElements()){
            Group g=(Group)enumGroups.nextElement();
            String pack=g.getPackage();
            Enumeration enumAnalyzers=g.enumerateAnalyzer();
            while (enumAnalyzers.hasMoreElements()) {
                Analyzer a=(Analyzer)enumAnalyzers.nextElement();
                
                try {
                    analyzerClass=Class.forName(pack+"."+a.getClassname());
                    analyzerConstructor=analyzerClass.getConstructor(null);
                    analyzerObject=analyzerConstructor.newInstance(null); 
                    analzingMethod=analyzerClass.getMethod("analyse", new Class[]{Connection.class});
                    result.appendAnalyseResult((AnalyseResult)analzingMethod.invoke(analyzerObject, new Object[] {metaDB}));
                } catch (Exception e) {
                    //Reflection Exception
                    result.appendHeading(a.getClassname());
                    result.appendText("Fehler beim Aufruf dieses Analysebereichs:\n\t"+e);
                }
            }
        }
        return result;   
    }
}
