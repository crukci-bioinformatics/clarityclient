@XmlSchema(
        namespace = STEP_NAMESPACE,
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = @XmlNs(prefix = "stp", namespaceURI = STEP_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.clarity.api.jaxb.URIAdapter.class)
package com.genologics.ri.step;

import static com.genologics.ri.Namespaces.STEP_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
