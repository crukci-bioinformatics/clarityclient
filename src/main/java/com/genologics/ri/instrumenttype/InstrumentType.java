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

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;

/**
 * An instrument describes properties common to all instruments of that type.
 *
 * @since 2.24
 */
@ClarityEntity(uriSection = "configuration/instrumenttypes")
@XmlRootElement(name = "instrument-type")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrument-type", propOrder = { "name", "vendor", "processTypes" })
public class InstrumentType implements Linkable<InstrumentType>, Serializable
{
    private static final long serialVersionUID = 7273963191417975L;

    protected String name;

    protected String vendor;

    @XmlElementWrapper(name = "process-types")
    @XmlElement(name = "process-type")
    protected List<InstrumentTypeProcessType> processTypes;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;


    public InstrumentType()
    {
    }

    public InstrumentType(URI uri)
    {
        setUri(uri);
    }

    public InstrumentType(URI uri, String name)
    {
        setUri(uri);
        setName(name);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public String getVendor()
    {
        return vendor;
    }

    public void setVendor(String value)
    {
        this.vendor = value;
    }

    public List<InstrumentTypeProcessType> getProcessTypes()
    {
        if (processTypes == null)
        {
            processTypes = new ArrayList<InstrumentTypeProcessType>();
        }
        return processTypes;
    }

    @Override
    public URI getUri()
    {
        return uri;
    }

    @Override
    public void setUri(URI value)
    {
        this.uri = value;
    }
}
