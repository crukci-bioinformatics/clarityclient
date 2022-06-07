@XmlSchema(
        namespace = UDF_NAMESPACE,
        elementFormDefault = XmlNsForm.UNQUALIFIED,
        xmlns = @XmlNs(prefix = "udf", namespaceURI = UDF_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.clarity.api.jaxb.URIAdapter.class)
package com.genologics.ri.userdefined;

import static com.genologics.ri.Namespaces.UDF_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
