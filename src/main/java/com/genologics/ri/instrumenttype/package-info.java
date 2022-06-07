@XmlSchema(
    namespace = INSTRUMENT_TYPE_NAMESPACE,
    elementFormDefault = XmlNsForm.UNQUALIFIED,
    xmlns = @XmlNs(prefix = "itp", namespaceURI = INSTRUMENT_TYPE_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.clarity.api.jaxb.URIAdapter.class)
package com.genologics.ri.instrumenttype;

import static com.genologics.ri.Namespaces.INSTRUMENT_TYPE_NAMESPACE;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
