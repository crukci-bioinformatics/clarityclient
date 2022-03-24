/**
 * The root package of the classes generated (originally) from the
 * Genologics schemas.
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
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri;

import static com.genologics.ri.Namespaces.ROOT_NAMESPACE;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
