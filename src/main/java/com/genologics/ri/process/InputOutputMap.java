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

package com.genologics.ri.process;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

/**
 *
 * Input-output-map is a child element of Process and relates one of the Process
 * inputs to one of the outputs that was produced for that input.
 * <p>
 * There will be a distinct input-output-map for each pairing of Process input
 * to Process output.
 * </p>
 * <p>
 * If an input is not mapped to any outputs, the input will be listed with no
 * outputs.
 * </p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "input-output-map", propOrder = { "input", "output" })
public class InputOutputMap implements Serializable
{
    private static final long serialVersionUID = 1174916419431443420L;

    protected ArtifactLink input;
    protected ArtifactLink output;

    /**
     * Gets the value of the input property.
     *
     * @return possible object is {@link ArtifactLink }
     *
     */
    public ArtifactLink getInput()
    {
        return input;
    }

    /**
     * Sets the value of the input property.
     *
     * @param value allowed object is {@link ArtifactLink }
     *
     */
    public void setInput(ArtifactLink value)
    {
        this.input = value;
    }

    /**
     * Gets the value of the output property.
     *
     * @return possible object is {@link ArtifactLink }
     *
     */
    public ArtifactLink getOutput()
    {
        return output;
    }

    /**
     * Sets the value of the output property.
     *
     * @param value allowed object is {@link ArtifactLink }
     *
     */
    public void setOutput(ArtifactLink value)
    {
        this.output = value;
    }

}
