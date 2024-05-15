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
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.Locatable;

/**
 * Details of an individual artifact that is part of a pooled artifact.
 *
 * @since 2.26
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demux-artifact", propOrder = { "samples", "reagentLabels", "demux" })
public class DemuxArtifact implements Locatable, Serializable
{
    private static final long serialVersionUID = 8205564798487086835L;

    @XmlElementWrapper(name = "samples")
    @XmlElement(name = "sample")
    protected List<DemuxArtifactSample> samples;

    @XmlElementWrapper(name = "reagent-labels")
    @XmlElement(name = "reagent-label")
    protected List<ReagentLabel> reagentLabels;

    @XmlElement
    protected DemuxDetails demux;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public DemuxArtifact()
    {
    }

    public DemuxDetails getDemux()
    {
        return demux;
    }

    public void setDemux(DemuxDetails demux)
    {
        this.demux = demux;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
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

    public List<DemuxArtifactSample> getSamples()
    {
        if (samples == null)
        {
            samples = new ArrayList<>();
        }
        return samples;
    }

    public List<ReagentLabel> getReagentLabels()
    {
        if (reagentLabels == null)
        {
            reagentLabels = new ArrayList<>();
        }
        return reagentLabels;
    }

}
