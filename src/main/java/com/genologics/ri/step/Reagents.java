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

package com.genologics.ri.step;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;

/**
 *
 * The detailed representation of a step's output artifact reagents.
 */
@ClarityEntity(uriSection = "steps", uriSubsection = "reagents", creatable = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reagents", propOrder = { "step", "configuration", "reagentCategory", "outputReagents" })
@XmlRootElement(name = "reagents")
public class Reagents implements Linkable<Reagents>, Serializable
{
    private static final long serialVersionUID = 4896945008573422890L;

    @XmlElement(name = "step")
    protected Link step;

    @XmlElement(name = "configuration")
    protected StepConfiguration configuration;

    @XmlElement(name = "reagent-category")
    protected String reagentCategory;

    @XmlElementWrapper(name = "output-reagents")
    @XmlElement(name = "output")
    protected List<Output> outputReagents;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Reagents()
    {
    }

    public Reagents(URI uri)
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

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    /**
     * The permitted reagent category of the step. Reagent labels used in the POST must be from this reagent category.
     *
     * @return The reagent category.
     */
    public String getReagentCategory()
    {
        return reagentCategory;
    }

    public void setReagentCategory(String reagentCategory)
    {
        this.reagentCategory = reagentCategory;
    }

    public List<Output> getOutputReagents()
    {
        if (outputReagents == null)
        {
            outputReagents = new ArrayList<Output>();
        }
        return outputReagents;
    }

    public Output addOutputReagent(Output output)
    {
        getOutputReagents().add(output);
        return output;
    }
}
