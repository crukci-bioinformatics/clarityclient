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

package com.genologics.ri.step;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;
import com.genologics.ri.container.Container;

/**
 *
 * The detailed representation of a step's output artifact container placements
 */
@GenologicsEntity(uriSection = "steps", uriSubsection = "placements", creatable = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "placements", propOrder = { "step", "configuration", "selectedContainers", "outputPlacements" })
@XmlRootElement(name = "placements")
public class Placements implements Linkable<Placements>, Serializable
{
    private static final long serialVersionUID = -1949893881292269955L;

    @XmlElement(name = "step")
    protected Link step;

    @XmlElement(name = "configuration")
    protected StepConfiguration configuration;

    @XmlElementWrapper(name = "selected-containers")
    @XmlElement(name = "container")
    protected List<ContainerLink> selectedContainers;

    @XmlElementWrapper(name = "output-placements")
    @XmlElement(name = "output-placement")
    protected List<OutputPlacement> outputPlacements;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Placements()
    {
    }

    public Placements(URI uri)
    {
        this.uri = uri;
    }

    public Link getStep()
    {
        return step;
    }

    public void setStep(Link step)
    {
        this.step = step;
    }

    public StepConfiguration getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(StepConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public List<ContainerLink> getSelectedContainers()
    {
        if (selectedContainers == null)
        {
            selectedContainers = new ArrayList<ContainerLink>();
        }
        return selectedContainers;
    }

    public ContainerLink addSelectedContainer(Linkable<Container> container)
    {
        ContainerLink link = new ContainerLink(container);
        getSelectedContainers().add(link);
        return link;
    }

    public List<OutputPlacement> getOutputPlacements()
    {
        if (outputPlacements == null)
        {
            outputPlacements = new ArrayList<OutputPlacement>();
        }
        return outputPlacements;
    }

    public OutputPlacement addOutputPlacement(OutputPlacement placement)
    {
        getOutputPlacements().add(placement);
        return placement;
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
