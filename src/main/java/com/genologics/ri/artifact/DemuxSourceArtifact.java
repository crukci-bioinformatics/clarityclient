/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2018 Cancer Research UK Cambridge Institute.
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

package com.genologics.ri.artifact;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;


/**
 * @since 2.26
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demux-source-artifact")
public class DemuxSourceArtifact implements LimsLink<Artifact>, Serializable
{
    private static final long serialVersionUID = -3058956251238626047L;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    @XmlAttribute(name = "name")
    protected String name;

    public DemuxSourceArtifact()
    {
    }

    public DemuxSourceArtifact(URI uri)
    {
        this.uri = uri;
    }

    public DemuxSourceArtifact(URI uri, String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public void setArtifact(Linkable<Artifact> artifact)
    {
        this.uri = artifact == null ? null : artifact.getUri();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public Class<Artifact> getEntityClass()
    {
        return Artifact.class;
    }

    @Override
    public String toString()
    {
        return name == null ? "null" : name;
    }
}
