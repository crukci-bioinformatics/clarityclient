@javax.xml.bind.annotation.XmlSchema(
    namespace = com.genologics.ri.Namespaces.CONTAINER_TYPE_NAMESPACE,
    elementFormDefault = javax.xml.bind.annotation.XmlNsForm.UNQUALIFIED,
    xmlns = @javax.xml.bind.annotation.XmlNs(prefix = "ctp", namespaceURI = com.genologics.ri.Namespaces.CONTAINER_TYPE_NAMESPACE))
@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.containertype;
