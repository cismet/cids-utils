/*
 * CidsAnalyzer.java
 *
 * Created on 4. Februar 2004, 13:38
 */

package de.cismet.cids.admin.analyze;
import java.sql.Connection; 
/**   
 *
 * @author  hell
 */
public abstract class CidsAnalyzer {
    public abstract AnalyseResult analyse(Connection conn);
}
 