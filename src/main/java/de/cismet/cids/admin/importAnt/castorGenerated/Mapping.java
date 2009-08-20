/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Mapping.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
 */

package de.cismet.cids.admin.importAnt.castorGenerated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Mapping.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class Mapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * Field _path
     */
    private java.lang.String _path;

    /**
     * Field _targetTable
     */
    private java.lang.String _targetTable;

    /**
     * Field _targetField
     */
    private java.lang.String _targetField;

    /**
     * Field _autoIncrement
     */
    private boolean _autoIncrement;

    /**
     * keeps track of state for field: _autoIncrement
     */
    private boolean _has_autoIncrement;

    /**
     * Field _comparing
     */
    private boolean _comparing;

    /**
     * keeps track of state for field: _comparing
     */
    private boolean _has_comparing;

    /**
     * Field _enclosingChar
     */
    private java.lang.String _enclosingChar;


      //----------------/
     //- Constructors -/
    //----------------/

    public Mapping() {
        super();
        setContent("");
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Mapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteAutoIncrement
     */
    public void deleteAutoIncrement()
    {
        this._has_autoIncrement= false;
    } //-- void deleteAutoIncrement() 

    /**
     * Method deleteComparing
     */
    public void deleteComparing()
    {
        this._has_comparing= false;
    } //-- void deleteComparing() 

    /**
     * Returns the value of field 'autoIncrement'.
     * 
     * @return the value of field 'autoIncrement'.
     */
    public boolean getAutoIncrement()
    {
        return this._autoIncrement;
    } //-- boolean getAutoIncrement() 

    /**
     * Returns the value of field 'comparing'.
     * 
     * @return the value of field 'comparing'.
     */
    public boolean getComparing()
    {
        return this._comparing;
    } //-- boolean getComparing() 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

    /**
     * Returns the value of field 'enclosingChar'.
     * 
     * @return the value of field 'enclosingChar'.
     */
    public java.lang.String getEnclosingChar()
    {
        return this._enclosingChar;
    } //-- java.lang.String getEnclosingChar() 

    /**
     * Returns the value of field 'path'.
     * 
     * @return the value of field 'path'.
     */
    public java.lang.String getPath()
    {
        return this._path;
    } //-- java.lang.String getPath() 

    /**
     * Returns the value of field 'targetField'.
     * 
     * @return the value of field 'targetField'.
     */
    public java.lang.String getTargetField()
    {
        return this._targetField;
    } //-- java.lang.String getTargetField() 

    /**
     * Returns the value of field 'targetTable'.
     * 
     * @return the value of field 'targetTable'.
     */
    public java.lang.String getTargetTable()
    {
        return this._targetTable;
    } //-- java.lang.String getTargetTable() 

    /**
     * Method hasAutoIncrement
     */
    public boolean hasAutoIncrement()
    {
        return this._has_autoIncrement;
    } //-- boolean hasAutoIncrement() 

    /**
     * Method hasComparing
     */
    public boolean hasComparing()
    {
        return this._has_comparing;
    } //-- boolean hasComparing() 

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
     * Sets the value of field 'autoIncrement'.
     * 
     * @param autoIncrement the value of field 'autoIncrement'.
     */
    public void setAutoIncrement(boolean autoIncrement)
    {
        this._autoIncrement = autoIncrement;
        this._has_autoIncrement = true;
    } //-- void setAutoIncrement(boolean) 

    /**
     * Sets the value of field 'comparing'.
     * 
     * @param comparing the value of field 'comparing'.
     */
    public void setComparing(boolean comparing)
    {
        this._comparing = comparing;
        this._has_comparing = true;
    } //-- void setComparing(boolean) 

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Sets the value of field 'enclosingChar'.
     * 
     * @param enclosingChar the value of field 'enclosingChar'.
     */
    public void setEnclosingChar(java.lang.String enclosingChar)
    {
        this._enclosingChar = enclosingChar;
    } //-- void setEnclosingChar(java.lang.String) 

    /**
     * Sets the value of field 'path'.
     * 
     * @param path the value of field 'path'.
     */
    public void setPath(java.lang.String path)
    {
        this._path = path;
    } //-- void setPath(java.lang.String) 

    /**
     * Sets the value of field 'targetField'.
     * 
     * @param targetField the value of field 'targetField'.
     */
    public void setTargetField(java.lang.String targetField)
    {
        this._targetField = targetField;
    } //-- void setTargetField(java.lang.String) 

    /**
     * Sets the value of field 'targetTable'.
     * 
     * @param targetTable the value of field 'targetTable'.
     */
    public void setTargetTable(java.lang.String targetTable)
    {
        this._targetTable = targetTable;
    } //-- void setTargetTable(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.Mapping unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.Mapping) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.Mapping.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Mapping unmarshal(java.io.Reader) 

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
