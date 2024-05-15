@XmlSchema(
    namespace = CONTROL_TYPE_NAMESPACE,
    elementFormDefault = XmlNsForm.UNQUALIFIED,
    xmlns = @XmlNs(prefix = "ctrltp", namespaceURI = CONTROL_TYPE_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.clarity.api.jaxb.URIAdapter.class)
package com.genologics.ri.controltype;

import static com.genologics.ri.Namespaces.CONTROL_TYPE_NAMESPACE;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
