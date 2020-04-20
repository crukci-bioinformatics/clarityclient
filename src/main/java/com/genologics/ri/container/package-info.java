@XmlSchema(
    namespace = CONTAINER_NAMESPACE,
    elementFormDefault = XmlNsForm.UNQUALIFIED,
    xmlns = @XmlNs(prefix = "con", namespaceURI = CONTAINER_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.container;

import static com.genologics.ri.Namespaces.CONTAINER_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
