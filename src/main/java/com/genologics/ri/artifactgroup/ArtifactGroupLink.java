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

package com.genologics.ri.artifactgroup;

import java.io.Serializable;
import java.net.URI;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;

/**
 * artifactGroup-link is a child element type of artifactGroup and provides a
 * URI linking to the detailed representation of a artifactGroup.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "artifactgroup-link")
public class ArtifactGroupLink implements LimsLink<ArtifactGroup>, Serializable
{
    private static final long serialVersionUID = 3168021328679428589L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    protected String name;

    public ArtifactGroupLink()
    {
    }

    public ArtifactGroupLink(URI uri)
    {
        this.uri = uri;
    }

    public ArtifactGroupLink(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public ArtifactGroupLink(Linkable<ArtifactGroup> link)
    {
        this.uri = link.getUri();
        /*
        try
        {
            this.name = (String)PropertyUtils.getProperty(link, "name");
        }
        catch (Exception e)
        {
            // Ignore.
        }
        */
    }

    @Override
    public Class<ArtifactGroup> getEntityClass()
    {
        return ArtifactGroup.class;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    @Override
    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI value)
    {
        this.uri = value;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
