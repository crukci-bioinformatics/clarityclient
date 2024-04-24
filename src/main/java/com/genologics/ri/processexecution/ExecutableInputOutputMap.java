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

package com.genologics.ri.processexecution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.artifact.OutputType;
import com.genologics.ri.container.Container;

/**
 *
 * Processes link inputs to outputs and this relationship is called an
 * input-output map. Input-output-map is a child element of the Process element.
 * <p>
 * When a Process creates multiple outputs per input, there is a distinct
 * input-output map for each input to output relationship. When a Process
 * produces a shared output, there is a single input-output map for the shared
 * output and all its related inputs.
 * <p>
 * In situations where a Process is used to affect the properties of inputs only
 * and therefore, does not create outputs, you can omit the output element.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "input-output-map", propOrder = { "inputs", "output" })
public class ExecutableInputOutputMap implements Serializable
{
    private static final long serialVersionUID = -5751963471358280360L;

    @XmlElement(name = "input")
    protected List<Input> inputs;

    @XmlElement(name = "output")
    protected Output output;

    @XmlAttribute(name = "shared")
    protected Boolean shared;

    public ExecutableInputOutputMap()
    {
    }

    public ExecutableInputOutputMap(Boolean shared)
    {
        this.shared = shared;
    }

    public List<Input> getInputs()
    {
        if (inputs == null)
        {
            inputs = new ArrayList<Input>();
        }
        return this.inputs;
    }

    public void setInputs(Collection<? extends Linkable<Artifact>> links)
    {
        getInputs().clear();
        for (Linkable<Artifact> link : links)
        {
            inputs.add(new Input(link));
        }
    }

    public Input addInput(Input input)
    {
        getInputs().add(input);
        return input;
    }

    public Input addInput(Linkable<Artifact> link, QCFlag qc)
    {
        Input input = new Input(link);
        input.setQCFlag(qc);
        getInputs().add(input);
        return input;
    }

    public Output getOutput()
    {
        return output;
    }

    public Output setOutput(Output output)
    {
        this.output = output;
        return this.output;
    }

    public Output setOutput(OutputType type, LimsEntityLinkable<Container> container, String position)
    {
        this.output = new Output(type, container, position);
        return this.output;
    }

    public Boolean isShared()
    {
        return shared;
    }

    public void setShared(Boolean shared)
    {
        this.shared = shared;
    }

}
