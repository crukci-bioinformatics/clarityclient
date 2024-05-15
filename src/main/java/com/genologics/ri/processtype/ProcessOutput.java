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

package com.genologics.ri.processtype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import com.genologics.ri.configuration.FieldLink;

/**
 *
 * Process-output is a child element of process type specifying the configured
 * output generation types for the process
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process-output",
         propOrder = { "artifactType", "displayName", "outputGenerationType",
                       "variabilityType", "numberOfOutputs", "outputName",
                       "fieldDefinitions", "assignWorkingFlag" })
public class ProcessOutput implements Serializable
{
    private static final long serialVersionUID = 3584171153300804850L;

    @XmlElement(name = "artifact-type")
    protected String artifactType;

    @XmlElement(name = "display-name")
    protected String displayName;

    @XmlElement(name = "output-generation-type")
    protected OutputGenerationType outputGenerationType;

    @XmlElement(name = "variability-type")
    protected VariabilityType variabilityType;

    @XmlElement(name = "number-of-outputs")
    protected Integer numberOfOutputs;

    @XmlElement(name = "output-name")
    protected String outputName;

    @Deprecated
    @XmlElement(name = "field-definition")
    protected List<FieldLink> fieldDefinitions;

    @Deprecated
    @XmlElement(name = "assign-working-flag")
    protected Boolean assignWorkingFlag;


    /**
     * Each field definition provides a URI linking to the configuration of a user defined field for the output type.
     *
     * @return A list of links to fields.
     *
     * @deprecated These field definitions are ignored as of Clarity LIMS 5.0.
     */
    public List<FieldLink> getFieldDefinitions()
    {
        if (fieldDefinitions == null)
        {
            fieldDefinitions = new ArrayList<FieldLink>();
        }
        return this.fieldDefinitions;
    }

    /**
     * Artifact type for this output.
     *
     * @return The artifact type.
     */
    public String getArtifactType()
    {
        return artifactType;
    }

    public void setArtifactType(String artifactType)
    {
        this.artifactType = artifactType;
    }

    /**
     * The display name for the output.
     *
     * @return The display name.
     */
    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Specifies how the outputs are generated in relation to the inputs
     * (COMPOUND, PER_REAGENT_LABEL, or PER_INPUT).
     *
     * @return The output generation type.
     */
    public OutputGenerationType getOutputGenerationType()
    {
        return outputGenerationType;
    }

    public void setOutputGenerationType(OutputGenerationType outputGenerationType)
    {
        this.outputGenerationType = outputGenerationType;
    }

    /**
     * Specifies how the process determines the number of outputs to generated
     * (FIXED, VARIABLE, or VARIABLE_BY_INPUT).
     *
     * @return The number of outputs type.
     */
    public VariabilityType getVariabilityType()
    {
        return variabilityType;
    }

    public void setVariabilityType(VariabilityType variabilityType)
    {
        this.variabilityType = variabilityType;
    }

    /**
     * Number of outputs to generate (only applies if variabilityType is FIXED).
     * @return The number of outputs.
     */
    public Integer getNumberOfOutputs()
    {
        return numberOfOutputs;
    }

    public void setNumberOfOutputs(Integer numberOfOutputs)
    {
        this.numberOfOutputs = numberOfOutputs;
    }

    /**
     * Pattern for specifying how the output name is generated.
     * @return The output name pattern.
     */
    public String getOutputName()
    {
        return outputName;
    }

    public void setOutputName(String outputName)
    {
        this.outputName = outputName;
    }

    /**
     * Whether the working flag should be assigned to the output when the process runs.
     *
     * @return true if it should be removed, false if not.
     * @deprecated This property is no longer supported.
     */
    @Deprecated
    public Boolean isAssignWorkingFlag()
    {
        return assignWorkingFlag;
    }

    @Deprecated
    public void setAssignWorkingFlag(Boolean assignWorkingFlag)
    {
        this.assignWorkingFlag = assignWorkingFlag;
    }

}
