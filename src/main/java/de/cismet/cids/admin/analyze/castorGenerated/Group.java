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
 * $Id: Group.java,v 1.1.1.1 2009-08-20 12:23:31 spuhl Exp $
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
 * Class Group.
 *
 * @version  $Revision: 1.1.1.1 $ $Date: 2009-08-20 12:23:31 $
 */
public class Group implements java.io.Serializable {

    //~ Instance fields --------------------------------------------------------

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** Field _package. */
    private java.lang.String _package;

    /** Field _description. */
    private java.lang.String _description;

    /** Field _analyzerList. */
    private java.util.Vector _analyzerList;

    //~ Constructors -----------------------------------------------------------

    /**
     * ----------------/ - Constructors -/ ----------------/
     */
    public Group() {
        super();
        _analyzerList = new Vector();
    } // -- de.cismet.cids.admin.analyze.castorGenerated.Group()

    //~ Methods ----------------------------------------------------------------

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method addAnalyzer.
     *
     * @param   vAnalyzer  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */
    public void addAnalyzer(final de.cismet.cids.admin.analyze.castorGenerated.Analyzer vAnalyzer)
            throws java.lang.IndexOutOfBoundsException {
        _analyzerList.addElement(vAnalyzer);
    } // -- void addAnalyzer(de.cismet.cids.admin.analyze.castorGenerated.Analyzer)

    /**
     * Method addAnalyzer.
     *
     * @param   index      DOCUMENT ME!
     * @param   vAnalyzer  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     */
    public void addAnalyzer(final int index, final de.cismet.cids.admin.analyze.castorGenerated.Analyzer vAnalyzer)
            throws java.lang.IndexOutOfBoundsException {
        _analyzerList.insertElementAt(vAnalyzer, index);
    } // -- void addAnalyzer(int, de.cismet.cids.admin.analyze.castorGenerated.Analyzer)

    /**
     * Method enumerateAnalyzer.
     *
     * @return  DOCUMENT ME!
     */
    public java.util.Enumeration enumerateAnalyzer() {
        return _analyzerList.elements();
    } // -- java.util.Enumeration enumerateAnalyzer()

    /**
     * Method getAnalyzer.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Analyzer getAnalyzer(final int index)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _analyzerList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (de.cismet.cids.admin.analyze.castorGenerated.Analyzer)_analyzerList.elementAt(index);
    } // -- de.cismet.cids.admin.analyze.castorGenerated.Analyzer getAnalyzer(int)

    /**
     * Method getAnalyzer.
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Analyzer[] getAnalyzer() {
        final int size = _analyzerList.size();
        final de.cismet.cids.admin.analyze.castorGenerated.Analyzer[] mArray =
            new de.cismet.cids.admin.analyze.castorGenerated.Analyzer[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (de.cismet.cids.admin.analyze.castorGenerated.Analyzer)_analyzerList.elementAt(index);
        }
        return mArray;
    } // -- de.cismet.cids.admin.analyze.castorGenerated.Analyzer[] getAnalyzer()

    /**
     * Method getAnalyzerCount.
     *
     * @return  DOCUMENT ME!
     */
    public int getAnalyzerCount() {
        return _analyzerList.size();
    } // -- int getAnalyzerCount()

    /**
     * Returns the value of field 'description'.
     *
     * @return  the value of field 'description'.
     */
    public java.lang.String getDescription() {
        return this._description;
    } // -- java.lang.String getDescription()

    /**
     * Returns the value of field 'package'.
     *
     * @return  the value of field 'package'.
     */
    public java.lang.String getPackage() {
        return this._package;
    } // -- java.lang.String getPackage()

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
     * Method removeAllAnalyzer.
     */
    public void removeAllAnalyzer() {
        _analyzerList.removeAllElements();
    } // -- void removeAllAnalyzer()

    /**
     * Method removeAnalyzer.
     *
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public de.cismet.cids.admin.analyze.castorGenerated.Analyzer removeAnalyzer(final int index) {
        final java.lang.Object obj = _analyzerList.elementAt(index);
        _analyzerList.removeElementAt(index);
        return (de.cismet.cids.admin.analyze.castorGenerated.Analyzer)obj;
    } // -- de.cismet.cids.admin.analyze.castorGenerated.Analyzer removeAnalyzer(int)

    /**
     * Method setAnalyzer.
     *
     * @param   index      DOCUMENT ME!
     * @param   vAnalyzer  DOCUMENT ME!
     *
     * @throws  java.lang.IndexOutOfBoundsException  DOCUMENT ME!
     * @throws  IndexOutOfBoundsException            DOCUMENT ME!
     */
    public void setAnalyzer(final int index, final de.cismet.cids.admin.analyze.castorGenerated.Analyzer vAnalyzer)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _analyzerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _analyzerList.setElementAt(vAnalyzer, index);
    } // -- void setAnalyzer(int, de.cismet.cids.admin.analyze.castorGenerated.Analyzer)

    /**
     * Method setAnalyzer.
     *
     * @param  analyzerArray  DOCUMENT ME!
     */
    public void setAnalyzer(final de.cismet.cids.admin.analyze.castorGenerated.Analyzer[] analyzerArray) {
        // -- copy array
        _analyzerList.removeAllElements();
        for (int i = 0; i < analyzerArray.length; i++) {
            _analyzerList.addElement(analyzerArray[i]);
        }
    } // -- void setAnalyzer(de.cismet.cids.admin.analyze.castorGenerated.Analyzer)

    /**
     * Sets the value of field 'description'.
     *
     * @param  description  the value of field 'description'.
     */
    public void setDescription(final java.lang.String description) {
        this._description = description;
    } // -- void setDescription(java.lang.String)

    /**
     * Sets the value of field 'package'.
     *
     * @param  _package  DOCUMENT ME!
     */
    public void setPackage(final java.lang.String _package) {
        this._package = _package;
    } // -- void setPackage(java.lang.String)

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
    public static de.cismet.cids.admin.analyze.castorGenerated.Group unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (de.cismet.cids.admin.analyze.castorGenerated.Group)Unmarshaller.unmarshal(
                de.cismet.cids.admin.analyze.castorGenerated.Group.class,
                reader);
    } // -- de.cismet.cids.admin.analyze.castorGenerated.Group unmarshal(java.io.Reader)

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
