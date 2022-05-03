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

package com.genologics.ri.configuration;

import static com.genologics.ri.Namespaces.CONFIGURATION_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.configuration package.
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

    private final static QName _Type_QNAME = new QName(CONFIGURATION_NAMESPACE, "type");
    private final static QName _UdtconfigLink_QNAME = new QName(CONFIGURATION_NAMESPACE, "udtconfig-link");
    private final static QName _Udfs_QNAME = new QName(CONFIGURATION_NAMESPACE, "udfs");
    private final static QName _UdfconfigLink_QNAME = new QName(CONFIGURATION_NAMESPACE, "udfconfig-link");
    private final static QName _Udts_QNAME = new QName(CONFIGURATION_NAMESPACE, "udts");
    private final static QName _Field_QNAME = new QName(CONFIGURATION_NAMESPACE, "field");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.configuration
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Udts }
     *
     */
    public Udts createUdts() {
        return new Udts();
    }

    /**
     * Create an instance of {@link FieldLink }
     *
     */
    public FieldLink createFieldLink() {
        return new FieldLink();
    }

    /**
     * Create an instance of {@link Udfs }
     *
     */
    public Udfs createUdfs() {
        return new Udfs();
    }

    /**
     * Create an instance of {@link UdfConfigLink }
     *
     */
    public UdfConfigLink createUdfconfigLink() {
        return new UdfConfigLink();
    }

    /**
     * Create an instance of {@link TypeDefinition }
     *
     */
    public TypeDefinition createTypeDefinition() {
        return new TypeDefinition();
    }

    /**
     * Create an instance of {@link Field }
     *
     */
    public Field createField() {
        return new Field();
    }

    /**
     * Create an instance of {@link UdtConfigLink }
     *
     */
    public UdtConfigLink createUdtconfigLink() {
        return new UdtConfigLink();
    }

    /**
     * Create an instance of {@link Type }
     *
     */
    public Type createType() {
        return new Type();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Type }{@code >}}
     *
     */
    @XmlElementDecl(namespace = CONFIGURATION_NAMESPACE, name = "type")
    public JAXBElement<Type> createType(Type value) {
        return new JAXBElement<Type>(_Type_QNAME, Type.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UdtConfigLink }{@code >}}
     *
     */
    @XmlElementDecl(namespace = CONFIGURATION_NAMESPACE, name = "udtconfig-link")
    public JAXBElement<UdtConfigLink> createUdtconfigLink(UdtConfigLink value) {
        return new JAXBElement<UdtConfigLink>(_UdtconfigLink_QNAME, UdtConfigLink.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Udfs }{@code >}}
     *
     */
    @XmlElementDecl(namespace = CONFIGURATION_NAMESPACE, name = "udfs")
    public JAXBElement<Udfs> createUdfs(Udfs value) {
        return new JAXBElement<Udfs>(_Udfs_QNAME, Udfs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UdfConfigLink }{@code >}}
     *
     */
    @XmlElementDecl(namespace = CONFIGURATION_NAMESPACE, name = "udfconfig-link")
    public JAXBElement<UdfConfigLink> createUdfconfigLink(UdfConfigLink value) {
        return new JAXBElement<UdfConfigLink>(_UdfconfigLink_QNAME, UdfConfigLink.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Udts }{@code >}}
     *
     */
    @XmlElementDecl(namespace = CONFIGURATION_NAMESPACE, name = "udts")
    public JAXBElement<Udts> createUdts(Udts value) {
        return new JAXBElement<Udts>(_Udts_QNAME, Udts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Field }{@code >}}
     *
     */
    @XmlElementDecl(namespace = CONFIGURATION_NAMESPACE, name = "field")
    public JAXBElement<Field> createField(Field value) {
        return new JAXBElement<Field>(_Field_QNAME, Field.class, null, value);
    }

}
