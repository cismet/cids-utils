/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: PreProcessingAndMapping.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Class PreProcessingAndMapping.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class PreProcessingAndMapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mappingList
     */
    private java.util.Vector _mappingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public PreProcessingAndMapping() {
        super();
        _mappingList = new Vector();
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMapping
     * 
     * @param vMapping
     */
    public void addMapping(de.cismet.cids.admin.importAnt.castorGenerated.Mapping vMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _mappingList.addElement(vMapping);
    } //-- void addMapping(de.cismet.cids.admin.importAnt.castorGenerated.Mapping) 

    /**
     * Method addMapping
     * 
     * @param index
     * @param vMapping
     */
    public void addMapping(int index, de.cismet.cids.admin.importAnt.castorGenerated.Mapping vMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _mappingList.insertElementAt(vMapping, index);
    } //-- void addMapping(int, de.cismet.cids.admin.importAnt.castorGenerated.Mapping) 

    /**
     * Method enumerateMapping
     */
    public java.util.Enumeration enumerateMapping()
    {
        return _mappingList.elements();
    } //-- java.util.Enumeration enumerateMapping() 

    /**
     * Method getMapping
     * 
     * @param index
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Mapping getMapping(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.cismet.cids.admin.importAnt.castorGenerated.Mapping) _mappingList.elementAt(index);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Mapping getMapping(int) 

    /**
     * Method getMapping
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Mapping[] getMapping()
    {
        int size = _mappingList.size();
        de.cismet.cids.admin.importAnt.castorGenerated.Mapping[] mArray = new de.cismet.cids.admin.importAnt.castorGenerated.Mapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.cids.admin.importAnt.castorGenerated.Mapping) _mappingList.elementAt(index);
        }
        return mArray;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Mapping[] getMapping() 

    /**
     * Method getMappingCount
     */
    public int getMappingCount()
    {
        return _mappingList.size();
    } //-- int getMappingCount() 

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
     * Method removeAllMapping
     */
    public void removeAllMapping()
    {
        _mappingList.removeAllElements();
    } //-- void removeAllMapping() 

    /**
     * Method removeMapping
     * 
     * @param index
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Mapping removeMapping(int index)
    {
        java.lang.Object obj = _mappingList.elementAt(index);
        _mappingList.removeElementAt(index);
        return (de.cismet.cids.admin.importAnt.castorGenerated.Mapping) obj;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Mapping removeMapping(int) 

    /**
     * Method setMapping
     * 
     * @param index
     * @param vMapping
     */
    public void setMapping(int index, de.cismet.cids.admin.importAnt.castorGenerated.Mapping vMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mappingList.setElementAt(vMapping, index);
    } //-- void setMapping(int, de.cismet.cids.admin.importAnt.castorGenerated.Mapping) 

    /**
     * Method setMapping
     * 
     * @param mappingArray
     */
    public void setMapping(de.cismet.cids.admin.importAnt.castorGenerated.Mapping[] mappingArray)
    {
        //-- copy array
        _mappingList.removeAllElements();
        for (int i = 0; i < mappingArray.length; i++) {
            _mappingList.addElement(mappingArray[i]);
        }
    } //-- void setMapping(de.cismet.cids.admin.importAnt.castorGenerated.Mapping) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping unmarshal(java.io.Reader) 

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
