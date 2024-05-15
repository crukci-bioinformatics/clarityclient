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

package com.genologics.ri.routing;

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
import com.genologics.ri.stage.Stage;
import com.genologics.ri.workflowconfiguration.Workflow;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extArtifactAssignments")
public class ExtArtifactAssignments implements Serializable
{
    private static final long serialVersionUID = 5445690350646421054L;

    @XmlElement(name = "artifact")
    protected List<ArtifactLink> artifacts;

    @XmlAttribute(name = "workflow-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI workflowUri;

    @XmlAttribute(name = "stage-uri")
    @XmlSchemaType(name = "anyURI")
    protected URI stageUri;

    public List<ArtifactLink> getArtifacts()
    {
        if (artifacts == null)
        {
            artifacts = new ArrayList<ArtifactLink>();
        }
        return artifacts;
    }

    public void addArtifact(Linkable<Artifact> artifact)
    {
        getArtifacts().add(new ArtifactLink(artifact));
    }

    public void addAll(Collection<? extends Linkable<Artifact>> links)
    {
        getArtifacts();
        for (Linkable<Artifact> l : links)
        {
            if (l != null && l.getUri() != null)
            {
                artifacts.add(new ArtifactLink(l));
            }
        }
    }

    public URI getWorkflowUri()
    {
        return workflowUri;
    }

    public void setWorkflowUri(URI workflowUri)
    {
        this.workflowUri = workflowUri;
    }

    public void setWorkflow(Linkable<Workflow> workflow)
    {
        this.workflowUri = workflow.getUri();
    }

    public URI getStageUri()
    {
        return stageUri;
    }

    public void setStageUri(URI stageUri)
    {
        this.stageUri = stageUri;
    }

    public void setStage(Linkable<Stage> stage)
    {
        this.stageUri = stage.getUri();
    }

}
