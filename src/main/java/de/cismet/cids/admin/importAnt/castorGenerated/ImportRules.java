/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ImportRules.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Rules to import 
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class ImportRules implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _connectionInfo
     */
    private de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo _connectionInfo;

    /**
     * Field _preProcessingAndMapping
     */
    private de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping _preProcessingAndMapping;

    /**
     * Field _relations
     */
    private de.cismet.cids.admin.importAnt.castorGenerated.Relations _relations;

    /**
     * Field _options
     */
    private de.cismet.cids.admin.importAnt.castorGenerated.Options _options;

    /**
     * Field _code
     */
    private de.cismet.cids.admin.importAnt.castorGenerated.Code _code;

    /**
     * Field _runtimeProps
     */
    private de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps _runtimeProps;


      //----------------/
     //- Constructors -/
    //----------------/

    public ImportRules() {
        super();
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.ImportRules()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'code'.
     * 
     * @return the value of field 'code'.
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Code getCode()
    {
        return this._code;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Code getCode() 

    /**
     * Returns the value of field 'connectionInfo'.
     * 
     * @return the value of field 'connectionInfo'.
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo getConnectionInfo()
    {
        return this._connectionInfo;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo getConnectionInfo() 

    /**
     * Returns the value of field 'options'.
     * 
     * @return the value of field 'options'.
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Options getOptions()
    {
        return this._options;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Options getOptions() 

    /**
     * Returns the value of field 'preProcessingAndMapping'.
     * 
     * @return the value of field 'preProcessingAndMapping'.
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping getPreProcessingAndMapping()
    {
        return this._preProcessingAndMapping;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping getPreProcessingAndMapping() 

    /**
     * Returns the value of field 'relations'.
     * 
     * @return the value of field 'relations'.
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Relations getRelations()
    {
        return this._relations;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Relations getRelations() 

    /**
     * Returns the value of field 'runtimeProps'.
     * 
     * @return the value of field 'runtimeProps'.
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps getRuntimeProps()
    {
        return this._runtimeProps;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps getRuntimeProps() 

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
     * Sets the value of field 'code'.
     * 
     * @param code the value of field 'code'.
     */
    public void setCode(de.cismet.cids.admin.importAnt.castorGenerated.Code code)
    {
        this._code = code;
    } //-- void setCode(de.cismet.cids.admin.importAnt.castorGenerated.Code) 

    /**
     * Sets the value of field 'connectionInfo'.
     * 
     * @param connectionInfo the value of field 'connectionInfo'.
     */
    public void setConnectionInfo(de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo connectionInfo)
    {
        this._connectionInfo = connectionInfo;
    } //-- void setConnectionInfo(de.cismet.cids.admin.importAnt.castorGenerated.ConnectionInfo) 

    /**
     * Sets the value of field 'options'.
     * 
     * @param options the value of field 'options'.
     */
    public void setOptions(de.cismet.cids.admin.importAnt.castorGenerated.Options options)
    {
        this._options = options;
    } //-- void setOptions(de.cismet.cids.admin.importAnt.castorGenerated.Options) 

    /**
     * Sets the value of field 'preProcessingAndMapping'.
     * 
     * @param preProcessingAndMapping the value of field
     * 'preProcessingAndMapping'.
     */
    public void setPreProcessingAndMapping(de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping preProcessingAndMapping)
    {
        this._preProcessingAndMapping = preProcessingAndMapping;
    } //-- void setPreProcessingAndMapping(de.cismet.cids.admin.importAnt.castorGenerated.PreProcessingAndMapping) 

    /**
     * Sets the value of field 'relations'.
     * 
     * @param relations the value of field 'relations'.
     */
    public void setRelations(de.cismet.cids.admin.importAnt.castorGenerated.Relations relations)
    {
        this._relations = relations;
    } //-- void setRelations(de.cismet.cids.admin.importAnt.castorGenerated.Relations) 

    /**
     * Sets the value of field 'runtimeProps'.
     * 
     * @param runtimeProps the value of field 'runtimeProps'.
     */
    public void setRuntimeProps(de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps runtimeProps)
    {
        this._runtimeProps = runtimeProps;
    } //-- void setRuntimeProps(de.cismet.cids.admin.importAnt.castorGenerated.RuntimeProps) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.ImportRules unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.ImportRules) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.ImportRules.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.ImportRules unmarshal(java.io.Reader) 

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
