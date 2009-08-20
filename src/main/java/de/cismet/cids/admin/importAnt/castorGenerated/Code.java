/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Code.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Class Code.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class Code implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _import
     */
    private java.lang.String _import;

    /**
     * Field _functionList
     */
    private java.util.Vector _functionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Code() {
        super();
        _functionList = new Vector();
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Code()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFunction
     * 
     * @param vFunction
     */
    public void addFunction(de.cismet.cids.admin.importAnt.castorGenerated.Function vFunction)
        throws java.lang.IndexOutOfBoundsException
    {
        _functionList.addElement(vFunction);
    } //-- void addFunction(de.cismet.cids.admin.importAnt.castorGenerated.Function) 

    /**
     * Method addFunction
     * 
     * @param index
     * @param vFunction
     */
    public void addFunction(int index, de.cismet.cids.admin.importAnt.castorGenerated.Function vFunction)
        throws java.lang.IndexOutOfBoundsException
    {
        _functionList.insertElementAt(vFunction, index);
    } //-- void addFunction(int, de.cismet.cids.admin.importAnt.castorGenerated.Function) 

    /**
     * Method enumerateFunction
     */
    public java.util.Enumeration enumerateFunction()
    {
        return _functionList.elements();
    } //-- java.util.Enumeration enumerateFunction() 

    /**
     * Method getFunction
     * 
     * @param index
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Function getFunction(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _functionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.cismet.cids.admin.importAnt.castorGenerated.Function) _functionList.elementAt(index);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Function getFunction(int) 

    /**
     * Method getFunction
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Function[] getFunction()
    {
        int size = _functionList.size();
        de.cismet.cids.admin.importAnt.castorGenerated.Function[] mArray = new de.cismet.cids.admin.importAnt.castorGenerated.Function[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.cids.admin.importAnt.castorGenerated.Function) _functionList.elementAt(index);
        }
        return mArray;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Function[] getFunction() 

    /**
     * Method getFunctionCount
     */
    public int getFunctionCount()
    {
        return _functionList.size();
    } //-- int getFunctionCount() 

    /**
     * Returns the value of field 'import'.
     * 
     * @return the value of field 'import'.
     */
    public java.lang.String getImport()
    {
        return this._import;
    } //-- java.lang.String getImport() 

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
     * Method removeAllFunction
     */
    public void removeAllFunction()
    {
        _functionList.removeAllElements();
    } //-- void removeAllFunction() 

    /**
     * Method removeFunction
     * 
     * @param index
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Function removeFunction(int index)
    {
        java.lang.Object obj = _functionList.elementAt(index);
        _functionList.removeElementAt(index);
        return (de.cismet.cids.admin.importAnt.castorGenerated.Function) obj;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Function removeFunction(int) 

    /**
     * Method setFunction
     * 
     * @param index
     * @param vFunction
     */
    public void setFunction(int index, de.cismet.cids.admin.importAnt.castorGenerated.Function vFunction)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _functionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _functionList.setElementAt(vFunction, index);
    } //-- void setFunction(int, de.cismet.cids.admin.importAnt.castorGenerated.Function) 

    /**
     * Method setFunction
     * 
     * @param functionArray
     */
    public void setFunction(de.cismet.cids.admin.importAnt.castorGenerated.Function[] functionArray)
    {
        //-- copy array
        _functionList.removeAllElements();
        for (int i = 0; i < functionArray.length; i++) {
            _functionList.addElement(functionArray[i]);
        }
    } //-- void setFunction(de.cismet.cids.admin.importAnt.castorGenerated.Function) 

    /**
     * Sets the value of field 'import'.
     * 
     * @param _import
     * @param import the value of field 'import'.
     */
    public void setImport(java.lang.String _import)
    {
        this._import = _import;
    } //-- void setImport(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.Code unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.Code) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.Code.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Code unmarshal(java.io.Reader) 

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
