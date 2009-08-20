/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Relations.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Class Relations.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class Relations implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _relationList
     */
    private java.util.Vector _relationList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Relations() {
        super();
        _relationList = new Vector();
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Relations()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRelation
     * 
     * @param vRelation
     */
    public void addRelation(de.cismet.cids.admin.importAnt.castorGenerated.Relation vRelation)
        throws java.lang.IndexOutOfBoundsException
    {
        _relationList.addElement(vRelation);
    } //-- void addRelation(de.cismet.cids.admin.importAnt.castorGenerated.Relation) 

    /**
     * Method addRelation
     * 
     * @param index
     * @param vRelation
     */
    public void addRelation(int index, de.cismet.cids.admin.importAnt.castorGenerated.Relation vRelation)
        throws java.lang.IndexOutOfBoundsException
    {
        _relationList.insertElementAt(vRelation, index);
    } //-- void addRelation(int, de.cismet.cids.admin.importAnt.castorGenerated.Relation) 

    /**
     * Method enumerateRelation
     */
    public java.util.Enumeration enumerateRelation()
    {
        return _relationList.elements();
    } //-- java.util.Enumeration enumerateRelation() 

    /**
     * Method getRelation
     * 
     * @param index
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Relation getRelation(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (de.cismet.cids.admin.importAnt.castorGenerated.Relation) _relationList.elementAt(index);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Relation getRelation(int) 

    /**
     * Method getRelation
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Relation[] getRelation()
    {
        int size = _relationList.size();
        de.cismet.cids.admin.importAnt.castorGenerated.Relation[] mArray = new de.cismet.cids.admin.importAnt.castorGenerated.Relation[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.cids.admin.importAnt.castorGenerated.Relation) _relationList.elementAt(index);
        }
        return mArray;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Relation[] getRelation() 

    /**
     * Method getRelationCount
     */
    public int getRelationCount()
    {
        return _relationList.size();
    } //-- int getRelationCount() 

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
     * Method removeAllRelation
     */
    public void removeAllRelation()
    {
        _relationList.removeAllElements();
    } //-- void removeAllRelation() 

    /**
     * Method removeRelation
     * 
     * @param index
     */
    public de.cismet.cids.admin.importAnt.castorGenerated.Relation removeRelation(int index)
    {
        java.lang.Object obj = _relationList.elementAt(index);
        _relationList.removeElementAt(index);
        return (de.cismet.cids.admin.importAnt.castorGenerated.Relation) obj;
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Relation removeRelation(int) 

    /**
     * Method setRelation
     * 
     * @param index
     * @param vRelation
     */
    public void setRelation(int index, de.cismet.cids.admin.importAnt.castorGenerated.Relation vRelation)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _relationList.setElementAt(vRelation, index);
    } //-- void setRelation(int, de.cismet.cids.admin.importAnt.castorGenerated.Relation) 

    /**
     * Method setRelation
     * 
     * @param relationArray
     */
    public void setRelation(de.cismet.cids.admin.importAnt.castorGenerated.Relation[] relationArray)
    {
        //-- copy array
        _relationList.removeAllElements();
        for (int i = 0; i < relationArray.length; i++) {
            _relationList.addElement(relationArray[i]);
        }
    } //-- void setRelation(de.cismet.cids.admin.importAnt.castorGenerated.Relation) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static de.cismet.cids.admin.importAnt.castorGenerated.Relations unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.cismet.cids.admin.importAnt.castorGenerated.Relations) Unmarshaller.unmarshal(de.cismet.cids.admin.importAnt.castorGenerated.Relations.class, reader);
    } //-- de.cismet.cids.admin.importAnt.castorGenerated.Relations unmarshal(java.io.Reader) 

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
