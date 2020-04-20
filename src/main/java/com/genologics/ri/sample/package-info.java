@XmlSchema(
        namespace = SAMPLE_NAMESPACE,
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = @XmlNs(prefix = "smp", namespaceURI = SAMPLE_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.sample;

import static com.genologics.ri.Namespaces.SAMPLE_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
