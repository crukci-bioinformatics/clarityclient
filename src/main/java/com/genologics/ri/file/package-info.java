@XmlSchema(
        namespace = FILE_NAMESPACE,
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = @XmlNs(prefix = "file", namespaceURI = FILE_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.file;

import static com.genologics.ri.Namespaces.FILE_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
