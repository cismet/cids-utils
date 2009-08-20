/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: RuntimeProps.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Class RuntimeProps.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class RuntimeProps implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _keepCode
     */
    private boolean _keepCode;

    /**
     * keeps track of state for field: _keepCode
     */
    private boolean _has_keepCode;

    /**
     * Field _tmpDir
     */
    private java.lang.String _tmpDir;

    /**
     * Field _finalizer
     */
    private de.cismet.cids.admin.importAnt.castorGenerated.Finalizer _finalizer;


      //----------------/
     //- Constructors -/
    //----------------/

    public RuntimeProps() {
        super();
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteKeepCode
     */
    public void deleteKeepCode()
    {
        this._has_keepCode= false;
    } //-- void deleteKeepCode() 

    /**
     * Returns the value of field 'finalizer'.
     * 
     * @return the value of field 'finalizer'.
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Finalizer getFinalizer()
    {
        return this._finalizer;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Finalizer getFinalizer() 

    /**
     * Returns the value of field 'keepCode'.
     * 
     * @return the value of field 'keepCode'.
     */
    public boolean getKeepCode()
    {
        return this._keepCode;
    } //-- boolean getKeepCode() 

    /**
     * Returns the value of field 'tmpDir'.
     * 
     * @return the value of field 'tmpDir'.
     */
    public java.lang.String getTmpDir()
    {
        return this._tmpDir;
    } //-- java.lang.String getTmpDir() 

    /**
     * Method hasKeepCode
     */
    public boolean hasKeepCode()
    {
        return this._has_keepCode;
    } //-- boolean hasKeepCode() 

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
     * Sets the value of field 'finalizer'.
     * 
     * @param finalizer the value of field 'finalizer'.
     */
    public void setFinalizer(de.cismet.cids.admin.importAnt.castorGenerated.Finalizer finalizer)
    {
        this._finalizer = finalizer;
    } //-- void setFinalizer(de.cismet.cids.admin.importAnt.castorGenerated.Finalizer) 

    /**
     * Sets the value of field 'keepCode'.
     * 
     * @param keepCode the value of field 'keepCode'.
     */
    public void setKeepCode(boolean keepCode)
    {
        this._keepCode = keepCode;
        this._has_keepCode = true;
    } //-- void setKeepCode(boolean) 

    /**
     * Sets the value of field 'tmpDir'.
     * 
     * @param tmpDir the value of field 'tmpDir'.
     */
    public void setTmpDir(java.lang.String tmpDir)
    {
        this._tmpDir = tmpDir;
    } //-- void setTmpDir(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps unmarshal(java.io.Reader) 

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
