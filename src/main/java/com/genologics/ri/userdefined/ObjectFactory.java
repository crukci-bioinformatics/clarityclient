/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2013 Cancer Research UK Cambridge Institute.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.genologics.ri.userdefined;

import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.userdefined package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Field_QNAME = new QName(UDF_NAMESPACE, "field");
    private final static QName _Type_QNAME = new QName(UDF_NAMESPACE, "type");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.userdefined
     *
     */
    public ObjectFactory() {
    }

    public UDT createType() {
        return new UDT();
    }

    public UDF createField() {
        return new UDF();
    }

    @XmlElementDecl(namespace = UDF_NAMESPACE, name = "field")
    public JAXBElement<UDF> createField(UDF value) {
        return new JAXBElement<UDF>(_Field_QNAME, UDF.class, null, value);
    }

    @XmlElementDecl(namespace = UDF_NAMESPACE, name = "type")
    public JAXBElement<UDT> createType(UDT value) {
        return new JAXBElement<UDT>(_Type_QNAME, UDT.class, null, value);
    }
}
