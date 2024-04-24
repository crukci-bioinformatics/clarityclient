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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;

/**
 *
 * Provides pooled input groups.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pooled-inputs", propOrder = { "inputs" })
public class PooledInputs implements Serializable
{
    private static final long serialVersionUID = -3807477794297469797L;

    @XmlElement(name = "input")
    protected List<Input> inputs;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "output-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI outputUri;

    public PooledInputs()
    {
    }

    public PooledInputs(Collection<? extends Linkable<Artifact>> inputArtifacts, String poolName)
    {
        setInputArtifacts(inputArtifacts);
        setName(poolName);
    }

    public PooledInputs(Collection<? extends Linkable<Artifact>> inputArtifacts, String poolName, Linkable<Artifact> outputArtifact)
    {
        setInputArtifacts(inputArtifacts);
        setName(poolName);
        setOutputArtifact(outputArtifact);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public URI getOutputUri()
    {
        return outputUri;
    }

    public void setOutputUri(URI outputUri)
    {
        this.outputUri = outputUri;
    }

    public void setOutput(Output output)
    {
        outputUri = output == null ? null : output.getUri();
    }

    public void setOutputArtifact(Linkable<Artifact> artifact)
    {
        outputUri = artifact == null ? null : artifact.getUri();
    }

    public List<Input> getInputs()
    {
        if (inputs == null)
        {
            inputs = new ArrayList<Input>();
        }
        return inputs;
    }

    public Input addInput(Input input)
    {
        getInputs().add(input);
        return input;
    }

    public Input addInput(Linkable<Artifact> artifact)
    {
        return addInput(new Input(artifact));
    }

    public void setInputArtifacts(Collection<? extends Linkable<Artifact>> artifacts)
    {
        getInputs().clear();
        for (Linkable<Artifact> a : artifacts)
        {
            inputs.add(new Input(a));
        }
    }
}
