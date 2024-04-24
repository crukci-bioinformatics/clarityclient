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

package com.genologics.ri.containertype;

import static com.genologics.ri.Namespaces.CONTAINER_TYPE_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.containertype package.
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

    private final static QName _ContainerType_QNAME = new QName(CONTAINER_TYPE_NAMESPACE, "container-type");
    private final static QName _ContainerTypes_QNAME = new QName(CONTAINER_TYPE_NAMESPACE, "container-types");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.containertype
     *
     */
    public ObjectFactory() {
    }

    @Deprecated
    public CalibrantWell createCalibrantWell() {
        return new CalibrantWell();
    }

    public ContainerType createContainerType() {
        return new ContainerType();
    }

    public ContainerTypes createContainerTypes() {
        return new ContainerTypes();
    }

    public ContainerTypeLink createContainerTypeLink() {
        return new ContainerTypeLink();
    }

    public Dimension createDimension() {
        return new Dimension();
    }

    @XmlElementDecl(namespace = CONTAINER_TYPE_NAMESPACE, name = "container-type")
    public JAXBElement<ContainerType> createContainerType(ContainerType value) {
        return new JAXBElement<ContainerType>(_ContainerType_QNAME, ContainerType.class, null, value);
    }

    @XmlElementDecl(namespace = CONTAINER_TYPE_NAMESPACE, name = "container-types")
    public JAXBElement<ContainerTypes> createContainerTypes(ContainerTypes value) {
        return new JAXBElement<ContainerTypes>(_ContainerTypes_QNAME, ContainerTypes.class, null, value);
    }
}
