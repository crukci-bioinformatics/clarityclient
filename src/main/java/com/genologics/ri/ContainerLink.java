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

package com.genologics.ri;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.container.Container;

/**
 *
 * Container is a child element of location and provides a URI linking to the
 * detailed representation of the Container for the location.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "container")
public class ContainerLink implements LimsEntityLink<Container>, Serializable
{
    private static final long serialVersionUID = 4920381830737280136L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    public ContainerLink()
    {
    }

    public ContainerLink(URI uri)
    {
        this.uri = uri;
    }

    public ContainerLink(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public ContainerLink(Linkable<Container> link)
    {
        this.uri = link.getUri();
    }

    public ContainerLink(LimsEntityLinkable<Container> link)
    {
        this.uri = link.getUri();
        this.limsid = link.getLimsid();
    }

    @Override
    public Class<Container> getEntityClass()
    {
        return Container.class;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public String getLimsid()
    {
        return limsid;
    }

    public void setLimsid(String limsid)
    {
        this.limsid = limsid;
    }

    @Override
    public int hashCode()
    {
        return uri == null ? 41 : uri.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean equal = obj == this;
        if (!equal && obj != null)
        {
            if (getClass().equals(obj.getClass()))
            {
                ContainerLink other = (ContainerLink)obj;
                if (uri != null)
                {
                    equal = uri.equals(other.getUri());
                }
            }
        }
        return equal;
    }

    @Override
    public String toString()
    {
        return limsid;
    }
}
