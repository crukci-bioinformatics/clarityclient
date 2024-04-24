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

package com.genologics.ri;

import static com.genologics.ri.Namespaces.ROOT_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
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

    public ExternalId createExternalid() {
        return new ExternalId();
    }

    public ContainerLink createContainer() {
        return new ContainerLink();
    }

    public Link createLink() {
        return new Link();
    }

    public Index createIndex() {
        return new Index();
    }

    public Location createLocation() {
        return new Location();
    }

    public Address createAddress() {
        return new Address();
    }

    public Links createLinks() {
        return new Links();
    }

    public Page createPage() {
        return new Page();
    }

    @XmlElementDecl(namespace = ROOT_NAMESPACE, name = "links")
    public JAXBElement<Links> createLinks(Links value) {
        return new JAXBElement<Links>(_Links_QNAME, Links.class, null, value);
    }

    @XmlElementDecl(namespace = ROOT_NAMESPACE, name = "index")
    public JAXBElement<Index> createIndex(Index value) {
        return new JAXBElement<Index>(_Index_QNAME, Index.class, null, value);
    }

    @XmlElementDecl(namespace = ROOT_NAMESPACE, name = "externalid")
    public JAXBElement<ExternalId> createExternalid(ExternalId value) {
        return new JAXBElement<ExternalId>(_Externalid_QNAME, ExternalId.class, null, value);
    }
}
