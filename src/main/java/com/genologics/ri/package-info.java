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

/**
 * The root package of the classes generated (originally) from the
 * Clarity schemas.
 *
 * <p>
 * This package also adds the interfaces and annotations that embellish
 * the classes that map directly to the XML exchanges.
 * </p>
 */
@XmlSchema(
    namespace = ROOT_NAMESPACE,
    elementFormDefault = XmlNsForm.UNQUALIFIED,
    xmlns = {
        @XmlNs(prefix = "xsi", namespaceURI = W3C_XML_SCHEMA_INSTANCE_NS_URI),
        @XmlNs(prefix = "xsd", namespaceURI = W3C_XML_SCHEMA_NS_URI),
        @XmlNs(prefix = "xmime", namespaceURI = "http://www.w3.org/2005/05/xmlmime"),
        @XmlNs(prefix = "ri", namespaceURI = ROOT_NAMESPACE)
    })
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.clarity.api.jaxb.URIAdapter.class)
package com.genologics.ri;

import static com.genologics.ri.Namespaces.ROOT_NAMESPACE;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
