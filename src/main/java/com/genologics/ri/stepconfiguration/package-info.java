@XmlSchema(
        namespace = STEP_CONFIGURATION_NAMESPACE,
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = @XmlNs(prefix = "protstepcnf", namespaceURI = STEP_CONFIGURATION_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.clarity.api.jaxb.URIAdapter.class)
package com.genologics.ri.stepconfiguration;

import static com.genologics.ri.Namespaces.STEP_CONFIGURATION_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
