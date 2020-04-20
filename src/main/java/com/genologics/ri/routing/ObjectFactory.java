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


package com.genologics.ri.routing;

import static com.genologics.ri.Namespaces.ROUTING_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.routing package.
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

    private final static QName _Routing_QNAME = new QName(ROUTING_NAMESPACE, "routing");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.routing
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArtifactLink }
     *
     */
    public ArtifactLink createArtifact() {
        return new ArtifactLink();
    }

    /**
     * Create an instance of {@link ExtArtifactAssignments }
     *
     */
    public ExtArtifactAssignments createExternalArtifactAssignments() {
        return new ExtArtifactAssignments();
    }

    /**
     * Create an instance of {@link Routing }
     *
     */
    public Routing createRouting() {
        return new Routing();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Routing }{@code >}}
     *
     */
    @XmlElementDecl(namespace = ROUTING_NAMESPACE, name = "routing")
    public JAXBElement<Routing> createRouting(Routing value) {
        return new JAXBElement<Routing>(_Routing_QNAME, Routing.class, null, value);
    }

}
