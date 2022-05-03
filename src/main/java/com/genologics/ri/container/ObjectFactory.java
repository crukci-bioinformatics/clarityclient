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

package com.genologics.ri.container;

import static com.genologics.ri.Namespaces.CONTAINER_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.container package.
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

    private final static QName _Containers_QNAME = new QName(CONTAINER_NAMESPACE, "containers");
    private final static QName _Container_QNAME = new QName(CONTAINER_NAMESPACE, "container");
    private final static QName _Details_QNAME = new QName(CONTAINER_NAMESPACE, "details");
    private final static QName _ContainerPlacement_QNAME = new QName("", "placement");
    private final static QName _ContainerOccupiedWells_QNAME = new QName("", "occupied-wells");
    private final static QName _ContainerName_QNAME = new QName("", "name");
    private final static QName _ContainerState_QNAME = new QName("", "state");
    private final static QName _ContainerType_QNAME = new QName("", "type");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.container
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Container }
     *
     */
    public Container createContainer() {
        return new Container();
    }

    /**
     * Create an instance of {@link Placement }
     *
     */
    public Placement createPlacement() {
        return new Placement();
    }

    /**
     * Create an instance of {@link ContainerTypeLink }
     *
     */
    public ContainerTypeLink createContainerType() {
        return new ContainerTypeLink();
    }

    /**
     * Create an instance of {@link ContainerLink }
     *
     */
    public ContainerLink createContainerLink() {
        return new ContainerLink();
    }

    /**
     * Create an instance of {@link ContainerBatchFetchResult }
     *
     */
    public ContainerBatchFetchResult createDetails() {
        return new ContainerBatchFetchResult();
    }

    /**
     * Create an instance of {@link Containers }
     *
     */
    public Containers createContainers() {
        return new Containers();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Containers }{@code >}}
     *
     */
    @XmlElementDecl(namespace = CONTAINER_NAMESPACE, name = "containers")
    public JAXBElement<Containers> createContainers(Containers value) {
        return new JAXBElement<Containers>(_Containers_QNAME, Containers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Container }{@code >}}
     *
     */
    @XmlElementDecl(namespace = CONTAINER_NAMESPACE, name = "container")
    public JAXBElement<Container> createContainer(Container value) {
        return new JAXBElement<Container>(_Container_QNAME, Container.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContainerBatchFetchResult }{@code >}}
     *
     */
    @XmlElementDecl(namespace = CONTAINER_NAMESPACE, name = "details")
    public JAXBElement<ContainerBatchFetchResult> createDetails(ContainerBatchFetchResult value) {
        return new JAXBElement<ContainerBatchFetchResult>(_Details_QNAME, ContainerBatchFetchResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Placement }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "placement", scope = Container.class)
    public JAXBElement<Placement> createContainerPlacement(Placement value) {
        return new JAXBElement<Placement>(_ContainerPlacement_QNAME, Placement.class, Container.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "occupied-wells", scope = Container.class)
    public JAXBElement<Long> createContainerOccupiedWells(Long value) {
        return new JAXBElement<Long>(_ContainerOccupiedWells_QNAME, Long.class, Container.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "name", scope = Container.class)
    public JAXBElement<String> createContainerName(String value) {
        return new JAXBElement<String>(_ContainerName_QNAME, String.class, Container.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "state", scope = Container.class)
    public JAXBElement<String> createContainerState(String value) {
        return new JAXBElement<String>(_ContainerState_QNAME, String.class, Container.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContainerTypeLink }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "type", scope = Container.class)
    public JAXBElement<ContainerTypeLink> createContainerType(ContainerTypeLink value) {
        return new JAXBElement<ContainerTypeLink>(_ContainerType_QNAME, ContainerTypeLink.class, Container.class, value);
    }

}
