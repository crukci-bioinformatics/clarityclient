@javax.xml.bind.annotation.XmlSchema(
        namespace = com.genologics.ri.Namespaces.EXCEPTION_NAMESPACE,
        elementFormDefault = javax.xml.bind.annotation.XmlNsForm.UNQUALIFIED,
        xmlns = @javax.xml.bind.annotation.XmlNs(prefix = "exc", namespaceURI = com.genologics.ri.Namespaces.EXCEPTION_NAMESPACE))
@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.exception;
