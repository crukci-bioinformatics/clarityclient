/**
 * The root package of the classes generated (originally) from the
 * Genologics schemas.
 *
 * <p>
 * This package also adds the interfaces and annotations that embellish
 * the classes that map directly to the XML exchanges.
 * </p>
 */
@javax.xml.bind.annotation.XmlSchema(
    namespace = com.genologics.ri.Namespaces.ROOT_NAMESPACE,
    elementFormDefault = javax.xml.bind.annotation.XmlNsForm.UNQUALIFIED,
    xmlns = {
        @javax.xml.bind.annotation.XmlNs(prefix = "xsi", namespaceURI = javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI),
        @javax.xml.bind.annotation.XmlNs(prefix = "xsd", namespaceURI = javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI),
        @javax.xml.bind.annotation.XmlNs(prefix = "xmime", namespaceURI = "http://www.w3.org/2005/05/xmlmime"),
        @javax.xml.bind.annotation.XmlNs(prefix = "ri", namespaceURI = com.genologics.ri.Namespaces.ROOT_NAMESPACE)
    })
@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri;
