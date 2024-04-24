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
import java.util.Collection;
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
import com.genologics.ri.artifact.Artifact;

/**
 *
 * The detailed representation of a step's pooled inputs.
 */
@ClarityEntity(uriSection = "steps", uriSubsection = "pools", updateable = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pools", propOrder = { "step", "configuration", "pooledInputs", "availableInputs" })
@XmlRootElement(name = "pools")
public class Pools implements Linkable<Pools>, Serializable
{
    private static final long serialVersionUID = 5682428399776236959L;

    @XmlElement(name = "step")
    protected Link step;

    @XmlElement(name = "configuration")
    protected StepConfiguration configuration;

    @XmlElementWrapper(name = "pooled-inputs")
    @XmlElement(name = "pool")
    protected List<PooledInputs> pooledInputs;

    @XmlElementWrapper(name = "available-inputs")
    @XmlElement(name = "input")
    protected List<Input> availableInputs;

    @XmlAttribute(name = "uri")
    @XmlSchemaType(name = "anyURI")
    protected URI uri;

    public Pools()
    {
    }

    public Pools(URI uri)
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

    public List<PooledInputs> getPooledInputs()
    {
        if (pooledInputs == null)
        {
            pooledInputs = new ArrayList<PooledInputs>();
        }
        return pooledInputs;
    }

    public PooledInputs addPooledInputs(PooledInputs inputs)
    {
        getPooledInputs().add(inputs);
        return inputs;
    }

    public PooledInputs addPooledInputs(Collection<? extends Linkable<Artifact>> inputArtifacts, String poolName, Linkable<Artifact> outputArtifact)
    {
        return addPooledInputs(new PooledInputs(inputArtifacts, poolName, outputArtifact));
    }

    public List<Input> getAvailableInputs()
    {
        if (availableInputs == null)
        {
            availableInputs = new ArrayList<Input>();
        }
        return availableInputs;
    }

    public Input addAvailableInput(Input input)
    {
        getAvailableInputs().add(input);
        return input;
    }

    public Input addAvailableInput(Linkable<Artifact> artifact)
    {
        return addAvailableInput(new Input(artifact));
    }
}
