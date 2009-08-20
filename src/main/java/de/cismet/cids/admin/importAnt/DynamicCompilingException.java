/*
 * DynamicCompilingException.java
 *
 * Created on 26. September 2003, 12:43
 */

package de.cismet.cids.admin.importAnt;


import java.util.*;


/** Exception die beim dynamischen Kompilieren der Assigner Klassen
 * geworfen wird. Mit dieser Exception kann man n\u00E4heres \u00FCber die Fehlerursache
 * herausfinden.
 *
 * Teile des Codes sind nicht von mir. Aus dem OpenSource Projekt Schmortopf ;-)
 * @author hell
 */
public class DynamicCompilingException extends CidsImportAntException {
    /** Logger */    
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    /** Fehlermeldungstext vom Compiler */    
    private String errors;
    /** Anzahl der Fehler */    
    private int numberOfErrors;
    /** Container der die Fehler-Zeilen speichert */    
    Vector lineNos=new Vector();
    /** Container der die Spalten der Fehler speichert */    
    Vector columns=new Vector();
    /** Contaioner der die beschreibungen der Fehler speichert */    
    Vector errorDescs=new Vector();
    /** Creates a new instance of DynamicCompilingException
     * @param s Komplette Fehlerausgabe des Java-Compilers
     */
    public DynamicCompilingException(String s) {
        super(s);
        errors=s;

        StringTokenizer tokenizer = new StringTokenizer(errors,"\n");
        int numberOfLines = tokenizer.countTokens(); // call this only once !
        Vector linesVector = new Vector();
        while( tokenizer.hasMoreElements() )
         {
           final String thisLine = tokenizer.nextToken();
           // Exclude file check,load and wrote messages, which all start
           // with a [ sign :
           if( !thisLine.startsWith("[") )
            {
              linesVector.addElement(thisLine);
            }
         }
        String[] lines = new String[ linesVector.size() ];
        linesVector.copyInto(lines);
    
        final Vector singleMessageVector = new Vector();
        for( int i=0; i < lines.length; i++ )
        {
           if( (lines[i].indexOf(".java") > 0) && (lines[i].indexOf(": ") > 0) )
            {
              // new single message starts :
              if( singleMessageVector.size() > 0 )
               {
                 // send what we have until now :
                 final String[] singleMessageLines = new String[singleMessageVector.size()];
                 singleMessageVector.copyInto(singleMessageLines);
                 this.decodeSingleErrorMessage( singleMessageLines );
               }
              singleMessageVector.removeAllElements();
            }
           singleMessageVector.addElement( lines[i] );
         } // for
        // Test the rest message: If it too starts with a valid error descriptor,
        // send it too :
        if( singleMessageVector.size() > 0 )
         {
           // send what we have until now :
           final String[] singleMessageLines = new String[singleMessageVector.size()];
           singleMessageVector.copyInto(singleMessageLines);
           if( (singleMessageLines[0].indexOf(".java") > 0) && (singleMessageLines[0].indexOf(": ") > 0) )
            {
              this.decodeSingleErrorMessage(singleMessageLines);
            }
           else
            {
              // Obviously we couldnt parse this correctly, so we at least
              // print it as it is :
              for( int i=0; i < singleMessageLines.length; i++ )
               {
                 // this.outputManager.printErrorLine( singleMessageLines[i] );
                 // ACHTUNG HIER NACHBESSERN !!!!!!!!!!!!!!!!!!!!!!!
               }
            }
         }
    }

    
    
    /** Liefert die Anzahl der Fehler
     * @return Anzahl der Fehler (int)
     */    
    public int getNumberOfErrors() {
        return numberOfErrors;
    }
    
    
    
    

    /** Interne Methode um die Fehlermeldungen zu analysieren (Schmortopf)
     * @param errorLines AUsgabe des Compilers in zeilen aufgesplittet
     */    
  private void decodeSingleErrorMessage( final String[] errorLines )
  {
    this.numberOfErrors++;

    final String[] parsedErrorLines = new String[errorLines.length];
    for( int i=0; i < parsedErrorLines.length; i++ )
     {
       parsedErrorLines[i] = "";
     }

    // The first one is the descriptor. Extract the filePath :
    final String descriptor = errorLines[0]; // exists always
    int numberEndIndex = descriptor.indexOf(": ");
    if( numberEndIndex > 0 )
     {
       // go back to previous : sign :
       int numberStartIndex = numberEndIndex-1;
       while( ( descriptor.charAt(numberStartIndex) != ':' ) &&
              ( numberStartIndex > 0 ) )
        {
          numberStartIndex--;
        }
       if( numberStartIndex > 0 )
        {
          final String filePath = descriptor.substring(0,numberStartIndex);
          final String lineNumberString = descriptor.substring(numberStartIndex+1,numberEndIndex);
          // Get the lineNumber
          int lineNumber = -1;
          try
           {
             lineNumber = Integer.parseInt(lineNumberString);
           }
          catch( Exception anyEx ){}
          // The linenumber must be decremented by one :
          lineNumber -= 1;
          // Get the first pased errorline :
          parsedErrorLines[0] = descriptor.substring(numberEndIndex+2,descriptor.length()-1);
          // Move the other ones and catch the locationPointer line,
          // which consists of spaces and one ^ sign :
          int column = -1;
          for( int i=1; i < errorLines.length; i++ )
           {
             parsedErrorLines[i] = errorLines[i];
             final int c = errorLines[i].indexOf('^');
             if( c >= 0 )
              {
                column = c;
              }
           }
          // Folgende Daten stehen zur Verf\u00FCgung
          // filePath,
          //
          errorDescs.add(errorLines);
          this.lineNos.add(new Integer(lineNumber));
          this.columns.add(new Integer(column));
        }
     }
  } // decodeSingleErrorMessage

  
  /** Liefert die Zeilennummer (Sourcecode) des \u00FCbergebenen Fehlers
   * @param errorNo Fehlernummer
   * @return Zeilennummer des Fehlers
   */  
  public int getErrorLineNo(int errorNo) {
    return ((Integer)(lineNos.elementAt(errorNo))).intValue();
  }
  /** Liefert die Spaltennummer (Sourcecode) des \u00FCbergebenen Fehlers
   * @param errorNo Fehlernummer
   * @return Spaltennummer
   */  
  public int getErrorColumn(int errorNo) {
    return ((Integer)(columns.elementAt(errorNo))).intValue();
  }
  /** Liefert die Fehlerbeschreibung des Compilers des \u00FCbergebenen Fehlers
   * @param errorNo Fehlernummer
   * @return Fehlerbeschreibung
   */  
  public String[] getErrorDesc(int errorNo) {
    return ((String[])(errorDescs.elementAt(errorNo)));
  }
  
  

    
    
}
