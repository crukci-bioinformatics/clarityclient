@XmlSchema(
        namespace = PROJECT_NAMESPACE,
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = @XmlNs(prefix = "prj", namespaceURI = PROJECT_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.project;

import static com.genologics.ri.Namespaces.PROJECT_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
