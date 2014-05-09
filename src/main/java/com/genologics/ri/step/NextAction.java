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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.stepconfiguration.ProtocolStep;

/**
 *
 * Sample next action or step.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "next-action")
public class NextAction implements Serializable
{
    private static final long serialVersionUID = -3281325983996842509L;

    @XmlAttribute(name = "artifact-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI artifactUri;

    @XmlAttribute(name = "action")
    protected ActionType action;

    @XmlAttribute(name = "step-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI stepUri;


    public NextAction()
    {
    }

    public NextAction(URI artifactUri)
    {
        this.artifactUri = artifactUri;
    }

    public NextAction(URI artifactUri, ActionType action)
    {
        this.artifactUri = artifactUri;
        this.action = action;
    }

    public NextAction(URI artifactUri, ActionType action, Linkable<ProtocolStep> nextStep)
    {
        this.artifactUri = artifactUri;
        this.action = action;
        this.stepUri = nextStep.getUri();
    }

    public NextAction(Linkable<Artifact> link)
    {
        this.artifactUri = link.getUri();
    }

    public NextAction(Linkable<Artifact> link, ActionType action)
    {
        this.artifactUri = link.getUri();
        this.action = action;
    }

    public NextAction(Linkable<Artifact> link, ActionType action, Linkable<ProtocolStep> nextStep)
    {
        this.artifactUri = link.getUri();
        this.action = action;
        this.stepUri = nextStep.getUri();
    }

    public URI getArtifactUri()
    {
        return artifactUri;
    }

    public void setArtifactUri(URI artifactUri)
    {
        this.artifactUri = artifactUri;
    }

    public void setArtifact(Linkable<Sample> link)
    {
        artifactUri = link.getUri();
    }

    public ActionType getAction()
    {
        return action;
    }

    public void setAction(ActionType action)
    {
        this.action = action;
    }

    public URI getStepUri()
    {
        return stepUri;
    }

    public void setStepUri(URI stepUri)
    {
        this.stepUri = stepUri;
    }

    public void setStep(Linkable<ProcessStep> step)
    {
        stepUri = step.getUri();
    }

}
