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

package com.genologics.ri.instrument;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.Link;

/**
 * The detailed representation of an instrument.
 */
@GenologicsEntity(uriSection = "instruments")
@XmlRootElement(name = "instrument")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrument", propOrder = { "name", "type" })
public class Instrument implements LimsEntity<Instrument>, Serializable
{
    private static final long serialVersionUID = -6971307261105200113L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    protected String name;

    protected String type;

    public Instrument()
    {
    }

    public Instrument(URI uri)
    {
        this.uri = uri;
    }

    public Instrument(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public Instrument(URI uri, String name, String type)
    {
        this.uri = uri;
        this.name = name;
        this.type = type;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
