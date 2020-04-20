/*
 * CRUK-CI Genologics REST API Java Client.
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

package com.genologics.ri.property;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.genologics.ri.Namespaces;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.property package.
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

    private final static QName _Properties_QNAME = new QName(Namespaces.PROPERTY_NAMESPACE, "properties");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.property
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PropertyLink }
     *
     */
    public PropertyLink createPropertyLink() {
        return new PropertyLink();
    }

    /**
     * Create an instance of {@link Properties }
     *
     */
    public Properties createProperties() {
        return new Properties();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Properties }{@code >}}
     *
     */
    @XmlElementDecl(namespace = Namespaces.PROPERTY_NAMESPACE, name = "properties")
    public JAXBElement<Properties> createProperties(Properties value) {
        return new JAXBElement<Properties>(_Properties_QNAME, Properties.class, null, value);
    }

}
