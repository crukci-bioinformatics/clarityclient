@XmlSchema(
    namespace = CONTAINER_TYPE_NAMESPACE,
    elementFormDefault = XmlNsForm.UNQUALIFIED,
    xmlns = @XmlNs(prefix = "ctp", namespaceURI = CONTAINER_TYPE_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.containertype;

import static com.genologics.ri.Namespaces.CONTAINER_TYPE_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
