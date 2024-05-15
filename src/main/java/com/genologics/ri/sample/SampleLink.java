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

package com.genologics.ri.sample;

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
 * Sample-link is a child element type of samples and provides a URI linking to
 * the detailed representation of a sample.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sample-link")
public class SampleLink implements LimsEntityLink<Sample>, Serializable
{
    private static final long serialVersionUID = -5247716763134908401L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "limsid")
    protected String limsid;

    public SampleLink()
    {
    }

    public SampleLink(URI uri)
    {
        this.uri = uri;
    }

    public SampleLink(URI uri, String limsid)
    {
        this.uri = uri;
        this.limsid = limsid;
    }

    public SampleLink(LimsEntityLinkable<Sample> link)
    {
        this.uri = link.getUri();
        this.limsid = link.getLimsid();
    }

    @Override
    public Class<Sample> getEntityClass()
    {
        return Sample.class;
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
