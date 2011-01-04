/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * CidsAnalyzer.java
 *
 * Created on 4. Februar 2004, 13:38
 */
package de.cismet.cids.admin.analyze;
import java.sql.Connection;
/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public abstract class CidsAnalyzer {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   conn  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract AnalyseResult analyse(Connection conn);
}
