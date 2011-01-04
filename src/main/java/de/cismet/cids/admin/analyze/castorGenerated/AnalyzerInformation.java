/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AnalyzerInformation.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
 */
package de.cismet.cids.admin.analyze.castorGenerated;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Comment describing your root element.
 *
 * @version  $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class AnalyzerInformation implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field _groupList. */
    private java.util.Vector _groupList;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public AnalyzerInformation() {
        super();
        _groupList = new Vector();
    } // -- de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method addGroup.
     *
     * @param   vGroup  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */
    public void addGroup(final de.cismet.cids.admin.analyze.castorGenerated.Group vGroup)
            throws java.lang.IndexOutOfBoundsException {
        _groupList.addElement(vGroup);
    } // -- void addGroup(de.cismet.cids.admin.analyze.castorGenerated.Group)

    /**
     * Method addGroup.
     *
     * @param   index   DOCUMENT ME!
     * @param   vGroup  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */
    public void addGroup(final int index, final de.cismet.cids.admin.analyze.castorGenerated.Group vGroup)
            throws java.lang.IndexOutOfBoundsException {
        _groupList.insertElementAt(vGroup, index);
    } // -- void addGroup(int, de.cismet.cids.admin.analyze.castorGenerated.Group)

    /**
     * Method enumerateGroup.
     *
     * @return  DOCUMENT ME!
     */
    public java.util.Enumeration enumerateGroup() {
        return _groupList.elements();
    } // -- java.util.Enumeration enumerateGroup()

    /**
     * Method getGroup.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Group getGroup(final int index)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (de.cismet.cids.admin.analyze.castorGenerated.Group)_groupList.elementAt(index);
    } // -- de.cismet.cids.admin.analyze.castorGenerated.Group getGroup(int)

    /**
     * Method getGroup.
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Group[] getGroup() {
        final int size = _groupList.size();
        final de.cismet.cids.admin.analyze.castorGenerated.Group[] mArray =
            new de.cismet.cids.admin.analyze.castorGenerated.Group[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.cids.admin.analyze.castorGenerated.Group)_groupList.elementAt(index);
        }
        return mArray;
    } // -- de.cismet.cids.admin.analyze.castorGenerated.Group[] getGroup()

    /**
     * Method getGroupCount.
     *
     * @return  DOCUMENT ME!
     */
    public int getGroupCount() {
        return _groupList.size();
    } // -- int getGroupCount()

    /**
     * Method isValid.
     *
     * @return  DOCUMENT ME!
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } // -- boolean isValid()

    /**
     * Method marshal.
     *
     * @param   out  DOCUMENT ME!
     *
     * @throws  org.exolab.castor.xml.MarshalException     DOCUMENT ME!
     * @throws  org.exolab.castor.xml.ValidationException  DOCUMENT ME!
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException,
        org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    } // -- void marshal(java.io.Writer)

    /**
     * Method marshal.
     *
     * @param   handler  DOCUMENT ME!
     *
     * @throws  java.io.IOException                        DOCUMENT ME!
     * @throws  org.exolab.castor.xml.MarshalException     DOCUMENT ME!
     * @throws  org.exolab.castor.xml.ValidationException  DOCUMENT ME!
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException,
        org.exolab.castor.xml.MarshalException,
        org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    } // -- void marshal(org.xml.sax.ContentHandler)

    /**
     * Method removeAllGroup.
     */
    public void removeAllGroup() {
        _groupList.removeAllElements();
    } // -- void removeAllGroup()

    /**
     * Method removeGroup.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Group removeGroup(final int index) {
        final java.lang.Object obj = _groupList.elementAt(index);
        _groupList.removeElementAt(index);
        return (de.cismet.cids.admin.analyze.castorGenerated.Group)obj;
    } // -- de.cismet.cids.admin.analyze.castorGenerated.Group removeGroup(int)

    /**
     * Method setGroup.
     *
     * @param   index   DOCUMENT ME!
     * @param   vGroup  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */
    public void setGroup(final int index, final de.cismet.cids.admin.analyze.castorGenerated.Group vGroup)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupList.setElementAt(vGroup, index);
    } // -- void setGroup(int, de.cismet.cids.admin.analyze.castorGenerated.Group)

    /**
     * Method setGroup.
     *
     * @param  groupArray  DOCUMENT ME!
     */
    public void setGroup(final de.cismet.cids.admin.analyze.castorGenerated.Group[] groupArray) {
        // -- copy array
        _groupList.removeAllElements();
        for (int i = 0; i < groupArray.length; i++) {
            _groupList.addElement(groupArray[i]);
        }
    } // -- void setGroup(de.cismet.cids.admin.analyze.castorGenerated.Group)

    /**
     * Method unmarshal.
     *
     * @param   reader  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  org.exolab.castor.xml.MarshalException     DOCUMENT ME!
     * @throws  org.exolab.castor.xml.ValidationException  DOCUMENT ME!
     */
    public static de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation unmarshal(
            final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException,
        org.exolab.castor.xml.ValidationException {
        return (de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation)Unmarshaller.unmarshal(
                de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation.class,
                reader);
    } // -- de.cismet.cids.admin.analyze.castorGenerated.AnalyzerInformation unmarshal(java.io.Reader)

    /**
     * Method validate.
     *
     * @throws  org.exolab.castor.xml.ValidationException  DOCUMENT ME!
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        final org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()
}
