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

package com.genologics.ri.lab;

import static com.genologics.ri.Namespaces.LAB_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.lab package.
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

    private final static QName _Labs_QNAME = new QName(LAB_NAMESPACE, "labs");
    private final static QName _Lab_QNAME = new QName(LAB_NAMESPACE, "lab");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.lab
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Lab }
     *
     */
    public Lab createLab() {
        return new Lab();
    }

    /**
     * Create an instance of {@link Labs }
     *
     */
    public Labs createLabs() {
        return new Labs();
    }

    /**
     * Create an instance of {@link LabLink }
     *
     */
    public LabLink createLabLink() {
        return new LabLink();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Labs }{@code >}}
     *
     */
    @XmlElementDecl(namespace = LAB_NAMESPACE, name = "labs")
    public JAXBElement<Labs> createLabs(Labs value) {
        return new JAXBElement<Labs>(_Labs_QNAME, Labs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Lab }{@code >}}
     *
     */
    @XmlElementDecl(namespace = LAB_NAMESPACE, name = "lab")
    public JAXBElement<Lab> createLab(Lab value) {
        return new JAXBElement<Lab>(_Lab_QNAME, Lab.class, null, value);
    }

}
