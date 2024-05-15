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
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import com.genologics.ri.ClarityEntity;
import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.containertype.ContainerType;
import com.genologics.ri.controltype.ControlType;
import com.genologics.ri.stepconfiguration.ProtocolStep;

/**
 *
 * A request to create a step.
 *
 * @since 2.18
 */
@ClarityEntity(uriSection = "steps", creatable = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "step-creation", propOrder = { "configuration", "containerType", "reagentCategory", "inputs" })
@XmlRootElement(name = "step-creation")
public class StepCreation implements Serializable
{
    private static final long serialVersionUID = 3624008363559662022L;

    @XmlElement
    protected StepConfiguration configuration;

    @XmlElement(name = "container-type")
    protected String containerType;

    @XmlElement(name = "reagent-category")
    protected String reagentCategory;

    @XmlElementWrapper(name = "inputs")
    @XmlElement(name = "input")
    protected List<CreationInput> inputs;

    public StepCreation()
    {
    }

    public StepCreation(StepConfiguration configuration)
    {
        setConfiguration(configuration);
    }

    public StepCreation(Linkable<ProtocolStep> step)
    {
        setConfiguration(step);
    }

    public StepConfiguration getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(StepConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public void setConfiguration(Linkable<ProtocolStep> step)
    {
        this.configuration = step == null ? null : new StepConfiguration(step);
    }

    public String getContainerType()
    {
        return containerType;
    }

    public void setContainerType(String containerType)
    {
        this.containerType = containerType;
    }

    public void setContainerType(ContainerType containerType)
    {
        this.containerType = containerType == null ? null : containerType.getName();
    }

    public String getReagentCategory()
    {
        return reagentCategory;
    }

    public void setReagentCategory(String reagentCategory)
    {
        this.reagentCategory = reagentCategory;
    }

    public List<CreationInput> getInputs()
    {
        if (inputs == null)
        {
            inputs = new ArrayList<CreationInput>();
        }
        return inputs;
    }

    public CreationInput addInput(CreationInput input)
    {
        getInputs().add(input);
        return input;
    }

    public CreationInput addInput(Linkable<Artifact> artifact, Linkable<ControlType> controlType, Long replicates)
    {
        return addInput(new CreationInput(artifact, controlType, replicates));
    }
}
