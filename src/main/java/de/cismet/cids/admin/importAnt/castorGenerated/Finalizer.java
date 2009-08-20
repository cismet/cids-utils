/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Finalizer.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Class Finalizer.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class Finalizer implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _className
     */
    private java.lang.String _className;

    /**
     * Field _propList
     */
    private java.util.Vector _propList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Finalizer() {
        super();
        _propList = new Vector();
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Finalizer()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addProp
     * 
     * @param vProp
     */
    public void addProp(de.cismet.cids.admin.importAnt.castorGenerated.Prop vProp)
        throws java.lang.IndexOutOfBoundsException
    {
        _propList.addElement(vProp);
    } //-- void addProp(de.cismet.cids.admin.importAnt.castorGenerated.Prop) 

    /**
     * Method addProp
     * 
     * @param index
     * @param vProp
     */
    public void addProp(int index, de.cismet.cids.admin.importAnt.castorGenerated.Prop vProp)
        throws java.lang.IndexOutOfBoundsException
    {
        _propList.insertElementAt(vProp, index);
    } //-- void addProp(int, de.cismet.cids.admin.importAnt.castorGenerated.Prop) 

    /**
     * Method enumerateProp
     */
    public java.util.Enumeration enumerateProp()
    {
        return _propList.elements();
    } //-- java.util.Enumeration enumerateProp() 

    /**
     * Returns the value of field 'className'.
     * 
     * @return the value of field 'className'.
     */
    public java.lang.String getClassName()
    {
        return this._className;
    } //-- java.lang.String getClassName() 

    /**
     * Method getProp
     * 
     * @param index
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Prop getProp(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.cismet.cids.admin.importAnt.castorGenerated.Prop) _propList.elementAt(index);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Prop getProp(int) 

    /**
     * Method getProp
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Prop[] getProp()
    {
        int size = _propList.size();
        de.cismet.cids.admin.importAnt.castorGenerated.Prop[] mArray = new de.cismet.cids.admin.importAnt.castorGenerated.Prop[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.cids.admin.importAnt.castorGenerated.Prop) _propList.elementAt(index);
        }
        return mArray;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Prop[] getProp() 

    /**
     * Method getPropCount
     */
    public int getPropCount()
    {
        return _propList.size();
    } //-- int getPropCount() 

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
     * Method removeAllProp
     */
    public void removeAllProp()
    {
        _propList.removeAllElements();
    } //-- void removeAllProp() 

    /**
     * Method removeProp
     * 
     * @param index
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Prop removeProp(int index)
    {
        java.lang.Object obj = _propList.elementAt(index);
        _propList.removeElementAt(index);
        return (de.cismet.cids.admin.importAnt.castorGenerated.Prop) obj;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Prop removeProp(int) 

    /**
     * Sets the value of field 'className'.
     * 
     * @param className the value of field 'className'.
     */
    public void setClassName(java.lang.String className)
    {
        this._className = className;
    } //-- void setClassName(java.lang.String) 

    /**
     * Method setProp
     * 
     * @param index
     * @param vProp
     */
    public void setProp(int index, de.cismet.cids.admin.importAnt.castorGenerated.Prop vProp)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _propList.setElementAt(vProp, index);
    } //-- void setProp(int, de.cismet.cids.admin.importAnt.castorGenerated.Prop) 

    /**
     * Method setProp
     * 
     * @param propArray
     */
    public void setProp(de.cismet.cids.admin.importAnt.castorGenerated.Prop[] propArray)
    {
        //-- copy array
        _propList.removeAllElements();
        for (int i = 0; i < propArray.length; i++) {
            _propList.addElement(propArray[i]);
        }
    } //-- void setProp(de.cismet.cids.admin.importAnt.castorGenerated.Prop) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.Finalizer unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.Finalizer) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.Finalizer.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Finalizer unmarshal(java.io.Reader) 

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
