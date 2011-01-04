/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SimpleAnalyzer.java
 *
 * Created on 4. Februar 2004, 13:37
 */
package de.cismet.cids.admin.analyze;
import java.lang.reflect.*;

import java.sql.Connection;

import java.util.*;

import de.cismet.cids.admin.analyze.castorGenerated.*;
/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class MasterAnalyzer {

    //~ Instance fields --------------------------------------------------------

    Connection metaDB;
    AnalyzerInformation info;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of SimpleAnalyzer.
     *
     * @param  conn    DOCUMENT ME!
     * @param  anInfo  DOCUMENT ME!
     */
    public MasterAnalyzer(final Connection conn, final AnalyzerInformation anInfo) {
        metaDB = conn;
        info = anInfo;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public AnalyseResult analyse() {
        Class analyzerClass;
        Object analyzerObject;
        Constructor analyzerConstructor;
        Method analzingMethod;
        final AnalyseResult result = new AnalyseResult();
        final Enumeration enumGroups = info.enumerateGroup();
        while (enumGroups.hasMoreElements()) {
            final Group g = (Group)enumGroups.nextElement();
            final String pack = g.getPackage();
            final Enumeration enumAnalyzers = g.enumerateAnalyzer();
            while (enumAnalyzers.hasMoreElements()) {
                final Analyzer a = (Analyzer)enumAnalyzers.nextElement();

                try {
                    analyzerClass = Class.forName(pack + "." + a.getClassname());
                    analyzerConstructor = analyzerClass.getConstructor(null);
                    analyzerObject = analyzerConstructor.newInstance(null);
                    analzingMethod = analyzerClass.getMethod("analyse", new Class[] { Connection.class });
                    result.appendAnalyseResult((AnalyseResult)analzingMethod.invoke(
                            analyzerObject,
                            new Object[] { metaDB }));
                } catch (Exception e) {
                    // Reflection Exception
                    result.appendHeading(a.getClassname());
                    result.appendText("Fehler beim Aufruf dieses Analysebereichs:\n\t" + e);
                }
            }
        }
        return result;
    }
}
