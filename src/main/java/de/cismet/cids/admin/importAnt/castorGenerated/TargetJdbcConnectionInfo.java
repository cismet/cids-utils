/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TargetJdbcConnectionInfo.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Class TargetJdbcConnectionInfo.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class TargetJdbcConnectionInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _driverClass
     */
    private java.lang.String _driverClass;

    /**
     * Field _url
     */
    private java.lang.String _url;

    /**
     * Field _user
     */
    private java.lang.String _user;

    /**
     * Field _pass
     */
    private java.lang.String _pass;

    /**
     * Field _propList
     */
    private java.util.Vector _propList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TargetJdbcConnectionInfo() {
        super();
        _propList = new Vector();
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo()


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
     * Returns the value of field 'driverClass'.
     * 
     * @return the value of field 'driverClass'.
     */
    public java.lang.String getDriverClass()
    {
        return this._driverClass;
    } //-- java.lang.String getDriverClass() 

    /**
     * Returns the value of field 'pass'.
     * 
     * @return the value of field 'pass'.
     */
    public java.lang.String getPass()
    {
        return this._pass;
    } //-- java.lang.String getPass() 

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
     * Returns the value of field 'url'.
     * 
     * @return the value of field 'url'.
     */
    public java.lang.String getUrl()
    {
        return this._url;
    } //-- java.lang.String getUrl() 

    /**
     * Returns the value of field 'user'.
     * 
     * @return the value of field 'user'.
     */
    public java.lang.String getUser()
    {
        return this._user;
    } //-- java.lang.String getUser() 

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
     * Sets the value of field 'driverClass'.
     * 
     * @param driverClass the value of field 'driverClass'.
     */
    public void setDriverClass(java.lang.String driverClass)
    {
        this._driverClass = driverClass;
    } //-- void setDriverClass(java.lang.String) 

    /**
     * Sets the value of field 'pass'.
     * 
     * @param pass the value of field 'pass'.
     */
    public void setPass(java.lang.String pass)
    {
        this._pass = pass;
    } //-- void setPass(java.lang.String) 

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
     * Sets the value of field 'url'.
     * 
     * @param url the value of field 'url'.
     */
    public void setUrl(java.lang.String url)
    {
        this._url = url;
    } //-- void setUrl(java.lang.String) 

    /**
     * Sets the value of field 'user'.
     * 
     * @param user the value of field 'user'.
     */
    public void setUser(java.lang.String user)
    {
        this._user = user;
    } //-- void setUser(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo unmarshal(java.io.Reader) 

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
