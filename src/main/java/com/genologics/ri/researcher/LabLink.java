/*
 * CRUK-CI Clarity REST API Java Client.
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

package com.genologics.ri.researcher;

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
import com.genologics.ri.lab.Lab;

/**
 *
 * Lab is a child element of researcher and provides a URI linking to the
 * detailed representation of the lab for the researcher.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lab")
public class LabLink implements LimsEntityLink<Lab>, Serializable
{
    private static final long serialVersionUID = -2199818978989305791L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public LabLink()
    {
    }

    public LabLink(URI uri)
    {
        this.uri = uri;
    }

    public LabLink(Linkable<Lab> link)
    {
        this.uri = link.getUri();
    }

    public Class<Lab> getEntityClass()
    {
        return Lab.class;
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
