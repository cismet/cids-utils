/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Group.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
 */

package de.cismet.cids.admin.analyze.castorGenerated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Group.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class Group implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _package
     */
    private java.lang.String _package;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _analyzerList
     */
    private java.util.Vector _analyzerList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Group() {
        super();
        _analyzerList = new Vector();
    } //-- de.cismet.cids.admin.analyze.castorGenerated.Group()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAnalyzer
     * 
     * @param vAnalyzer
     */
    public void addAnalyzer(de.cismet.cids.admin.analyze.castorGenerated.Analyzer vAnalyzer)
        throws java.lang.IndexOutOfBoundsException
    {
        _analyzerList.addElement(vAnalyzer);
    } //-- void addAnalyzer(de.cismet.cids.admin.analyze.castorGenerated.Analyzer) 

    /**
     * Method addAnalyzer
     * 
     * @param index
     * @param vAnalyzer
     */
    public void addAnalyzer(int index, de.cismet.cids.admin.analyze.castorGenerated.Analyzer vAnalyzer)
        throws java.lang.IndexOutOfBoundsException
    {
        _analyzerList.insertElementAt(vAnalyzer, index);
    } //-- void addAnalyzer(int, de.cismet.cids.admin.analyze.castorGenerated.Analyzer) 

    /**
     * Method enumerateAnalyzer
     */
    public java.util.Enumeration enumerateAnalyzer()
    {
        return _analyzerList.elements();
    } //-- java.util.Enumeration enumerateAnalyzer() 

    /**
     * Method getAnalyzer
     * 
     * @param index
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Analyzer getAnalyzer(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _analyzerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.cismet.cids.admin.analyze.castorGenerated.Analyzer) _analyzerList.elementAt(index);
    } //-- de.cismet.cids.admin.analyze.castorGenerated.Analyzer getAnalyzer(int) 

    /**
     * Method getAnalyzer
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Analyzer[] getAnalyzer()
    {
        int size = _analyzerList.size();
        de.cismet.cids.admin.analyze.castorGenerated.Analyzer[] mArray = new de.cismet.cids.admin.analyze.castorGenerated.Analyzer[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.cids.admin.analyze.castorGenerated.Analyzer) _analyzerList.elementAt(index);
        }
        return mArray;
    } //-- de.cismet.cids.admin.analyze.castorGenerated.Analyzer[] getAnalyzer() 

    /**
     * Method getAnalyzerCount
     */
    public int getAnalyzerCount()
    {
        return _analyzerList.size();
    } //-- int getAnalyzerCount() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'package'.
     * 
     * @return the value of field 'package'.
     */
    public java.lang.String getPackage()
    {
        return this._package;
    } //-- java.lang.String getPackage() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeAllAnalyzer
     */
    public void removeAllAnalyzer()
    {
        _analyzerList.removeAllElements();
    } //-- void removeAllAnalyzer() 

    /**
     * Method removeAnalyzer
     * 
     * @param index
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Analyzer removeAnalyzer(int index)
    {
        java.lang.Object obj = _analyzerList.elementAt(index);
        _analyzerList.removeElementAt(index);
        return (de.cismet.cids.admin.analyze.castorGenerated.Analyzer) obj;
    } //-- de.cismet.cids.admin.analyze.castorGenerated.Analyzer removeAnalyzer(int) 

    /**
     * Method setAnalyzer
     * 
     * @param index
     * @param vAnalyzer
     */
    public void setAnalyzer(int index, de.cismet.cids.admin.analyze.castorGenerated.Analyzer vAnalyzer)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _analyzerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _analyzerList.setElementAt(vAnalyzer, index);
    } //-- void setAnalyzer(int, de.cismet.cids.admin.analyze.castorGenerated.Analyzer) 

    /**
     * Method setAnalyzer
     * 
     * @param analyzerArray
     */
    public void setAnalyzer(de.cismet.cids.admin.analyze.castorGenerated.Analyzer[] analyzerArray)
    {
        //-- copy array
        _analyzerList.removeAllElements();
        for (int i = 0; i < analyzerArray.length; i++) {
            _analyzerList.addElement(analyzerArray[i]);
        }
    } //-- void setAnalyzer(de.cismet.cids.admin.analyze.castorGenerated.Analyzer) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'package'.
     * 
     * @param _package
     * @param package the value of field 'package'.
     */
    public void setPackage(java.lang.String _package)
    {
        this._package = _package;
    } //-- void setPackage(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.analyze.castorGenerated.Group unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.analyze.castorGenerated.Group) Unmarshaller.unmarshal(de.cismet.cids.admin.analyze.castorGenerated.Group.class, reader);
    } //-- de.cismet.cids.admin.analyze.castorGenerated.Group unmarshal(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
