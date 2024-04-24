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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Locatable;

/**
 * The detailed representation of the demultiplexing of a pooled artifact.
 *
 * @since 2.26
 */
@ClarityEntity(uriSection = "artifacts", uriSubsection = "demux")
@XmlRootElement(name = "demux")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demux", propOrder = { "artifact", "demuxDetails" })
public class Demux implements Locatable, Serializable
{
    private static final long serialVersionUID = 8873892961025197978L;

    @XmlElement
    protected DemuxSourceArtifact artifact;

    @XmlElement(name = "demux")
    protected DemuxDetails demuxDetails;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Demux()
    {
    }

    public DemuxSourceArtifact getArtifact()
    {
        return artifact;
    }

    public void setArtifact(DemuxSourceArtifact artifact)
    {
        this.artifact = artifact;
    }

    public DemuxDetails getDemuxDetails()
    {
        return demuxDetails;
    }

    public void setDemuxDetails(DemuxDetails demuxDetails)
    {
        this.demuxDetails = demuxDetails;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }
}
