/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ConnectionInfo.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Class ConnectionInfo.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class ConnectionInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sourceJdbcConnectionInfo
     */
    private de.cismet.cids.admin.importAnt.castorGenerated.SourceJdbcConnectionInfo _sourceJdbcConnectionInfo;

    /**
     * Field _targetJdbcConnectionInfo
     */
    private de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo _targetJdbcConnectionInfo;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConnectionInfo() {
        super();
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'sourceJdbcConnectionInfo'.
     * 
     * @return the value of field 'sourceJdbcConnectionInfo'.
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.SourceJdbcConnectionInfo getSourceJdbcConnectionInfo()
    {
        return this._sourceJdbcConnectionInfo;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.SourceJdbcConnectionInfo getSourceJdbcConnectionInfo() 

    /**
     * Returns the value of field 'targetJdbcConnectionInfo'.
     * 
     * @return the value of field 'targetJdbcConnectionInfo'.
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo getTargetJdbcConnectionInfo()
    {
        return this._targetJdbcConnectionInfo;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo getTargetJdbcConnectionInfo() 

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
     * Sets the value of field 'sourceJdbcConnectionInfo'.
     * 
     * @param sourceJdbcConnectionInfo the value of field
     * 'sourceJdbcConnectionInfo'.
     */
    public void setSourceJdbcConnectionInfo(de.cismet.cids.admin.importAnt.castorGenerated.SourceJdbcConnectionInfo sourceJdbcConnectionInfo)
    {
        this._sourceJdbcConnectionInfo = sourceJdbcConnectionInfo;
    } //-- void setSourceJdbcConnectionInfo(de.cismet.cids.admin.importAnt.castorGenerated.SourceJdbcConnectionInfo) 

    /**
     * Sets the value of field 'targetJdbcConnectionInfo'.
     * 
     * @param targetJdbcConnectionInfo the value of field
     * 'targetJdbcConnectionInfo'.
     */
    public void setTargetJdbcConnectionInfo(de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo targetJdbcConnectionInfo)
    {
        this._targetJdbcConnectionInfo = targetJdbcConnectionInfo;
    } //-- void setTargetJdbcConnectionInfo(de.cismet.cids.admin.importAnt.castorGenerated.TargetJdbcConnectionInfo) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo unmarshal(java.io.Reader) 

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
