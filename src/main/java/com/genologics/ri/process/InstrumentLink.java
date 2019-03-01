/*
 * CRUK-CI Genologics REST API Java Client.
 * Copyright (C) 2013 Cancer Research UK Cambridge Institute.
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

package com.genologics.ri.process;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;
import com.genologics.ri.instrument.Instrument;

/**
 *
 * The instrument element provides a URI to the instrument that is responsible
 * for running the process.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrument")
public class InstrumentLink implements LimsEntityLink<Instrument>, Serializable
{
    private static final long serialVersionUID = -5626088397896460506L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public InstrumentLink()
    {
    }

    public InstrumentLink(URI uri)
    {
        this.uri = uri;
    }

    public InstrumentLink(Linkable<Instrument> link)
    {
        this.uri = link.getUri();
    }

    @Override
    public Class<Instrument> getEntityClass()
    {
        return Instrument.class;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

    @Override
    public String getLimsid()
    {
        return Link.limsIdFromUri(uri);
    }

    @Override
    public void setLimsid(String id)
    {
        // Does nothing.
    }
}
