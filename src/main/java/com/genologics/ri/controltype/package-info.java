@XmlSchema(
    namespace = CONTROL_TYPE_NAMESPACE,
    elementFormDefault = XmlNsForm.UNQUALIFIED,
    xmlns = @XmlNs(prefix = "ctrltp", namespaceURI = CONTROL_TYPE_NAMESPACE))
@XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.clarity.api.jaxb.URIAdapter.class)
package com.genologics.ri.controltype;

import static com.genologics.ri.Namespaces.CONTROL_TYPE_NAMESPACE;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/*
// Package level annotation
@jakarta.xml.bind.annotation.XmlSchema (
  xmlns = {
    @jakarta.xml.bind.annotation.XmlNs(prefix = "po",
               namespaceURI="http://www.example.com/myPO1"),

    @jakarta.xml.bind.annotation.XmlNs(prefix="xs",
               namespaceURI="http://www.w3.org/2001/XMLSchema")
  }
)


<!-- XML Schema fragment -->
<schema
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:po="http://www.example.com/PO1"
    targetNamespace="http://www.example.com/PO1">
*/