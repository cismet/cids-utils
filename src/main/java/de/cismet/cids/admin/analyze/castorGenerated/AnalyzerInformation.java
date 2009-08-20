/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AnalyzerInformation.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Comment describing your root element
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class AnalyzerInformation implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _groupList
     */
    private java.util.Vector _groupList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AnalyzerInformation() {
        super();
        _groupList = new Vector();
    } //-- de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addGroup
     * 
     * @param vGroup
     */
    public void addGroup(de.cismet.cids.admin.analyze.castorGenerated.Group vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupList.addElement(vGroup);
    } //-- void addGroup(de.cismet.cids.admin.analyze.castorGenerated.Group) 

    /**
     * Method addGroup
     * 
     * @param index
     * @param vGroup
     */
    public void addGroup(int index, de.cismet.cids.admin.analyze.castorGenerated.Group vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupList.insertElementAt(vGroup, index);
    } //-- void addGroup(int, de.cismet.cids.admin.analyze.castorGenerated.Group) 

    /**
     * Method enumerateGroup
     */
    public java.util.Enumeration enumerateGroup()
    {
        return _groupList.elements();
    } //-- java.util.Enumeration enumerateGroup() 

    /**
     * Method getGroup
     * 
     * @param index
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Group getGroup(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.cismet.cids.admin.analyze.castorGenerated.Group) _groupList.elementAt(index);
    } //-- de.cismet.cids.admin.analyze.castorGenerated.Group getGroup(int) 

    /**
     * Method getGroup
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Group[] getGroup()
    {
        int size = _groupList.size();
        de.cismet.cids.admin.analyze.castorGenerated.Group[] mArray = new de.cismet.cids.admin.analyze.castorGenerated.Group[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.cids.admin.analyze.castorGenerated.Group) _groupList.elementAt(index);
        }
        return mArray;
    } //-- de.cismet.cids.admin.analyze.castorGenerated.Group[] getGroup() 

    /**
     * Method getGroupCount
     */
    public int getGroupCount()
    {
        return _groupList.size();
    } //-- int getGroupCount() 

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
     * Method removeAllGroup
     */
    public void removeAllGroup()
    {
        _groupList.removeAllElements();
    } //-- void removeAllGroup() 

    /**
     * Method removeGroup
     * 
     * @param index
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Group removeGroup(int index)
    {
        java.lang.Object obj = _groupList.elementAt(index);
        _groupList.removeElementAt(index);
        return (de.cismet.cids.admin.analyze.castorGenerated.Group) obj;
    } //-- de.cismet.cids.admin.analyze.castorGenerated.Group removeGroup(int) 

    /**
     * Method setGroup
     * 
     * @param index
     * @param vGroup
     */
    public void setGroup(int index, de.cismet.cids.admin.analyze.castorGenerated.Group vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupList.setElementAt(vGroup, index);
    } //-- void setGroup(int, de.cismet.cids.admin.analyze.castorGenerated.Group) 

    /**
     * Method setGroup
     * 
     * @param groupArray
     */
    public void setGroup(de.cismet.cids.admin.analyze.castorGenerated.Group[] groupArray)
    {
        //-- copy array
        _groupList.removeAllElements();
        for (int i = 0; i < groupArray.length; i++) {
            _groupList.addElement(groupArray[i]);
        }
    } //-- void setGroup(de.cismet.cids.admin.analyze.castorGenerated.Group) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation) Unmarshaller.unmarshal(de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation.class, reader);
    } //-- de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation unmarshal(java.io.Reader) 

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
