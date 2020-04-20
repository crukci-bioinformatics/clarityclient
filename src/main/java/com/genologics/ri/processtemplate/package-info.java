@XmlSchema(
        namespace = PROCESS_TEMPLATE_NAMESPACE,
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = @XmlNs(prefix = "ptm", namespaceURI = PROCESS_TEMPLATE_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.processtemplate;

import static com.genologics.ri.Namespaces.PROCESS_TEMPLATE_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
