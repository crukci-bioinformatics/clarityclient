@XmlSchema(
        namespace = ARTIFACT_GROUP_NAMESPACE,
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = @XmlNs(prefix = "artgr", namespaceURI = ARTIFACT_GROUP_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.artifactgroup;

import static com.genologics.ri.Namespaces.ARTIFACT_GROUP_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
