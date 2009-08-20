/*
 * AnalyseResult.java
 *
 * Created on 5. Februar 2004, 11:32
 */

package de.cismet.cids.admin.analyze;
import java.sql.*;
import java.util.*;

/**
 *
 * @author  hell
 */
public class AnalyseResult {
    Vector content;
    /** Creates a new instance of AnalyseResult */
    public AnalyseResult() {
        content=new Vector();
    }
  
    public void appendHeading(String heading) {
        content.add(new Heading(heading));
    }
    public void appendSubHeading(String heading) {
        content.add(new SubHeading(heading));
    }
    
    public void appendText(String text) {
        content.add(text);
    }
    
    public void appendResultSet(ResultSet r) {
        content.add(r);
    }
    
    protected Vector getContentVector() {
        return content;
    }
    
    public void appendAnalyseResult(AnalyseResult result) {
        Vector r=result.getContentVector();
        Iterator i=r.iterator();
        while (i.hasNext()) {
            Object o=i.next();
            content.add(o);
        }
    }
    
    public String toString() {
        Iterator i=content.iterator();
        String res="";
        while (i.hasNext()) {
            Object o=i.next();
            if (o instanceof SubHeading) {
                res+="+   "+(SubHeading)o+"\n";
            }

            else if (o instanceof Heading) {
                res+="\n+++++++\n+   "+((Heading)o)+"\n+++++++\n\n";
            }
            else if (o instanceof ResultSet) {
                try {
                    ResultSet resultSet=(ResultSet)o;
                    ResultSetMetaData md=resultSet.getMetaData();
                    int cc=md.getColumnCount();
                    res+="\n\t";
                    for (int j=1;j<cc;++j) {
                        res+=md.getColumnLabel(j)+", ";
                    }
                    res+=md.getColumnLabel(cc)+"\n\t";
                    res+="-------------------------------------\n\t";
                    while(resultSet.next()) {
                        for (int j=1;j<cc;++j) {
                            res+=resultSet.getObject(j)+", ";
                        }
                        res+=resultSet.getObject(cc)+"\n\t";
                    }
                }
                catch (Exception e) {
                    
                }
                res+="\n";
                
                
            }
            else if (o instanceof String) {
                res+=(String)o+"\n";
            }
        }

        return res;
    }
    
 

    public String toHTMLString() {
        Iterator i=content.iterator();
        String res="<html>";
 
        
        res+="\n<head><title>cids MetaDB Analyzer</title></head>";
        res+="\n<FONT FACE=\"Arial, sans-serif\">";
        res+="\n<body>";
        res+="\n<table border=\"0\" BORDERCOLOR=\"#000000\"  width=100% CELLPADDING=12> <tr><th align=\"right\" valign=\"center\" BGCOLOR=#4E5D8F ><FONT  STYLE=\"font-size: 24pt\" ptSIZE=1 COLOR=\"#ffffff\">";
        res+="\ncids MetaDB Analyzer";
        res+="\n</Font></th></table>";
        res+="\n<br><br>";

        
        
        
        while (i.hasNext()) {
            Object o=i.next();
            if (o instanceof SubHeading) {
                res+="\n<h3>"+(SubHeading)o+"</h3>";
            }

            else if (o instanceof Heading) {
                res+="\n<table border=\"0\" BORDERCOLOR=\"#000000\"  width=100% CELLPADDING=10> <tr><th align=\"left\" valign=\"center\" BGCOLOR=#9DA5BF>";
                res+="<FONT SIZE=1 STYLE=\"font-size: 20pt\">";
                res+="\n"+(Heading)o;
                res+="\n</Font></th></table>";
            }
            else if (o instanceof ResultSet) {
                try {
                    ResultSet resultSet=(ResultSet)o;
                    ResultSetMetaData md=resultSet.getMetaData();
                    int cc=md.getColumnCount();
                    
                    res+="\n<P STYLE=\"text-align:left; margin-left:1.4cm;\"><table border=\"1\" BORDERCOLOR=\"#000000\" CELLPADDING=4 CELLSPACING=0>";
                    res+="\n<tr>";
                    for (int j=1;j<=cc;++j) {
                        res+=getTableHeader(md.getColumnLabel(j));
                    }
                    res+="\n</tr>";
                    
                    while(resultSet.next()) {
                        res+="\n<tr>";
                        for (int j=1;j<=cc;++j) {
                            if (resultSet.getObject(j)==null) {
                                res+=getCell("null");
                            }
                            else {
                                res+=getCell(resultSet.getObject(j).toString());
                            }
                        }
                        
                        res+="\n</tr>";
                    }
                    res+="\n</TABLE></P>";
                }
                catch (Exception e) {

                }
                res+="\n</TABLE></P>";
                res+="\n";


            }
            else if (o instanceof String) {
                res+="\n<P STYLE=\"text-align:left; margin-left:1.4cm;\"><FONT FACE=\"Arial, sans-serif\">";
                res+="\n"+(String)o;
                res+="\n"+"</FONT></P>";
            }
        }
        
        res+="\n</FONT>";
        res+="\n<br><br>";
        res+="\n<table border=\"0\" BORDERCOLOR=\"#000000\"  width=100% CELLPADDING=12> <tr><th align=\"right\" valign=\"center\" BGCOLOR=#4E5D8F ><FONT  STYLE=\"font-size: 24pt\" ptSIZE=1 COLOR=\"#ffffff\"><br>";
        res+="\n<br></Font></th></table>";
        
        res+="</body></html>";
        return res;
    }
    
    private String getTableHeader(String title) {
        return "\n<th BGCOLOR=#9DA5BF><FONT SIZE=1 STYLE=\"font-size: 9pt\">"+title+"</FONT></th>";
    }
    
    private String getCell(String content) {
        return "\n<td BGCOLOR=#e6e6e6><FONT SIZE=1 STYLE=\"font-size: 8pt\">"+content+"</FONT></td>";
    }
        
}

class Heading {
    String h;
    public Heading(String s) {
        h=new String(s);
    }
    public String toString() {
        return h;
    }
}

class SubHeading extends Heading {
    public SubHeading(String s) {
        super(s);
    }
}