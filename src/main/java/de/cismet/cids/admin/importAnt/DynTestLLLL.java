/**
 * cids ImportAnt mapping class (DO NOT CHANGE)
 * Source created @ 1080995236734
 */


import java.util.*;

// user defined imports


// wird keine Import ben�t

 public class DynTestLLLL {
    public DynTestLLLL(){}

    private static java.sql.Connection targetConnection=null;
    public static LinkedHashMap assign(java.sql.Connection tc,String[] args) {
        //init
        targetConnection=tc;
        String ObjectID=args[0];
        String ObjectName=args[1];
        String Description=args[2];
        String ISBA_Nummer=args[3];
        String Quelle=args[4];
        String Ende_Ablagerung=args[5];
        String Beginn_Ablagerung=args[6];
        String Umschreibendes_Rechteck=args[7];
        String Bemerkung=args[8];
        String Flaechenkuerzel=args[9];

        String[] COORDINATE$FROM$Altablagerung$Umschreibendes_Rechteck$ = new String[5];
        String[] Url_Base$FROM$Altablagerung$Description$Url$URL_BASE_ID$ = new String[4];
        String[] Url$FROM$Altablagerung$Description$ = new String[3];
        String[] Url$FROM$TEST$ = new String[3];
        String[] Altablagerung = new String[10];

        // Assigning Section
        Altablagerung[0] = ObjectID;//by-cids-error-finder:ObjectID
        Altablagerung[1] = "-->CIDS-CROSS-REFERENCE:COORDINATE.ID";//by-cids-error-finder:"-->CIDS-CROSS-REFERENCE:COORDINATE.ID"
        Altablagerung[2] = Ende_Ablagerung;//by-cids-error-finder:Ende_Ablagerung
        Altablagerung[3] = Beginn_Ablagerung;//by-cids-error-finder:Beginn_Ablagerung
        Url$FROM$Altablagerung$Description$[1] = getFile(Description);//by-cids-error-finder:getFile(Description)
        Url_Base$FROM$Altablagerung$Description$Url$URL_BASE_ID$[1] = getProt(Description);//by-cids-error-finder:getProt(Description)
        Url_Base$FROM$Altablagerung$Description$Url$URL_BASE_ID$[2] = getDomain(Description);//by-cids-error-finder:getDomain(Description)
        Url_Base$FROM$Altablagerung$Description$Url$URL_BASE_ID$[3] = getPath(Description);//by-cids-error-finder:getPath(Description)
        COORDINATE$FROM$Altablagerung$Umschreibendes_Rechteck$[1] = extractCoord(1,Umschreibendes_Rechteck);//by-cids-error-finder:extractCoord(1,Umschreibendes_Rechteck)
        COORDINATE$FROM$Altablagerung$Umschreibendes_Rechteck$[2] = extractCoord(2,Umschreibendes_Rechteck);//by-cids-error-finder:extractCoord(2,Umschreibendes_Rechteck)
        COORDINATE$FROM$Altablagerung$Umschreibendes_Rechteck$[3] = extractCoord(3,Umschreibendes_Rechteck);//by-cids-error-finder:extractCoord(3,Umschreibendes_Rechteck)
        COORDINATE$FROM$Altablagerung$Umschreibendes_Rechteck$[4] = extractCoord(4,Umschreibendes_Rechteck);//by-cids-error-finder:extractCoord(4,Umschreibendes_Rechteck)
        Altablagerung[4] = Quelle;//by-cids-error-finder:Quelle
        Altablagerung[5] = ISBA_Nummer;//by-cids-error-finder:ISBA_Nummer
        Altablagerung[6] = Bemerkung;//by-cids-error-finder:Bemerkung
        Altablagerung[7] = Flaechenkuerzel;//by-cids-error-finder:Flaechenkuerzel
        Url$FROM$TEST$[0] = ObjectID;//by-cids-error-finder:ObjectID

        // Preparing the return value

        LinkedHashMap hm=new LinkedHashMap();        hm.put("COORDINATE$FROM$Altablagerung$Umschreibendes_Rechteck$",COORDINATE$FROM$Altablagerung$Umschreibendes_Rechteck$);
        hm.put("Url_Base$FROM$Altablagerung$Description$Url$URL_BASE_ID$",Url_Base$FROM$Altablagerung$Description$Url$URL_BASE_ID$);
        hm.put("Url$FROM$Altablagerung$Description$",Url$FROM$Altablagerung$Description$);
        hm.put("Url$FROM$TEST$",Url$FROM$TEST$);
        hm.put("Altablagerung",Altablagerung);
        return hm;
    }
 // User defined Procedures
public static String extractCoord(int pos,String ko) {
	if (ko==null) return "0";
	if (ko.equals("NULL")) return "0";

	switch (pos) {
	case 1:
		return ko.substring(ko.indexOf("(")+1,ko.indexOf(","));
	case 2:
		return ko.substring(ko.indexOf(",")+1,ko.indexOf(",",ko.indexOf(",")+1));
	case 3:
		return ko.substring(ko.indexOf(",",ko.indexOf(",")+1)+1,ko.lastIndexOf(","));
	case 4:
		return ko.substring(ko.lastIndexOf(",")+1,ko.lastIndexOf(")"));
	default:
		return null;
	}
}

public static String getFromTS(String ti) {
	if (ti==null||ti.equals("NULL")) return "0001-01-01 00:00:00.000000" ;
	String day=ti.substring(ti.indexOf("(")+1,ti.indexOf("/"));
	String month=ti.substring(ti.indexOf("/")+1,ti.indexOf("/",ti.indexOf("/")+1));
	String year=ti.substring(ti.indexOf("/",ti.indexOf("/")+1)+1,ti.indexOf(","));
	return year+"-"+month+"-"+day+" 00:00:00.000000";
}

public static String getToTS(String ti) {
	if (ti==null||ti.equals("NULL")) return "0001-01-01 00:00:00.000000";
	String day=ti.substring(ti.indexOf(",")+1,ti.indexOf("/",ti.indexOf(",")));
	String month=ti.substring(ti.lastIndexOf("/",ti.lastIndexOf("/")-1)+1,ti.lastIndexOf("/"));
	String year=ti.substring(ti.lastIndexOf("/")+1,ti.lastIndexOf(")"));
	return year+"-"+month+"-"+day+" 00:00:00.000000";
}

public static String getFile(String url) {
	if (url==null) return null;
	return url.substring(url.lastIndexOf("/")+1,url.length());
}

public static String getProt(String url) {
	if (url==null) return null;
	return url.substring(0,url.indexOf("://")+3);
}

public static String getDomain(String url) {
	try {
		if (url==null) return null;
		return url.substring(url.indexOf("://")+3,url.indexOf("/",url.indexOf("://")+3));
	} catch (Exception e) {
		System.out.println(url);
		return null;
	}
}

public static String getPath(String url) {
	if (url==null) return null;
	return url.substring(url.indexOf("/",url.indexOf("://")+3),url.lastIndexOf("/")+1);
}

}

