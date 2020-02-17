/**
 * @since 2.18
 */
@javax.xml.bind.annotation.XmlSchema(
        namespace = com.genologics.ri.Namespaces.REAGENT_LOT_NAMESPACE,
        elementFormDefault = javax.xml.bind.annotation.XmlNsForm.UNQUALIFIED,
        xmlns = @javax.xml.bind.annotation.XmlNs(prefix = "lot", namespaceURI = com.genologics.ri.Namespaces.REAGENT_LOT_NAMESPACE))
@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(type = java.net.URI.class, value = org.cruk.genologics.api.jaxb.URIAdapter.class)
package com.genologics.ri.reagentlot;
