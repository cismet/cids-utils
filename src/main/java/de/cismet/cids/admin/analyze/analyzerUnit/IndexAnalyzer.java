/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ExampleAnalyzer.java
 *
 * Created on 5. Februar 2004, 14:32
 */
package de.cismet.cids.admin.analyze.analyzerUnit;
import java.sql.*;

import de.cismet.cids.admin.analyze.*;
/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class IndexAnalyzer extends CidsAnalyzer {

    //~ Methods ----------------------------------------------------------------

    @Override
    public AnalyseResult analyse(final Connection metaConn) {
        final AnalyseResult r = new AnalyseResult();
        r.appendHeading("Indexanalyse");
        r.appendSubHeading("Volltextindex");
        r.appendText(
            "Es folgt eine Auflistung welche Attribute von welchen Klassen im Volltextindex eingetragen sind.");
        try {
            final Statement s0 = metaConn.createStatement();
            final Statement s1 = metaConn.createStatement();
            ResultSet rs = s0.executeQuery(
                    "select distinct c.name as Klassenname,a.name as Attribut from cs_attr_string i, cs_class c ,cs_attr a where c.id=i.class_id and a.id=i.attr_id order by klassenname,attribut");
            r.appendResultSet(rs);
            r.appendText("Nur was im Index eingetragen ist, kann auch \u00FCber die Volltextsuche gefunden werden !");

            r.appendSubHeading("Objektindex");
            r.appendText("Es folgt eine Auflistung welche kompl. Attributtypen f\u00FCr welche Klasse indiziert sind.");
            rs = s1.executeQuery(
                    "select distinct c1.name as \"Klasse\",c2.name as \"komplexer Attributtyp\", count(c1.name) as \"Anzahl der indizierten Objekte\" from cs_all_attr_mapping m,cs_class c1,cs_class c2 where m.class_id=c1.id and m.attr_class_id=c2.id group by c1.name, c2.name order by c1.name, c2.name");
            r.appendResultSet(rs);
        } catch (Exception e) {
            r.appendText("Statement konnte nicht abgesetzt werden (" + e.getMessage() + ")");
        }

        return r;
    }
}
