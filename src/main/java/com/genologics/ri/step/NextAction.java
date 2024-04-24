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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.stepconfiguration.ProtocolStep;

/**
 *
 * Sample next action or step.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "next-action")
public class NextAction implements LimsLink<Artifact>, Serializable
{
    private static final long serialVersionUID = -8061858911629830727L;

    @XmlAttribute(name = "artifact-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI artifactUri;

    @XmlAttribute(name = "action")
    protected ActionType action;

    @XmlAttribute(name = "step-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI stepUri;

    /**
     * @since 2.20
     */
    @XmlAttribute(name = "rework-step-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI reworkStepUri;


    public NextAction()
    {
    }

    public NextAction(URI artifactUri)
    {
        setArtifactUri(artifactUri);
    }

    public NextAction(URI artifactUri, ActionType action)
    {
        setArtifactUri(artifactUri);
        setAction(action);
    }

    public NextAction(URI artifactUri, ActionType action, Linkable<ProtocolStep> nextStep)
    {
        setArtifactUri(artifactUri);
        setAction(action);
        setStep(nextStep);
    }

    public NextAction(Linkable<Artifact> link)
    {
        setArtifact(link);
    }

    public NextAction(Linkable<Artifact> link, ActionType action)
    {
        setArtifact(link);
        setAction(action);
    }

    public NextAction(Linkable<Artifact> link, ActionType action, Linkable<ProtocolStep> nextStep)
    {
        setArtifact(link);
        setAction(action);
        setStep(nextStep);
    }

    public URI getArtifactUri()
    {
        return artifactUri;
    }

    public void setArtifactUri(URI artifactUri)
    {
        this.artifactUri = artifactUri;
    }

    public void setArtifact(Linkable<Artifact> link)
    {
        artifactUri = link == null ? null : link.getUri();
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

    public void setStep(Linkable<ProtocolStep> step)
    {
        stepUri = step == null ? null : step.getUri();
    }

    public URI getReworkStepUri()
    {
        return reworkStepUri;
    }

    public void setReworkStepUri(URI reworkStepUri)
    {
        this.reworkStepUri = reworkStepUri;
    }

    public void setReworkStep(Linkable<ProtocolStep> step)
    {
        reworkStepUri = step == null ? null : step.getUri();
    }

    @Override
    public void setUri(URI uri)
    {
        this.artifactUri = uri;
    }

    @Override
    public URI getUri()
    {
        return artifactUri;
    }

    @Override
    public Class<Artifact> getEntityClass()
    {
        return Artifact.class;
    }

    @Override
    public String toString()
    {
        ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        b.append("artifactUri", artifactUri);
        b.append("action", action);
        if (reworkStepUri != null)
        {
            b.append("reworkStepUri", reworkStepUri);
        }
        return b.toString();
    }
}
