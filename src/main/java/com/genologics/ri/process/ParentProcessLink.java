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

package com.genologics.ri.process;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.LimsEntityLinkable;

/**
 *
 * Parent-process is a child element of Artifact and provides a URI linking to
 * the Process that created the Artifact.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parent-process")
public class ParentProcessLink implements LimsEntityLink<ClarityProcess>, Serializable
{
    private static final long serialVersionUID = -6557381570589306115L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    public ParentProcessLink()
    {
    }

    public ParentProcessLink(URI uri)
    {
        this.uri = uri;
    }

    public ParentProcessLink(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public ParentProcessLink(LimsEntityLinkable<ClarityProcess> link)
    {
        this.uri = link.getUri();
        this.limsid = link.getLimsid();
    }

    @Override
    public Class<ClarityProcess> getEntityClass()
    {
        return ClarityProcess.class;
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
    public String toString()
    {
        return limsid;
    }
}
