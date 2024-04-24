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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;


/**
 * @since 2.24
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrument-type-link")
public class InstrumentTypeLink implements LimsLink<InstrumentType>, Serializable
{
    private static final long serialVersionUID = 3927957034901059387L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "vendor")
    protected String vendor;


    public InstrumentTypeLink()
    {
    }

    public InstrumentTypeLink(URI uri)
    {
        setUri(uri);
    }

    public InstrumentTypeLink(URI uri, String name)
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

    @Override
    public Class<InstrumentType> getEntityClass()
    {
        return InstrumentType.class;
    }
}
