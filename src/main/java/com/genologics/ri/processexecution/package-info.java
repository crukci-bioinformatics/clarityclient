@XmlSchema(
        namespace = PROCESS_EXECUTION_NAMESPACE,
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = @XmlNs(prefix = "prx", namespaceURI = PROCESS_EXECUTION_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.clarity.api.jaxb.URIAdapter.class)
package com.genologics.ri.processexecution;

import static com.genologics.ri.Namespaces.PROCESS_EXECUTION_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
