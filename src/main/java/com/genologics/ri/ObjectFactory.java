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

package com.genologics.ri;

import static com.genologics.ri.Namespaces.ROOT_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri package.
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

    private final static QName _Links_QNAME = new QName(ROOT_NAMESPACE, "links");
    private final static QName _Index_QNAME = new QName(ROOT_NAMESPACE, "index");
    private final static QName _Externalid_QNAME = new QName(ROOT_NAMESPACE, "externalid");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExternalId }
     *
     */
    public ExternalId createExternalid() {
        return new ExternalId();
    }

    /**
     * Create an instance of {@link ContainerLink }
     *
     */
    public ContainerLink createContainer() {
        return new ContainerLink();
    }

    /**
     * Create an instance of {@link Link }
     *
     */
    public Link createLink() {
        return new Link();
    }

    /**
     * Create an instance of {@link Index }
     *
     */
    public Index createIndex() {
        return new Index();
    }

    /**
     * Create an instance of {@link Location }
     *
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link Address }
     *
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link Links }
     *
     */
    public Links createLinks() {
        return new Links();
    }

    /**
     * Create an instance of {@link Page }
     *
     */
    public Page createPage() {
        return new Page();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Links }{@code >}}
     *
     */
    @XmlElementDecl(namespace = ROOT_NAMESPACE, name = "links")
    public JAXBElement<Links> createLinks(Links value) {
        return new JAXBElement<Links>(_Links_QNAME, Links.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Index }{@code >}}
     *
     */
    @XmlElementDecl(namespace = ROOT_NAMESPACE, name = "index")
    public JAXBElement<Index> createIndex(Index value) {
        return new JAXBElement<Index>(_Index_QNAME, Index.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExternalId }{@code >}}
     *
     */
    @XmlElementDecl(namespace = ROOT_NAMESPACE, name = "externalid")
    public JAXBElement<ExternalId> createExternalid(ExternalId value) {
        return new JAXBElement<ExternalId>(_Externalid_QNAME, ExternalId.class, null, value);
    }

}
