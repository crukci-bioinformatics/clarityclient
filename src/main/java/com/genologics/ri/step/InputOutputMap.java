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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.artifact.Artifact;

/**
 *
 * Input-output-map is a child element of Step and relates one of the Step
 * inputs to one of the outputs that was produced for that input.
 * <p>
 * There will be a distinct input-output-map for each pairing of Step input to
 * Step output.
 * </p>
 * <p>
 * If an input is not mapped to any outputs, the input will be listed with no
 * outputs.
 * </p>
 *
 * @since 2.18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "input-output-map", propOrder = { "input", "output" })
public class InputOutputMap implements Serializable
{
    private static final long serialVersionUID = -6553131277891392216L;

    protected ArtifactLink input;
    protected ArtifactLink output;


    public InputOutputMap()
    {
    }

    public InputOutputMap(LimsEntityLinkable<Artifact> input, LimsEntityLinkable<Artifact> output)
    {
        this.input = new ArtifactLink(input);
        this.output = new ArtifactLink(output);
    }

    public ArtifactLink getInput()
    {
        return input;
    }

    public void setInput(LimsEntityLinkable<Artifact> value)
    {
        this.input = new ArtifactLink(value);
    }

    public ArtifactLink getOutput()
    {
        return output;
    }

    public void setOutput(LimsEntityLinkable<Artifact> value)
    {
        this.output = new ArtifactLink(value);
    }

}
