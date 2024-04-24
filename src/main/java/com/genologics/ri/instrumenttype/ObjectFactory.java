/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2016 Cancer Research UK Cambridge Institute.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.genologics.ri.instrumenttype;

import static com.genologics.ri.Namespaces.INSTRUMENT_TYPE_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.genologics.ri.instrumenttype package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 *
 * @since 2.24
 */
@XmlRegistry
public class ObjectFactory
{

    private final static QName _InstrumentTypes_QNAME = new QName(INSTRUMENT_TYPE_NAMESPACE, "instrument-types");
    private final static QName _InstrumentType_QNAME = new QName(INSTRUMENT_TYPE_NAMESPACE, "instrument-type");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: com.genologics.ri.instrumenttype
     *
     */
    public ObjectFactory()
    {
    }

    public InstrumentTypeLink createInstrumentTypeLink()
    {
        return new InstrumentTypeLink();
    }

    public InstrumentTypes createInstrumentTypes()
    {
        return new InstrumentTypes();
    }

    public InstrumentTypeProcessType createInstrumentTypeProcessType()
    {
        return new InstrumentTypeProcessType();
    }

    public InstrumentType createInstrumentType()
    {
        return new InstrumentType();
    }

    @XmlElementDecl(namespace = INSTRUMENT_TYPE_NAMESPACE, name = "instrument-types")
    public JAXBElement<InstrumentTypes> createInstrumentTypes(InstrumentTypes value)
    {
        return new JAXBElement<InstrumentTypes>(_InstrumentTypes_QNAME, InstrumentTypes.class, null, value);
    }

    @XmlElementDecl(namespace = INSTRUMENT_TYPE_NAMESPACE, name = "instrument-type")
    public JAXBElement<InstrumentType> createInstrumentType(InstrumentType value)
    {
        return new JAXBElement<InstrumentType>(_InstrumentType_QNAME, InstrumentType.class, null, value);
    }
}
