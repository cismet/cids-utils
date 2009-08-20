/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Options.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
 */

package de.cismet.cids.admin.importAnt.castorGenerated;

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
 * Class Options.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class Options implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _normalizeList
     */
    private java.util.Vector _normalizeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Options() {
        super();
        _normalizeList = new Vector();
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Options()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addNormalize
     * 
     * @param vNormalize
     */
    public void addNormalize(java.lang.String vNormalize)
        throws java.lang.IndexOutOfBoundsException
    {
        _normalizeList.addElement(vNormalize);
    } //-- void addNormalize(java.lang.String) 

    /**
     * Method addNormalize
     * 
     * @param index
     * @param vNormalize
     */
    public void addNormalize(int index, java.lang.String vNormalize)
        throws java.lang.IndexOutOfBoundsException
    {
        _normalizeList.insertElementAt(vNormalize, index);
    } //-- void addNormalize(int, java.lang.String) 

    /**
     * Method enumerateNormalize
     */
    public java.util.Enumeration enumerateNormalize()
    {
        return _normalizeList.elements();
    } //-- java.util.Enumeration enumerateNormalize() 

    /**
     * Method getNormalize
     * 
     * @param index
     */
    public java.lang.String getNormalize(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _normalizeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_normalizeList.elementAt(index);
    } //-- java.lang.String getNormalize(int) 

    /**
     * Method getNormalize
     */
    public java.lang.String[] getNormalize()
    {
        int size = _normalizeList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_normalizeList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getNormalize() 

    /**
     * Method getNormalizeCount
     */
    public int getNormalizeCount()
    {
        return _normalizeList.size();
    } //-- int getNormalizeCount() 

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
     * Method removeAllNormalize
     */
    public void removeAllNormalize()
    {
        _normalizeList.removeAllElements();
    } //-- void removeAllNormalize() 

    /**
     * Method removeNormalize
     * 
     * @param index
     */
    public java.lang.String removeNormalize(int index)
    {
        java.lang.Object obj = _normalizeList.elementAt(index);
        _normalizeList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeNormalize(int) 

    /**
     * Method setNormalize
     * 
     * @param index
     * @param vNormalize
     */
    public void setNormalize(int index, java.lang.String vNormalize)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _normalizeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _normalizeList.setElementAt(vNormalize, index);
    } //-- void setNormalize(int, java.lang.String) 

    /**
     * Method setNormalize
     * 
     * @param normalizeArray
     */
    public void setNormalize(java.lang.String[] normalizeArray)
    {
        //-- copy array
        _normalizeList.removeAllElements();
        for (int i = 0; i < normalizeArray.length; i++) {
            _normalizeList.addElement(normalizeArray[i]);
        }
    } //-- void setNormalize(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.Options unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.Options) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.Options.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Options unmarshal(java.io.Reader) 

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
